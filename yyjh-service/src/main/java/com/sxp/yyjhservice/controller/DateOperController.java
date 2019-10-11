package com.sxp.yyjhservice.controller;

import com.sxp.yyjhservice.domain.dateForm.TDateForm;
import com.sxp.yyjhservice.domain.user.TUser;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.dateForm.TDateFormService;
import com.sxp.yyjhservice.service.user.TUserService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dateOper")
public class DateOperController {
    @Autowired
    TUserService tUserService;
    @Autowired
    TDateFormService tDateFormService;

    @RequestMapping("/setForm")
    @RequiresPermissions(value = {"time_conf"})
    public Object setForm(@RequestParam Integer id){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        TDateForm tDateForm = tDateFormService.findDateFormByState(1);
        tDateForm.setForm(null);
        if (tDateForm!=null){
            tDateForm.setState(0);
            if (tDateFormService.updDateForm(tDateForm)){
                tDateForm.setState(1);tDateForm.setId(id);
                if (tDateFormService.updDateForm(tDateForm)){
                    controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
                    controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
            }
        }
        return controllerResult;
    }

    @RequestMapping("/getForm")
    public Object getForm(){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        TDateForm tDateForm = tDateFormService.findDateFormByState(1);
        if (tDateForm!=null){
            controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
            controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
            controllerResult.setPayload(tDateForm);
        }
        return controllerResult;
    }
}
