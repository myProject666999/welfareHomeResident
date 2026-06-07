package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.ActivityCheckin;
import com.welfare.service.ActivityCheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activity-checkin")
public class ActivityCheckinController {

    @Autowired
    private ActivityCheckinService checkinService;

    @GetMapping("/list")
    public Result<List<ActivityCheckin>> list(@RequestParam Long activityId,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate) {
        return Result.success(checkinService.listByActivityAndDate(activityId, checkinDate));
    }

    @PostMapping("/checkin")
    public Result<Boolean> checkin(@RequestParam Long activityId,
                                   @RequestParam Long residentId,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate) {
        return Result.success(checkinService.checkin(activityId, residentId, checkinDate));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody ActivityCheckin checkin) {
        return Result.success(checkinService.updateById(checkin));
    }
}
