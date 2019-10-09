package com.sxp.yyjhservice.service.datasource;

import com.sxp.yyjhservice.domain.datasource.TDatasource;

import java.util.List;

public interface TDatasourceService {
    boolean delTDataSourceById(Integer id);

    boolean addTDataSource(TDatasource record);

    TDatasource findTDataSourceById(Integer id);

    boolean updTDataSourceById(TDatasource record);

    List<TDatasource> getAll();

    Integer getTotal();

    List<TDatasource> getTDatasourceListPage(TDatasource record);

    boolean batchDeleteById(List<Integer> idsList);
}
