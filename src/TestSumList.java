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
    public static final String  FILE_PATH       = "D:\\Users\\5191007\\Documents\\ws\\TestSumList\\testdata.txt";
    /** データファイルの文字コード */
    public static final String  CHARSET         = "UTF-8";
    /** 入出力エラーメッセージ */
    public static final String  E001            = "I/O エラーが発生しました。";
    /** データ区切り文字 */
    public static final String  SPLITER         = ",";
    /** 正常データの正規表現 */
    public static final String  REGEX           = "^(?:\\W+)(?:" + SPLITER + "(?:-1|[0-9]?[0-9]|100)){" + SCORES_QUANTITY + "}$";
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
    public static final String  TITLE_1         = "【試験成績順位】";
    /** 再試験者の見出し */
    public static final String  TITLE_2         = "【再試験者】";
    /** 再試験該当者なしの場合のメッセージ */
    public static final String  TITLE_3         = "該当者なし";
    /** 得点の最大値に付けるマーク */
    public static final String  MAX_MARK        = "*";
    /** 得点の最大値以外に付けるマーク */
    public static final String  NON_MAX_MARK    = " ";

    /** 各科目の最高得点を算出する際Studentのscoresのインデックスに使用 */
    public static int           counter;
    /** ランクを求める際に使用するひとつ前のランク格納用 */
    public static int           prev_rank       = PREV_RANK_INIT;

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
            System.out.println(E001);
            System.exit(ABNORMAL);
        }

        /* 再試験者の氏名を抽出 */
        List<String> retesters = students.stream().filter(s -> s.isRetester).map(s -> s.name).collect(Collectors.toList());

        /* 合計得点の降順、氏名の昇順で並び替え */
        students = students.stream().sorted(Comparator.comparing(Student::getSum, Comparator.reverseOrder()).thenComparing(s -> s.name)).collect(Collectors.toList());

        /* 最高点を算 出 */
        int sum_max = students.stream().max(Comparator.comparingInt(Student::getSum)).get().getSum();
        int [] scores_max = new int[SCORES_QUANTITY];
        for (counter = ZERO; counter < SCORES_QUANTITY; counter++) {
            scores_max[counter] = students.stream().mapToInt(s -> s.scores[counter]).max().getAsInt();
        }

        /* 試験成績順位を出力 */
        if (students.stream().anyMatch(s -> !s.isRetester)) {
            /* 見出し */
            System.out.println(TITLE_1);

            /* インデント数の設定 */
            int rank_len = String.valueOf(students.size()).length();
            int [] scores_len = Arrays.stream(scores_max).map(s -> String.valueOf(s).length()).toArray();
            
            /* 氏名の最大長 */
            int name_max_len = students.stream().max((s1, s2) -> s1.name.length() - s2.name.length()).get().name.length();
            
            /* 一覧 */
            for (int i = ZERO; i < students.size(); i++) {
                /* 合計得点の最大値にマークを付ける */
                String sum_max_mark = students.get(i).getSum() == sum_max ? MAX_MARK : NON_MAX_MARK;

                /* ランクを求める */
                int rank = prev_rank == PREV_RANK_INIT || students.get(i).getSum() != students.get(i - PREV).getSum() ? i + ONE : prev_rank;
                prev_rank = rank;

                /* 氏名のインデント数の設定 */
                int name_len = name_max_len + name_max_len - students.get(i).name.length();

                /* フォーマットして出力 */
                String result = String.format(FORMAT_1 + rank_len + FORMAT_2 + name_len + FORMAT_3, rank, sum_max_mark, students.get(i).name);
                for (int j = ZERO; j < SCORES_QUANTITY; j++) {
                    /* 各科目の最高得点にマークを付ける */
                    String score_max_mark = students.get(i).scores[j] == scores_max[j] ? MAX_MARK : NON_MAX_MARK;
                    
                    result += String.format(FORMAT_4 + scores_len[j] + FORMAT_5, score_max_mark, students.get(i).scores[j]);
                }
                System.out.println(result);
            }
        }

        /* 再試験者を出力 */
        System.out.println(TITLE_2);
        if (!retesters.isEmpty()) {
            /* 該当者ありの場合 */
            retesters.forEach(System.out::println);
        } else {
            /* 該当者なしの場合 */
            System.out.println(TITLE_3);
        }
    }
}