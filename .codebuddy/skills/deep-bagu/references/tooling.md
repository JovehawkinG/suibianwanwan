<!-- file: deep-bagu/references/tooling.md -->

# 工具命令模板与安全提示

## 1. 通用采集顺序

1. 指标与日志：监控面板、错误日志、慢日志、GC 日志、连接池指标。
2. 轻量诊断：`jstat`、`jcmd` 只读命令、线程栈、`SHOW PROCESSLIST`、Redis `INFO`。
3. 限时追踪：Arthas `watch`/`trace`、链路追踪采样、锁等待查询。
4. 重量采集：堆 dump、NMT 详细追踪、全量扫描、详细火焰图。
5. 变更验证：基线对比、灰度、观察指标、回滚。

安全要求：

- 生产命令必须评估停顿、CPU、磁盘、网络与脱敏。
- 连接串、密码、用户数据、内部域名不进入输出。
- 采集文件落盘前确认磁盘余量与保留周期。
- 重量采集需低峰期、审批、限时、限范围。

## 2. JVM / GC

### 2.1 轻量采集

```bash
java -version
jcmd <pid> VM.version
jstat -gcutil <pid> 1000 10
jcmd <pid> GC.heap_info
jcmd <pid> VM.flags
```

输出解读要点：

- `jstat -gcutil`：关注老年代使用率、Full GC 次数与耗时趋势。
- `GC.heap_info`：关注各代容量、已用、提交量。
- `VM.flags`：确认实际生效参数，避免只看启动脚本。
- 默认值与行为需按 JDK 版本确认。

### 2.2 NMT

```bash
jcmd <pid> VM.native_memory summary
```

前置条件：启动参数开启 NMT，例如 `-XX:NativeMemoryTracking=summary`。详细追踪有额外开销，生产需评估。

### 2.3 堆直方图与堆 dump

```bash
jmap -histo:live <pid>
jcmd <pid> GC.heap_dump /data/dump/heap-$(date +%s).hprof
```

风险提示：

- `-histo:live` 可能触发 STW，生产避免高频执行。
- 堆 dump 可能停顿并占用大量磁盘，需低峰期、审批、限磁盘余量。
- dump 文件含敏感数据，传输与保存需脱敏和权限控制。

### 2.4 分析工具

- MAT：支配树、泄漏嫌疑、引用链。
- GCViewer / GC 日志分析：停顿分布、吞吐、晋升速率。
- 源码关键路径只给类/方法/机制，不伪造行号。

## 3. 并发 / AQS / JMM

```bash
jstack -l <pid>
jcmd <pid> Thread.print -l
```

Arthas 限时示例：

```bash
thread -n 5
thread -b
watch <class> <method> '{params,returnObj,throwExp}' -n 5 -x 2
trace <class> <method> '#cost > 100' -n 5
```

输出解读要点：

- 线程状态分布：RUNNABLE、BLOCKED、WAITING、TIMED_WAITING。
- 锁持有与等待链，死锁检测结果。
- 线程池指标：活跃线程、队列长度、拒绝次数、任务耗时。
- `watch`/`trace` 避免高频大范围，生产限时并脱敏。

## 4. MySQL

```sql
EXPLAIN <sql>;
EXPLAIN ANALYZE <sql>;
SHOW ENGINE INNODB STATUS\G
SHOW PROCESSLIST;
SELECT * FROM performance_schema.data_lock_waits;
```

慢日志字段关注：

- `Query_time`、`Lock_time`、`Rows_examined`、`Rows_sent`。
- 执行计划关注：访问类型、可用索引、实际行数、回表、临时表、排序。
- 锁等待关注：等待事务、持有事务、锁类型、索引与间隙。
- 生产 DDL、全表扫描、锁分析需低峰期与审批。

## 5. Redis

```bash
redis-cli INFO memory
redis-cli INFO stats
redis-cli SLOWLOG GET 10
redis-cli MEMORY USAGE <key>
redis-cli --scan --pattern '<prefix>*' --count 100
```

风险提示：

- 禁止生产 `KEYS *`。
- `SCAN` 限频限量，避免影响事件循环。
- 大 key / 热 key 采样优先只读从库或低峰期。
- 关注内存碎片率、淘汰次数、命中率、持久化延迟、慢日志。

## 6. Spring

采集点：

- 启动日志：Bean 创建耗时、条件装配报告、代理创建异常。
- 条件装配报告：`--debug` 输出，确认自动配置命中与排除。
- 代理链：AOP 切面顺序、事务代理、循环依赖相关日志。
- 线程栈：启动卡住时抓取线程栈，定位锁与初始化阻塞。
- 动态代理与启动耗时分析避免频繁重启生产实例。

## 7. 分布式 / 中间件

采集点：

- 超时、重试、幂等、熔断、降级、限流指标。
- 链路追踪：调用链、耗时分布、错误码、重试次数。
- 消息：积压量、消费延迟、重投次数、死信队列。
- 压测：环境、数据量、并发量、持续时间、P99/P999。
- 变更需灰度与回滚，压测在隔离环境进行。

## 8. 最小证据包

| 主题 | 最小证据包 |
|---|---|
| JVM / GC | 版本、生效参数、GC 趋势、堆/元空间指标、1 份线程栈 |
| 并发 | 线程栈、锁等待链、线程池指标、关键接口耗时 |
| MySQL | 慢日志、执行计划、锁等待、事务隔离级别 |
| Redis | `INFO` 关键指标、慢日志、大 key/热 key 采样、内存趋势 |
| Spring | 启动日志、条件装配报告、线程栈、代理链信息 |
| 分布式 | 链路追踪、超时重试指标、熔断指标、压测数据 |