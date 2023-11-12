package christmas

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

private val decimalFormat = DecimalFormat("#,###")

class OrderingService {

    private val inputView = InputView()
    private val outputView = OutputView()
    fun run() {
        outputView.printGetVisitDateGuideMention()
        val visitDate = inputView.getVisitDate()

        outputView.printGetOrderGuideMention()
        val orders = inputView.getOrders()

        printResults(visitDate, orders)
    }

    fun printResults(visitDate: Int, orders: List<Order>){
        outputView.printPreviewMention(visitDate)
        outputView.printOrderList(orders)
        outputView.printPriceBeforeDiscount(getTotalPrice(orders))
        outputView.printComplimentaryMenu(getTotalPrice(orders))
        outputView.printBenefitList(getBenifitList(visitDate, orders))
        outputView.printTotalBenefitPrice(getTotalBenefitAmount(visitDate, orders))
        outputView.printExpectedPriceAfterDiscount(getTotalPrice(orders), getTotalDiscountAmount(visitDate, orders))
        outputView.printEventBadge(getBadge(visitDate, orders))
    }

    fun getTotalPrice(orders: List<Order>): Int {
        menuPriceMapInit()
        var sum = 0
        orders.forEach { sum += menuPriceMap.getValue(it.name) * it.num }
        return sum
    }

    fun menuPriceMapInit() {
        for (i in menuList.indices)
            menuPriceMap[menuList[i]] = priceList[i]
    }

    fun checkComplimentary(price: Int): String {
        return if (price >= 120000) "샴페인 1개"
        else "없음"
    }

    fun getBenifitList(date: Int, orders: List<Order>): String {
        var discountList = ""
        if (getDdayDiscountAmount(date) != 0) {
            discountList += "크리스마스 디데이 할인: -"
            discountList += "${decimalFormat.format(getDdayDiscountAmount(date))}원\n"
        }
        if (getWeeklyDiscountAmount(date, orders) != 0) {
            if (date % 7 == 1 || date % 7 == 2) discountList += "평일 할인: -${decimalFormat.format(getWeeklyDiscountAmount(date, orders))}원\n"
            else discountList += "주말 할인: -${decimalFormat.format(getWeeklyDiscountAmount(date, orders))}원\n"
        }

        if (getSpecialDiscountAmount(date) != 0) discountList += "특별 할인: -${decimalFormat.format(getSpecialDiscountAmount(date))}원\n"

        if (checkComplimentary(getTotalPrice(orders)) != "없음") discountList += "증정 이벤트: -25,000원\n"

        if (discountList.isEmpty()) return "없음"
        return discountList
    }

    fun getTotalBenefitAmount(date: Int, orders: List<Order>): Int {
        if(getTotalPrice(orders) < 10000) return 0
        var totalBenefitAmount = 0
        totalBenefitAmount += getDdayDiscountAmount(date)
        totalBenefitAmount += getWeeklyDiscountAmount(date, orders)
        totalBenefitAmount += getSpecialDiscountAmount(date)
        totalBenefitAmount += getComplimentaryDiscountAmount(orders)
        return totalBenefitAmount
    }

    fun getTotalDiscountAmount(date: Int, orders: List<Order>): Int {
        if(getTotalPrice(orders) < 10000) return 0
        var totalDiscountAmount = 0
        totalDiscountAmount += getDdayDiscountAmount(date)
        totalDiscountAmount += getWeeklyDiscountAmount(date, orders)
        totalDiscountAmount += getSpecialDiscountAmount(date)
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

    fun getWeeklyDiscountAmount(date: Int, orders: List<Order>): Int {
        if (date % 7 == 1 || date % 7 == 2) return getWeekendDiscountAmount(orders)
        return getWeekdayDiscountAmount(orders)
    }

    fun getWeekendDiscountAmount(orders: List<Order>): Int {
        var discountAmount = 0
        orders.forEach {
            if (mainMenu.contains(it.name))
                discountAmount += it.num * 2023
        }
        return discountAmount
    }

    fun getWeekdayDiscountAmount(orders: List<Order>): Int {
        var discountAmount = 0
        orders.forEach {
            if (dessertMenu.contains(it.name))
                discountAmount += it.num * 2023
        }
        return discountAmount
    }

    fun getComplimentaryDiscountAmount(orders: List<Order>): Int {
        if (checkComplimentary(getTotalPrice(orders)) != "없음") return 25000
        return 0
    }

    fun getBadge(date: Int, orders: List<Order>): String {
        return when (getTotalBenefitAmount(date, orders)) {
            in 0..<5000 -> "없음"
            in 5000..<10000 -> "별"
            in 10000..<20000 -> "트리"
            else -> "산타"
        }
    }
}