package com.bosyon.zisnackdesk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosyon.zisnackdesk.mapper.ApplicationMapper;
import com.bosyon.zisnackdesk.model.Application;
import com.bosyon.zisnackdesk.model.dto.ApplicationCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationVO;
import com.bosyon.zisnackdesk.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Override
    public ApplicationVO createApplication(ApplicationCreateDTO createDTO) {
        Application app = new Application();
        app.setApplicantId(createDTO.applicantId());
        app.setStatus(createDTO.status());
        save(app);
        log.info("创建申请单成功, id: {}", app.getId());
        return toVO(app);
    }

    @Override
    public ApplicationVO updateApplication(ApplicationUpdateDTO updateDTO) {
        Application app = getById(updateDTO.id());
        if (app == null) {
            throw new RuntimeException("申请单不存在, id: " + updateDTO.id());
        }
        app.setApplicantId(updateDTO.applicantId());
        app.setStatus(updateDTO.status());
        updateById(app);
        log.info("更新申请单成功, id: {}", app.getId());
        return toVO(app);
    }

    @Override
    public ApplicationVO getApplicationVOById(Long id) {
        Application app = getById(id);
        return app != null ? toVO(app) : null;
    }

    @Override
    public IPage<ApplicationVO> queryApplications(ApplicationQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(Application::getDeletedAt);

        if (queryDTO.applicantId() != null) {
            wrapper.eq(Application::getApplicantId, queryDTO.applicantId());
        }
        if (queryDTO.status() != null) {
            wrapper.eq(Application::getStatus, queryDTO.status());
        }

        wrapper.orderByDesc(Application::getCreatedAt);

        IPage<Application> page = page(new Page<>(pageNum, pageSize), wrapper);
        return page.convert(this::toVO);
    }

    @Override
    public boolean deleteApplication(Long id) {
        Application app = getById(id);
        if (app == null) {
            return false;
        }
        app.setDeletedAt(LocalDateTime.now());
        boolean result = updateById(app);
        if (result) {
            log.info("软删除申请单成功, id: {}", id);
        }
        return result;
    }

    @Override
    public boolean batchDeleteApplications(List<Long> ids) {
        List<Application> list = listByIds(ids);
        if (list.isEmpty()) {
            return false;
        }
        list.forEach(a -> {
            a.setDeletedAt(LocalDateTime.now());
            updateById(a);
        });
        log.info("批量软删除申请单成功, ids: {}", ids);
        return true;
    }

    private ApplicationVO toVO(Application app) {
        ApplicationVO vo = new ApplicationVO();
        BeanUtils.copyProperties(app, vo);
        return vo;
    }

}
