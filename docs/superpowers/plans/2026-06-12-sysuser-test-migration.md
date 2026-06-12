# SysUser 测试迁移实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将旧 `SysUserServiceImplTest` 迁移为新的 `SysUserApplicationServiceTest`，适配 DDD 半血模型架构

**Architecture:** 新测试 mock `SysUserRepository` 接口而非 `SysUserMapper`，测试 `SysUserApplicationService` 的编排逻辑；DTO/VO 使用新 Record 类型；验证 Domain 实体行为方法被正确调用

**Tech Stack:** Java 21, JUnit 5, Mockito, Spring Boot 4

---

### Task 1: 创建 SysUserApplicationServiceTest

**Files:**
- Create: `src/test/java/com/bosyon/zisnackdesk/user/application/SysUserApplicationServiceTest.java`

- [ ] **Step 1: 创建测试文件，编写包声明、导入和类结构**

```java
package com.bosyon.zisnackdesk.user.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosyon.zisnackdesk.user.domain.SysUser;
import com.bosyon.zisnackdesk.user.domain.SysUserRepository;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserCreateRequest;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserQueryRequest;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserResponse;
import com.bosyon.zisnackdesk.user.interfaces.dto.SysUserUpdateRequest;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
```

- [ ] **Step 2: 编写「创建用户」测试 — 映射原来的 CreateUserTests**

核心变化：
- `SysUserCreateDTO` → `SysUserCreateRequest` (Record)
- mock `repository.save()` 替代 mock `service.save()`
- 需要 mock `repository.existsByAccount()` 返回 false（唯一性校验）
- 验证 domain 实体 `validateForCreate()` 被隐式调用

```java
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
            assertEquals("member", saved.getUserType()); // 默认值
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
```

- [ ] **Step 3: 编写「更新用户」测试 — 映射原来的 UpdateUserTests**

核心变化：
- `SysUserUpdateDTO` → `SysUserUpdateRequest` (Record)
- `repository.findById()` 替代 `service.getById()`
- `repository.update()` 替代 `service.updateById()`
- 验证 domain 实体 `validateForUpdate()` 被隐式调用

```java
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
```

- [ ] **Step 4: 编写「分页查询」测试 — 映射原来的 QueryTests**

```java
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
```

- [ ] **Step 5: 编写「删除」测试 — 映射原来的 DeleteTests**

核心变化：
- `repository.findById()` 替代 `service.getById()`
- `user.markAsDeleted()` 替代手动 `setDeletedAt`
- `repository.update()` 替代 `service.updateById()`
- 批量删除直接调用 `repository.softDeleteBatch()`

```java
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
```

- [ ] **Step 6: 补全类结尾括号并保存文件**

```java
}
```

- [ ] **Step 7: 编译验证**

Run: `mvn -DskipTests compile test-compile`
Expected: BUILD SUCCESS

- [ ] **Step 8: 运行新测试**

Run: `mvn test -pl . -Dtest=com.bosyon.zisnackdesk.user.application.SysUserApplicationServiceTest`
Expected: 所有测试通过

---

### Task 2: 删除旧测试文件

- [ ] **Step 1: 删除旧的 SysUserServiceImplTest**

Delete: `src/test/java/com/bosyon/zisnackdesk/service/impl/SysUserServiceImplTest.java`

- [ ] **Step 2: 编译验证无残留引用**

Run: `mvn -DskipTests compile test-compile`
Expected: BUILD SUCCESS

---

### Task 3: 最终验证

- [ ] **Step 1: 运行完整测试套件**

Run: `mvn test`
Expected: BUILD SUCCESS

- [ ] **Step 2: Commit**

```bash
git add src/test/java/com/bosyon/zisnackdesk/user/application/SysUserApplicationServiceTest.java
git rm src/test/java/com/bosyon/zisnackdesk/service/impl/SysUserServiceImplTest.java
git add docs/superpowers/specs/2026-06-12-sysuser-test-migration-design.md
git add docs/superpowers/plans/2026-06-12-sysuser-test-migration.md
git commit -m "test: migrate SysUserServiceImplTest to SysUserApplicationServiceTest

- 迁移到 DDD 半血模型架构
- 测试 SysUserApplicationService，mock SysUserRepository
- 适配新的 DTO/VO Record 类型
- 删除旧测试文件"
```
