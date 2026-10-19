# Command: /deep-lee

## 用途

只根据用户给出的「题号 + 题目名称」检索并还原题目原文，输出一份**纯题目描述**（题干、示例、约束、函数签名），同时在 `javaSuibianwanwan/src/logic` 下按项目既有约定新建一个**待实现的模板类**。

本指令是「出题器」，不是「解题器」：不分析、不提示、不给思路、不给解法、不写实现代码、不评估难度。模板里只允许出现方法签名、空方法体（`// TODO` + 默认返回值）和题目给出的节点类。

## 触发方式

1. 用户显式输入：`/deep-lee <题号> <题目名称>`
2. 用户只给出题号或只给出名称，也可触发，但先按下方「执行步骤」第 1 步做参数校验，再检索

## 参数

```text
/deep-lee <题号> <题目名称>
```

- 题号可能的形式：`155`、`LCR 155`、`LCR155`、`面试题 36`、`剑指 Offer 36`、`912`
- 题目名称：中文或英文均可，允许模糊（如"二叉搜索树转链表"）

## 执行步骤

1. **解析参数**：拆出题号与题目名称，各自作为独立检索键。记录题号前缀（`LCR` / `面试题` / `剑指 Offer` / 纯数字），前缀决定题目来源（LCR 与面试题系列只在中文站存在）。
2. **检索**：用 `web_search` 搜索，关键词组合至少覆盖两种：
   - `<题号> <题目名称> 力扣`
   - `<题号> <题目名称> leetcode`
   - 名称不确定时追加：`<题号> 力扣 题目`
3. **抓取原文**：用 `web_fetch` 打开命中页面，优先级：
   1. `leetcode.cn/problems/<slug>/`
   2. `leetcode.com/problems/<slug>/`
   3. 其他可信镜像（仅当官方页抓取失败）
4. **只提取以下内容**：
   - 题目描述正文（含"注意""说明""进阶"）
   - 输入 / 输出定义（若有）
   - 全部示例（含示例的输入、输出、解释）
   - 提示 / 约束 / 数据范围
   - 函数签名与题目已给出的类定义（如 `ListNode`、`TreeNode`、`Node`）——这些属于题目内容，可以保留
5. **校验题号与名称是否指向同一题**：若两者冲突（题号对应 A，名称对应 B），停止后续动作，列出候选并请用户确认，不擅自二选一，也不建文件。
6. **按「模板文件规范」新建模板类**：写入 `javaSuibianwanwan/src/logic/`，只建这一个文件。
7. **按输出模板作答**：题目原文 + 新建文件路径，除此之外不追加任何内容。

## 模板文件规范

### 路径与命名

文件放在 `javaSuibianwanwan/src/logic/`，类名 = 文件名：

| 题号形式 | 命名格式 | 示例 |
| --- | --- | --- |
| 纯数字 | `S_<题号>_S<方法名首字母大写>.java` | `230` + `kthSmallest` → `S_230_SKthSmallest.java` |
| LCR | `S_LCR<题号>_S<方法名首字母大写>.java` | `LCR 155` + `treeToDoublyList` → `S_LCR155_STreeToDoublyList.java` |
| 面试题 / 剑指 Offer | 若同题在力扣中文站有 LCR 编号，优先用 LCR 编号命名；否则 `S_Offer<题号>_S<方法名首字母大写>.java` | `面试题 36` → 用 `S_LCR155_STreeToDoublyList.java` |
| 设计类题目（`MinStack`、`LFUCache` 等） | `S_<题号>_S<题目类名>.java` | `155` + `MinStack` → `S_155_SMinStack.java` |

命名以**官方英文方法名 / 类名**为准，不要自己翻译题名造词；同名文件已存在时，**不覆盖、不改动**，只在输出中标注「已存在，跳过创建」。

### 类结构

1. 首行固定 `package logic;`，其后空一行写 `// <题号>`（如 `// LCR 155`、`// 230`），与现有文件一致。
2. 类声明 `public class <类名>`。
3. 方法体只写 `// TODO` + 合法默认返回：
   - 引用类型 → `return null;`
   - `boolean` → `return false;`
   - 数值 / `char` → `return 0;`
   - `void` → 空方法体
4. 题目给出的节点类以 `public static class` 定义在类末尾，字段照抄官方定义（`Node` / `ListNode` 用 `public`，`TreeNode` 用包级），并补齐项目里常见的三种构造器：

```java
public static class Node {
    public int val;
    public Node left;
    public Node right;
    public Node() {}
    public Node(int val) { this.val = val; }
    public Node(int val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
```

5. 设计类题目：类骨架 + 字段声明（仅声明，不初始化逻辑）+ 各方法 `// TODO`，构造器留空或 `// TODO`。
6. 需要 `List`/`Map` 等类型时，只加签名必需的 `import`，按字典序放在 `package` 之后。
7. 模板中**不得**出现任何注释形式的提示：不写"用中序遍历""双指针""先排序"之类，也不写复杂度。

### 参考成品形态

```java
package logic;

// LCR 155
public class S_LCR155_STreeToDoublyList {
    public Node treeToDoublyList(Node root) {
        // TODO
        return null;
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node() {}
        public Node(int val) { this.val = val; }
        public Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
```

## 输出模板

````markdown
## <题号> <题目名称>

### 题目描述

<原文，保留分段与"注意/说明/进阶">

### 示例

**示例 1：**

```text
输入：...
输出：...
解释：...
```

### 提示

- <约束 1>
- <约束 2>

### 函数签名

```java
<题目给出的签名与类定义>
```

### 新建文件

`javaSuibianwanwan/src/logic/<类名>.java`（已存在则标注「已存在，跳过创建」）

### 来源

<官方题目链接>
````

## 强制约束（红线）

以下为硬性禁止，任何情况下不得突破：

1. **禁止解法**：不输出思路、算法、步骤、伪代码、正确代码、复杂度分析、时间/空间优化方向。
2. **禁止提示**：不输出"关键点""想到 XX 就能做""可以用 XX 数据结构""注意边界"等任何引导性内容，模板注释里同样禁止。
3. **禁止剧透**：不总结题型、不给相似题推荐、不引用题解区/讨论区内容、不评价难度与通过率。
4. **禁止读本地题解**：检索与建模板阶段不得读取 `src/logic`、`src/practice` 下任何可能包含该题实现的文件，避免把已有解法带入描述或模板。仅允许 `list_dir`/文件名检查以判断是否已存在。
5. **写文件范围**：只允许在 `javaSuibianwanwan/src/logic/` 下新建**至多一个**模板文件；禁止修改、覆盖、删除任何已有文件，禁止改动其他目录。
6. **禁止臆造**：题目原文与函数签名必须以抓取结果为准。未抓取成功时，明确说明"未检索到原文"，不建文件，绝不凭记忆补全题干、示例、约束或签名。
7. **禁止越界回答**：输出完题目与文件路径即结束。用户若在后续消息中要求解法，那是新的请求，由用户另行提出，本指令不主动引导。

## 使用示例

### 示例 1

用户输入：

```text
/deep-lee LCR 155 将二叉搜索树转化为双向链表
```

预期动作：

- 新建 `javaSuibianwanwan/src/logic/S_LCR155_STreeToDoublyList.java`，内容为 `treeToDoublyList` 空实现 + `Node` 内部类。
- 输出题干、示例、约束（如 `0 <= 节点数 <= 2000`、`-1000 <= Node.val <= 1000`）、`Node` 类定义与来源链接。

两句都不出现"中序遍历""pre 指针""首尾相连"等任何解法信息。

### 示例 2

用户输入：

```text
/deep-lee 912 排序数组
```

预期动作：新建 `S_912_SSortArray.java`（方法 `sortArray`，`return null;` + `// TODO`）；输出题干、示例、约束（`1 <= nums.length <= 50000` 等）、签名 `int[] sortArray(int[] nums)`。不出现"快排/归并/堆排"等任何算法名词。

### 示例 3

用户输入：

```text
/deep-lee 14 最长公共前缀
```

若检索到的题号与名称不一致（如题号指向其他题目），输出候选列表并请用户确认，不输出任何一题的正文，也不建文件。

### 示例 4

用户输入：

```text
/deep-lee LCR 155 将二叉搜索树转化为双向链表
```

且 `S_LCR155_STreeToDoublyList.java` 已存在时：不覆盖、不改写，输出中标注「已存在，跳过创建」，其余照常。
