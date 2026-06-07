package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.ActivityParticipant;
import com.welfare.service.ActivityParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-participant")
public class ActivityParticipantController {

    @Autowired
    private ActivityParticipantService participantService;

    @GetMapping("/list/{activityId}")
    public Result<List<ActivityParticipant>> listByActivityId(@PathVariable Long activityId) {
        return Result.success(participantService.listByActivityId(activityId));
    }

    @PostMapping("/set")
    public Result<Boolean> setParticipant(@RequestParam Long activityId,
                                          @RequestParam Long residentId,
                                          @RequestParam Boolean canParticipate) {
        return Result.success(participantService.setParticipant(activityId, residentId, canParticipate));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(participantService.removeById(id));
    }
}
