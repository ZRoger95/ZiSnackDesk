-- 档案表
CREATE TABLE archive (
    id BIGSERIAL PRIMARY KEY,
    status SMALLINT NOT NULL DEFAULT 0,  -- 0:空闲, 1:申请中, 2:已归档
    current_application_id BIGINT,
    deleted_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 申请单主表（使用 BIGSERIAL 实现自增主键）
CREATE TABLE application (
    id BIGSERIAL PRIMARY KEY,
    applicant_id BIGINT,
    status SMALLINT,  -- 0:处理中, 1:成功, 2:失败
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 申请单明细表
CREATE TABLE application_detail (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT,
    archive_id BIGINT,
    deleted_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 为 archive 表插入 10 条测试数据（状态均为空闲）
INSERT INTO archive (id, status, current_application_id) VALUES
(1, 0, NULL),
(2, 0, NULL),
(3, 0, NULL),
(4, 0, NULL),
(5, 0, NULL),
(6, 0, NULL),
(7, 0, NULL),
(8, 0, NULL),
(9, 0, NULL),
(10, 0, NULL);