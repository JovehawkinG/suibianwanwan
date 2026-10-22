<!-- file: deep-bagu/references/examples.md -->

# 好例与反例

对齐模板 `0. 一句话结论` ~ `6. 落地与命令`，重点看示例与断点的写法。

## 1. Golden Example：Spring 循环依赖（L2）

### 0. 一句话结论

字段注入的循环依赖靠三级缓存解决：`singletonFactories` 提前暴露对象工厂，让 B 在 A 未完成初始化时先拿到 A 的引用。构造器注入的循环依赖无法用三级缓存解决，直接抛 `BeanCurrentlyInCreationException`。适用 Spring Framework 5.3.x / 6.x。

### 1. 核心机制

1. 创建 A → 实例化后、填充属性前，把 `ObjectFactory` 放入 `singletonFactories`。
2. 填充 A 的属性时发现依赖 B → 转去创建 B。
3. B 填充属性时发现依赖 A → 再次 `getBean("a")`，从 `singletonFactories` 取工厂并调用 `getObject()`，得到 A 的早期引用，放入 `earlySingletonObjects`。
4. B 完成创建后回到 A，A 继续填充属性并初始化，最后进入 `singletonObjects`。

### 2. 最小示例

```java
// JDK 17 + Spring Framework 6.x，原生容器即可复现，不依赖 Spring Boot
@Configuration
@ComponentScan
public class CircularDemoApplication {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(CircularDemoApplication.class);
    }

    @Component
    static class A {
        @Autowired
        private B b;                    // BREAKPOINT 1：观察 a 已实例化、b 仍为 null
    }

    @Component
    static class B {
        @Autowired
        private A a;                    // BREAKPOINT 2：观察 a 是通过 ObjectFactory 提前暴露的引用
    }
}
```

预期现象：应用正常启动；把 `A` 的注入方式改成构造器 `A(B b)` 后启动失败，异常为 `BeanCurrentlyInCreationException`。

> Spring Boot 2.6+ 默认 `spring.main.allow-circular-references=false`，同一份代码放进 Boot 应用会直接失败；演示三级缓存需显式设为 true，或使用上面的原生容器写法。

### 3. 源码链路与断点

| 步骤 | 类#方法 | 断点条件 | 观察变量 |
|---|---|---|---|
| 1 | `DefaultSingletonBeanRegistry#getSingleton(String, boolean)` | beanName = "a"，allowEarlyReference = false | `singletonObjects` 无 a，返回 null |
| 2 | `AbstractAutowireCapableBeanFactory#doCreateBean` | beanName = "a"，进入 `addSingletonFactory` | `singletonFactories` 出现 a 的 `ObjectFactory` |
| 3 | `AbstractAutowireCapableBeanFactory#populateBean` | beanName = "a" | `b` 字段为 null，即将触发 `getBean("b")` |
| 4 | `DefaultSingletonBeanRegistry#getSingleton(String, boolean)` | beanName = "a"，allowEarlyReference = true | `earlySingletonObjects` 仍为空，随后从 `singletonFactories` 取工厂 |

预期调用栈（自上而下）：

```text
getBean("a")
└─ doGetBean
   └─ createBean
      └─ doCreateBean
         ├─ addSingletonFactory("a", ObjectFactory)
         └─ populateBean("a")
            └─ getBean("b")
               └─ doCreateBean("b")
                  └─ populateBean("b")
                     └─ getBean("a") → getSingleton("a", true)
                        └─ singletonFactories.get("a").getObject()
```

### 4. 常见坑与误判

- 以为三级缓存能解决构造器循环依赖：不能，构造器参数必须在实例化前解析，此时对象还不存在。
- 以为 `earlySingletonObjects` 一开始就有值：它只在第二次获取同一 Bean 时被填充。
- 加了 `@Async` / `@Transactional` 后循环依赖报错：早期引用可能触发代理创建，行为与无代理时不同。
- Boot 2.6+ 默认禁止循环依赖，误判为 Spring 版本 bug。

### 5. 延伸追问

- 为什么需要第三级 `singletonFactories` 而不是直接放早期对象：为了在需要时才对原始对象生成代理。
- `@Lazy` 如何打断循环：注入代理对象，把真实创建推迟到首次调用。
- 构造器注入的循环依赖能否用 setter 改造解决：可以，但会掩盖设计问题。

### 6. 落地与命令

- 排查启动失败：看异常栈顶是 `BeanCurrentlyInCreationException` 还是 `BeanCreationException`，前者优先查构造器注入链。
- 生产不建议开启 `allow-circular-references`，应拆依赖或引入事件解耦。

## 2. Golden Example：MySQL 死锁（L2）

### 0. 一句话结论

死锁是多个事务形成循环等待，InnoDB 通过等待图检测并回滚代价较小的事务。定位靠 `SHOW ENGINE INNODB STATUS` 的 `LATEST DETECTED DEADLOCK` 段，治理靠统一加锁顺序与缩短事务。适用 MySQL 8.0，RR 隔离级别。

### 1. 核心机制

1. 事务 1 持有 id=1 的行锁，请求 id=2。
2. 事务 2 持有 id=2 的行锁，请求 id=1。
3. 等待图成环，InnoDB 选一个事务回滚，报 `Deadlock found when trying to get lock`。
4. 间隙锁、唯一键冲突、索引缺失都会扩大锁范围，提高成环概率。

### 2. 最小示例

```sql
-- MySQL 8.0，RR 隔离级别，两个会话按顺序执行
CREATE TABLE account (id INT PRIMARY KEY, balance INT NOT NULL) ENGINE = InnoDB;
INSERT INTO account VALUES (1, 100), (2, 100);

-- 会话 1
BEGIN;
UPDATE account SET balance = balance - 10 WHERE id = 1;

-- 会话 2
BEGIN;
UPDATE account SET balance = balance - 10 WHERE id = 2;

-- 会话 1：等待会话 2 释放 id=2
UPDATE account SET balance = balance + 10 WHERE id = 2;

-- 会话 2：触发死锁，其中一个事务被回滚
UPDATE account SET balance = balance + 10 WHERE id = 1;
```

预期现象：会话 2 返回 `ERROR 1213 (40001): Deadlock found when trying to get lock; try restarting transaction`。

### 3. 源码链路与断点

| 步骤 | 观察点 | 命令 | 关键字段 |
|---|---|---|---|
| 1 | 最近一次死锁 | `SHOW ENGINE INNODB STATUS\G` | `LATEST DETECTED DEADLOCK` 段的两个事务 SQL |
| 2 | 当前锁等待 | `performance_schema.data_lock_waits` | `REQUESTING_ENGINE_LOCK_ID`、`BLOCKING_ENGINE_LOCK_ID` |
| 3 | 持有与等待的锁 | `performance_schema.data_locks` | `LOCK_TYPE`、`LOCK_MODE`、`LOCK_DATA` |
| 4 | 访问路径 | `EXPLAIN UPDATE ...` | `type`、`key`、`rows` |

无代码断点，用上述视图代替：`LOCK_DATA` 能看到具体锁在哪一行或哪个间隙。

### 4. 常见坑与误判

- 只看慢日志：死锁事务可能执行很快，慢日志里没有记录。
- 以为死锁一定发生在两条相反顺序的 UPDATE：间隙锁与唯一键冲突也会成环。
- 索引缺失导致锁全表，误判为"数据量小不可能死锁"。
- 应用层重试无退避，死锁回滚后立刻重试再次成环。

### 5. 延伸追问

- 为什么 RR 下需要间隙锁：防止幻读，代价是锁范围扩大。
- 死锁与锁等待超时的区别：前者由检测机制主动回滚，后者是等不到锁自行超时。
- 如何用 `innodb_print_all_deadlocks` 把死锁写入错误日志。

### 6. 落地与命令

- 开 `innodb_print_all_deadlocks=ON`（需重启或按版本确认是否支持动态修改），把死锁落到错误日志。
- 统一加锁顺序、缩短事务、补全索引，是比调大 `innodb_lock_wait_timeout` 更有效的治理方式。
- 应用层重试需带退避与幂等，避免放大。

## 3. Bad Example：概念堆砌

问题：

- 开头铺垫三段背景，结论藏在最后。
- 只列名词，没有示例代码，没有断点，没有观察变量。
- 默认值无版本，源码给行号。
- 危险命令无风险提示。

修正：

- 结论前置，3 行内说清。
- 补最小可运行示例与 `// BREAKPOINT` 标注。
- 源码只给类名、方法名、断点条件、预期调用栈。
- 危险命令加低峰期、审批、脱敏、回滚。

## 4. 示例自查

- 示例是否含完整 import 与必要配置，能直接运行。
- 是否标注 2~4 处断点与观察变量。
- 是否给出 5~8 帧预期调用栈。
