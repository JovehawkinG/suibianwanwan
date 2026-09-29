# Command: /deep-bagu

## 用途

调用 `.codebuddy/skills/deep-bagu/SKILL.md` 中的“八股文实战硬核讲解”技能，对用户提出的八股文 / Interview 题进行 3 年程序员深度的实战解析。

## 触发方式

满足任一条件即触发：

1. 用户显式输入：`/deep-bagu <问题>`
2. 用户消息包含：八股、Interview 题、JVM、GC、并发、AQS、MySQL、Redis、Spring、分布式、底层原理、源码、调优、排查 等关键词
3. 用户明显在问经典 Interview 知识点，但希望从实战角度理解

## 执行步骤

1. 读取 `.codebuddy/skills/deep-bagu/SKILL.md`，加载“八股文实战硬核讲解”技能。
2. 识别用户问题所属领域：JVM、并发、MySQL、Redis、Spring、分布式、网络、操作系统等。
3. 严格按 `.codebuddy/skills/deep-bagu/SKILL.md` 中的“输出模板”组织回答：
   - 本质一句话
   - 实战故障现场
   - 底层原理
   - 工具与命令
   - 参数与调优
   - 常见坑与 Interview 陷阱
   - 三年程序员加分回答
4. 确保深度达到 3 年程序员必备水平：
   - 涉及源码、字节码、HotSpot、Linux 内核、JVM 参数、GC 日志、NMT、Arthas、MAT 等
   - 有错误日志、有排查命令、有调优取舍、有生产案例
5. 禁止只给概念定义，禁止泛泛而谈。
6. 如果用户问题太宽泛，先帮用户缩小范围，再按模板深挖。
7. 回答中不得出现“面试”中文二字，统一用 `Interview` 代替。

## 使用示例

### 示例 1

用户输入：

```text
/deep-bagu JVM 运行时内存区域有哪些？