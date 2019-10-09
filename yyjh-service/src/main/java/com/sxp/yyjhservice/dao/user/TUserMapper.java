package com.sxp.yyjhservice.dao.user;

import com.sxp.yyjhservice.domain.user.TUser;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TUserMapper {
    int delTUserById(Integer id);

    int addTUser(TUser record);

    TUser findTUserById(Integer id);

    int updTUser(TUser record);

    List<TUser> getAll();

    TUser findTUserByLogid(String loginid);

    TUser findTUserByTel(String tel);

    TUser findUserByEmail(String email);
}