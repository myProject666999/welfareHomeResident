package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("visit_appointment")
public class VisitAppointment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long residentId;
    private Long relativeId;
    private String visitorName;
    private String visitorPhone;
    private LocalDate visitDate;
    private LocalTime visitStartTime;
    private LocalTime visitEndTime;
    private String relation;
    private String visitReason;
    private String status;
    private String approvalRemark;
    private LocalDateTime actualArrivalTime;
    private LocalDateTime actualLeaveTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
