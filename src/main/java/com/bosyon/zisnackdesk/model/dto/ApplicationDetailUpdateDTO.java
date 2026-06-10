package com.bosyon.zisnackdesk.model.dto;

import jakarta.validation.constraints.NotNull;

public record ApplicationDetailUpdateDTO(

        @NotNull(message = "id 不能为空")
        Long id,

        Long applicationId,

        Long archiveId

) {

}
