package jp.project1.testsumlist;

/***************************************************************************************************
 * 　試験結果集計プログラム：Studentクラス
 *
 * 　プログラム名：	Student
 * 　概要：			試験結果データ用
 * 　作成日付：		2020/09/25
 * 　版数：			1.1版
 * 　作成者(班:PL)：杉谷一祝(1:京岡大純)
 * 　修正履歴：		1.1版   フィールドにint型変数rankを追加
 * 　備考：			なし
 * 　課題No：		3
 **************************************************************************************************/

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

    /** 順位　1.1版 */
    protected int           rank;
    /** 各科目の得点 */
    private int []          scores;
    /** 氏名 */
    private String          name;
    /** 再試験に該当するかどうか */
    private boolean         isRetester;

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

/***************************************************************************************************
 * 　メソッド(クラス)名：getScores(Student)
 *
 * 　メソッド名：   getScores
 * 　概要：			各科目の得点を取得 
 * 　引数：			なし
 * 　返却値:        int[]型scores
 *   備考:          なし
 **************************************************************************************************/
    /**
     * 各科目の得点を取得
     * @return int[]型scores
     */
    public int[] getScores() {
        return scores;
    }

/***************************************************************************************************
 * 　メソッド(クラス)名：getName(Student)
 *
 * 　メソッド名：   getName
 * 　概要：			氏名を取得 
 * 　引数：			なし
 * 　返却値:        String型name
 *   備考:          なし
 **************************************************************************************************/
    /**
     * 氏名を取得
     * @return String型name
     */
    public String getName() {
        return name;
    }

/***************************************************************************************************
 * 　メソッド(クラス)名：getIsRetester(Student)
 *
 * 　メソッド名：   getIsRetester
 * 　概要：			再試験に該当するかどうか 
 * 　引数：			なし
 * 　返却値:        boolean型isRetester
 *   備考:          なし
 **************************************************************************************************/
    /**
     * 再試験に該当するかどうか
     * @return boolean型isRetester
     */
    public boolean getIsRetester() {
        return isRetester;
    }

/***************************************************************************************************
 * 　メソッド(クラス)名：getSum(Student)
 *
 * 　メソッド名：   getSum
 * 　概要：			得点の合計を算出 
 * 　引数：			なし
 * 　返却値:        int型の得点の合計値
 *   備考:          なし
 **************************************************************************************************/
    /**
     * 得点合計値
     */
    public int getSum() {
        return Arrays.stream(scores).sum();
    }

/***************************************************************************************************
 * 　メソッド(クラス)名：toString(Student)
 *
 * 　メソッド名：   toString
 * 　概要：			必要なフィールドの返却 
 * 　引数：			なし
 * 　返却値:        String型のフィールド値一覧
 *   備考:          デバッグ用
 **************************************************************************************************/
    /**
     * デバッグ用
     */
    @Override
    public String toString() {
        return "Student [isRetester=" + isRetester + ", name=" + name + ", scores=" + Arrays.toString(scores) + "]";
    }

}