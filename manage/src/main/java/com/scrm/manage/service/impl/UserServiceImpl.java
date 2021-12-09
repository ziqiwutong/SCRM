package com.scrm.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.manage.dao.UserInfoDao;
import com.scrm.manage.entity.UserInfo;
import com.scrm.manage.service.IuapService;
import com.scrm.manage.service.UserService;
import com.scrm.manage.vo.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private IuapService iuapService;

    @Resource
    private UserInfoDao userInfoDao;

    private final RestTemplate restTemplate;

    public UserServiceImpl() {
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    @Override
    public List<User> query(Integer count, String preId, String departmentId) {
        ArrayList<User> result = new ArrayList<>();

        String token = iuapService.getAccessToken();

        String url = "https://openapi.yonyoucloud.com/user/";
        if (departmentId.equals("")) {
            url = url + "get?access_token=" + token + "&count=" + count;
            if (!preId.equals("")) {
                url = url + "&next_id=" + preId;
            }
        } else {
            url = url + "dept/" + departmentId + "/" + count;
            if (!preId.equals("")) {
                url = url + "/" + preId;
            }
            url = url + "?access_token=" + token;
        }
        ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

        JSONObject body = JSONObject.parseObject(respEntity.getBody());
        if (responseFail(body)) return result;

        JSONArray data = body.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject info = data.getJSONObject(i);
            result.add(generateUser(info, false));
        }

        return result;
    }

    @Override
    public User queryById(Long id) {
        String token = iuapService.getAccessToken();

        String url = "https://openapi.yonyoucloud.com/user/get?access_token=" + token +
                "&next_id=" + (id - 1) + "&count=1";
        ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

        JSONObject body = JSONObject.parseObject(respEntity.getBody());
        if (responseFail(body)) return null;

        JSONArray data = body.getJSONArray("data");
        if (data == null || data.size() < 1) return null;
        JSONObject info = data.getJSONObject(0);
        if (info == null || !id.equals(info.getLong("id"))) return null;

        return generateUser(info, true);
    }

    @Override
    public String save(UserInfo userInfo) {
        if (userInfoDao.selectById(userInfo.getId()) == null) {
            if (userInfoDao.insert(userInfo) < 1) return "保存失败";
        } else {
            if (userInfoDao.updateById(userInfo) < 1) return "保存失败";
        }
        return null;
    }

    /**
     * 检查API调用是否失败
     */
    private boolean responseFail(JSONObject body) {
        String status = body.getString("code");
        if (!(status != null && status.equals("0"))) {
            return true;
        }
        String message = body.getString("msg");
        return message == null || !message.equals("成功");
    }

    /**
     * 将用友返回信息转为用户信息
     */
    private User generateUser(JSONObject info, boolean detail) {
        User user = new User();
        user.setId(info.getString("id"));
        user.setName(info.getString("name"));
        user.setDepartmentId(info.getString("dept_id"));
        user.setDuty(info.getString("duty"));
        user.setDutyId(info.getString("duty_id"));
        user.setWorkPhoto(info.getString("work_photo"));
        user.setOfficeDesk(info.getString("office_desk"));
        user.setStaffNo(info.getString("staff_no"));
        user.setUpdateTime(info.getString("updatetime"));
        user.setIsAdmin(info.getInteger("isAdmin"));
        user.setMobile(info.getString("mobile"));
        user.setEmail(info.getString("email"));
        user.setAvatar(info.getString("avatar"));
        user.setSex(info.getString("sex"));
        user.setBirthday(info.getString("birthday"));

        if (detail) {
            UserInfo userInfo = userInfoDao.selectById(info.getString("id"));
            if (userInfo != null) {
                user.setWeimobId(userInfo.getWeimobId());
            }
        }

        return user;
    }
}
