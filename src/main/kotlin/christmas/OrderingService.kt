package christmas

import christmas.MenuList.Companion.dessertMenu
import christmas.MenuList.Companion.mainMenu
import christmas.MenuList.Companion.menuList
import christmas.MenuList.Companion.priceList
import java.text.DecimalFormat

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

    fun printResults(visitDate: Int, orders: List<Order>) {
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
        if (getTotalPrice(orders) < 10000) return "없음\n"
        var benefitList = ""
        benefitList += getDDayBenefitString(date)
        benefitList += getWeeklyBenefitString(date, orders)
        benefitList += getSpecialBenefitString(date)
        benefitList += getComplimentaryBenefitString(orders)

        if (benefitList.isEmpty()) return "없음\n"
        return benefitList
    }

    fun getDDayBenefitString(date: Int): String {
        if (getDDayDiscountAmount(date) != 0)
            return "크리스마스 디데이 할인: -${decimalFormat.format(getDDayDiscountAmount(date))}원\n"
        return ""
    }

    fun getWeeklyBenefitString(date: Int, orders: List<Order>): String {
        if (getWeeklyDiscountAmount(date, orders) != 0) {
            if (date % 7 == 1 || date % 7 == 2) return "주말 할인: -${
                decimalFormat.format(
                    getWeeklyDiscountAmount(
                        date,
                        orders
                    )
                )
            }원\n"
            return "평일 할인: -${decimalFormat.format(getWeeklyDiscountAmount(date, orders))}원\n"
        }
        return ""
    }

    fun getSpecialBenefitString(date: Int): String {
        if (getSpecialDiscountAmount(date) != 0) return "특별 할인: -${decimalFormat.format(getSpecialDiscountAmount(date))}원\n"
        return ""
    }

    fun getComplimentaryBenefitString(orders: List<Order>): String {
        if (getTotalPrice(orders) >= 120000) return "증정 이벤트: -25,000원\n"
        return ""
    }

    fun getTotalBenefitAmount(date: Int, orders: List<Order>): Int {
        if (getTotalPrice(orders) < 10000) return 0
        var totalBenefitAmount = 0
        totalBenefitAmount += getDDayDiscountAmount(date)
        totalBenefitAmount += getWeeklyDiscountAmount(date, orders)
        totalBenefitAmount += getSpecialDiscountAmount(date)
        totalBenefitAmount += getComplimentaryDiscountAmount(orders)
        return totalBenefitAmount
    }

    fun getTotalDiscountAmount(date: Int, orders: List<Order>): Int {
        if (getTotalPrice(orders) < 10000) return 0
        var totalDiscountAmount = 0
        totalDiscountAmount += getDDayDiscountAmount(date)
        totalDiscountAmount += getWeeklyDiscountAmount(date, orders)
        totalDiscountAmount += getSpecialDiscountAmount(date)
        return totalDiscountAmount
    }

    fun getDDayDiscountAmount(date: Int): Int {
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

    companion object {
        private val menuPriceMap = HashMap<String, Int>()
        private val decimalFormat = DecimalFormat("#,###")
    }
}