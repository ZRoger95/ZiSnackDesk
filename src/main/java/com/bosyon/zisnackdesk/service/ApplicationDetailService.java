package com.bosyon.zisnackdesk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosyon.zisnackdesk.model.ApplicationDetail;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailCreateDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailQueryDTO;
import com.bosyon.zisnackdesk.model.dto.ApplicationDetailUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.ApplicationDetailVO;

import java.util.List;

public interface ApplicationDetailService extends IService<ApplicationDetail> {

    ApplicationDetailVO createDetail(ApplicationDetailCreateDTO createDTO);

    ApplicationDetailVO updateDetail(ApplicationDetailUpdateDTO updateDTO);

    ApplicationDetailVO getDetailById(Long id);

    IPage<ApplicationDetailVO> queryDetails(ApplicationDetailQueryDTO queryDTO, int pageNum, int pageSize);

    boolean deleteDetail(Long id);

    boolean batchDeleteDetails(List<Long> ids);

}
