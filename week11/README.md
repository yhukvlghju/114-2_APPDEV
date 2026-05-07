# W11 進階介面元件：Spinner、ListView、ArrayAdapter、Options Menu

> **APP 開發課程** ｜ 第 11 週 ｜ 5/7
> **教科書**：Ch07 進階介面元件
> **單元**：7-1 下拉式選單實習　/　7-2 列舉清單方塊實習　/　7-3 接合器變更顯示項目實習　/　7-4 選項選單與動作列實習
> **課後作業繳交期限**：W12 上課前（5/13）
> **進階補充**：📖 [課後進階練習（選項）— 從 ListView 進化到 RecyclerView](advanced.md)

---

## 學習目標

1. 用 **Spinner** 建立下拉式選單，並處理選取事件（`OnItemSelectedListener`）
2. 用 **ListView** 建立可捲動清單，並處理點擊事件（`OnItemClickListener`）
3. 用 **ArrayAdapter** 把資料來源（Java 陣列 / 字串資源）橋接到清單元件
4. 在 Action Bar 建立 **Options Menu**，並處理選項點擊（`onOptionsItemSelected`）

---

## 一、7-1 下拉式選單（Spinner）

> **PPT：Android_Ch07.ppt（投影片 P3–P12）**

### 7-1-1 建立 Spinner 元件

#### Spinner 是什麼？

類似 Windows 作業系統的下拉式清單方塊——**單選**的清單元件，使用者點擊後展開項目選一個。

#### Android Studio 介面設計工具新增方式

從元件面板「**Containers**」區段拖入 **Spinner**。

#### 用 entries 屬性綁字串陣列資源

Spinner 顯示什麼項目？最常用做法是綁**字串陣列資源**（放在 `res/values/strings.xml`）：

**Step 1：在 `strings.xml` 定義字串陣列**

```xml
<resources>
    <string-array name="steaks">
        <item>三分熟</item>
        <item>五分熟</item>
        <item>七分熟</item>
        <item>全熟</item>
    </string-array>
</resources>
```

**Step 2：在 layout XML 用 entries 綁定**

```xml
<Spinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:entries="@array/steaks" />
```

> 📌 `@array/steaks` 中：`@array` 表示字串陣列資源，`steaks` 是 `<string-array>` 的 name。

#### getSelectedItemPosition() 方法 — 取得選取項目索引

```java
Spinner sp = (Spinner) findViewById(R.id.spinner);
int index = sp.getSelectedItemPosition();
```

回傳整數**索引值**（從 0 開始）。

#### 取出陣列值的另一種寫法

```java
String[] steaks = getResources().getStringArray(R.array.steaks);
String selected = steaks[index];
```

> 📌 `getResources().getStringArray(R.array.xxx)` 是 Java 程式碼**從資源檔讀字串陣列**的標準方式。

#### 🛠️ 實作專案 Ch7_1_1：牛排點餐單

**功能**：用 Spinner 選擇牛排幾分熟，按下按鈕後在下方 TextView 顯示選擇結果。

**元件配置**：

| 元件 | id | 內容 |
|---|---|---|
| Spinner | `spinner` | 綁 `@array/steaks`（三分熟 / 五分熟 / 七分熟 / 全熟）|
| Button | `btnShow` | 「顯示選擇」 |
| TextView | `lblOutput` | 顯示結果 |

---

### 7-1-2 Spinner 的選取項目事件

#### 動機

按按鈕才顯示太被動——**選的當下就立刻反應**，靠 `OnItemSelectedListener`。

#### 實作 OnItemSelectedListener 介面

```java
public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(this);
    }
    ...
}
```

#### 兩個必實作方法

| 方法 | 觸發時機 | 是否常用 |
|---|---|---|
| **`onItemSelected()`** | 使用者選了某項 | **常用** |
| `onNothingSelected()` | 沒選任何項（罕見）| 通常空實作 |

```java
@Override
public void onItemSelected(AdapterView<?> adapterView,
                            View view, int position, long id) {
    int r = 0;
    switch (position) {
        case 0:  r = v1 + v2; break;  // 加法
        case 1:  r = v1 - v2; break;  // 減法
        case 2:  r = v1 * v2; break;  // 乘法
        case 3:  r = v1 / v2; break;  // 除法
    }
    output.setText("運算結果 = " + r);
}

@Override
public void onNothingSelected(AdapterView<?> adapterView) {
    // 通常留空
}
```

> 📌 `position` 參數是「**目前選中的項目索引**」，可直接用 `switch` 對應動作。

#### 🛠️ 實作專案 Ch7_1_2：四則計算機

**功能**：用 Spinner 選擇 + / - / × / ÷ 四種運算，選擇後立即顯示兩個輸入數字的運算結果。

---

## 二、7-2 列舉清單方塊（ListView）

> **PPT：Android_Ch07.ppt（投影片 P13–P17）**

### ListView 元件

#### 與 Spinner 的差異

| 元件 | 顯示方式 | 互動 |
|---|---|---|
| Spinner | **下拉**展開後選一個 | 點 → 展開 → 選 |
| **ListView** | **直接顯示**多項，超出可捲動 | 直接點某項 |

#### Android Studio 新增方式

從「**Legacy**」區段拖入 **ListView**。

> ⚠️ **易混淆點**：ListView 在「**Legacy**」（舊版）區段，不在「Common」。新版 Android 推薦用 RecyclerView 取代——但本週**先用 ListView 學 Adapter 概念**，下週的 RecyclerView 才好理解。

#### OnItemClickListener — 處理項目點擊

```java
public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {

    private String[] cities = {"高雄", "台北", "台中", "台南", "新竹"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,
                            View view, int position, long id) {
        TextView output = (TextView) findViewById(R.id.lblOutput);
        output.setText("你是住在: " + cities[position]);
    }
}
```

> 📌 `position` 用法和 7-1-2 的 `onItemSelected` 一樣——「**目前點到的項目索引**」。

#### 🛠️ 實作專案 Ch7_2：城市選擇

**功能**：ListView 顯示 5 個城市名稱，點擊任一個立即在 TextView 顯示「你是住在：高雄」。

---

## 三、7-3 ArrayAdapter 接合器

> **PPT：Android_Ch07.ppt（投影片 P18–P23）**

### 為什麼需要 ArrayAdapter？

| 情境 | 解法 |
|---|---|
| 清單項目**寫死在 XML** | `entries="@array/xxx"` 即可 ✅ |
| 清單項目**程式執行時才決定**（從資料庫、網路、運算結果）| 需要 **ArrayAdapter** ✅ |

#### 接合器（Adapter）概念

> 接合器 = **資料來源** 與 **清單元件** 之間的**橋樑**

```
資料來源（陣列/資料庫/網路）
       ↓
   ArrayAdapter（接合器）
       ↓
清單元件（Spinner / ListView）
```

### 建立 ArrayAdapter 物件

#### 方法 1：用 Java 字串陣列建立

```java
String[] courses = {"美式漢堡", "特選牛排", "牛肉飯", "義大利麵"};

ArrayAdapter<String> a1 = new ArrayAdapter<>(this,
    android.R.layout.simple_spinner_item, courses);
```

> 📌 `<String>` 是泛型參數——告訴 ArrayAdapter「我裝的是字串」。

#### 方法 2：用字串陣列資源建立

```java
String[] desserts = getResources().getStringArray(R.array.dessert);

ArrayAdapter<String> a2 = new ArrayAdapter<>(this,
    android.R.layout.simple_spinner_item, desserts);
```

### setAdapter() — 指定使用的接合器

```java
Spinner sp1 = (Spinner) findViewById(R.id.spinner);
sp1.setAdapter(a1);
```

> 📌 ListView 也是同樣寫法：`listView.setAdapter(a1);`

### 🛠️ 實作專案 Ch7_3：團購點餐單

**功能**：兩個 Spinner 並列——
1. 第 1 個 Spinner（主餐）：用 **Java 陣列**建立 ArrayAdapter
2. 第 2 個 Spinner（點心）：用**字串陣列資源** `R.array.dessert` 建立 ArrayAdapter

按按鈕後在 TextView 顯示「主餐：XXX，點心：YYY」。

---

## 四、7-4 選項選單與動作列（Options Menu / Action Bar）

> **PPT：Android_Ch07.ppt（投影片 P25–P32）**

### 7-4-1 認識動作列（Action Bar）

#### 演進歷史

| 時期 | UI |
|---|---|
| 舊版 Android | 標題列 + **MENU 實體鍵**叫出選單 |
| Android 3.0+ | 整合為 **Action Bar**（活動最上方固定區）|
| 現在 | 沒有實體 MENU 鍵，選項整合進 Action Bar 的「**溢出選單**」（Overflow，垂直 3 點圖示）|

#### 動作列功能

- 顯示活動標題、圖示
- 提供切換 / 巡覽功能
- 顯示選單（**溢出選單** = Options Menu）

### 7-4-2 建立 Options Menu

#### Step 1：新增選單資源 XML

在 `res/menu/menu_main.xml`（用 Android Studio 介面設計工具建立）：

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/toF"
        android:title="轉華氏" />
    <item
        android:id="@+id/toC"
        android:title="轉攝氏" />
</menu>
```

#### Step 2：覆寫 onCreateOptionsMenu()

執行 Android Studio 選單 **Code > Override Methods** → 選 `onCreateOptionsMenu`：

```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
}
```

> 📌 `MenuInflater` 把 XML 選單**展開**成真正的選單。

#### Step 3：覆寫 onOptionsItemSelected() 處理點擊

```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.toF:
            // 轉華氏：F = C × 9/5 + 32
            ...
            break;
        case R.id.toC:
            // 轉攝氏：C = (F - 32) × 5/9
            ...
            break;
    }
    return super.onOptionsItemSelected(item);
}
```

> 📌 `MenuItem.getItemId()` 取得使用者點到的選項 id。

### 🛠️ 實作專案 Ch7_4_2：溫度轉換選單

**功能**：EditText 輸入溫度數值，從動作列右上角「垂直 3 點」開啟選單，選「轉華氏」或「轉攝氏」立即轉換顯示。

---

## 五、本週重點觀念複習卡

| 觀念 | 一句話記憶 |
|---|---|
| Spinner vs ListView | Spinner **下拉展開**選一個；ListView **直接顯示**多項可捲動 |
| `entries` 屬性 | 用 XML 寫死清單時的最快寫法 |
| `getSelectedItemPosition()` | Spinner 取目前選中的索引 |
| `OnItemSelectedListener` | Spinner 的「選的當下」事件 |
| `OnItemClickListener` | ListView 的「點的當下」事件 |
| `position` 參數 | 兩種事件方法**第三個參數**就是「點/選到的索引」|
| **ArrayAdapter** | **資料 → 清單**的橋樑（執行時才決定資料時必用）|
| `setAdapter()` | 把 ArrayAdapter 接到 Spinner / ListView |
| `R.array.xxx` | 從 strings.xml 字串陣列資源讀資料的索引 |
| Action Bar 溢出選單 | 「右上角垂直 3 點」= Options Menu |
| `onCreateOptionsMenu()` | XML 選單 → 真正選單 |
| `onOptionsItemSelected()` | 處理選單點擊（用 `getItemId()` 比對）|

---

## 六、本週作業

> 繳交方式：在你 fork 的 `114-2_APPDEV/week11/` 建立**一個 Android Studio 專案**完成下列功能，push 到你的 fork（沿用學期既有 PR）

### 作業：海洋生物分類查詢 App（整合 Ch07 四節觀念）

設計一個「**海洋生物分類查詢**」App，整合本週 Spinner、ListView、ArrayAdapter、Options Menu 四元件。

#### 功能規格

| 區塊 | 元件 | 內容 |
|---|---|---|
| 1. 分類選擇 | `Spinner` | 4 個分類：魚類 / 哺乳類 / 無脊椎 / 海底植物（綁字串陣列資源） |
| 2. 生物清單 | `ListView` | 顯示**該分類**下的海洋生物（每類至少 5 種，**用 ArrayAdapter 動態切換**）|
| 3. 詳細顯示 | `TextView` | 點擊清單某項後顯示「你選的是：海豚」 |
| 4. 動作選單 | `Options Menu` | 至少 2 項：「重置選擇」「顯示總生物數」 |

#### 技術要求

| # | 要求 |
|---|---|
| 1 | Spinner 用 `entries` 屬性綁分類字串陣列資源 |
| 2 | Spinner 用 `OnItemSelectedListener` 偵測分類切換 |
| 3 | 切換分類時用 **ArrayAdapter 重新綁定 ListView** 顯示對應生物清單 |
| 4 | ListView 用 `OnItemClickListener` 顯示點擊項目 |
| 5 | Options Menu 在 `res/menu/menu_main.xml` 定義，用 `onCreateOptionsMenu` + `onOptionsItemSelected` 處理 |

#### 繳交清單

| # | 內容 |
|---|---|
| 1 | Android Studio 專案資料夾（含 `app/`、`build.gradle` 等）|
| 2 | 模擬器執行截圖 **2 張**：① 切換 Spinner 後 ListView 內容變化 ② Options Menu 展開畫面 |
| 3 | `README.md`：列出你 4 個分類各放了哪 5 種以上生物，並簡述為什麼這樣分類 |

---

## 七、給卡住的同學

| 卡點 | 解法 |
|---|---|
| Spinner **不顯示任何項目** | `entries` 沒指定，或字串陣列資源名稱拼錯 |
| `OnItemSelectedListener` **找不到** | import 寫錯：`import android.widget.AdapterView.OnItemSelectedListener;` |
| 切換 Spinner 後 ListView **沒變化** | 沒呼叫 `listView.setAdapter(newAdapter)` 重新綁定 |
| ListView 拖入後**找不到 OnItemClickListener** | 用的不是 `View.OnClickListener`！ListView 用 `AdapterView.OnItemClickListener` |
| Options Menu **沒出現** | `onCreateOptionsMenu` 沒寫 `return super...` 或 `return true` |
| 點選單後**沒反應** | `onOptionsItemSelected` 的 `switch(item.getItemId())` id 對錯 |
| `R.menu.menu_main` 紅字 | `res/menu/menu_main.xml` 不存在或檔名錯，重新建立 |

---

## 八、下週預告（W12）

W12 進入 **Fragment 與導覽列**（Bottom Navigation），把多畫面 App 拆成可重用的 Fragment 模組。

> 💡 **想提早接觸現代 Android UI 寫法？** 課本下一章還在 Fragment，但業界**早已從 ListView 改用 RecyclerView**。
> 對清單元件想看更現代的做法，請看 [📖 課後進階練習（選項）— RecyclerView](advanced.md)，**自由探索、不繳交、不計分**。
