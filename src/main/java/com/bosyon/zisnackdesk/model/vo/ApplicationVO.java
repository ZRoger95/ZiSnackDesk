package com.bosyon.zisnackdesk.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationVO {

    private Long id;

    private Long applicantId;

    private Integer status;

    private LocalDateTime createdAt;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime updatedAt;

}
