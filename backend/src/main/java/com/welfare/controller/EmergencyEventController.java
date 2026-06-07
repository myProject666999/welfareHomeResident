package com.welfare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.welfare.common.Result;
import com.welfare.entity.EmergencyEvent;
import com.welfare.service.EmergencyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyEventController {

    @Autowired
    private EmergencyEventService emergencyEventService;

    @GetMapping("/page")
    public Result<IPage<EmergencyEvent>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) String eventType,
                                              @RequestParam(required = false) String severityLevel,
                                              @RequestParam(required = false) String status) {
        return Result.success(emergencyEventService.page(pageNum, pageSize, eventType, severityLevel, status));
    }

    @GetMapping("/{id}")
    public Result<EmergencyEvent> getById(@PathVariable Long id) {
        return Result.success(emergencyEventService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EmergencyEvent event) {
        return Result.success(emergencyEventService.save(event));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EmergencyEvent event) {
        return Result.success(emergencyEventService.updateById(event));
    }

    @PostMapping("/handle/{id}")
    public Result<Boolean> handleEvent(@PathVariable Long id,
                                       @RequestParam String handler,
                                       @RequestParam String handleProcess,
                                       @RequestParam String handleResult) {
        return Result.success(emergencyEventService.handleEvent(id, handler, handleProcess, handleResult));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(emergencyEventService.removeById(id));
    }
}
