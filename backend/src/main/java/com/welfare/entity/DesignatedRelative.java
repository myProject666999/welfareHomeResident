package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("designated_relative")
public class DesignatedRelative {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long residentId;
    private String name;
    private String relation;
    private String phone;
    private String idCard;
    private Boolean isAuthorized;
    private LocalDateTime createTime;
    @TableLogic
    private Integer isDeleted;
}
