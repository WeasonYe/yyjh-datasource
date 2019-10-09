package com.sxp.yyjhservice.service.auth;

import com.sxp.yyjhservice.dao.auth.TRolePermissionMapper;
import com.sxp.yyjhservice.domain.auth.TRolePermission;
import com.sxp.yyjhservice.vo.RolePerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("rpservice")
public class TRolePermissionServiceImpl implements TRolePermissionService {
    @Autowired
    private TRolePermissionMapper tRolePermissionMapper;

    @Override
    public boolean delTRolePerById(Integer id) {
        int count = tRolePermissionMapper.delTRolePerById(id);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public boolean addTRolePer(TRolePermission record) {
        int count = tRolePermissionMapper.addTRolePer(record);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public TRolePermission findTRolePerById(Integer id) {
        return tRolePermissionMapper.findTRolePerById(id);
    }

    @Override
    public boolean updTRolePerById(TRolePermission record) {
        int count = tRolePermissionMapper.updTRolePerById(record);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public List<RolePerVO> getAll() {
        return tRolePermissionMapper.getAll();
    }

    @Override
    public List<TRolePermission> findTRolePerByR_id(Integer id) {
        return tRolePermissionMapper.findTRolePerByR_id(id);
    }
}
