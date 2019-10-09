package com.sxp.yyjhservice.service.auth;

import com.sxp.yyjhservice.domain.auth.TPermission;

import java.util.List;

public interface TPermissionService {
    boolean delTPermissionById(Integer id);

    boolean addTPermission(TPermission record);

    TPermission findTPermissionById(Integer id);

    boolean updTPermissionById(TPermission record);

    List<TPermission> getAll();

    TPermission findTPermissionByName(String permission);

    List<TPermission> findTPermissionByRoleid(Integer id);
}
