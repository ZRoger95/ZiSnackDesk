package com.bosyon.zisnackdesk.model.dto;

import com.bosyon.zisnackdesk.validation.NotConflictAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SysUserCreateDTO(

        @NotBlank(message = "account 不能为空")
        @NotConflictAccount
        String account,

        String mobile,

        @Email(message = "email 格式不正确")
        String email,

        @NotBlank(message = "password 不能为空")
        @Size(min = 6, max = 128, message = "password 长度应在 6 到 128 之间")
        String password,

        String userType

) {

    public SysUserCreateDTO {
        if (userType == null) {
            userType = "member";
        }
    }

}
