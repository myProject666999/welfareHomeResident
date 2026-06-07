package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.MedicationRecord;
import com.welfare.mapper.MedicationRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationRecordService extends ServiceImpl<MedicationRecordMapper, MedicationRecord> {

    public List<MedicationRecord> listByResidentId(Long residentId) {
        LambdaQueryWrapper<MedicationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicationRecord::getResidentId, residentId);
        wrapper.orderByDesc(MedicationRecord::getCreateTime);
        return list(wrapper);
    }
}
