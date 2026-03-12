public class Q2_Fish {
    // 1. 定義屬性 (狀態)
    String name;
    double weight;

    // 2. 定義方法 (行為)
    public void displayInfo() {
        System.out.println("這隻魚的名字是：" + name + "，重量是：" + weight + "公斤");
    }

    public static void main(String[] args) {
        // 3. 實例化物件 (產生 myFish)
        Q2_Fish myFish = new Q2_Fish();

        // 4. 設定屬性值
        myFish.name = "黑鮪魚";
        myFish.weight = 250.5;

        // 5. 呼叫方法
        myFish.displayInfo();
    }
}