package com.sxp.yyjhservice.service.auth;

import com.sxp.yyjhservice.domain.auth.TRole;

import java.util.List;

public interface TRoleService {
    boolean delTRoleById(Integer id);

    boolean addTRole(TRole record);

    TRole findTRoleById(Integer id);

    boolean updTRoleById(TRole record);

    List<TRole> getAll();

    TRole findTRoleByRolename(String rolename);

    List<TRole> findTRoleByLogid(String logid);
}
