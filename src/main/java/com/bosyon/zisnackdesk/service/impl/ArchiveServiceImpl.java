package com.bosyon.zisnackdesk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosyon.zisnackdesk.mapper.ArchiveMapper;
import com.bosyon.zisnackdesk.model.Archive;
import com.bosyon.zisnackdesk.model.dto.ArchiveCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ArchiveUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ArchiveVO;
import com.bosyon.zisnackdesk.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements ArchiveService {

    @Override
    public ArchiveVO createArchive(ArchiveCreateDTO createDTO) {
        Archive archive = new Archive();
        archive.setStatus(createDTO.status());
        archive.setCurrentApplicationId(createDTO.currentApplicationId());
        save(archive);
        log.info("创建档案成功, id: {}", archive.getId());
        return toVO(archive);
    }

    @Override
    public ArchiveVO updateArchive(ArchiveUpdateDTO updateDTO) {
        Archive archive = getById(updateDTO.id());
        if (archive == null) {
            throw new RuntimeException("档案不存在, id: " + updateDTO.id());
        }
        archive.setStatus(updateDTO.status());
        archive.setCurrentApplicationId(updateDTO.currentApplicationId());
        updateById(archive);
        log.info("更新档案成功, id: {}", archive.getId());
        return toVO(archive);
    }

    @Override
    public ArchiveVO getArchiveVOById(Long id) {
        Archive archive = getById(id);
        return archive != null ? toVO(archive) : null;
    }

    @Override
    public IPage<ArchiveVO> queryArchives(ArchiveQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(Archive::getDeletedAt);

        if (queryDTO.status() != null) {
            wrapper.eq(Archive::getStatus, queryDTO.status());
        }
        if (queryDTO.currentApplicationId() != null) {
            wrapper.eq(Archive::getCurrentApplicationId, queryDTO.currentApplicationId());
        }

        wrapper.orderByDesc(Archive::getCreatedAt);

        IPage<Archive> page = page(new Page<>(pageNum, pageSize), wrapper);
        return page.convert(this::toVO);
    }

    @Override
    public boolean deleteArchive(Long id) {
        Archive archive = getById(id);
        if (archive == null) {
            return false;
        }
        archive.setDeletedAt(LocalDateTime.now());
        boolean result = updateById(archive);
        if (result) {
            log.info("软删除档案成功, id: {}", id);
        }
        return result;
    }

    @Override
    public boolean batchDeleteArchives(List<Long> ids) {
        List<Archive> list = listByIds(ids);
        if (list.isEmpty()) {
            return false;
        }
        list.forEach(a -> {
            a.setDeletedAt(LocalDateTime.now());
            updateById(a);
        });
        log.info("批量软删除档案成功, ids: {}", ids);
        return true;
    }

    private ArchiveVO toVO(Archive archive) {
        ArchiveVO vo = new ArchiveVO();
        BeanUtils.copyProperties(archive, vo);
        return vo;
    }

}
