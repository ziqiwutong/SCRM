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
}
