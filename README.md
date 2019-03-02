# 一 通用Dao组件,一个链接可以查询多个数据表
- 每张表只需要生成Bean和xml文件即可
- 一个Dao可以查询同一个链接池关联数据库下所有表

# 一 示例,具体其它方法可以查看ICommonDao接口

    @Resource
    private ICommonDao commonDao;
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