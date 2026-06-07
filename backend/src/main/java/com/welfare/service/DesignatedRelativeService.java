package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.DesignatedRelative;
import com.welfare.mapper.DesignatedRelativeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignatedRelativeService extends ServiceImpl<DesignatedRelativeMapper, DesignatedRelative> {

    public List<DesignatedRelative> listByResidentId(Long residentId) {
        LambdaQueryWrapper<DesignatedRelative> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DesignatedRelative::getResidentId, residentId);
        wrapper.eq(DesignatedRelative::getIsAuthorized, 1);
        return list(wrapper);
    }

    public boolean verifyRelative(Long residentId, String idCard) {
        LambdaQueryWrapper<DesignatedRelative> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DesignatedRelative::getResidentId, residentId);
        wrapper.eq(DesignatedRelative::getIdCard, idCard);
        wrapper.eq(DesignatedRelative::getIsAuthorized, 1);
        return getOne(wrapper) != null;
    }
}
