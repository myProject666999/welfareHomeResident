package com.welfare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.welfare.common.Result;
import com.welfare.entity.Resident;
import com.welfare.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping("/page")
    public Result<IPage<Resident>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String residentType) {
        return Result.success(residentService.page(pageNum, pageSize, name, residentType));
    }

    @GetMapping("/list")
    public Result<List<Resident>> list() {
        return Result.success(residentService.list());
    }

    @GetMapping("/{id}")
    public Result<Resident> getById(@PathVariable Long id) {
        return Result.success(residentService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Resident resident) {
        return Result.success(residentService.save(resident));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Resident resident) {
        return Result.success(residentService.updateById(resident));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(residentService.removeById(id));
    }
}
