package com.simple.frame.dao.spi.pager;


import java.util.List;
import java.util.Map;

import com.simple.frame.dao.spi.ICommonDao;
import com.simple.frame.dao.spi.IPager;
import com.simple.frame.dao.utils.Id;
import com.simple.frame.dao.utils.ParameterUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiang.lv@hnari.com
 * @date Create in 11:01 2017/11/14
 * @description
 **/
@Slf4j
public class PagerBuilder {

    public static  <T> void processingDataPager(IPager pager, ICommonDao commonDao, Class<T> clzss, Integer batchSize, Object... keyValue) {
        Id id = commonDao.getMinIdAndMaxId(clzss, keyValue);
        log.info("开始处理数据id={}", id);
        for (int i = id.getMinId(); i <= id.getMaxId(); ) {
            Map parameterMap = ParameterUtils.map(keyValue);
            parameterMap.put("batchSize",batchSize);
            parameterMap.put("startIndex",i);
            List<T> dataList =  commonDao.getDataList(clzss, parameterMap);
            if (null != dataList && dataList.size() > 0) {
                pager.processingData(dataList);
            }
            log.info("本批次数据处理完成startId={},dataSize={}", i, dataList.size());
            i = null == dataList ? pager.getMaxId(dataList) : i + batchSize;
        }
    }
}
