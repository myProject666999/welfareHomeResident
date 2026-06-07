package com.welfare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("resident")
public class Resident {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCard;
    private String residentType;
    private String photoUrl;
    private String address;
    private String familySituation;
    private String admissionReason;
    private LocalDate admissionDate;
    private String healthStatus;
    private String chronicDiseases;
    private String allergies;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
