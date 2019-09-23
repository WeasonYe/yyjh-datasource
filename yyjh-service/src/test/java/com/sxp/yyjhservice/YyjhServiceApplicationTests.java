package com.sxp.yyjhservice;

import com.sxp.yyjhservice.config.jedis.JedisUtil;
import com.sxp.yyjhservice.dao.user.TUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.sxp.yyjhservice")
public class YyjhServiceApplicationTests {

    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private TUserMapper tUserMapper;
    @Test
    public void contextLoads() {
        System.out.println(jedisUtil.get("jedis_key1"));
    }

    @Test
    public void testMybatis(){
        System.out.println(tUserMapper.selectByPrimaryKey(1));
    }

}
