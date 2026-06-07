package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.DesignatedRelative;
import com.welfare.service.DesignatedRelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relative")
public class DesignatedRelativeController {

    @Autowired
    private DesignatedRelativeService relativeService;

    @GetMapping("/list/{residentId}")
    public Result<List<DesignatedRelative>> listByResidentId(@PathVariable Long residentId) {
        return Result.success(relativeService.listByResidentId(residentId));
    }

    @GetMapping("/verify")
    public Result<Boolean> verifyRelative(@RequestParam Long residentId, @RequestParam String idCard) {
        return Result.success(relativeService.verifyRelative(residentId, idCard));
    }

    @GetMapping("/{id}")
    public Result<DesignatedRelative> getById(@PathVariable Long id) {
        return Result.success(relativeService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody DesignatedRelative relative) {
        return Result.success(relativeService.save(relative));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody DesignatedRelative relative) {
        return Result.success(relativeService.updateById(relative));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(relativeService.removeById(id));
    }
}
