package com.scrm.marketing.satoken;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaTokenConsts;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

@Component
public class SaTokenJwtUtil {
    static {
        try {
            Yaml yaml = new Yaml();
            InputStream in = SaTokenJwtUtil.class.getClassLoader().getResourceAsStream("application.yaml");
            if (in != null) {
                //获取yaml文件中的配置数据，然后转换为obj，
                //也可以将值转换为Map
                Map map = yaml.loadAs(in, Map.class);
                System.out.println(map);
                //通过map我们取值就可以了.
                Map my = (Map) map.get("my");
                Map jwt = (Map) my.get("jwt");
                // 这里写死了要求my.jwt下所需的3个属性
                Object secret = jwt.get("secret");
                Object timeout = jwt.get("timeout");
                Object loginIdKey = jwt.get("loginIdKey");
                if (secret == null || timeout == null || loginIdKey == null)
                    throw new Exception("请配置完整my.jwt下的3个属性");
                System.out.println(secret);
                System.out.println(timeout);
                System.out.println(loginIdKey);
                SaTokenJwtUtil.secret = secret.toString();
                SaTokenJwtUtil.TIMEOUT = Long.parseLong(timeout.toString());
                SaTokenJwtUtil.LOGIN_ID_KEY = loginIdKey.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 秘钥 (随便手打几个字母就好了)
     */
//    public static String secret = "myKey";
    public static String secret;

    /**
     * token有效期 (单位: 秒)
     */
//    public static long TIMEOUT = 60 * 60 * 24;// 1天
    public static long TIMEOUT;

//    public static String LOGIN_ID_KEY = "loginId";
    public static String LOGIN_ID_KEY;


    /**
     * 根据userId生成token
     *
     * @param loginId 账号id
     * @return jwt-token
     */
    public static String createToken(Object loginId) {
        Date nowDate = new Date();
        return JWT.create()
                //2.1添加头部.
                //.withHeader(map)

                // 2.2添加payload
                // 2.2.1.建议添加的声明
                .withIssuer("auth0")    // iss：发行者
                .withClaim(LOGIN_ID_KEY, loginId.toString())
                .withIssuedAt(nowDate)//"iat" claim . 发行时间
                .withExpiresAt(new Date(nowDate.getTime() + TIMEOUT * 1000)) // "exp" claim. 到期时间
                .withNotBefore(nowDate)    // 设置令牌在什么时间之前是不能使用的

                // 2.3签署令牌并返回JWT的Json String
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 从一个jwt里面解析出Claims
     *
     * @param tokenValue token值
     *                   //     * @param base64Security 秘钥
     * @return Claims对象
     */
    public static DecodedJWT getClaims(String tokenValue) {
//     System.out.println(tokenValue);
        DecodedJWT decodedJWT = null;

        // 先解码 看看能不能解码：不能将抛出JWTDecodeException异常
        decodedJWT = JWT.decode(tokenValue);

        // 再进行校验token：失败将抛出JWTVerificationException
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                //.withIssuer("auth0")
                .build(); //Reusable verifier instance
        verifier.verify(tokenValue);

        return decodedJWT;
    }

    /**
     * 从一个jwt里面解析loginId
     *
     * @param tokenValue token值
     *                   //     * @param base64Security 秘钥
     * @return loginId
     */
    public static String getLoginId(String tokenValue) {
        try {
            Claim loginId = getClaims(tokenValue).getClaim(LOGIN_ID_KEY);
            if (loginId == null) {
                return null;
            }
            return loginId.asString();
        } catch (JWTDecodeException e) {// 不能解码：可能是秘钥改了
//         throw NotLoginException.newInstance(StpUtil.TYPE, NotLoginException.TOKEN_TIMEOUT);
//            System.out.println("无效令牌：可能是时间验证不通过，可能是别人修改了，可能是秘钥不对");
            throw NotLoginException.newInstance(StpUtil.stpLogic.loginType, NotLoginException.INVALID_TOKEN);
        } catch (JWTVerificationException e) {// 解码成功，但是有异常，说明校验失败，应该是失效了
            return NotLoginException.TOKEN_TIMEOUT;
//            throw NotLoginException.newInstance(StpUtil.stpLogic.loginType, NotLoginException.INVALID_TOKEN);
        } catch (Exception e) {
            throw new SaTokenException(e);
        }
    }


    static {

        // 修改默认实现
        StpUtil.stpLogic = new StpLogic("login") {

            // 重写 (随机生成一个tokenValue)
            @Override
            public String createTokenValue(Object loginId) {
                return SaTokenJwtUtil.createToken(loginId);
            }

            // 重写 (在当前会话上登录id )
            @Override
            public void login(Object loginId, SaLoginModel loginModel) {
                // ------ 1、获取相应对象
                SaStorage storage = SaManager.getSaTokenContext().getStorage();
                SaTokenConfig config = getConfig();
                // ------ 2、生成一个token
                String tokenValue = createTokenValue(loginId);
                storage.set(splicingKeyJustCreatedSave(), tokenValue);    // 将token保存到本次request里

                if (config.getIsReadCookie()) {    // cookie注入
                    SaManager.getSaTokenContext().getResponse().addCookie(getTokenName(), tokenValue, "/", config.getCookieDomain(), (int) config.getTimeout());
                }
            }

            // 重写 (获取指定token对应的登录id)
            @Override
            public String getLoginIdNotHandle(String tokenValue) {
                try {
                    return SaTokenJwtUtil.getLoginId(tokenValue);
                } catch (Exception e) {
                    return null;
                }
            }

            // 重写 (当前会话注销登录)
            @Override
            public void logout() {
                // 如果连token都没有，那么无需执行任何操作
                String tokenValue = getTokenValue();
                if (tokenValue == null) {
                    return;
                }
                // 如果打开了cookie模式，把cookie清除掉
                if (getConfig().getIsReadCookie() == true) {
                    SaManager.getSaTokenContext().getResponse().deleteCookie(getTokenName());
                }
            }

            // 重写 (获取指定key的session)
            @Override
            public SaSession getSessionBySessionId(String sessionId, boolean isCreate) {
                throw new SaTokenException("jwt has not session");
            }

            // 重写 (获取当前登录者的token剩余有效时间 (单位: 秒))
            @Override
            public long getTokenTimeout() {
                // 如果没有token
                String tokenValue = getTokenValue();
                if (tokenValue == null) {
                    return SaTokenDao.NOT_VALUE_EXPIRE;
                }
                // 开始取值
                DecodedJWT decodedJWT = null;
                try {
                    decodedJWT = SaTokenJwtUtil.getClaims(tokenValue);
                } catch (Exception e) {
                    return SaTokenDao.NOT_VALUE_EXPIRE;
                }
//                if (decodedJWT == null) {
//                    return SaTokenDao.NOT_VALUE_EXPIRE;
//                }
                Date expiration = decodedJWT.getExpiresAt();
                if (expiration == null) {
                    return SaTokenDao.NOT_VALUE_EXPIRE;
                }
                return (expiration.getTime() - System.currentTimeMillis()) / 1000;
            }

            // 重写 (返回当前token的登录设备)
            @Override
            public String getLoginDevice() {
                return SaTokenConsts.DEFAULT_LOGIN_DEVICE;
            }

            // 重写 (获取当前会话的token信息)
            @Override
            public SaTokenInfo getTokenInfo() {
                SaTokenInfo info = new SaTokenInfo();
                info.tokenName = getTokenName();
                info.tokenValue = getTokenValue();
                info.isLogin = isLogin();
                info.loginId = getLoginIdDefaultNull();
                info.loginType = getLoginType();
                info.tokenTimeout = getTokenTimeout();
//           info.sessionTimeout = getSessionTimeout();
//           info.tokenSessionTimeout = getTokenSessionTimeout();
//           info.tokenActivityTimeout = getTokenActivityTimeout();
                info.loginDevice = getLoginDevice();
                return info;
            }
        };
    }
}