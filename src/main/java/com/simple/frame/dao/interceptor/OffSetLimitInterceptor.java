package com.simple.frame.dao.interceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by lvxiang@ganji.com 2019/1/21  12:08
 *
 * @author <a href="mailto:lvxiang@ganji.com">simple</a>
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffSetLimitInterceptor implements Interceptor, Serializable {
    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    void processIntercept(final Object[] queryArgs) {
        // queryArgs = query(MappedStatement ms, Object parameter, RowBounds
        // rowBounds, ResultHandler resultHandler)
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        if (dialect.supportLimit()
                && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();
            if (dialect.supportLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            limit = RowBounds.NO_ROW_LIMIT;
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),
                    sql, boundSql.getParameterMappings(), boundSql
                    .getParameterObject());
            MappedStatement newMs = copyFromMappedStatement(ms,
                    new BoundSqlSqlSource(newBoundSql));
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        }
    }

    // see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms
                .getConfiguration(), ms.getId(), newSqlSource, ms
                .getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            builder.keyProperty(Arrays.stream(ms.getKeyProperties()).collect(Collectors.joining(",")));
        }
        // setStatementTimeout()
        builder.timeout(ms.getTimeout());
        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());
        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                    "cannot create dialect instance by dialectClass:"
                            + dialectClass, e);
        }
        System.out.println(OffSetLimitInterceptor.class.getSimpleName()
                + ".dialect=" + dialectClass);
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}

