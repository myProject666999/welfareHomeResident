package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.Activity;
import com.welfare.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService extends ServiceImpl<ActivityMapper, Activity> {

    public List<Activity> listByWeekDay(Integer weekDay) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        if (weekDay != null) {
            wrapper.eq(Activity::getWeekDay, weekDay);
        }
        wrapper.eq(Activity::getStatus, "启用");
        wrapper.orderByAsc(Activity::getWeekDay).orderByAsc(Activity::getStartTime);
        return list(wrapper);
    }
}
