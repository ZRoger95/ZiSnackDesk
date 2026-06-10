package com.bosyon.zisnackdesk.sysUserService.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosyon.zisnackdesk.mapper.SysUserMapper;
import com.bosyon.zisnackdesk.model.SysUser;
import com.bosyon.zisnackdesk.model.dto.SysUserCreateDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserQueryDTO;
import com.bosyon.zisnackdesk.model.dto.SysUserUpdateDTO;
import com.bosyon.zisnackdesk.model.vo.SysUserVO;
import com.bosyon.zisnackdesk.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysUserServiceImpl 单元测试")
class SysUserServiceImplTest {


    @Mock
    private SysUserMapper sysUserMapper;

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    @Captor
    private ArgumentCaptor<SysUser> userCaptor;

    @BeforeEach
    void setUp() {
        // ServiceImpl 内部通过 baseMapper 调用 MyBatis-Plus 方法
        ReflectionTestUtils.setField(sysUserService, "baseMapper", sysUserMapper);
        // 使用 spy 包装 service，便于对 ServiceImpl 提供的方法（如 listByIds）进行存根
        sysUserService = spy(sysUserService);
    }

    @Nested
    @DisplayName("创建用户")
    class CreateUserTests {

        @Test
        @DisplayName("将 DTO 映射到实体并调用 save，返回 VO")
        void createUser_setsFieldsAndReturnsVO() {
            SysUserCreateDTO dto = new SysUserCreateDTO("alice", "13800138000", "alice@example.com", "pass123", null);

            doAnswer(invocation -> {
                SysUser u = invocation.getArgument(0);
                u.setId("id-1");
                u.setCreatedAt(LocalDateTime.now());
                return true;
            }).when(sysUserService).save(any(SysUser.class));

            SysUserVO vo = sysUserService.createUser(dto);

            assertNotNull(vo);
            assertEquals("id-1", vo.getId());

            verify(sysUserService).save(userCaptor.capture());
            SysUser saved = userCaptor.getValue();
            assertEquals("alice", saved.getAccount());
            assertEquals("13800138000", saved.getMobile());
            assertEquals("alice@example.com", saved.getEmail());
            assertEquals("member", saved.getUserType());
        }
    }

    @Nested
    @DisplayName("更新用户")
    class UpdateUserTests {

        @Test
        @DisplayName("更新存在的用户并返回 VO")
        void updateUser_success() {
            SysUser existing = new SysUser();
            existing.setId("id-2");
            existing.setAccount("old");
            existing.setMobile("111");
            existing.setEmail("old@example.com");
            existing.setMobileVerified(false);
            existing.setEmailVerified(false);

            when(sysUserService.getById("id-2")).thenReturn(existing);
            doReturn(true).when(sysUserService).updateById(any(SysUser.class));

            SysUserUpdateDTO dto = new SysUserUpdateDTO("id-2", "newacc", "222", "new@example.com", "newpass", "admin", true, true);

            SysUserVO vo = sysUserService.updateUser(dto);
            assertNotNull(vo);
            assertEquals("newacc", vo.getAccount());
            assertEquals("222", vo.getMobile());
            assertEquals("new@example.com", vo.getEmail());

            verify(sysUserService).updateById(userCaptor.capture());
            SysUser updated = userCaptor.getValue();
            assertEquals("newacc", updated.getAccount());
            assertEquals("222", updated.getMobile());
            assertTrue(updated.getMobileVerified());
            assertTrue(updated.getEmailVerified());
        }

        @Test
        @DisplayName("用户不存在时抛出异常")
        void updateUser_notFound_throws() {
            when(sysUserService.getById("no")).thenReturn(null);
            SysUserUpdateDTO dto = new SysUserUpdateDTO("no", null, null, null, null, null, null, null);
            assertThrows(RuntimeException.class, () -> sysUserService.updateUser(dto));
        }
    }

    @Nested
    @DisplayName("查询/分页")
    class QueryTests {

        @Test
        @DisplayName("分页查询返回 VO 列表")
        void queryUsers_returnsConvertedPage() {
            Page<SysUser> page = new Page<>(1, 10);
            SysUser s = new SysUser();
            s.setId("p1");
            s.setAccount("quser");
            page.setRecords(List.of(s));
            doReturn(page).when(sysUserService).page(any(Page.class), any());

            IPage<SysUserVO> res = sysUserService.queryUsers(new SysUserQueryDTO(null, null, null, null, null, null), 1, 10);
            assertNotNull(res);
            assertEquals(1, res.getRecords().size());
            assertEquals("quser", res.getRecords().get(0).getAccount());
        }
    }

    @Nested
    @DisplayName("删除")
    class DeleteTests {

        @Test
        @DisplayName("deleteUser 返回 false 当用户不存在；返回 true 并设置 deletedAt 当存在")
        void deleteUser_falseWhenNotFound_trueWhenDeleted() {
            when(sysUserService.getById("no")).thenReturn(null);
            assertFalse(sysUserService.deleteUser("no"));

            SysUser s = new SysUser();
            s.setId("d1");
            when(sysUserService.getById("d1")).thenReturn(s);

            doReturn(true).when(sysUserService).updateById(any(SysUser.class));

            boolean res = sysUserService.deleteUser("d1");
            assertTrue(res);

            verify(sysUserService).updateById(userCaptor.capture());
            SysUser updated = userCaptor.getValue();
            assertNotNull(updated.getDeletedAt());
        }

        @Test
        @DisplayName("batchDeleteUsers 对空列表返回 false，对非空列表执行批量软删除")
        void batchDeleteUsers_emptyAndNonEmpty() {
            when(sysUserService.listByIds(List.of("a", "b"))).thenReturn(Collections.emptyList());
            assertFalse(sysUserService.batchDeleteUsers(List.of("a", "b")));

            SysUser s1 = new SysUser(); s1.setId("a");
            SysUser s2 = new SysUser(); s2.setId("b");
            when(sysUserService.listByIds(List.of("a", "b"))).thenReturn(List.of(s1, s2));
            doReturn(true).when(sysUserService).updateById(any(SysUser.class));
            assertTrue(sysUserService.batchDeleteUsers(List.of("a", "b")));
            verify(sysUserService, times(2)).updateById(any(SysUser.class));

            assertNotNull(s1.getDeletedAt());
            assertNotNull(s2.getDeletedAt());
        }
    }

}
