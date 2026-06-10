package com.bosyon.zisnackdesk.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApplicationUpdateDTO(

        @NotNull(message = "id 不能为空")
        Long id,

        Long applicantId,

        Integer status,

        LocalDateTime createdAt

) {

}
