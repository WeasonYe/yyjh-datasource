package com.sxp.yyjhservice.dao.auth;

import com.sxp.yyjhservice.domain.auth.TRolePermission;
import org.springframework.stereotype.Repository;

@Repository
public interface TRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRolePermission record);

    int insertSelective(TRolePermission record);

    TRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRolePermission record);

    int updateByPrimaryKey(TRolePermission record);
}