package com.sxp.yyjhservice.dao.datasource;

import com.sxp.yyjhservice.domain.datasource.TDatasource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TDatasourceMapperTest {

    @Autowired
    private TDatasourceMapper tDatasourceMapper;
    @Test
    public void delTDataSourceById() {
        tDatasourceMapper.delTDataSourceById(1);
    }

    @Test
    public void addTDataSource() {
        TDatasource td = new TDatasource();
        td.setId(1);
        td.setDatabaseName("testdb1");
        td.setCreatetime(new Date());
        td.setUpdatetime(new Date());
        td.setUsername("admin");
        tDatasourceMapper.addTDataSource(td);
    }

    @Test
    public void findTDataSourceById() {
        System.out.println(tDatasourceMapper.findTDataSourceById(1));
    }

    @Test
    public void updTDataSourceById() {
        TDatasource td = new TDatasource();
        td.setId(1);
        td.setUpdatetime(new Date());
        td.setUsername("testadmin");
        tDatasourceMapper.updTDataSourceById(td);
    }

    @Test
    public void getAll() {
    }
}