package com.bosyon.zisnackdesk.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationDetailVO;
import com.bosyon.zisnackdesk.service.ApplicationDetailService;
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
@RequestMapping("/application-detail")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ApplicationDetailController {

    @Autowired
    private ApplicationDetailService applicationDetailService;

    @PostMapping
    public ApplicationDetailVO createDetail(@Valid @RequestBody ApplicationDetailCreateDTO createDTO) {
        return applicationDetailService.createDetail(createDTO);
    }

    @PutMapping
    public ApplicationDetailVO updateDetail(@Valid @RequestBody ApplicationDetailUpdateDTO updateDTO) {
        return applicationDetailService.updateDetail(updateDTO);
    }

    @GetMapping("/{id}")
    public ApplicationDetailVO getDetailById(@PathVariable @NotNull Long id) {
        return applicationDetailService.getDetailById(id);
    }

    @GetMapping("/list")
    public IPage<ApplicationDetailVO> queryDetails(ApplicationDetailQueryDTO queryDTO,
                                                   @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return applicationDetailService.queryDetails(queryDTO, pageNum, pageSize);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDetail(@PathVariable @NotNull Long id) {
        return applicationDetailService.deleteDetail(id);
    }

    @PostMapping("/batch-delete")
    public boolean batchDeleteDetails(@RequestBody @NotEmpty List<Long> ids) {
        return applicationDetailService.batchDeleteDetails(ids);
    }

}
