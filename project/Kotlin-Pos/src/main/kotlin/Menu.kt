enum class Menu(
    val itemPrice: Int
) {
    WATER(1000), NOODLE(3000), COKE(2000),
    AMERICANO(500), LATTE(700);

    companion object {
        fun menuList() {
            for (i in Menu.values()) {
                println("${i.name} : ${i.itemPrice}원")
            }
        }
    }
}
