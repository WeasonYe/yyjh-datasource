package com.sxp.yyjhservice.service.dateForm;

import com.sxp.yyjhservice.domain.dateForm.TDateForm;

public interface TDateFormService {
    boolean delDateFormById(Integer id);

    boolean addDateForm(TDateForm record);

    TDateForm findDateFormById(Integer id);

    TDateForm findDateFormByState(Integer state);

    boolean updDateForm(TDateForm record);
}
