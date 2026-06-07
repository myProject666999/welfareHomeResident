package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.entity.DesignatedRelative;
import com.welfare.entity.VisitAppointment;
import com.welfare.mapper.VisitAppointmentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitAppointmentService extends ServiceImpl<VisitAppointmentMapper, VisitAppointment> {

    @Autowired
    private DesignatedRelativeService relativeService;

    public IPage<VisitAppointment> page(Integer pageNum, Integer pageSize, Long residentId, String status) {
        Page<VisitAppointment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<VisitAppointment> wrapper = new LambdaQueryWrapper<>();
        if (residentId != null) {
            wrapper.eq(VisitAppointment::getResidentId, residentId);
        }
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq(VisitAppointment::getStatus, status);
        }
        wrapper.orderByDesc(VisitAppointment::getCreateTime);
        return page(page, wrapper);
    }

    public boolean createAppointment(VisitAppointment appointment) {
        DesignatedRelative relative = relativeService.getById(appointment.getRelativeId());
        if (relative == null || relative.getIsAuthorized() != 1) {
            throw new RuntimeException("该亲属未被授权探访");
        }
        if (!relative.getResidentId().equals(appointment.getResidentId())) {
            throw new RuntimeException("亲属与院民不匹配");
        }
        appointment.setStatus("待审核");
        return save(appointment);
    }

    public boolean approve(Long id, Boolean approved, String remark) {
        VisitAppointment appointment = getById(id);
        if (appointment == null) {
            return false;
        }
        appointment.setStatus(approved ? "已批准" : "已拒绝");
        appointment.setApprovalRemark(remark);
        return updateById(appointment);
    }
}
