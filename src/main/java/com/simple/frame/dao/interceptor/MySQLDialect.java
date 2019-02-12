package com.simple.frame.dao.interceptor;

import com.simple.frame.dao.utils.OrderType;
/**
 * 
 *Created by lvxiang@ganji.com 2019/1/21  11:00
 *
 *@author <a href="mailto:lvxiang@ganji.com">simple</a>
 */
public class MySQLDialect implements Dialect {
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" limit ");
        if (offset > 0) {
            stringBuilder.append(offset).append(",").append(limit);
        } else {
            stringBuilder.append(limit);
        }
        return stringBuilder.toString();
    }

    @Override
    public String getOrderString(String sql, String orderColumns, OrderType orderType) {
        return new StringBuilder(sql).append(" order by ").append(orderColumns).append(" ")
                .append(orderType.toString()).toString();
    }

    @Override
    public boolean supportLimit() {
        return true;
    }

    @Override
    public boolean supportLimitOffset() {
        return true;
    }

}