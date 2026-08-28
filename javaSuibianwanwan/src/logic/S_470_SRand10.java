package logic;

// 470
public class S_470_SRand10 {
    public int rand10() {
        while (true) {
            int num = (rand7() - 1) * 7 + rand7();
            if (num <= 40) {
                return (num - 1) % 10 + 1;
            }
        }
    }

    // 环境由 SolBase 提供，本地占位
    private int rand7() {
        return 0;
    }
}
