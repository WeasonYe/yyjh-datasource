package com.sxp.yyjhservice.service.user;

import com.sxp.yyjhservice.domain.user.TUser;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface TUserService {
    boolean delTUserById(Integer id);

    boolean addTUser(TUser record);

    TUser findTUserById(Integer id);

    boolean updTUser(TUser record);

    TUser findTUserByLogid(String logid);

    TUser findTUserByParam(String param);

    List<TUser> getAll();
}
