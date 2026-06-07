package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("medication_record")
public class MedicationRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long residentId;
    private String drugName;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String prescribingDoctor;
    private String notes;
    private LocalDateTime createTime;
    @TableLogic
    private Integer isDeleted;
}
