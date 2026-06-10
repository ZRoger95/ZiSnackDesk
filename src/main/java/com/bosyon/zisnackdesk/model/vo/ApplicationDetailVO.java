package com.bosyon.zisnackdesk.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDetailVO {

    private Long id;

    private Long applicationId;

    private Long archiveId;

    private LocalDateTime createdAt;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime updatedAt;

}
