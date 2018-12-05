package com.simple.frame.dao.service.impl;


import com.simple.frame.dao.spi.ICommonDao;

/**
 * Created by simple on 2016/11/11.
 */
public class CommonServiceImpl extends  DefaultServiceImpl {

    private ICommonDao commonDao;
    @Override
    public ICommonDao getCommonDao() {
        return commonDao;
    }
    @Override
    public   void setCommonDao( ICommonDao commonDao){
        this.commonDao =commonDao;
    }


}
