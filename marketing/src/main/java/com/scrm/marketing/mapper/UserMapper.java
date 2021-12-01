package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-15 20:59
 */
public interface UserMapper extends BaseMapper<User> {
    @SuppressWarnings("all")
    @Select("SELECT DISTINCT u.* FROM cms_user u " +
            " INNER JOIN mk_wx_read_record m ON u.id=m.share_id " +
            " WHERE m.article_id=#{articleId}")
    List<User> querySharePerson(Long articleId);
}
