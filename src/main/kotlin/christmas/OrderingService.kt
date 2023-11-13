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
        for (i in menuList.indices) menuPriceMap[menuList[i]] = priceList[i]
    }

    fun checkComplimentary(price: Int): String {
        return if (price >= COMPLIMENTARY_CRITERIA) CHAMPAGNE
        else NONE
    }

    fun getBenifitList(date: Int, orders: List<Order>): String {
        if (getTotalPrice(orders) < BENEFIT_APPLY_MINIMUM_PRICE) return "$NONE\n"
        var benefitList = ""
        benefitList += getDDayBenefitString(date)
        benefitList += getWeeklyBenefitString(date, orders)
        benefitList += getSpecialBenefitString(date)
        benefitList += getComplimentaryBenefitString(orders)

        if (benefitList.isEmpty()) return "$NONE\n"
        return benefitList
    }

    fun getDDayBenefitString(date: Int): String {
        if (getDDayDiscountAmount(date) != 0) return "$DDAY_DISCOUNT_MENTION${decimalFormat.format(getDDayDiscountAmount(date))}$WON\n"
        return ""
    }

    fun getWeeklyBenefitString(date: Int, orders: List<Order>): String {
        if (getWeeklyDiscountAmount(date, orders) != 0) {
            if (date % 7 == 1 || date % 7 == 2) return "$WEEKEND_DISCOUNT_MENTION${decimalFormat.format(getWeeklyDiscountAmount(date, orders))}$WON\n"
            return "$WEEKDAY_DISCOUNT_MENTION${decimalFormat.format(getWeeklyDiscountAmount(date, orders))}$WON\n"
        }
        return ""
    }

    fun getSpecialBenefitString(date: Int): String {
        if (getSpecialDiscountAmount(date) != ZERO) return "$SPECIAL_DISCOUNT_MENTION${decimalFormat.format(getSpecialDiscountAmount(date))}$WON\n"
        return ""
    }

    fun getComplimentaryBenefitString(orders: List<Order>): String {
        if (getTotalPrice(orders) >= COMPLIMENTARY_CRITERIA) return "$COMPLIMENTARY_MENTION\n"
        return ""
    }

    fun getTotalBenefitAmount(date: Int, orders: List<Order>): Int {
        if (getTotalPrice(orders) < BENEFIT_APPLY_MINIMUM_PRICE) return ZERO
        var totalBenefitAmount = 0
        totalBenefitAmount += getDDayDiscountAmount(date)
        totalBenefitAmount += getWeeklyDiscountAmount(date, orders)
        totalBenefitAmount += getSpecialDiscountAmount(date)
        totalBenefitAmount += getComplimentaryDiscountAmount(orders)
        return totalBenefitAmount
    }

    fun getTotalDiscountAmount(date: Int, orders: List<Order>): Int {
        if (getTotalPrice(orders) < BENEFIT_APPLY_MINIMUM_PRICE) return ZERO
        var totalDiscountAmount = 0
        totalDiscountAmount += getDDayDiscountAmount(date)
        totalDiscountAmount += getWeeklyDiscountAmount(date, orders)
        totalDiscountAmount += getSpecialDiscountAmount(date)
        return totalDiscountAmount
    }

    fun getDDayDiscountAmount(date: Int): Int {
        if (date > CHRISTMAS_DAY) return ZERO
        return DDAY_DISCOUNT_BASE_AMOUNT + (date - ONE) * DDAY_DISCOUNT_PER_DAY_AMOUNT
    }

    fun getSpecialDiscountAmount(date: Int): Int {
        if (date % NUMBER_OF_WEEK == SUNDAY || date == CHRISTMAS_DAY) return SPECIAL_DISCOUNT_AMOUNT
        return ZERO
    }

    fun getWeeklyDiscountAmount(date: Int, orders: List<Order>): Int {
        if (date % NUMBER_OF_WEEK == FRIDAY || date % NUMBER_OF_WEEK == SATURDAY) return getWeekendDiscountAmount(orders)
        return getWeekdayDiscountAmount(orders)
    }

    fun getWeekendDiscountAmount(orders: List<Order>): Int {
        var discountAmount = 0
        orders.forEach {
            if (mainMenu.contains(it.name)) discountAmount += it.num * WEEKLY_DISCOUNT_AMOUNT
        }
        return discountAmount
    }

    fun getWeekdayDiscountAmount(orders: List<Order>): Int {
        var discountAmount = 0
        orders.forEach {
            if (dessertMenu.contains(it.name)) discountAmount += it.num * WEEKLY_DISCOUNT_AMOUNT
        }
        return discountAmount
    }

    fun getComplimentaryDiscountAmount(orders: List<Order>): Int {
        if (checkComplimentary(getTotalPrice(orders)) != NONE) return COMPLIMENTARY_PRICE
        return 0
    }

    fun getBadge(date: Int, orders: List<Order>): String {
        return when (getTotalBenefitAmount(date, orders)) {
            in ZERO..<RANGE_UNTIL_NONE -> BADGE_NONE
            in RANGE_UNTIL_NONE..<RANGE_UNTIL_STAR -> BADGE_STAR
            in RANGE_UNTIL_STAR..<RANGE_UNTIL_TREE -> BADGE_TREE
            else -> BADGE_SANTA
        }
    }

    companion object {
        private val menuPriceMap = HashMap<String, Int>()
        private val decimalFormat = DecimalFormat("#,###")

        const val DDAY_DISCOUNT_MENTION = "크리스마스 디데이 할인: -"
        const val WEEKDAY_DISCOUNT_MENTION = "평일 할인: -"
        const val WEEKEND_DISCOUNT_MENTION = "주말 할인: -"
        const val SPECIAL_DISCOUNT_MENTION = "특별 할인: -"
        const val COMPLIMENTARY_MENTION = "증정 이벤트: -25,000원"

        const val CHAMPAGNE = "샴페인 1개"
        const val WON = "원"

        const val NUMBER_OF_WEEK = 7
        const val FRIDAY = 1
        const val SATURDAY = 2
        const val SUNDAY = 3
        const val CHRISTMAS_DAY = 25

        const val DDAY_DISCOUNT_BASE_AMOUNT = 1000
        const val DDAY_DISCOUNT_PER_DAY_AMOUNT = 100
        const val SPECIAL_DISCOUNT_AMOUNT = 1000
        const val WEEKLY_DISCOUNT_AMOUNT = 2023
        const val COMPLIMENTARY_PRICE = 25000
        const val NONE = "없음"

        const val BADGE_NONE = "없음"
        const val BADGE_STAR = "별"
        const val BADGE_TREE = "트리"
        const val BADGE_SANTA = "산타"

        const val ZERO = 0
        const val ONE = 1

        const val RANGE_UNTIL_NONE = 5000
        const val RANGE_UNTIL_STAR = 10000
        const val RANGE_UNTIL_TREE = 20000

        const val BENEFIT_APPLY_MINIMUM_PRICE = 10000
        const val COMPLIMENTARY_CRITERIA = 120000
    }
}