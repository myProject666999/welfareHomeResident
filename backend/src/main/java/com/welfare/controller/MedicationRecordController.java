package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.MedicationRecord;
import com.welfare.service.MedicationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medication")
public class MedicationRecordController {

    @Autowired
    private MedicationRecordService medicationRecordService;

    @GetMapping("/list/{residentId}")
    public Result<List<MedicationRecord>> listByResidentId(@PathVariable Long residentId) {
        return Result.success(medicationRecordService.listByResidentId(residentId));
    }

    @GetMapping("/{id}")
    public Result<MedicationRecord> getById(@PathVariable Long id) {
        return Result.success(medicationRecordService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody MedicationRecord record) {
        return Result.success(medicationRecordService.save(record));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody MedicationRecord record) {
        return Result.success(medicationRecordService.updateById(record));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(medicationRecordService.removeById(id));
    }
}
