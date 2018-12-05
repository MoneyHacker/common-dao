package com.simple.frame.dao.service.impl;



import com.simple.frame.dao.service.ICommonService;
import com.simple.frame.dao.utils.Id;
import com.simple.frame.dao.utils.Pager;
import com.simple.frame.dao.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * @author lvxiang
 * @date 2015年5月13日 下午5:31:09
 * 无状态,相当一个代理,最终转到Dao执行
 * 此类上没有加Service,避免Service 被继承时,service 需要Bean配置问题
 * 说明:对于spring-mybatis-1.0.2  spring-mybatis-1.2.2在不同的mybaits版本会不兼容
 */
public abstract class DefaultServiceImpl implements ICommonService {

    @Override
    public <T, V> List<V> getListBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getListBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T, V> List<V> getListFromMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getListFromMasterBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T> Integer updateBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().updateBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T, PK> Integer updateById(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().updateById(clzss,keyValuePair);
    }
    @Override
    public <T> List<T> getListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        return getCommonDao().getListBySqlId(clazz, sqlId, params, query);
    }

    @Override
    public <T> List<T> getMasterListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        return getCommonDao().getMasterListBySqlId(clazz, sqlId, params, query);
    }

    @Override
    public <T> Integer updateBySqlId(String sqlId, T paramBeam) {
        return getCommonDao().updateBySqlId(sqlId, paramBeam);
    }

    @Override
    public <PK, T, V> V get(final PK id, Class<T> clzss) {
        return getCommonDao().get(id, clzss);
    }

    @Override
    public <PK, T> int delete(PK id, Class<T> clzss) {
        return getCommonDao().delete(id, clzss);
    }

    @Override
    public <T> int save(T target) {
        return getCommonDao().save(target);
    }

    @Override
    public <T> int save(List<T> target) {
        return getCommonDao().save(target);
    }

    @Override
    public <T> int save(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().save(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T> int update(T target) {
        return getCommonDao().update(target);
    }

    @Override
    public <T> int update(List<T> beanList) {
        return getCommonDao().update(beanList);
    }
    @Override
    public <PK, T, V> List<V> get(List<PK> ids, Class<T> clzss) {
        return getCommonDao().get(ids, clzss);
    }

    @Override
    public <PK, T> List<T> getListByField(List<PK> key, String fieldName, Class<T> clzss) {
        return getCommonDao().getListByField(key, fieldName, clzss);
    }

    public <PK, T, V> List<V> findMasterByIds(List<PK> ids, Class<T> clzss) {
        return getCommonDao().getFromMaster(ids, clzss);
    }

    @Override
    public <T, V> V get(T bean) {
        return getCommonDao().get(bean);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object paramsObj) {
        return getCommonDao().getList(clzss, paramsObj);
    }

    @Override
    public <T> Id getMinIdAndMaxId(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getMinIdAndMaxId(clzss,keyValuePair);
    }

    @Override
    public <T, V> List<V> getDataList(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getDataList(clzss, keyValuePair);
    }
    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getList(clzss, keyValuePair);
    }


    @Override
    public <T, V> List<V> getByMap(Map<?, ?> map, Class<T> clzss) {
        return getCommonDao().getByMap(map, clzss);
    }


    @Override
    public <T, V> List<V> getList(T bean) {
        return getCommonDao().getList(bean);
    }


    @Override
    public <PK, T, V> V getFromMaster(PK key, Class<T> clzss) {
        return getCommonDao().getFromMaster(key, clzss);
    }


    @Override
    public <PK, T, V> List<V> getFromMaster(List<PK> key, Class<T> clzss) {
        return getCommonDao().getFromMaster(key, clzss);
    }


    @Override
    public <T, V> V getFromMaster(T bean) {
        return getCommonDao().getFromMaster(bean);
    }


    @Override
    public <T, V> List<V> getListFromMaster(T bean) {
        return getCommonDao().getListFromMaster(bean);
    }

    @Override
    public <T, V> V getFromMaster(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getFromMaster(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object paramsObj) {
        return getCommonDao().getListFromMaster(clzss, paramsObj);
    }

    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getListFromMaster(clzss, keyValuePair);
    }

    @Override
    public <T, V> V get(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().get(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss) {
        return getCommonDao().getList(clzss);
    }

    @Override
    public <T, V> V getBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T, V> V getMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getMasterBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <PK, T> int delete(List<PK> ids, Class<T> clzss) {
        return getCommonDao().delete(ids, clzss);
    }

    @Override
    public <T> int delete(T bean) {
        return getCommonDao().delete(bean);
    }

    @Override
    public <T> int deleteBySqlId(String sqlId, T bean) {
        return getCommonDao().deleteBySqlId(sqlId, bean);
    }

    @Override
    public <T> int delete(Class<T> clzss, Object... params) {
        return getCommonDao().delete(clzss, params);
    }

    @Override
    public <T> int deleteBySqlId(Class<T> clzss, final String sqlId, Object... keyValuePair) {
        return getCommonDao().deleteBySqlId(clzss, sqlId, keyValuePair);
    }


    /**
     * 使用默认的查询id查询分页
     *
     * @param bean
     * @param query
     * @return
     */
    @Override
    public <T, V> Pager<V> getMasterPager(T bean, Query query) {
        return getCommonDao().getMasterPager(bean, query);
    }

    @Override
    public <V, T> Pager<V> getPager(Class<T> clzss, Query query, String countSqlId, String dataSqlId, Object... param) {
        return getCommonDao().getPager(clzss,  query,  countSqlId,  dataSqlId, param);
    }

    @Override
    public <T> Pager<T> getPager(T bean, Query query) {
        return getCommonDao().getPager(bean, query);
    }

    @Override
    public <T> Pager<T> getPager(T bean, Query query, Map param) {
        return getCommonDao().getPager(bean, query, param);
    }

    @Override
    public <V,T> Pager<V> getPager(T params, Query query,
                                        String countSql, String dataSql) {
        return getCommonDao().getPager(params, query, countSql, dataSql);
    }

    @Override
    public <T> Pager<T> getPager(Class<T> clzss, Query query, Map param) {
        return getCommonDao().getPager(clzss, query, param);
    }

    @Override
    public <T> Integer batchInsert(final Integer batchSize, List<T> beanList) {
        if (null == beanList && 0 == beanList.size()) {
            return 0;
        }

        int count = 0;
        for (int startIndex = 0, endIndex = 0, size = beanList.size(); endIndex < size; startIndex = endIndex) {
            endIndex += batchSize;
            endIndex = endIndex > beanList.size() ? beanList.size() : endIndex;
            count += getCommonDao().save(beanList.subList(startIndex, endIndex));
        }
        return count;
    }

}
