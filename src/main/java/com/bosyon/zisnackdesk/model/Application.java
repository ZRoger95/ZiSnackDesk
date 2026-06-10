package com.bosyon.zisnackdesk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@TableName("application")
public class Application {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("applicant_id")
    private Long applicantId;

    private Integer status;

    @TableField("create_time")
    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private String createdBy;

    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
