package com.simple.frame.dao.spi.impl;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by simple on 2016/11/11.
 */
public class CommonDaoImpl extends DefaultDaoImpl {

    private SqlSessionTemplate sqlSession;
    @Getter @Setter
    private JdbcTemplate jdbcTemplate;

    private SqlSessionTemplate sqlSessionQuery;
    @Getter @Setter
    private JdbcTemplate queryJdbcTemplate;

    @Override
    public SqlSessionTemplate getSqlSessionQueryTemplate() {
        return sqlSessionQuery;
    }

    @Override
    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSession;
    }


    @Override
    public  void setSqlSessionQuery(SqlSessionTemplate sqlSessionQuery){
        this.sqlSessionQuery = sqlSessionQuery;
    }

    @Override
    public void setJdbcTemplateQuery(JdbcTemplate jdbcTemplate) {
        this.queryJdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public  void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession = sqlSession;
    }



}
