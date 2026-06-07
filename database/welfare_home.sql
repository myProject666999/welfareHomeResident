CREATE DATABASE IF NOT EXISTS welfare_home DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE welfare_home;

CREATE TABLE resident (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) UNIQUE COMMENT '身份证号',
    resident_type VARCHAR(20) NOT NULL COMMENT '院民类型：孤寡老人/孤儿/特殊困境人员',
    photo_url VARCHAR(255) COMMENT '照片地址',
    address VARCHAR(255) COMMENT '户籍地址',
    family_situation TEXT COMMENT '家庭情况',
    admission_reason TEXT COMMENT '入院原因',
    admission_date DATE COMMENT '入院日期',
    health_status VARCHAR(50) COMMENT '健康状况',
    chronic_diseases TEXT COMMENT '慢性病',
    allergies TEXT COMMENT '过敏史',
    emergency_contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
    emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
    emergency_contact_relation VARCHAR(20) COMMENT '紧急联系人关系',
    status VARCHAR(20) DEFAULT '在院' COMMENT '状态：在院/离院/离世',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院民档案表';

CREATE TABLE medication_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL COMMENT '院民ID',
    drug_name VARCHAR(100) NOT NULL COMMENT '药品名称',
    dosage VARCHAR(50) COMMENT '剂量',
    frequency VARCHAR(50) COMMENT '服用频次',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    prescribing_doctor VARCHAR(50) COMMENT '开药医生',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    FOREIGN KEY (resident_id) REFERENCES resident(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用药记录表';

CREATE TABLE activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '活动名称',
    description TEXT COMMENT '活动描述',
    week_day INT NOT NULL COMMENT '星期几：1-7',
    start_time TIME COMMENT '开始时间',
    end_time TIME COMMENT '结束时间',
    location VARCHAR(100) COMMENT '活动地点',
    organizer VARCHAR(50) COMMENT '组织者',
    status VARCHAR(20) DEFAULT '启用' COMMENT '状态：启用/停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

CREATE TABLE activity_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    resident_id BIGINT NOT NULL COMMENT '院民ID',
    can_participate TINYINT DEFAULT 1 COMMENT '是否可参与',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_activity_resident (activity_id, resident_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (resident_id) REFERENCES resident(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与表';

CREATE TABLE activity_checkin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    resident_id BIGINT NOT NULL COMMENT '院民ID',
    checkin_date DATE NOT NULL COMMENT '签到日期',
    checkin_time DATETIME COMMENT '签到时间',
    status VARCHAR(20) DEFAULT '未签到' COMMENT '状态：已签到/未签到/请假',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_activity_date_resident (activity_id, checkin_date, resident_id),
    FOREIGN KEY (activity_id) REFERENCES activity(id),
    FOREIGN KEY (resident_id) REFERENCES resident(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到表';

CREATE TABLE designated_relative (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL COMMENT '院民ID',
    name VARCHAR(50) NOT NULL COMMENT '亲属姓名',
    relation VARCHAR(20) COMMENT '与院民关系',
    phone VARCHAR(20) COMMENT '联系电话',
    id_card VARCHAR(18) COMMENT '身份证号',
    is_authorized TINYINT DEFAULT 1 COMMENT '是否授权探访',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    FOREIGN KEY (resident_id) REFERENCES resident(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指定亲属表';

CREATE TABLE visit_appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL COMMENT '院民ID',
    relative_id BIGINT NOT NULL COMMENT '亲属ID',
    visitor_name VARCHAR(50) NOT NULL COMMENT '探访人姓名',
    visitor_phone VARCHAR(20) COMMENT '探访人电话',
    visit_date DATE NOT NULL COMMENT '探访日期',
    visit_start_time TIME NOT NULL COMMENT '开始时间',
    visit_end_time TIME COMMENT '结束时间',
    relation VARCHAR(20) COMMENT '与院民关系',
    visit_reason TEXT COMMENT '探访事由',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态：待审核/已批准/已拒绝/已完成/已取消',
    approval_remark TEXT COMMENT '审核备注',
    actual_arrival_time DATETIME COMMENT '实际到达时间',
    actual_leave_time DATETIME COMMENT '实际离开时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    FOREIGN KEY (resident_id) REFERENCES resident(id),
    FOREIGN KEY (relative_id) REFERENCES designated_relative(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='探访预约表';

CREATE TABLE emergency_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT COMMENT '涉及院民ID',
    event_type VARCHAR(30) NOT NULL COMMENT '事件类型：生病/走失/伤亡/其他',
    severity_level VARCHAR(20) NOT NULL COMMENT '严重等级：一般/较重/严重/特别严重',
    event_title VARCHAR(200) NOT NULL COMMENT '事件标题',
    event_description TEXT COMMENT '事件详细描述',
    event_time DATETIME NOT NULL COMMENT '事件发生时间',
    location VARCHAR(200) COMMENT '发生地点',
    reporter VARCHAR(50) COMMENT '上报人',
    reporter_phone VARCHAR(20) COMMENT '上报人电话',
    handler VARCHAR(50) COMMENT '处理人',
    handle_process TEXT COMMENT '处理过程',
    handle_result TEXT COMMENT '处理结果',
    handle_time DATETIME COMMENT '处理时间',
    status VARCHAR(20) DEFAULT '待处理' COMMENT '状态：待处理/处理中/已处理',
    report_level VARCHAR(20) DEFAULT '院内' COMMENT '上报级别：院内/区级/市级',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    FOREIGN KEY (resident_id) REFERENCES resident(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='突发事件表';

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) DEFAULT '普通用户' COMMENT '角色：管理员/普通用户',
    status VARCHAR(20) DEFAULT '启用' COMMENT '状态：启用/停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

INSERT INTO sys_user (username, password, real_name, role) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', '管理员');

INSERT INTO activity (name, description, week_day, start_time, end_time, location, organizer) VALUES
('晨练', '晨间健身活动', 1, '06:30:00', '07:30:00', '院内操场', '张护士'),
('手工课', '手工制作活动', 1, '09:00:00', '10:30:00', '活动室', '李老师'),
('看电视', '集体观看电视节目', 1, '19:00:00', '21:00:00', '娱乐室', '王护工'),
('理发服务', '免费理发服务', 2, '09:00:00', '11:30:00', '理发室', '外心理发师'),
('健康检查', '每周健康检查', 3, '08:30:00', '11:00:00', '医务室', '陈医生'),
('书法课', '书法练习', 3, '14:30:00', '16:00:00', '书法室', '赵老师'),
('园艺活动', '花园种植养护', 4, '09:00:00', '10:30:00', '后花园', '孙护工'),
('唱歌活动', '集体唱歌', 4, '15:00:00', '16:30:00', '音乐室', '周老师'),
('电影观赏', '每周电影放映', 5, '19:00:00', '21:00:00', '娱乐室', '吴护工'),
('外出散步', '公园散步', 6, '08:30:00', '10:30:00', '附近公园', '郑护士'),
('家属接待日', '家属集中探访日', 6, '14:00:00', '17:00:00', '接待室', '冯主任'),
('宗教活动', '周日宗教活动', 7, '09:00:00', '10:00:00', '活动室', '钱护工'),
('棋牌娱乐', '棋牌游戏', 7, '14:00:00', '16:30:00', '棋牌室', '马护工');
