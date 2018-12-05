package com.simple.frame.dao.spi.impl;

import com.simple.frame.dao.jdbc.handler.RowBeanMapper;
import com.simple.frame.dao.spi.ICommonDao;
import com.simple.frame.dao.utils.Id;
import com.simple.frame.dao.utils.Pager;
import com.simple.frame.dao.utils.ParameterUtils;
import com.simple.frame.dao.utils.Query;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;


import java.util.*;


/**
 * @author lvxiang
 * @date 2015年5月13日 下午5:12:12
 * 所有数据库操作实现
 * DefaultDaoImpl 应该叫 AbstractDaoImpl
 */
public abstract class DefaultDaoImpl implements ICommonDao {
    /**
     * 从库用的SqlSession,不要暴露SqlSessionTemplate,隐藏对应的方法
     */
    abstract SqlSessionTemplate getSqlSessionQueryTemplate();

    /**
     *     主库用的SqlSession
     */
    abstract  SqlSessionTemplate getSqlSessionTemplate();



    @Override
    public <T, V> List<V> getListBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return selectList(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValuePair), false);
    }

    @Override
    public <T> Id getMinIdAndMaxId(Class<T> clzss, Object... keyValuePair) {
         Map<String,Integer> resultMap  = getSqlSession(false).selectOne(getMapperName(clzss) + SELECT_MINIDANDMAXID,
                 ParameterUtils.map(keyValuePair));
         if (null == resultMap || null == resultMap.get("minId")) {
             return Id.empty();
         }
         Id id = new Id();
         id.setMinId(Integer.valueOf(String.valueOf(resultMap.get("minId"))));
         id.setMaxId(Integer.valueOf(String.valueOf(resultMap.get("maxId"))));
         return id;
    }

    @Override
    public <T, V> List<V> getDataList(Class<T> clzss, Object... keyValuePair) {
        Map map =  ParameterUtils.map(keyValuePair);
        if (null == map || !map.containsKey("startIndex") || !map.containsKey("batchSize")) {
            throw  new IllegalArgumentException("缺少必须参数startIndex和batchSize");
        }
        return selectList(getMapperName(clzss) + SELECT_DATALIST, ParameterUtils.map(keyValuePair), false);
    }

    @Override
    public <T, V> List<V> getListFromMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return selectList(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValuePair), true);
    }



    @Override
    public <T> Integer updateBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        Map paramMap = keyValuePair.length == 1 ? ParameterUtils.map(keyValuePair) : ParameterUtils.map(keyValuePair);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(clzss) + sqlId + "update 参数不能为空,没有参数可加上1=1");
        }
        return update(getMapperName(clzss) + sqlId, paramMap);
    }

    @Override
    public <T,PK> Integer updateById(Class<T> clzss,  Object... keyValuePair) {
        Map paramMap = keyValuePair.length == 1 ? ParameterUtils.map(keyValuePair) : ParameterUtils.map(keyValuePair);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(clzss) + UPDATE_BY_PRIMARYKEY_ID + "update 参数不能为空,没有参数可加上1=1");
        }
        return update(getMapperName(clzss) + UPDATE_BY_PRIMARYKEY_ID, paramMap);
    }

    @Override
    public <T> Integer updateBySqlId(String sqlId, T paramBeam) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(paramBeam);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(paramBeam.getClass()) + sqlId + "update 参数不能为空,没有参数可加上1=1");
        }
        return update(getMapperName(paramBeam.getClass()) + sqlId, paramMap);
    }

    @Override
    public <T> List<T> getListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(params);
        return getSqlSession(false).selectList(getMapperName(clazz) + sqlId, paramMap, new RowBounds(query.getOffset(), query.getPageSize()));
    }

    @Override
    public <T> List<T> getMasterListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(params);
        return getSqlSession(true).selectList(getMapperName(clazz) + sqlId, paramMap, new RowBounds(query.getOffset(), query.getPageSize()));
    }

    /**
     * 从库查询
     *
     */
    private <T> T selectOne(String statement, Object parameter, boolean master) {
        return this.getSqlSession(master).selectOne(statement, parameter);
    }

    /**
     * 主库查询(查询及时信息)
     *
     * @param <T> public <T> T selectMasterOne (String statement, Object parameter) {
     *            return this.getSqlSession().selectOne(statement, parameter);
     *            }
     */
    @Override
    public <T, V> V getMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValueParams), true);
    }


    /**
     * 从库查询
     *
     * @param <T>
     */
    private <T> List<T> selectList(String statement) {
        return this.getSqlSession(false).selectList(statement);
    }

    private SqlSession getSqlSession(boolean master) {
        return (master ? this.getSqlSessionTemplate() : this.getSqlSessionQueryTemplate());
    }

    /**
     * 不返回NULL,返回空List
     *
     * @param statement
     * @param parameter
     * @param master    是否走主库
     * @param <T>
     * @return
     */
    private <T> List<T> selectList(String statement, Object parameter, boolean master) {
        List<T> resultList = getSqlSession(master).selectList(statement, parameter);
        return null == resultList ? Collections.emptyList() : resultList;
    }


    private int insert(String statement, Object parameter) {
        return this.getSqlSession(true).insert(statement, parameter);
    }
    public <T> int insert(T target) {
        return insert(getMapperName(target.getClass()) + INSERT_SELECTIVE_ID, target);
    }

    public <T> int insert(List<T> target) {
        return insert(getMapperName(target.get(0).getClass()) + INSERT_BATCH_ID, target);
    }

    @Override
    public <T> int deleteBySqlId(String sqlId, T bean) {
        Map map = ParameterUtils.convertBeanToMap(bean);
        return delete(getMapperName(bean.getClass()) + sqlId, map);
    }

    /**
     * 主库更新
     */
    public int update(String statement, Object parameter) {
        if (parameter != null) {
            return this.getSqlSession(true).update(statement, parameter);
        }
        return 0;
    }


    /**
     * all delete medthod with parameter deal here
     * 主库删除
     */
    public int delete(String statement, Object parameter) {
        if (parameter != null) {
            if (parameter instanceof Map) {
                if (((Map) parameter).size() == 0) {
                    throw new IllegalArgumentException(statement + " delete 参数不能为空,没有参数可以加上1=1");
                }
            }
            return this.getSqlSession(true).delete(statement, parameter);
        }
        return 0;
    }

    /**
     * @param params
     * @return
     */
    public int deleteByParamsObjs(Object params) {
        return delete(getMapperName(params.getClass()) + DELETE_BY_MAP_ID, ParameterUtils.convertBeanToMap(params));
    }


    /**
     * 查询分页（从库）
     *
     * @param <T>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    private <T> Pager<T> getPagerObjs(Object params, Query query, String countSql, String dataSql) {
        int count = getSqlSession(false).selectOne(countSql, params);
        List<T> datas = getSqlSession(false).selectList(dataSql, params, new RowBounds(query.getOffset(), query.getPageSize()));
        Pager<T> pager = new Pager<T>(query.getPage(), query.getPageSize(), count, datas);
        return pager;
    }


    /**
     * 查询分页（主库）
     *
     * @param <T>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    private <T> Pager<T> getMasterPagerObjs(Object params, Query query, String countSql, String dataSql) {
        int count = getSqlSession(true).selectOne(countSql, params);
        List<T> dataList= getSqlSession(true).selectList(dataSql, params, new RowBounds(query.getOffset(), query.getPageSize()));
        Pager<T> pager = null;
        if (count > 0) {
            pager = new Pager(query.getPage(), query.getPageSize(), count, dataList);
        } else {
            pager = new Pager(1, query.getPageSize(), 0);
        }
        return pager;
    }


    @Override
    public <PK, T, V> V get(PK key, Class<T> clzss) {
        return this.selectOne(getMapperName(clzss) + SELECT_BY_PRIMARYKEY_ID, key, false);
    }

    @Override
    public <T, V> V get(T bean) {
        return selectOne(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), false);
    }

    @Override
    public <T, V> List<V> getList(T bean) {
        return selectList(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), false);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object paramsObj) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(paramsObj), false);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID);
    }

    @Override
    public <T, V> V get(Class<T> clzss, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), false);
    }

    @Override
    public <T, V> V getBySqlId(Class<T> clzss, String sqlId, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValueParams), false);
    }


    @Override
    public <PK, T> int delete(PK key, Class<T> clzss) {
        return delete(getMapperName(clzss) + DELETE_BY_PRIMARYKEY_ID, key);
    }

    @Override
    public <T> int delete(T bean) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(bean);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException("delete 参数不能为空");
        }
        return delete(getMapperName(bean.getClass()) + DELETE_BY_MAP_ID, paramMap);
    }


    @Override
    public <T> int delete(Class<T> clzss, Object... params) {
        Map paramMap = ParameterUtils.map(params);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException("delete 参数不能为空 "+ Arrays.toString(params));
        }
        return delete(getMapperName(clzss) + DELETE_BY_MAP_ID, paramMap);
    }


    @Override
    public <T> int deleteBySqlId(Class<T> clzss, final String sqlId, Object... params) {
        return delete(getMapperName(clzss) + sqlId, ParameterUtils.map(params));
    }


    @Override
    public <T> int save(T bean) {
        return insert(getMapperName(bean.getClass()) + INSERT_SELECTIVE_ID, bean);
    }


    @Override
    public <T> int save(List<T> beanList) {
        return insert(getMapperName(beanList.get(0).getClass()) + INSERT_BATCH_ID, beanList);
    }

    @Override
    public <T> int save(Class<T> clzss, String sqlId, Object... keyValueParams) {
        Map paramMap = ParameterUtils.map(keyValueParams);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(clzss) + sqlId + " save 参数不能为空");
        }
        return insert(getMapperName(clzss) + sqlId, paramMap);
    }


    @Override
    public <T> int update(T target) {
        return update(getMapperName(target.getClass()) + UPDATE_BY_PRIMARYKEY_ID, target);
    }
    @Override
    public <T> int update(List<T> beanList) {
        return update(getMapperName(beanList.get(0).getClass()) + UPDATE_BATCH, beanList);
    }

    @Override
    public <PK, T, V> V getFromMaster(PK key, Class<T> clzss) {
        return selectOne(getMapperName(clzss) + SELECT_BY_PRIMARYKEY_ID, key, true);
    }


    @Override
    public <T, V> V getFromMaster(T bean) {
        return selectOne(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(T bean) {
        return selectList(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), true);
    }


    @Override
    public <T, V> V getFromMaster(Class<T> clzss, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object... keyValueParams) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object paramsObj) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(paramsObj), true);
    }

    @Override
    public <PK, T, V> List<V> get(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_IDS_ID, key, false);
    }


    @Override
    public <PK, T> List<T> getListByField(List<PK> key, String field, Class<T> clzss) {
        String fieldColumnName = RowBeanMapper.underscoreName(field);
        if (StringUtils.isEmpty(fieldColumnName)) {
            throw new IllegalArgumentException(" field not mapping to column ");
        }
        Map<String, Object> paramsObj = new HashMap();
        paramsObj.put("_fieldName", fieldColumnName);
        paramsObj.put("list", key);
        return selectList(getMapperName(clzss) + SELECT_BY_FIELD, paramsObj, false);

    }

    public <PK, T> List<T> getListByField(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_FIELD, key, false);
    }

    @Override
    public <PK, T> int delete(List<PK> key, Class<T> clzss) {
        return delete(getMapperName(clzss) + DELETE_BY_IDS_ID, key);
    }


    @Override
    public <PK, T, V> List<V> getFromMaster(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_IDS_ID, key, true);
    }


    @Override
    public <T, V> List<V> getByMap(Map<?, ?> map, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, map, false);
    }


    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object... params) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(params), false);
    }


    @Override
    public <T, V> Pager<V> getMasterPager(T bean, Query query) {
        return getMasterPagerObjs(ParameterUtils.convertBeanToMap(bean), query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);

    }

    @Override
    public <T> Pager<T> getPager(T bean, Query query) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        return getPagerObjs(params, query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <T> Pager<T> getPager(Class<T> clzss, Query query, Map param) {
        return getPagerObjs(param, query, getMapperName(clzss) + SELECT_PAGECOUNT_ID, getMapperName(clzss) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <T> Pager<T> getPager(T bean, Query query, Map param) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        //添加另外的参数
        params.putAll(param);
        return getPagerObjs(params, query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <V,T> Pager<V> getPager(T bean, Query query, String countSql, String dataSql) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        if(countSql.indexOf(".") > 0) {
            //兼容老版本不规范的写法
            return getPagerObjs(params, query,  countSql, dataSql);
        } else {
         return getPagerObjs(params, query,  getMapperName(bean.getClass()) + countSql, getMapperName(bean.getClass()) +dataSql);

        }
    }

    @Override
    public <V,T> Pager<V> getPager(Class<T> clzss, Query query, String countSqlId, String dataSqlId, Object... param) {
        Map params = ParameterUtils.map(param);
        return getPagerObjs(params, query, getMapperName(clzss) + countSqlId, getMapperName(clzss) +dataSqlId);
    }



}
