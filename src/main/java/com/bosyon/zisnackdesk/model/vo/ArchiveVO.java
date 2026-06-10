package com.bosyon.zisnackdesk.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchiveVO {

    private Long id;

    private Integer status;

    private Long currentApplicationId;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
