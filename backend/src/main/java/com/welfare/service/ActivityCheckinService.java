package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.ActivityCheckin;
import com.welfare.mapper.ActivityCheckinMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityCheckinService extends ServiceImpl<ActivityCheckinMapper, ActivityCheckin> {

    public List<ActivityCheckin> listByActivityAndDate(Long activityId, LocalDate checkinDate) {
        LambdaQueryWrapper<ActivityCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityCheckin::getActivityId, activityId);
        wrapper.eq(ActivityCheckin::getCheckinDate, checkinDate);
        return list(wrapper);
    }

    public boolean checkin(Long activityId, Long residentId, LocalDate checkinDate) {
        LambdaQueryWrapper<ActivityCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityCheckin::getActivityId, activityId);
        wrapper.eq(ActivityCheckin::getResidentId, residentId);
        wrapper.eq(ActivityCheckin::getCheckinDate, checkinDate);
        ActivityCheckin checkin = getOne(wrapper);
        if (checkin == null) {
            checkin = new ActivityCheckin();
            checkin.setActivityId(activityId);
            checkin.setResidentId(residentId);
            checkin.setCheckinDate(checkinDate);
            checkin.setCheckinTime(LocalDateTime.now());
            checkin.setStatus("已签到");
            return save(checkin);
        } else {
            checkin.setCheckinTime(LocalDateTime.now());
            checkin.setStatus("已签到");
            return updateById(checkin);
        }
    }
}
