package com.simple.frame.dao.spi;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author lvxiang
 * @date 2015年5月13日 下午5:05:39
 * 通用Dao 接口，不应该绑定具体实现相关的代码
 */
public interface ICommonDao extends IMapper {
    JdbcTemplate getJdbcTemplate();

    JdbcTemplate getQueryJdbcTemplate();

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setJdbcTemplateQuery(JdbcTemplate jdbcTemplate);

    void setSqlSession(SqlSessionTemplate sessionTemplate);

    void setSqlSessionQuery(SqlSessionTemplate sessionTemplate);
}
