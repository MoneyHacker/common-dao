package com.simple.frame.dao.service;



import com.simple.frame.dao.spi.ICommonDao;
import com.simple.frame.dao.spi.IMapper;

import java.util.List;

/**
 * 
 * @author lvxiang
 * @date   2015年5月13日 下午5:18:03
 * 通用Service
 */
public interface ICommonService extends IMapper {
	ICommonDao getCommonDao();

	void setCommonDao(ICommonDao commonDao);

	/**
	 * 批量处理数据,非事务
	 * @param batchSize
	 * @param beanList
	 * @return
	 */
	<T> Integer batchInsert(final Integer batchSize, List<T> beanList);
}
