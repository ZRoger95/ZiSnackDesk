package com.bosyon.zisnackdesk.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosyon.zisnackdesk.model.dto.ArchiveCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ArchiveVO;
import com.bosyon.zisnackdesk.service.ArchiveService;
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
@RequestMapping("/archive")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @PostMapping
    public ArchiveVO createArchive(@Valid @RequestBody ArchiveCreateDTO createDTO) {
        return archiveService.createArchive(createDTO);
    }

    @PutMapping
    public ArchiveVO updateArchive(@Valid @RequestBody ArchiveUpdateDTO updateDTO) {
        return archiveService.updateArchive(updateDTO);
    }

    @GetMapping("/{id}")
    public ArchiveVO getArchiveById(@PathVariable @NotNull Long id) {
        return archiveService.getArchiveVOById(id);
    }

    @GetMapping("/list")
    public IPage<ArchiveVO> queryArchives(ArchiveQueryDTO queryDTO,
                                          @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return archiveService.queryArchives(queryDTO, pageNum, pageSize);
    }

    @DeleteMapping("/{id}")
    public boolean deleteArchive(@PathVariable @NotNull Long id) {
        return archiveService.deleteArchive(id);
    }

    @PostMapping("/batch-delete")
    public boolean batchDeleteArchives(@RequestBody @NotEmpty List<Long> ids) {
        return archiveService.batchDeleteArchives(ids);
    }

}
