package christmas

import camp.nextstep.edu.missionutils.*
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.text.DecimalFormat

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
val appetizerMenu = listOf(
    "양송이수프",
    "타파스",
    "시저샐러드"
)
val mainMenu = listOf(
    "티본스테이크",
    "바비큐립",
    "해산물파스타",
    "크리스마스파스타"
)
val dessertMenu = listOf(
    "초코케이크",
    "아이스크림"
)
val drinkMenu = listOf(
    "제로콜라",
    "레드와인",
    "샴페인"
)

val priceList = listOf(6000, 5500, 8000, 55000, 54000, 35000, 25000, 15000, 5000, 3000, 60000, 25000)

val menuPriceMap = HashMap<String, Int>()

data class Order(val name: String, val num: Int)
private val decimalFormat = DecimalFormat("#,###")

fun main() {
    println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.")
    println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
    val visitDate = getVisitDate()

    println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)")
    val orders = getOrders()
    println("12월 ${visitDate}일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n")
    println("<주문 메뉴>")
    for (i in orders.indices) {
        println("${orders[i].name} ${orders[i].num}개")
    }

    //Todo
    //Decimal Format

    println("\n<할인 전 총주문 금액>")
    println("${decimalFormat.format(getTotalPrice(orders))}원")

    println("\n<증정 메뉴>")
    println("${checkComplimentary(getTotalPrice(orders))}")

    println("\n<혜택 내역>")
    printDiscountList(visitDate, orders)

    println("\n<총혜택 금액>")
    if(getTotalDiscountAmount(visitDate, orders) == 0) println("없음")
    else println("-${decimalFormat.format(getTotalDiscountAmount(visitDate, orders))}원")

    println("\n<할인 후 예상 결제 금액>")
    println("${decimalFormat.format(getTotalPrice(orders)- getTotalDiscountAmount(visitDate, orders) + getComplimentaryDiscountAmount(orders))}원")

    println("\n<12월 이벤트 배지>")
    println("${getBadge(visitDate, orders)}")
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
        //음료만 주문한 경우도
        //TODO()
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
    if (list.distinct().size == orderList.size) return true
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
        if (orderList[i].split('-')[1].toInt() !in 1..20) return false
    return true
}


fun menuPriceMapInit() {
    for (i in menuList.indices)
        menuPriceMap[menuList[i]] = priceList[i]
}

fun getTotalPrice(orders: List<Order>): Int {
    menuPriceMapInit()
    var sum = 0
    orders.forEach { sum += menuPriceMap.getValue(it.name) * it.num }
    return sum
}

fun checkComplimentary(price: Int): String {
    return if (price >= 120000) "샴페인 1개"
    else "없음"
}

fun printDiscountList(date: Int, orders: List<Order>) {
    var discountList = ""
    if(getDdayDiscountAmount(date) != 0){
        //print("크리스마스 디데이 할인: -")
        //println("${getDdayDiscountAmount(date)}원")
        discountList += "크리스마스 디데이 할인: -"
        discountList += "${getDdayDiscountAmount(date)}원\n"
    }
    if(getWeeklyDiscountAmount(date, orders) != 0) {
        if(date % 7 == 1 || date %7 == 2)
            //println("평일 할인: -${getWeeklyDiscountAmount(date,orders)}원")
            discountList += "평일 할인: -${getWeeklyDiscountAmount(date,orders)}원\n"
        //else println("주말 할인: -${getWeeklyDiscountAmount(date, orders)}원")
        else discountList += "주말 할인: -${getWeeklyDiscountAmount(date, orders)}원\n"
    }

    if(getSpecialDiscountAmount(date) != 0){
        //print("특별 할인: -")
        //println("${getSpecialDiscountAmount(date)}원")
        discountList += "특별 할인: -${getSpecialDiscountAmount(date)}원\n"
    }
    if(checkComplimentary(getTotalPrice(orders)) != "없음")
        //println("증정 이벤트: -25,000원")
        discountList += "증정 이벤트: -25,000원\n"

    if(discountList.isEmpty()) println("없음")
    print(discountList)
}

fun getTotalDiscountAmount(date: Int, orders: List<Order>): Int {
    var totalDiscountAmount = 0
    totalDiscountAmount += getDdayDiscountAmount(date)
    totalDiscountAmount += getWeeklyDiscountAmount(date, orders)
    totalDiscountAmount += getSpecialDiscountAmount(date)
    totalDiscountAmount += getComplimentaryDiscountAmount(orders)
    return totalDiscountAmount
}

fun getDdayDiscountAmount(date: Int): Int {
    if (date > 25) return 0
    return 1000 + (date - 1) * 100
}

fun getSpecialDiscountAmount(date: Int): Int {
    if (date % 7 == 3 || date == 25) return 1000
    return 0
}

fun getWeeklyDiscountAmount(date: Int, orders : List<Order>) : Int {
    if(date % 7 == 1 || date % 7 == 2) return getWeekendDiscountAmount(date, orders)
    return getWeekdayDiscountAmount(date, orders)
}

fun getWeekendDiscountAmount(date: Int, orders: List<Order>): Int {
    var discountAmount = 0
    orders.forEach {
        if(mainMenu.contains(it.name))
            discountAmount += it.num * 2023
    }
    return discountAmount
}

fun getWeekdayDiscountAmount(date: Int, orders: List<Order>): Int {
    var discountAmount = 0
    orders.forEach {
        if(dessertMenu.contains(it.name))
            discountAmount += it.num * 2023
    }
    return discountAmount
}

fun getComplimentaryDiscountAmount(orders: List<Order>) : Int {
    if(checkComplimentary(getTotalPrice(orders)) != "없음") return 25000
    return 0
}

fun getBadge(date: Int, orders: List<Order>) : String {
    return when(getTotalDiscountAmount(date, orders)) {
        in 0 ..< 5000 -> "없음"
        in 5000 ..< 10000 -> "별"
        in 10000 ..< 20000 -> "트리"
        else -> "산타"
    }
}
