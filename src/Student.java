import java.util.Arrays;

/**
 * Studentクラス
 */
public class Student {
    /** for文カウンター変数の初期値 */
    public static final int ZERO                = 0;
    /** 欠席している場合 */
    public static final int NON_TESTER          = -1;
    /** 欠席者の点数 */
    public static final int NON_TESTER_SCORE    = 0;
    /** 赤点 */
    public static final int RED_SCORE           = 25;

    /** 各科目の得点 */
    public int []           scores;
    /** 氏名 */
    public String           name;
    /** 再試験に該当するかどうか */
    public boolean          isRetester;

    /**
     * 初期化用コンストラクタ
     * @param name 氏名
     * @param scores 各科目の得点
     */
    public Student(String name, int[] scores) {
        /* 氏名を格納 */
        this.name = name;

        /* 得点から再試験者を判定 */
        for (int i = ZERO; i < scores.length; i++) {
            /* 欠席の場合 */
            if (scores[i] == NON_TESTER) {
                scores[i] = NON_TESTER_SCORE;
                isRetester = true;
                continue;
            }
            /* 赤点の場合 */
            if (scores[i] <= RED_SCORE) {
                isRetester = true;
            }
        }

        /* 得点を格納 */
        this.scores = scores;
    }

    /**
     * 得点合計値
     */
    public int getSum() {
        return Arrays.stream(scores).sum();
    }

    /**
     * デバッグ用
     */
    @Override
    public String toString() {
        return "Student [isRetester=" + isRetester + ", name=" + name + ", scores=" + Arrays.toString(scores) + "]";
    }
    
}