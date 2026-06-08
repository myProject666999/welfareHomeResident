package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.ActivityParticipant;
import com.welfare.mapper.ActivityParticipantMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityParticipantService extends ServiceImpl<ActivityParticipantMapper, ActivityParticipant> {

    public List<ActivityParticipant> listByActivityId(Long activityId) {
        LambdaQueryWrapper<ActivityParticipant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityParticipant::getActivityId, activityId);
        return list(wrapper);
    }

    public boolean setParticipant(Long activityId, Long residentId, Boolean canParticipate) {
        LambdaQueryWrapper<ActivityParticipant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityParticipant::getActivityId, activityId);
        wrapper.eq(ActivityParticipant::getResidentId, residentId);
        ActivityParticipant participant = getOne(wrapper);
        if (participant == null) {
            participant = new ActivityParticipant();
            participant.setActivityId(activityId);
            participant.setResidentId(residentId);
            participant.setCanParticipate(canParticipate);
            return save(participant);
        } else {
            participant.setCanParticipate(canParticipate);
            return updateById(participant);
        }
    }
}
