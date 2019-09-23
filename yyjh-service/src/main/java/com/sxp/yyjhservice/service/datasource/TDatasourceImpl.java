package com.sxp.yyjhservice.service.datasource;

import com.sxp.yyjhservice.dao.datasource.TDatasourceMapper;
import com.sxp.yyjhservice.domain.datasource.TDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@Slf4j
@Transactional
public class TDatasourceImpl implements TDatasourceService {

    @Autowired
    private TDatasourceMapper tDatasourceMapper;

    @Override
    public boolean delTDataSourceById(Integer id) {
        int count=tDatasourceMapper.delTDataSourceById(id);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public boolean addTDataSource(TDatasource record) {
        int count=tDatasourceMapper.addTDataSource(record);
        if (count>0)
            return true;
        return false;

    }

    @Override
    public TDatasource findTDataSourceById(Integer id) {
        return tDatasourceMapper.findTDataSourceById(id);
    }

    @Override
    public boolean updTDataSourceById(TDatasource record) {
        int count=tDatasourceMapper.updTDataSourceById(record);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public List<TDatasource> getAll() {
        return tDatasourceMapper.getAll();
    }
}
