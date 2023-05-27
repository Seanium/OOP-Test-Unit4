import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Test {
    private static final int PID_MIN = 21000000;
    private static final int PID_MAX = 22000000;
    private static HashSet<String> books = new HashSet<>();
    private static ArrayList<String> commands = new ArrayList<>();
    private static ArrayList<Integer> cmdDays = new ArrayList<>();
    private static HashSet<String> people = new HashSet<>();
    private static HashMap<String, HashSet<String>> borrowMap = new HashMap<>();

    public static int randInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String randBookName() {
        char type = (char) ('A' + Test.randInt(0, 2));
        int id = randInt(0, 9999);
        return String.format("%c-%04d", type, id);
    }

    public static String randShelfBookName() {
        ArrayList<String> list = new ArrayList<>(books);
        return list.get(randInt(0, list.size() - 1));
    }

    public static String randBorrowedBookName(String pid) {
        ArrayList<String> list = new ArrayList<>(borrowMap.get(pid));
        return list.get(randInt(0, list.size() - 1));
    }

    //概率pExist生成已存在pid，概率1-pExist生成任意pid
    public static String randPid(int pExist) {
        int randP = randInt(0, 100);
        //生成任意pid
        if (randP > pExist || people.size() == 0) {
            return String.valueOf(randInt(PID_MIN, PID_MAX));
        } else {    //生成已存在pid
            ArrayList<String> list = new ArrayList<>(people);
            return list.get(randInt(0, list.size() - 1));
        }
    }

    public static void randCmdDays(int cmdCnt) {
        for (int i = 0; i < cmdCnt; i++) {
            cmdDays.add(randInt(1, 365));
        }
        Collections.sort(cmdDays);
    }

    public static void addBookCmds(int bookCmdCnt) {
        commands.add(String.valueOf(bookCmdCnt));
        for (int i = 0; i < bookCmdCnt; i++) {
            String bookName = randBookName();
            while (books.contains(bookName)) {
                bookName = randBookName();
            }
            books.add(bookName);
            int bookCnt = randInt(1, 10);
            String bookCmd = String.format("%s %d", bookName, bookCnt);
            commands.add(bookCmd);
        }
    }

    public static String randBorrowedCmd() {
        String pid = randPid(50);
        people.add(pid);
        String shelfBookName = randShelfBookName();
        String cmd = String.format("%s borrowed %s", pid, shelfBookName);
        borrowMap.putIfAbsent(pid, new HashSet<>());
        borrowMap.get(pid).add(shelfBookName);
        return cmd;
    }

    public static String randSmearedCmd() {
        String pid = randPid(100);
        String borrowedBookName = randBorrowedBookName(pid);
        return String.format("%s smeared %s", pid, borrowedBookName);
    }

    public static String randLostCmd() {
        String pid = randPid(100);
        String borrowedBookName = randBorrowedBookName(pid);
        return String.format("%s lost %s", pid, borrowedBookName);
    }

    public static String randReturnedCmd() {
        String pid = randPid(100);
        String borrowedBookName = randBorrowedBookName(pid);
        return String.format("%s returned %s", pid, borrowedBookName);
    }

    public static void addOtherCmds(int cmdCnt) {
        commands.add(String.valueOf(cmdCnt));
        randCmdDays(cmdCnt);
        ArrayList<String> pool = new ArrayList<>();
        int borrowedWeight = 1;
        int smearedWeight = 1;
        int lostWeight = 1;
        int returnedWeight = 1;
        for (int i = 0; i < borrowedWeight; i++) {
            pool.add("borrowed");
        }
        for (int i = 0; i < smearedWeight; i++) {
            pool.add("smeared");
        }
        for (int i = 0; i < lostWeight; i++) {
            pool.add("lost");
        }
        for (int i = 0; i < returnedWeight; i++) {
            pool.add("returned");
        }
        //第一条一定是borrowed
        commands.add(String.format("%s %s", Date.daysToDate(cmdDays.get(0)), randBorrowedCmd()));
        for (int i = 1; i < cmdCnt; i++) {
            String date = Date.daysToDate(cmdDays.get(i));
            String type = pool.get(randInt(0, pool.size() - 1));
            String cmd = type.equals("borrowed") ? randBorrowedCmd() :
                    type.equals("smeared") ? randSmearedCmd() :
                            type.equals("lost") ? randLostCmd() :
                                    randReturnedCmd();
            String fullCmd = String.format("%s %s", date, cmd);
            commands.add(fullCmd);
        }
    }

    public static void printAllCmds() {
        for (String s : commands) {
            System.out.println(s);
        }
    }
}
