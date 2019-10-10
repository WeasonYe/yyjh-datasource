package com.sxp.yyjhservice.config.shiro;

import com.sxp.yyjhservice.domain.auth.TPermission;
import com.sxp.yyjhservice.domain.auth.TRole;
import com.sxp.yyjhservice.domain.user.TUser;
import com.sxp.yyjhservice.service.auth.TPermissionService;
import com.sxp.yyjhservice.service.auth.TRoleService;
import com.sxp.yyjhservice.service.user.TUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    @Qualifier("userservice")
    private TUserService tUserService;
    @Autowired
    @Qualifier("roleservice")
    private TRoleService tRoleService;
    @Autowired
    @Qualifier("perservice")
    private TPermissionService tPermissionService;
    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {//进行权限验证
        //获取登录用户名
        String name= principals.getPrimaryPrincipal().toString();
        //查询用户
        TUser user = tUserService.findTUserByLogid(name);
        //查询角色
        List<TRole> roles = tRoleService.findTRoleByLogid(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (TRole role:roles) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRolename());
            //查询权限
            List<TPermission> permissions = tPermissionService.findTPermissionByRoleid(role.getId());
            for (TPermission permission:permissions) {
                //添加权限
                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {//进行身份验证的(登录验证)
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name= token.getPrincipal().toString();
        //查询用户
        TUser user = tUserService.findTUserByLogid(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证token和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, user.getPassword(),getName());
            return simpleAuthenticationInfo;
        }
    }
}