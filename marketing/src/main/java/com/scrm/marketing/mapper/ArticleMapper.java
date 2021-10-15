package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:03
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 除了文章内容外，其余字段均查询出来了
     * @param index 偏移量
     * @param pageSize 页大小
     * @return 文章列表
     */
    @Select("SELECT id,author_id,author_name,article_title,article_image, " +
            "   product_id,article_view_times,article_read_time_sum, " +
            "   examine_flag,examine_id,examine_name,examine_notes, " +
            "   create_time,update_time " +
            "   FROM mk_article " +
            "   LIMIT #{pageSize} OFFSET #{index}")
    List<Article> queryPage(int index, int pageSize);

}
