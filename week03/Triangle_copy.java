public class Triangle {
    public static void main(String[] args) {
        System.out.println("helloworld");
        printTriangle();
    }

    private static String str = "";

    private static void printTriangle() {
        int i, j;
        for (i = 1; i <= 5; i++) {
            for (j = 1; j <= i; j++) {
                str += "*"; // 只加星星，不換行
            }
            str += "\n"; // 當一整列的星星印完後，才換行
        }
        System.out.println(str);
    }
}