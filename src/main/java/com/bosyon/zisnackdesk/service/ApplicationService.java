package com.bosyon.zisnackdesk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosyon.zisnackdesk.model.Application;
import com.bosyon.zisnackdesk.model.dto.ApplicationCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationVO;

import java.util.List;

public interface ApplicationService extends IService<Application> {

    ApplicationVO createApplication(ApplicationCreateDTO createDTO);

    ApplicationVO updateApplication(ApplicationUpdateDTO updateDTO);

    ApplicationVO getApplicationVOById(Long id);

    IPage<ApplicationVO> queryApplications(ApplicationQueryDTO queryDTO, int pageNum, int pageSize);

    boolean deleteApplication(Long id);

    boolean batchDeleteApplications(List<Long> ids);

}
