enum class Menu(
    val itemName: String,
    val itemPrice: Int
) {
    WATER("물", 1000), NOODLE("라면", 3000), COKE("콜라",2000),
    AMERICANO("아메리카노",500), LATTE("라떼", 700);

    companion object {
        var menu = hashMapOf<String, Int>()
        fun menuList() {
            for (i in Menu.values()) {
                println("${i.itemName} : ${i.itemPrice}원")
                // menu 호출할 때마다 hashmap에 값을 넣는 무의미한 행동을 반복함
                menu.put(i.itemName, i.itemPrice)
            }
            menu.menuCount()
        }
    }
}
