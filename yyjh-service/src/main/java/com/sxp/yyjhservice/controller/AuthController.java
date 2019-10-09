package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sxp.yyjhservice.domain.auth.TPermission;
import com.sxp.yyjhservice.domain.auth.TRole;
import com.sxp.yyjhservice.domain.auth.TRolePermission;
import com.sxp.yyjhservice.domain.auth.TUserRole;
import com.sxp.yyjhservice.domain.user.TUser;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.auth.TPermissionService;
import com.sxp.yyjhservice.service.auth.TRolePermissionService;
import com.sxp.yyjhservice.service.auth.TRoleService;
import com.sxp.yyjhservice.service.auth.TUserRoleService;
import com.sxp.yyjhservice.service.user.TUserService;
import com.sxp.yyjhservice.vo.ControllerResult;
import com.sxp.yyjhservice.vo.RolePerVO;
import com.sxp.yyjhservice.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/authority")
public class AuthController {
    @Autowired
    @Qualifier("urservice")
    private TUserRoleService tUserRoleService;

    @Autowired
    @Qualifier("rpservice")
    private TRolePermissionService tRolePermissionService;

    @Autowired
    @Qualifier("userservice")
    private TUserService tUserService;

    @Autowired
    @Qualifier("roleservice")
    private TRoleService tRoleService;

    @Autowired
    @Qualifier("perservice")
    private TPermissionService tPermissionService;

    @RequestMapping("/getUserRoles")
    public Object getUserRole() {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());

        List<TUser> userList = tUserService.getAll();
        List<TRole> roleList = tRoleService.getAll();
        List<UserRoleVO> urList = tUserRoleService.getAll();

        Map<String, Object> payload = new HashMap<>();
        payload.put("users", userList);
        payload.put("urs", urList);
        if (roleList.size() != 0 && urList.size() != 0) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(payload);
        }
        return result;
    }

    @RequestMapping("/getRolePer")
    public Object getRolePer() {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());

        List<TRole> roleList = tRoleService.getAll();
        List<RolePerVO> rpList = tRolePermissionService.getAll();

        Map<String, Object> payload = new HashMap<>();
        payload.put("roles", roleList);
        payload.put("rps", rpList);
        if ( roleList.size() != 0 && rpList.size() != 0) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(payload);
        }
        return result;
    }

    @RequestMapping("/getOneUR")
    public Object getOneUR(@RequestParam("u_id") Integer id) {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        List<TUserRole> urs = tUserRoleService.findTUserRoleByU_Id(id);
        List<Integer> r_ids = new ArrayList<>();
        for (TUserRole ur : urs) {
            r_ids.add(ur.getRoleId());
        }
        List<String> rolenams = new ArrayList<>();
        for (Integer r_id : r_ids) {
            rolenams.add(tRoleService.findTRoleById(r_id).getRolename());
        }
        List<TRole> rlist = tRoleService.getAll();
        for (int i = 0; i < rlist.size(); i++){
            TRole role = rlist.get(i);
            for (String rolename:rolenams){
                if (rolename.equals(role.getRolename())){
                    rlist.remove(i);
                    i--;
                    break;
                }
            }
        }
        if (rlist != null && rlist.size() != 0) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        result.setPayload(rlist);
        return result;
    }

    @RequestMapping("/getOneRP")
    public Object getOneRP(@RequestParam("r_id") Integer id) {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        List<TRolePermission> rps = tRolePermissionService.findTRolePerByR_id(id);
        List<Integer> p_ids = new ArrayList<>();
        for (TRolePermission rp : rps) {
            p_ids.add(rp.getPermissionId());
        }
        List<String> permissions = new ArrayList<>();
        for (Integer p_id : p_ids) {
            permissions.add(tPermissionService.findTPermissionById(p_id).getPermission());
        }
        List<TPermission> plist = tPermissionService.getAll();
        for (int i = 0; i < plist.size(); i++){
            TPermission per = plist.get(i);
            for (String pername:permissions){
                if (pername.equals(per.getPermission())){
                    plist.remove(i);
                    i--;
                    break;
                }
            }
        }
        if (plist != null && plist.size() != 0) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        result.setPayload(plist);
        return result;
    }

    @RequestMapping("/confUserRole")
    public Object confUserRole(@RequestParam("conf_info") String conf_info) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        boolean flag = false;
        JsonNode conf = new ObjectMapper().readValue(conf_info, JsonNode.class);
        String username = conf.get("username").asText();
        int u_id = tUserService.findTUserByLogid(username).getId();
        TUserRole u_r = new TUserRole();
        u_r.setUserId(u_id);
        ArrayNode rolenames = (ArrayNode) conf.get("rolenames");
        for (int j = 0; j < rolenames.size(); j++) {
            String rolename = rolenames.get(j).asText();
            u_r.setRoleId(tRoleService.findTRoleByRolename(rolename).getId());
            flag = tUserRoleService.addTUserRole(u_r);
        }
        if (flag) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @RequestMapping("/confRolePer")
    public Object confRolePer(@RequestParam("conf_info") String conf_info) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        boolean flag = false;
            JsonNode conf = new ObjectMapper().readValue(conf_info,JsonNode.class);
            String rolename = conf.get("rolename").asText();
            int r_id = tRoleService.findTRoleByRolename(rolename).getId();
            TRolePermission r_p = new TRolePermission();
            r_p.setRoleId(r_id);
            ArrayNode permissions = (ArrayNode) conf.get("permissions");
            for (int j = 0; j < permissions.size(); j++) {
                String permission = permissions.get(j).asText();
                r_p.setPermissionId(tPermissionService.findTPermissionByName(permission).getId());
                flag = tRolePermissionService.addTRolePer(r_p);
            }
        if (flag) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @RequestMapping("/delUserRole")
    public Object delUserRole(@RequestParam("del_ur_id") Integer id){
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());

        boolean flag = tUserRoleService.delTUserRoleById(id);
        if (flag) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @RequestMapping("/delRolePer")
    public Object delRolePer(@RequestParam("del_rp_id") Integer id){
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());

        boolean flag = tRolePermissionService.delTRolePerById(id);
        if (flag) {
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

}
