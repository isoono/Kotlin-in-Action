fun <K, V> HashMap<K, V>.menuCount() {
    when (Menu.values().size) {
        0 -> println("등록된 메뉴가 없습니다.")
        else -> println("${Menu.values().size} 개")
    }
}
