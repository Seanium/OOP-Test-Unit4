public class Main {
    public static void main(String[] args) {
        Test.addBookCmds(Integer.parseInt(args[0]));
        Test.addOtherCmds(Integer.parseInt(args[1]));
        Test.printAllCmds();
    }
}
