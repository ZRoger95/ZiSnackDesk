# SysUserServiceImplTest → SysUserApplicationServiceTest 迁移设计

> 将旧贫血模型下的 Service 测试迁移到 DDD 半血模型下的 ApplicationService 测试
> 日期：2026-06-12

## 1. 背景

项目已按 `2026-06-12-lightweight-ddd-halfblood-model-design.md` 完成轻量级 DDD 改造：

- `SysUserServiceImpl`（`service/impl/`）→ `SysUserApplicationService`（`user/application/`）
- `SysUserMapper` 直接调用 → 通过 `SysUserRepository` 接口隔离
- 领域逻辑从 Service 下沉到 Domain 实体（`validateForCreate`、`markAsDeleted` 等）
- DTO/VO 替换为 Record 类型

旧测试 `SysUserServiceImplTest` 仍针对旧架构，需迁移。

## 2. 设计

### 2.1 文件布局

| 项目 | 旧值 | 新值 |
|------|------|------|
| 测试类名 | `SysUserServiceImplTest` | `SysUserApplicationServiceTest` |
| 包路径 | `com.bosyon.zisnackdesk.service.impl` | `com.bosyon.zisnackdesk.user.application` |
| 目标类 | `SysUserServiceImpl` | `SysUserApplicationService` |
| Mock 目标 | `SysUserMapper` | `SysUserRepository` |

### 2.2 Mock 策略

```java
@ExtendWith(MockitoExtension.class)
class SysUserApplicationServiceTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private SysUserApplicationService sysUserService;
}
```

不再需要：
- `ReflectionTestUtils.setField(field, "baseMapper", ...)` — 因为不再继承 `ServiceImpl`
- `spy(sysUserService)` — 因为不再需要 stub `ServiceImpl` 的内置方法（如 `save`, `getById`）
- `ArgumentCaptor` 的使用场景从捕获 `SysUser` 实体变为仍然捕获 `SysUser`（但通过 `repository.save()` / `repository.update()` 传入）

### 2.3 测试场景映射

| 场景 | 旧实现 | 新实现 |
|------|--------|--------|
| **创建用户** | mock `service.save()` → 返回带 ID 的实体 | mock `repository.save()` → 返回带 ID 的 domain，注意 `existsByAccount` 前置校验 |
| **更新用户（存在）** | mock `service.getById()` + `service.updateById()` | mock `repository.findById()` + `repository.update()` |
| **更新用户（不存在）** | mock `service.getById()` → null | mock `repository.findById()` → `Optional.empty()` |
| **分页查询** | mock `service.page()` | mock `repository.query()` |
| **删除（存在）** | mock `service.getById()` → 设置 `deletedAt` | mock `repository.findById()` → 调用 `user.markAsDeleted()` + `repository.update()` |
| **删除（不存在）** | mock `service.getById()` → null | mock `repository.findById()` → `Optional.empty()` |
| **批量删除** | mock `service.listByIds()` + 循环 `updateById()` | mock `repository.softDeleteBatch()` |

### 2.4 DTO/VO 变更

| 旧类型 | 新类型 |
|--------|--------|
| `SysUserCreateDTO` | `SysUserCreateRequest` |
| `SysUserUpdateDTO` | `SysUserUpdateRequest` |
| `SysUserQueryDTO` | `SysUserQueryRequest` |
| `SysUserVO` | `SysUserResponse` |

### 2.5 Domain 行为验证

新测试需要验证 Domain 实体的行为方法被正确调用：

- **创建**：`user.validateForCreate()` 在 `save()` 之前被调用
- **更新**：`user.validateForUpdate()` 在 `update()` 之前被调用
- **删除**：`user.markAsDeleted()` 在 `update()` 之前被调用

通过捕获 `repository.save()` / `repository.update()` 的参数来验证 domain 状态。

## 3. 不纳入范围

- 不新增 `SysUserRepositoryImplTest`（基础设施层测试单独考虑）
- 不修改 Controller 测试（不在本次范围）
- 不修改其他 bounded context 的测试

## 4. 风险

| 风险 | 缓解 |
|------|------|
| `existsByAccount` 校验在旧测试中不存在，新测试需添加 | 在 createUser 测试中增加 `when(repository.existsByAccount()).thenReturn(false)` |
| Domain 行为在测试中不易直接验证 | 通过捕获 repository 参数并断言 domain 状态来间接验证 |
