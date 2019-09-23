package com.sxp.yyjhservice.dao.datasource;

import com.sxp.yyjhservice.domain.datasource.TDatasource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDatasourceMapper {
    int delTDataSourceById(Integer id);

    int addTDataSource(TDatasource record);

    TDatasource findTDataSourceById(Integer id);

    int updTDataSourceById(TDatasource record);

    List<TDatasource> getAll();
}