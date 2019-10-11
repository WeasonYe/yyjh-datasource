package com.sxp.yyjhservice.dao.dateForm;

import com.sxp.yyjhservice.domain.dateForm.TDateForm;
import org.springframework.stereotype.Repository;

@Repository
public interface TDateFormMapper {

    int delDateFormById(Integer id);

    int addDateForm(TDateForm record);

    TDateForm findDateFormById(Integer id);

    TDateForm findDateFormByState(Integer state);

    int updDateForm(TDateForm record);

}