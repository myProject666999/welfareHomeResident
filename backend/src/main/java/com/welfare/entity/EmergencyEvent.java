package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("emergency_event")
public class EmergencyEvent {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long residentId;
    private String eventType;
    private String severityLevel;
    private String eventTitle;
    private String eventDescription;
    private LocalDateTime eventTime;
    private String location;
    private String reporter;
    private String reporterPhone;
    private String handler;
    private String handleProcess;
    private String handleResult;
    private LocalDateTime handleTime;
    private String status;
    private String reportLevel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
