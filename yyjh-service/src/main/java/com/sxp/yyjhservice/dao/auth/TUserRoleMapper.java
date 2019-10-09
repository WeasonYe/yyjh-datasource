package com.sxp.yyjhservice.dao.auth;

import com.sxp.yyjhservice.domain.auth.TUserRole;
import com.sxp.yyjhservice.vo.UserRoleVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TUserRoleMapper {
    int delTUserRoleById(Integer id);

    int addTUserRole(TUserRole record);

    TUserRole findTUserRoleById(Integer id);

    int updTUserRoleById(TUserRole record);

    List<UserRoleVO> getAll();

    List<TUserRole> findTUserRoleByU_Id(Integer u_id);
}