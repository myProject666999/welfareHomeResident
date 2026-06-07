package com.welfare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.welfare.common.Result;
import com.welfare.entity.VisitAppointment;
import com.welfare.service.VisitAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class VisitAppointmentController {

    @Autowired
    private VisitAppointmentService appointmentService;

    @GetMapping("/page")
    public Result<IPage<VisitAppointment>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) Long residentId,
                                                @RequestParam(required = false) String status) {
        return Result.success(appointmentService.page(pageNum, pageSize, residentId, status));
    }

    @GetMapping("/{id}")
    public Result<VisitAppointment> getById(@PathVariable Long id) {
        return Result.success(appointmentService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody VisitAppointment appointment) {
        try {
            return Result.success(appointmentService.createAppointment(appointment));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody VisitAppointment appointment) {
        return Result.success(appointmentService.updateById(appointment));
    }

    @PostMapping("/approve/{id}")
    public Result<Boolean> approve(@PathVariable Long id,
                                   @RequestParam Boolean approved,
                                   @RequestParam(required = false) String remark) {
        return Result.success(appointmentService.approve(id, approved, remark));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(appointmentService.removeById(id));
    }
}
