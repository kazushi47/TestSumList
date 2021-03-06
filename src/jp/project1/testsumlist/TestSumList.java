package jp.project1.testsumlist;

/***************************************************************************************************
 * 　試験結果集計プログラム：メインクラス
 *
 * 　プログラム名：	TestSumList
 * 　概要：			テキストファイル内の試験結果データを集計し表示
 * 　作成日付：		2020/09/25
 * 　版数：			1.1版
 * 　作成者(班:PL)：杉谷一祝(1:京岡大純)
 * 　修正履歴：		1.1版   順位の算出及びインデント整列を修正
 * 　備考：			なし
 * 　課題No：		3
 **************************************************************************************************/

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TestSumListクラス
 */
public class TestSumList {
    /** 異常終了コード */
    public static final int     ABNORMAL        = -1;
    /** データにある科目数 */
    public static final int     SCORES_QUANTITY = 3;
    /** データの氏名に該当するインデックス */
    public static final int     NAME_INDEX      = 0;
    /** データの氏名の次に該当するインデックス */
    public static final int     NAME_INDEX_NEXT = NAME_INDEX + 1;
    /** ランクを求める際のひとつ前のインデックス算出用 */
    public static final int     PREV            = 1;
    /** ランク算出用 */
    public static final int     ONE             = 1;
    /** prev_rankの初期値 */
    public static final int     PREV_RANK_INIT  = 0;
    /** for文カウンター変数の初期値 */
    public static final int     ZERO            = 0;
    /** データファイルのパス */
    public static final String  FILE_PATH       = "C:/Users/5191007/Desktop/wsjava/TestSumList/bin/testsum.txt";
    /** データファイルの文字コード */
    public static final String  CHARSET         = "MS932";
    /** 入出力エラー時メッセージ */
    public static final String  E001            = "入出力エラーが発生しました。";
    /** 異常終了時メッセージ */
    public static final String  I001            = "強制終了します。";
    /** データ区切り文字 */
    public static final String  SPLITER         = ",";
    /** 正常データの正規表現 */
    public static final String  REGEX           = "\\A(?:[^\\w" + SPLITER + "]+)(?:" + SPLITER + "(?:-1|[0-9]?[0-9]|100)){" + SCORES_QUANTITY + "}\\Z";
    /** 出力フォーマット用 */
    public static final String  FORMAT_1        = "%";
    /** 出力フォーマット用 */
    public static final String  FORMAT_2        = "d%s%-";
    /** 出力フォーマット用 */
    public static final String  FORMAT_3        = "s";
    /** 出力フォーマット用 */
    public static final String  FORMAT_4        = "　%s%";
    /** 出力フォーマット用 */
    public static final String  FORMAT_5        = "d";
    /** 試験成績順位の見出し */
    public static final String  RANKING_TITLE   = "【試験成績順位】";
    /** 再試験者の見出し */
    public static final String  RETESTERS_TITLE = "【再試験者】";
    /** 再試験該当者なしの場合のメッセージ */
    public static final String  NON_RETESTER_MS = "該当者なし";
    /** 得点の最大値に付けるマーク */
    public static final String  MAX_MARK        = "*";
    /** 得点の最大値以外に付けるマーク */
    public static final String  NON_MAX_MARK    = " ";

    /** 各科目の最高得点を算出する際Studentのscoresのインデックスに使用 */
    public static int           counter;
    /** ランクを求める際に使用するひとつ前のランク格納用 */
    public static int           prev_rank       = PREV_RANK_INIT;


/***************************************************************************************************
 * 　メインメソッド：main(String[] args)
 *
 * 　メソッド名：   main
 * 　概要：			プログラムエントリ 
 * 　引数：			String[] args : コマンドライン引数
 * 　返却値:        なし
 *   備考:          なし
 **************************************************************************************************/
    /**
     * メインメソッド
     * @param args
     */
    public static void main(String[] args) {
        /* データ格納用変数の初期化 */
        List<Student> students = List.of();
        
        /* ファイルから正常データを抽出 */
        try {
            students = Files.lines(Paths.get(FILE_PATH), Charset.forName(CHARSET))
                .filter(s -> Pattern.matches(REGEX, s))
                .map(s -> s.split(SPLITER))
                .map(s -> new Student(s[NAME_INDEX], Arrays.stream(s).skip(NAME_INDEX_NEXT).mapToInt(Integer::parseInt).toArray()))
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(E001 + I001);
            System.exit(ABNORMAL);
        }

        /* 再試験者の氏名を抽出 */
        List<String> retesters = students.stream().filter(Student::getIsRetester).map(Student::getName).collect(Collectors.toList());

        /* 試験成績順位を出力 */
        if (students.stream().anyMatch(s -> !s.getIsRetester())) {
            /* 見出し */
            System.out.println(RANKING_TITLE);

            /* 合計得点の降順、氏名の昇順で並び替え */
            students = students.stream().sorted(Comparator.comparing(Student::getSum, Comparator.reverseOrder()).thenComparing(Student::getName)).collect(Collectors.toList());

            /* 順位を算出　1.1版 */
            for (int i = ZERO; i < students.size(); i++) {
                int rank = prev_rank == PREV_RANK_INIT || students.get(i).getSum() != students.get(i - PREV).getSum() ? i + ONE : prev_rank;
                students.get(i).rank = rank;
                prev_rank = rank;
            }
    
            /* 最高点を算出 */
            int sum_max = students.stream().max(Comparator.comparingInt(Student::getSum)).get().getSum();
            int [] scores_max = new int[SCORES_QUANTITY];
            for (counter = ZERO; counter < SCORES_QUANTITY; counter++) {
                scores_max[counter] = students.stream().map(Student::getScores).mapToInt(s -> s[counter]).max().getAsInt();
            }

            /* インデント数の設定 */
            int rank_len = String.valueOf(students.stream().max(Comparator.comparingInt(s -> s.rank)).get().rank).length();     // 1.1版
            int [] scores_len = Arrays.stream(scores_max).map(s -> String.valueOf(s).length()).toArray();
            
            /* 氏名の最大長 */
            int name_max_len = students.stream().max((s1, s2) -> s1.getName().length() - s2.getName().length()).get().getName().length();
            
            /* 一覧 */
            students.forEach(s -> {
                /* 合計得点の最大値にマークを付ける */
                String sum_max_mark = s.getSum() == sum_max ? MAX_MARK : NON_MAX_MARK;
    
                /* 氏名のインデント数の設定 */
                int name_len = name_max_len + name_max_len - s.getName().length();
    
                /* フォーマットして出力 */
                String result = String.format(FORMAT_1 + rank_len + FORMAT_2 + name_len + FORMAT_3, s.rank, sum_max_mark, s.getName());
                int [] scores = s.getScores();
                for (int j = ZERO; j < SCORES_QUANTITY; j++) {
                    /* 各科目の最高得点にマークを付ける */
                    String score_max_mark = scores[j] == scores_max[j] ? MAX_MARK : NON_MAX_MARK;
                    
                    result += String.format(FORMAT_4 + scores_len[j] + FORMAT_5, score_max_mark, scores[j]);
                }
                System.out.println(result);
            });
        }

        /* 再試験者を出力 */
        System.out.println(RETESTERS_TITLE);
        if (!retesters.isEmpty()) {
            /* 該当者ありの場合 */
            retesters.forEach(System.out::println);
        } else {
            /* 該当者なしの場合 */
            System.out.println(NON_RETESTER_MS);
        }
    }
}

// git test