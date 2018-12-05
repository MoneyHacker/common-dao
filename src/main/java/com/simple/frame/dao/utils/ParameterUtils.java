package com.simple.frame.dao.utils;


import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simple on 2016/11/17 19:36
 *
 * @mail moneyhacker@163.com
 * @since 1.0
 */
@Slf4j
final public class ParameterUtils {

    private ParameterUtils() {
    }

    public static <T> T newInstance(Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[]args) {
      Map map =  map("putId",1212,"rere",332);
      System.out.println(map.size());
    }
    /**
     * 根据参数构造map，参数必须为偶数个，依次为key1，value1，key2，value2……. key是Bean 的属性字段
     *
     * @param datas 参数列表
     * @return 构造出的map
     */
    public static Map map(final Object... datas) {
        //当只有一个参数时,先判断是否是Map?若不是则认为是Bean
        if (1 == datas.length) {
            if (datas[0] instanceof Map) {
                return (Map) datas[0];
            } else {
                return convertBeanToMap(datas[0]);
            }
        }

        if (datas.length % 2 != 0) {
            throw new IllegalArgumentException("wrong nubmer parameters ,must be  even number ");
        }
        final Map map = new HashMap();
        for (int i = 0; i < datas.length; i += 2) {
            if (null == datas[i + 1]) {
                log.warn("查询字段{}  值为null",datas[i]);
            }
            map.put(datas[i], datas[i + 1]);
        }
        return map;
    }


    /**
     * 当bean是泛型时,处理需要注意
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> convertBeanToMap(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("参数bean不能为空");
        }
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (bean instanceof Map) {
            return (Map<String, Object>) bean;
        }
        if (bean.getClass().isArray()) {
            //泛型数据类型.
            Object[] beans = (Object[]) bean;
            if (beans.length == 1 && beans[0] instanceof Map) {
                //只有一个参数,且第一个参数是map
                return (Map<String, Object>) beans[0];
            }
            //多个实体bean类型,或都多种参数的组合,
            boolean isPraramValue = false;
            String paramKey = null;
            for (Object objBean : beans) {
                if (isPraramValue) {
                    //上一个参数为key,此时合法的参数类型应该为基本数据+String+List
                    returnMap.put(paramKey, objBean);
                    paramKey = null;
                    isPraramValue = false;
                } else if (objBean instanceof String) {
                    //则下一个参数必须为该参数的值
                    paramKey = objBean.toString();
                    isPraramValue = true;
                } else if (objBean instanceof Map) {
                    //参数为map
                    returnMap.putAll((Map<String, String>) objBean);
                } else {
                    //参数为普通bean
                    addBeanPropertiesToMap(objBean, returnMap);
                }
            }
            if (isPraramValue) {
                throw new IllegalArgumentException("参数不配对,key=" + paramKey + " 没有对象的参数");
            }
        } else {
            //bean为普通实体对象
            addBeanPropertiesToMap(bean, returnMap);
        }
        return returnMap;
    }

    /**
     * 将实体类 bean的所有非空属性放到paramterMap中,key-value:属性名称－属性值
     *
     * @param bean
     * @param paramterMap
     * @since 1.6+
     */
    public static <T> void addBeanPropertiesToMap(T bean, Map<String, Object> paramterMap) {
        //参数为实体对象
        Class type = bean.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        if (result instanceof String && result.toString().trim().length() == 0) {
                            continue;
                        }
                        paramterMap.put(propertyName, result);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("将 JavaBean : " + bean.getClass().getSimpleName() + " 转化为 Map失败！", e);
        }
    }
}
