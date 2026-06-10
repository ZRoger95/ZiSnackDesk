package com.bosyon.zisnackdesk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosyon.zisnackdesk.model.Archive;
import com.bosyon.zisnackdesk.model.dto.ArchiveCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ArchiveVO;

import java.util.List;

public interface ArchiveService extends IService<Archive> {

    ArchiveVO createArchive(ArchiveCreateDTO createDTO);

    ArchiveVO updateArchive(ArchiveUpdateDTO updateDTO);

    ArchiveVO getArchiveVOById(Long id);

    IPage<ArchiveVO> queryArchives(ArchiveQueryDTO queryDTO, int pageNum, int pageSize);

    boolean deleteArchive(Long id);

    boolean batchDeleteArchives(List<Long> ids);

}
