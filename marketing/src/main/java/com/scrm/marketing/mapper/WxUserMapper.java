package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.WxUser;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-28 14:38
 */
public interface WxUserMapper extends BaseMapper<WxUser> {
    @SuppressWarnings("all")
    @Select("SELECT * FROM mk_wx_user WHERE openid=#{openid}")
    List<WxUser> selectByOpenid(String openid);

    @SuppressWarnings("all")
    @Update("UPDATE mk_wx_user SET reader_status=#{readerStatus} WHERE id=#{id}")
    int updateReaderStatus(long id, String readerStatus);

    @SuppressWarnings("all")
    @Update("UPDATE se_customer SET wx_name=#{nickname},wx_openid=#{openid} WHERE id=#{customerId}")
    int bindWxUser(long customerId, String nickname, String openid);

    @SuppressWarnings("all")
    @Update("UPDATE mk_wx_user SET customer_id=#{customerId},reader_status=(SELECT customer_status FROM se_customer WHERE id=#{customerId})" +
            " WHERE openid=#{openid}")
    int copyCusStatusToWxUser(long customerId, String openid);

    @SuppressWarnings("all")
    @Update("UPDATE se_customer SET wx_name=NULL,wx_openid=NULL WHERE wx_openid=#{openid}")
    int unBindByOpenid(String openid);
}
