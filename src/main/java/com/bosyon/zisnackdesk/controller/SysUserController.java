package com.bosyon.zisnackdesk.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosyon.zisnackdesk.model.dto.SysUserCreateDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserQueryDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.SysUserVO;
import com.bosyon.zisnackdesk.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping
    public SysUserVO createUser(@Valid @RequestBody SysUserCreateDTO createDTO) {
        return sysUserService.createUser(createDTO);
    }

    @PutMapping
    public SysUserVO updateUser(@Valid @RequestBody SysUserUpdateDTO updateDTO) {
        return sysUserService.updateUser(updateDTO);
    }

    @GetMapping("/{id}")
    public SysUserVO getUserById(@PathVariable @NotBlank String id) {
        return sysUserService.getUserVOById(id);
    }

    @GetMapping("/list")
    public IPage<SysUserVO> queryUsers(SysUserQueryDTO queryDTO,
                                       @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return sysUserService.queryUsers(queryDTO, pageNum, pageSize);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable @NotBlank String id) {
        return sysUserService.deleteUser(id);
    }

    @PostMapping("/batch-delete")
    public boolean batchDeleteUsers(@RequestBody @NotEmpty List<String> ids) {
        return sysUserService.batchDeleteUsers(ids);
    }

}
