package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Integer weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String organizer;
    private String status;
    private LocalDateTime createTime;
    @TableLogic
    private Integer isDeleted;
}
