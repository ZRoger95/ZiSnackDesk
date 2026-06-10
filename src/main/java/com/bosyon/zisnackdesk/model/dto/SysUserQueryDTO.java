package com.bosyon.zisnackdesk.model.dto;

public record SysUserQueryDTO(
        String account,
        String mobile,
        String email,
        String userType,
        Boolean mobileVerified,
        Boolean emailVerified
) {

}
