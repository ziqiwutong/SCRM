package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-15 20:59
 */
public interface UserMapper extends BaseMapper<User> {
    @SuppressWarnings("all")
    @Select("select u.* from cms_user u " +
            "   INNER JOIN mk_article_share_record s ON u.id=s.share_id " +
            "   WHERE s.article_id=#{articleId}")
    List<User> querySharePerson(@NonNull Long articleId);
}
