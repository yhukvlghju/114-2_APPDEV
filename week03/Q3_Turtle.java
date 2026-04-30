public class Q3_Turtle {
    String species;
    int age;

    // 1. 撰寫建構子 (Constructor)
    public Q3_Turtle(String species, int age) {
        this.species = species; // 把傳入的品種存到屬性裡
        this.age = age;         // 把傳入的年紀存到屬性裡
    }

    // 已經幫你準備好的顯示方法
    public void showDetails() {
        System.out.println("品種：" + species + "，年紀：" + age + "歲");
    }

    public static void main(String[] args) {
        // 2. 利用建構子「直接」建立物件並給值
        // 不再需要 myTurtle.species = "..."，這就是建構子的好處！
        Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);

        // 3. 呼叫方法
        myTurtle.showDetails();
    }
}