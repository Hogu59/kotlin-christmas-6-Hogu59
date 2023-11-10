package christmas

import camp.nextstep.edu.missionutils.*
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

val menuList = listOf<String>(
    "양송이수프",
    "타파스",
    "시저샐러드",
    "티본스테이크",
    "바비큐립",
    "해산물파스타",
    "크리스마스파스타",
    "초코케이크",
    "아이스크림",
    "제로콜라",
    "레드와인",
    "샴페인"
)

fun main() {
    println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.")
    println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
    val visitDate = getVisitDate()

    println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)")
    val orders = getOrders()

}

fun getVisitDate(): Int {
    val visitDate = Console.readLine()
    return try {
        validateVisitDate(visitDate)
    } catch (exception: IllegalArgumentException) {
        println(exception.message)
        getVisitDate()
    }
}

fun validateVisitDate(visitDate: String): Int {
    try {
        require(visitDate.isNotEmpty()) { "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요." }
        val validVisitDate = visitDate.toInt()
        require(validVisitDate in 1..<32) { "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요." }
    } catch (exception: NumberFormatException) {
        throw IllegalArgumentException("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
    }
    return visitDate.toInt()
}

fun getOrders(): List<Order> {
    val orders = Console.readLine()
    return try {
        validateOrders(orders)
    } catch (exception: IllegalArgumentException) {
        println(exception.message)
        getOrders()
    }
}

fun validateOrders(orders: String): List<Order> {
    val validOrderList = mutableListOf<Order>()
    try {
        //null case
        require(orders.isNotEmpty()) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        //contains space
        require(!orders.contains(" ")) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        val orderList = orders.split(',')
        require(checkNameOfMenu(orderList)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        require(checkDuplicateMenu(orderList)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        require(checkNumberOfMenu(orderList)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        require(checkTotalNumberOfMenu(orderList)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
        for (i in orderList.indices) {
            val (menu, num) = orderList[i].split('-')
            validOrderList.add(Order(menu, num.toInt()))
        }
    } catch (exception: NumberFormatException) {
        throw IllegalArgumentException("뭔가 에러임.")
    }
    return validOrderList
}

fun checkNameOfMenu(orderList: List<String>): Boolean {
    for (i in orderList.indices)
        if (!menuList.contains(orderList[i].split('-')[0])) return false
    return true
}

fun checkDuplicateMenu(orderList: List<String>): Boolean {
    val list = mutableListOf<String>()
    for (i in orderList.indices)
        list.add(orderList[i].split('-')[0])
    if(list.distinct().size == orderList.size) return true
    return false
}

fun checkTotalNumberOfMenu(orderList: List<String>): Boolean {
    var numberOfMenu = 0
    for (i in orderList.indices)
        numberOfMenu = orderList[i].split('-')[1].toInt()
    if (numberOfMenu > 20) return false
    return true
}

fun checkNumberOfMenu(orderList: List<String>): Boolean {
    for (i in orderList.indices)
        if(orderList[i].split('-')[1].toInt() !in 1 .. 20) return false
    return true
}

data class Order(val menu: String, val num: Int)