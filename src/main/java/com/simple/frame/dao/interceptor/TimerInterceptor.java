package com.simple.frame.dao.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by simple on 2017/3/27 15:48
 * SQL 执行时间拦截器
 * @mail moneyhacker@163.com
 * @since 1.0
 */
@Intercepts(value = {
        @Signature(type=Executor.class, method="update", args={MappedStatement.class,Object.class}),
        @Signature(type=Executor.class, method="query",  args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class,CacheKey.class,BoundSql.class}),
        @Signature(type=Executor.class,method="query",   args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})
                    })
public class TimerInterceptor implements Interceptor {
    private Boolean showSql  = false;
    private Long longQueryTimeMillis ;
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,
    20,
    10,
    TimeUnit.MINUTES,
    new ArrayBlockingQueue<Runnable>(1000));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object  returnValue = invocation.proceed();
        long end = System.currentTimeMillis();
        poolExecutor.execute(() -> {
            if (showSql || (null != longQueryTimeMillis && (end - start) >= longQueryTimeMillis)){
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object parameter = null;
                if (invocation.getArgs().length > 1) {
                    parameter = invocation.getArgs()[1];
                }
                String sqlId = mappedStatement.getId();
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                Configuration configuration = mappedStatement.getConfiguration();
                String sql = getSql(configuration, boundSql, sqlId, end - start);
                System.err.println("SQL: " + sql);
            }
        });
        return returnValue;
    }

    private  String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(200);
        str.append(sqlId);
        str.append(" : ");
        str.append(sql);
        str.append(" : ");
        str.append(time);
        str.append(" ms");
        return str.toString();
    }

    private  String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private  String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 配置　plugin时配置的参数
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        if (null != properties) {
            if (properties.containsKey("showSql")) {
                this.showSql = Boolean.valueOf(properties.getProperty("showSql"));
            }
            if (properties.containsKey("longQueryTimeMillis")) {
                this.longQueryTimeMillis = Long.valueOf(properties.getProperty("longQueryTimeMillis"));
            }
        }
    }
}