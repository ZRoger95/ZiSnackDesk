package com.bosyon.zisnackdesk.model.dto;

import jakarta.validation.constraints.NotNull;

public record ArchiveUpdateDTO(

        @NotNull(message = "id 不能为空")
        Long id,

        Integer status,

        Long currentApplicationId

) {

}
