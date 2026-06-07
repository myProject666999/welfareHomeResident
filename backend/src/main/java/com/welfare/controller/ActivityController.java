package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.Activity;
import com.welfare.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/list")
    public Result<List<Activity>> list(@RequestParam(required = false) Integer weekDay) {
        return Result.success(activityService.listByWeekDay(weekDay));
    }

    @GetMapping("/{id}")
    public Result<Activity> getById(@PathVariable Long id) {
        return Result.success(activityService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Activity activity) {
        return Result.success(activityService.save(activity));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Activity activity) {
        return Result.success(activityService.updateById(activity));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(activityService.removeById(id));
    }
}
