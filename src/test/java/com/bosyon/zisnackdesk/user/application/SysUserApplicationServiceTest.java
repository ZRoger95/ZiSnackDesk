package com.bosyon.zisnackdesk.user.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosyon.zisnackdesk.user.domain.SysUser;
import com.bosyon.zisnackdesk.user.domain.SysUserRepository;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserCreateRequest;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserQueryRequest;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserResponse;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysUserApplicationService 单元测试")
class SysUserApplicationServiceTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private SysUserApplicationService sysUserService;

    @Captor
    private ArgumentCaptor<SysUser> userCaptor;

    @Nested
    @DisplayName("创建用户")
    class CreateUserTests {

        @Test
        @DisplayName("创建用户成功，返回 SysUserResponse")
        void createUser_success() {
            // given
            SysUserCreateRequest request = new SysUserCreateRequest("alice", "13800138000", "alice@example.com", "pass123", null);

            when(sysUserRepository.existsByAccount("alice")).thenReturn(false);
            when(sysUserRepository.save(any(SysUser.class))).thenAnswer(invocation -> {
                SysUser u = invocation.getArgument(0);
                u.setId("id-1");
                u.setCreatedAt(LocalDateTime.now());
                return u;
            });

            // when
            SysUserResponse response = sysUserService.createUser(request);

            // then
            assertNotNull(response);
            assertEquals("id-1", response.getId());
            assertEquals("alice", response.getAccount());

            verify(sysUserRepository).save(userCaptor.capture());
            SysUser saved = userCaptor.getValue();
            assertEquals("alice", saved.getAccount());
            assertEquals("13800138000", saved.getMobile());
            assertEquals("alice@example.com", saved.getEmail());
            assertEquals("member", saved.getUserType());
        }

        @Test
        @DisplayName("账号已存在时抛出异常")
        void createUser_accountExists_throws() {
            SysUserCreateRequest request = new SysUserCreateRequest("alice", null, null, "pass123", null);
            when(sysUserRepository.existsByAccount("alice")).thenReturn(true);

            assertThrows(RuntimeException.class, () -> sysUserService.createUser(request));
            verify(sysUserRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("更新用户")
    class UpdateUserTests {

        @Test
        @DisplayName("更新存在的用户并返回 SysUserResponse")
        void updateUser_success() {
            // given
            SysUser existing = new SysUser("old", "oldpass", "member");
            existing.setId("id-2");
            existing.setMobile("111");
            existing.setEmail("old@example.com");
            existing.setMobileVerified(false);
            existing.setEmailVerified(false);

            when(sysUserRepository.findById("id-2")).thenReturn(Optional.of(existing));
            when(sysUserRepository.update(any(SysUser.class))).thenAnswer(inv -> inv.getArgument(0));

            SysUserUpdateRequest request = new SysUserUpdateRequest(
                    "id-2", "newacc", "222", "new@example.com", "newpass", "admin", true, true);

            // when
            SysUserResponse response = sysUserService.updateUser(request);

            // then
            assertNotNull(response);
            assertEquals("newacc", response.getAccount());
            assertEquals("222", response.getMobile());
            assertEquals("new@example.com", response.getEmail());

            verify(sysUserRepository).update(userCaptor.capture());
            SysUser updated = userCaptor.getValue();
            assertEquals("newacc", updated.getAccount());
            assertEquals("222", updated.getMobile());
            assertTrue(updated.getMobileVerified());
            assertTrue(updated.getEmailVerified());
        }

        @Test
        @DisplayName("用户不存在时抛出异常")
        void updateUser_notFound_throws() {
            when(sysUserRepository.findById("no")).thenReturn(Optional.empty());

            SysUserUpdateRequest request = new SysUserUpdateRequest("no", null, null, null, null, null, null, null);

            assertThrows(RuntimeException.class, () -> sysUserService.updateUser(request));
            verify(sysUserRepository, never()).update(any());
        }
    }

    @Nested
    @DisplayName("查询/分页")
    class QueryTests {

        @Test
        @DisplayName("分页查询返回 SysUserResponse 列表")
        void queryUsers_returnsConvertedPage() {
            // given
            Page<SysUser> page = new Page<>(1, 10);
            SysUser user = new SysUser("quser", "pwd", "member");
            user.setId("p1");
            page.setRecords(List.of(user));

            when(sysUserRepository.query(any(SysUserQueryRequest.class), eq(1), eq(10))).thenReturn(page);

            SysUserQueryRequest query = new SysUserQueryRequest(null, null, null, null, null, null);

            // when
            IPage<SysUserResponse> res = sysUserService.queryUsers(query, 1, 10);

            // then
            assertNotNull(res);
            assertEquals(1, res.getRecords().size());
            assertEquals("quser", res.getRecords().get(0).getAccount());
        }
    }

    @Nested
    @DisplayName("删除")
    class DeleteTests {

        @Test
        @DisplayName("用户不存在时返回 false")
        void deleteUser_notFound_returnsFalse() {
            when(sysUserRepository.findById("no")).thenReturn(Optional.empty());
            assertFalse(sysUserService.deleteUser("no"));
        }

        @Test
        @DisplayName("删除存在的用户，设置 deletedAt 并返回 true")
        void deleteUser_success() {
            SysUser user = new SysUser("d1", "pwd", "member");
            user.setId("d1");
            when(sysUserRepository.findById("d1")).thenReturn(Optional.of(user));
            when(sysUserRepository.update(any(SysUser.class))).thenAnswer(inv -> inv.getArgument(0));

            boolean result = sysUserService.deleteUser("d1");
            assertTrue(result);

            verify(sysUserRepository).update(userCaptor.capture());
            SysUser updated = userCaptor.getValue();
            assertNotNull(updated.getDeletedAt());
        }

        @Test
        @DisplayName("批量删除直接调用 repository.softDeleteBatch")
        void batchDeleteUsers_callsSoftDeleteBatch() {
            List<String> ids = List.of("a", "b");
            doNothing().when(sysUserRepository).softDeleteBatch(ids);

            boolean result = sysUserService.batchDeleteUsers(ids);
            assertTrue(result);

            verify(sysUserRepository).softDeleteBatch(ids);
        }
    }
}
