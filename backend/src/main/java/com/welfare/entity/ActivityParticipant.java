package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("activity_participant")
public class ActivityParticipant {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long residentId;
    private Boolean canParticipate;
    private LocalDateTime createTime;
}
