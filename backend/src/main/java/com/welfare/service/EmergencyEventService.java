package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.EmergencyEvent;
import com.welfare.mapper.EmergencyEventMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EmergencyEventService extends ServiceImpl<EmergencyEventMapper, EmergencyEvent> {

    public IPage<EmergencyEvent> page(Integer pageNum, Integer pageSize, String eventType, String severityLevel, String status) {
        Page<EmergencyEvent> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EmergencyEvent> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(eventType)) {
            wrapper.eq(EmergencyEvent::getEventType, eventType);
        }
        if (StringUtils.isNotBlank(severityLevel)) {
            wrapper.eq(EmergencyEvent::getSeverityLevel, severityLevel);
        }
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq(EmergencyEvent::getStatus, status);
        }
        wrapper.orderByDesc(EmergencyEvent::getEventTime);
        return page(page, wrapper);
    }

    public boolean handleEvent(Long id, String handler, String handleProcess, String handleResult) {
        EmergencyEvent event = getById(id);
        if (event == null) {
            return false;
        }
        event.setHandler(handler);
        event.setHandleProcess(handleProcess);
        event.setHandleResult(handleResult);
        event.setStatus("已处理");
        return updateById(event);
    }
}
