package com.bosyon.zisnackdesk.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosyon.zisnackdesk.model.dto.ApplicationCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationVO;
import com.bosyon.zisnackdesk.service.ApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ApplicationVO createApplication(@Valid @RequestBody ApplicationCreateDTO createDTO) {
        return applicationService.createApplication(createDTO);
    }

    @PutMapping
    public ApplicationVO updateApplication(@Valid @RequestBody ApplicationUpdateDTO updateDTO) {
        return applicationService.updateApplication(updateDTO);
    }

    @GetMapping("/{id}")
    public ApplicationVO getApplicationById(@PathVariable @NotNull Long id) {
        return applicationService.getApplicationVOById(id);
    }

    @GetMapping("/list")
    public IPage<ApplicationVO> queryApplications(ApplicationQueryDTO queryDTO,
                                                  @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return applicationService.queryApplications(queryDTO, pageNum, pageSize);
    }

    @DeleteMapping("/{id}")
    public boolean deleteApplication(@PathVariable @NotNull Long id) {
        return applicationService.deleteApplication(id);
    }

    @PostMapping("/batch-delete")
    public boolean batchDeleteApplications(@RequestBody @NotEmpty List<Long> ids) {
        return applicationService.batchDeleteApplications(ids);
    }

}
