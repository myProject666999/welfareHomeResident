package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("activity_checkin")
public class ActivityCheckin {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long residentId;
    private LocalDate checkinDate;
    private LocalDateTime checkinTime;
    private String status;
    private String notes;
    private LocalDateTime createTime;
}
