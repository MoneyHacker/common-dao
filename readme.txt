通用Dao组件
一个链接可以查询多个数据表
详情请查看示例
具体暴露接口,可查看IMapper类,可使用内置方法,也可添加自定义SQL


 /**
     * 一个链接可以查所有表
     */
    @Resource
    private ICommonDao commonDao;

    /**
     * 其它CURD方法差不多,具体可以看IMapper接口
     */
    @Test
    public void test() {
        SysUser user = commonDao.get(1L, SysUser.class);
        Assert.assertNotNull(user);
        //根据内置sql查询,按条件查询
        user = commonDao.get(SysUser.class,"userName","aaa");
        Assert.assertNotNull(user);
        //根据自己定义sql查询,sqlId是在xml的sqlId
        List<SysUser> userList =  commonDao.getListBySqlId(SysUser.class, "selectByIds","list", Arrays.asList(1L,2L));
        Assert.assertNotNull(userList);

    }
