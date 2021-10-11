class Pos {
    fun processing() {
        while(true) {
            println("=====================================")
            println("선택")
            println("1.시작 2.메뉴 리스트 3.계산 4.종료")
            print(">> ")

            when(readLine()) {
                "시작", "1" -> println("Pos기 시작합니다.")
                "메뉴", "2" -> {
                    println("메뉴 리스트입니다.")
                    Menu.menuList()
                }
                "계산", "3" -> println("계산 방법을 선택하세요.")
                "종료", "4" -> {
                    println("Pos기 종료합니다.")
                    break
                }
            }
            println("=====================================")
        }
    }
}


