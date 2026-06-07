package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.Resident;
import com.welfare.mapper.ResidentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ResidentService extends ServiceImpl<ResidentMapper, Resident> {

    public IPage<Resident> page(Integer pageNum, Integer pageSize, String name, String residentType) {
        Page<Resident> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Resident> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Resident::getName, name);
        }
        if (StringUtils.isNotBlank(residentType)) {
            wrapper.eq(Resident::getResidentType, residentType);
        }
        wrapper.orderByDesc(Resident::getCreateTime);
        return page(page, wrapper);
    }
}
