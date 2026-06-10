package com.bosyon.zisnackdesk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosyon.zisnackdesk.mapper.SysUserMapper;
import com.bosyon.zisnackdesk.model.SysUser;
import com.bosyon.zisnackdesk.model.dto.SysUserCreateDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserQueryDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.SysUserVO;
import com.bosyon.zisnackdesk.service.SysUserService;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUserVO createUser(SysUserCreateDTO createDTO) {
        SysUser user = new SysUser();
        user.setAccount(createDTO.account());
        user.setMobile(createDTO.mobile());
        user.setEmail(createDTO.email());
        user.setPassword(createDTO.password());
        user.setUserType(createDTO.userType());
        save(user);
        log.info("创建用户成功, id: {}, account: {}", user.getId(), user.getAccount());
        return toVO(user);
    }

    @Override
    public SysUserVO updateUser(SysUserUpdateDTO updateDTO) {
        SysUser user = getById(updateDTO.id());
        if (user == null) {
            throw new RuntimeException("用户不存在, id: " + updateDTO.id());
        }
        user.setAccount(updateDTO.account());
        user.setMobile(updateDTO.mobile());
        user.setEmail(updateDTO.email());
        user.setPassword(updateDTO.password());
        user.setUserType(updateDTO.userType());
        user.setMobileVerified(updateDTO.mobileVerified());
        user.setEmailVerified(updateDTO.emailVerified());
        updateById(user);
        log.info("更新用户成功, id: {}", user.getId());
        return toVO(user);
    }

    @Override
    public SysUserVO getUserVOById(String id) {
        SysUser user = getById(id);
        return user != null ? toVO(user) : null;
    }

    @Override
    public IPage<SysUserVO> queryUsers(SysUserQueryDTO queryDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 只查询未删除的记录
        wrapper.isNull(SysUser::getDeletedAt);

        // 动态条件拼接
        if (StringUtils.hasText(queryDTO.account())) {
            wrapper.like(SysUser::getAccount, queryDTO.account());
        }
        if (StringUtils.hasText(queryDTO.mobile())) {
            wrapper.like(SysUser::getMobile, queryDTO.mobile());
        }
        if (StringUtils.hasText(queryDTO.email())) {
            wrapper.like(SysUser::getEmail, queryDTO.email());
        }
        if (StringUtils.hasText(queryDTO.userType())) {
            wrapper.eq(SysUser::getUserType, queryDTO.userType());
        }
        if (queryDTO.mobileVerified() != null) {
            wrapper.eq(SysUser::getMobileVerified, queryDTO.mobileVerified());
        }
        if (queryDTO.emailVerified() != null) {
            wrapper.eq(SysUser::getEmailVerified, queryDTO.emailVerified());
        }

        wrapper.orderByDesc(SysUser::getCreatedAt);

        IPage<SysUser> page = page(new Page<>(pageNum, pageSize), wrapper);
        return page.convert(this::toVO);
    }

    @Override
    public boolean deleteUser(String id) {
        SysUser user = getById(id);
        if (user == null) {
            return false;
        }
        user.setDeletedAt(LocalDateTime.now());
        boolean result = updateById(user);
        if (result) {
            log.info("软删除用户成功, id: {}", id);
        }
        return result;
    }

    @Override
    public boolean batchDeleteUsers(List<String> ids) {
        List<SysUser> users = listByIds(ids);
        if (users.isEmpty()) {
            return false;
        }
        users.forEach(user -> {
            user.setDeletedAt(LocalDateTime.now());
            updateById(user);
        });
        log.info("批量软删除用户成功, ids: {}", ids);
        return true;
    }

    /**
     * SysUser -> SysUserVO 转换
     */
    private SysUserVO toVO(SysUser user) {
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
