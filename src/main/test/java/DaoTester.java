/**
 * Created by lvxiang@ganji.com 2018/12/5 14:14
 *
 * @author <a href="mailto:lvxiang@ganji.com">simple</a>
 */
import com.simple.frame.dao.spi.ICommonDao;
import com.simple.module.SysDict;
import com.simple.module.SysUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:config/spring-config.xml"}) //加载配置文件
//------------如果加入以下代码，所有继承该类的测试类都会遵循该配置，也可以不加，在测试类的方法上///控制事务，参见下一个实例
//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！
//@Transactional
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//------------
public class DaoTester {
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
    @Test
    public void testDict() {
        SysDict user = commonDao.get(1L, SysDict.class);
        Assert.assertNotNull(user);
    }
}
