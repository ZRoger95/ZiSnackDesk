package com.bosyon.zisnackdesk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosyon.zisnackdesk.mapper.ApplicationDetailMapper;
import com.bosyon.zisnackdesk.model.ApplicationDetail;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationDetailVO;
import com.bosyon.zisnackdesk.service.ApplicationDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationDetailServiceImpl extends ServiceImpl<ApplicationDetailMapper, ApplicationDetail> implements ApplicationDetailService {

    @Override
    public ApplicationDetailVO createDetail(ApplicationDetailCreateDTO createDTO) {
        ApplicationDetail d = new ApplicationDetail();
        d.setApplicationId(createDTO.applicationId());
        d.setArchiveId(createDTO.archiveId());
        save(d);
        log.info("创建申请单明细成功, id: {}", d.getId());
        return toVO(d);
    }

    @Override
    public ApplicationDetailVO updateDetail(ApplicationDetailUpdateDTO updateDTO) {
        ApplicationDetail d = getById(updateDTO.id());
        if (d == null) {
            throw new RuntimeException("申请单明细不存在, id: " + updateDTO.id());
        }
        d.setApplicationId(updateDTO.applicationId());
        d.setArchiveId(updateDTO.archiveId());
        updateById(d);
        log.info("更新申请单明细成功, id: {}", d.getId());
        return toVO(d);
    }

    @Override
    public ApplicationDetailVO getDetailById(Long id) {
        ApplicationDetail d = getById(id);
        return d != null ? toVO(d) : null;
    }

    @Override
    public IPage<ApplicationDetailVO> queryDetails(ApplicationDetailQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<ApplicationDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(ApplicationDetail::getDeletedAt);

        if (queryDTO.applicationId() != null) {
            wrapper.eq(ApplicationDetail::getApplicationId, queryDTO.applicationId());
        }
        if (queryDTO.archiveId() != null) {
            wrapper.eq(ApplicationDetail::getArchiveId, queryDTO.archiveId());
        }

        wrapper.orderByDesc(ApplicationDetail::getCreatedAt);

        IPage<ApplicationDetail> page = page(new Page<>(pageNum, pageSize), wrapper);
        return page.convert(this::toVO);
    }

    @Override
    public boolean deleteDetail(Long id) {
        ApplicationDetail d = getById(id);
        if (d == null) {
            return false;
        }
        d.setDeletedAt(LocalDateTime.now());
        boolean result = updateById(d);
        if (result) {
            log.info("软删除申请单明细成功, id: {}", id);
        }
        return result;
    }

    @Override
    public boolean batchDeleteDetails(List<Long> ids) {
        List<ApplicationDetail> list = listByIds(ids);
        if (list.isEmpty()) {
            return false;
        }
        list.forEach(d -> {
            d.setDeletedAt(LocalDateTime.now());
            updateById(d);
        });
        log.info("批量软删除申请单明细成功, ids: {}", ids);
        return true;
    }

    private ApplicationDetailVO toVO(ApplicationDetail d) {
        ApplicationDetailVO vo = new ApplicationDetailVO();
        BeanUtils.copyProperties(d, vo);
        return vo;
    }

}
