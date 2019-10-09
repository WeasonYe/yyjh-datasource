package com.sxp.yyjhservice.service.auth;

import com.sxp.yyjhservice.dao.auth.TUserRoleMapper;
import com.sxp.yyjhservice.domain.auth.TUserRole;
import com.sxp.yyjhservice.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("urservice")
public class TUserRoleServiceImpl implements TUserRoleService {
    @Autowired
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public boolean delTUserRoleById(Integer id) {
        int count = tUserRoleMapper.delTUserRoleById(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addTUserRole(TUserRole record) {
        int count = tUserRoleMapper.addTUserRole(record);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public TUserRole findTUserRoleById(Integer id) {
        return tUserRoleMapper.findTUserRoleById(id);
    }

    @Override
    public boolean updTUserRoleById(TUserRole record) {
        int count = tUserRoleMapper.updTUserRoleById(record);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<UserRoleVO> getAll() {
        return tUserRoleMapper.getAll();
    }

    @Override
    public List<TUserRole> findTUserRoleByU_Id(Integer u_id) {
        return tUserRoleMapper.findTUserRoleByU_Id(u_id);
    }
}
