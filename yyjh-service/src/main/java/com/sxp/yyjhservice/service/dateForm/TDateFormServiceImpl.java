package com.sxp.yyjhservice.service.dateForm;

import com.sxp.yyjhservice.dao.dateForm.TDateFormMapper;
import com.sxp.yyjhservice.domain.dateForm.TDateForm;
import com.sxp.yyjhservice.service.dateForm.TDateFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class TDateFormServiceImpl implements TDateFormService {
    @Autowired
    TDateFormMapper tDateFormMapper;
    @Override
    public boolean delDateFormById(Integer id) {
        boolean flag = false;
        int count = tDateFormMapper.delDateFormById(id);
        if (count>0) flag = true;
        return flag;
    }

    @Override
    public boolean addDateForm(TDateForm record) {
        boolean flag = false;
        int count = tDateFormMapper.addDateForm(record);
        if (count>0) flag = true;
        return flag;
    }

    @Override
    public TDateForm findDateFormById(Integer id) {
        return tDateFormMapper.findDateFormById(id);
    }

    @Override
    public TDateForm findDateFormByState(Integer state) {
        return tDateFormMapper.findDateFormByState(state);
    }

    @Override
    public boolean updDateForm(TDateForm record) {
        boolean flag = false;
        int count = tDateFormMapper.updDateForm(record);
        if (count>0) flag = true;
        return flag;
    }
}
