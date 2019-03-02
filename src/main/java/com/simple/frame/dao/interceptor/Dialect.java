package com.simple.frame.dao.interceptor;

import com.simple.frame.dao.utils.OrderType;
import com.simple.frame.dao.utils.Query;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.*;

public interface Dialect {

    /**
     * 将sql转换为分页SQL
     *
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @return 分页查询的sql
     */
    String getLimitString(String sql, int offset, int limit);


    /**
     * 将sql转换为排序SQL
     *
     * @param sql
     * @param orderColumns 排序的列，多个由逗号隔开
     * @param orderType 排序类型
     * @return
     */
    String getOrderString(String sql, String orderColumns, OrderType orderType);

    boolean supportLimit();

    boolean supportLimitOffset();
}

