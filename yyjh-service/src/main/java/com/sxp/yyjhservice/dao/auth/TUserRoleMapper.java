package com.sxp.yyjhservice.dao.auth;

import com.sxp.yyjhservice.domain.auth.TUserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface TUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);
}