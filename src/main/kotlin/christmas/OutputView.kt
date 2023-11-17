package christmas

import java.text.DecimalFormat

class OutputView {

    fun printGetVisitDateGuideMention() {
        println(GREETING_MENTION)
        println(INPUT_DATE_GUIDE_MENTION)
    }

    fun printGetOrderGuideMention() = println(INPUT_ORDER_GUIDE_MENTION)

    fun printPreviewMention(visitDate: Int) {
        println("$DECEMBER ${visitDate}$PREVIEW_MENTION")
    }

    fun printOrderList(orders: List<Order>) {
        println(ORDER_MENU_TITLE)
        for (i in orders.indices)
            println("${orders[i].name} ${orders[i].num}$NUM")
    }

    fun printPriceBeforeDiscount(totalPrice: Int) {
        println(PRICE_BEFORE_DISCOUNT_TITLE)
        println("${decimalFormat.format(totalPrice)}$WON")
    }

    fun printComplimentaryMenu(totalPrice: Int) {
        println(COMPLIMENTARY_MENU_TITLE)
        if (totalPrice >= COMPLIMENTARY_CRITERIA) println(CHAMPAGNE)
        else println(NONE)
    }

    fun printBenefitList(benefitList: String) {
        println(BENEFIT_LIST_TITLE)
        print(benefitList)
    }

    fun printTotalBenefitPrice(totalBenefitPrice: Int) {
        println(TOTAL_BENEFIT_PRICE_TITLE)
        //if (totalBenefitPrice == 0) println(ZERO_WON)
        //else println("-${decimalFormat.format(totalBenefitPrice)}$WON")
        if(totalBenefitPrice != ZERO) print(MINUS)
        println("${decimalFormat.format(totalBenefitPrice)}$WON")
    }

    fun printExpectedPriceAfterDiscount(totalPrice: Int, discountAmount: Int) {
        println(PRICE_AFTER_DISCOUNT_TITLE)
        println("${decimalFormat.format(totalPrice - discountAmount)}$WON")
    }

    fun printEventBadge(badge: String) {
        println(EVENT_BADGE_TITLE)
        println(badge)
    }

    companion object {
        private val decimalFormat = DecimalFormat("#,###")

        const val GREETING_MENTION = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."
        const val INPUT_DATE_GUIDE_MENTION = "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)"
        const val INPUT_ORDER_GUIDE_MENTION = "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)"
        const val PREVIEW_MENTION = "일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n"

        const val ORDER_MENU_TITLE = "<주문 메뉴>"
        const val PRICE_BEFORE_DISCOUNT_TITLE = "\n<할인 전 총주문 금액>"
        const val COMPLIMENTARY_MENU_TITLE = "\n<증정 메뉴>"
        const val BENEFIT_LIST_TITLE = "\n<혜택 내역>"
        const val TOTAL_BENEFIT_PRICE_TITLE = "\n<총혜택 금액>"
        const val PRICE_AFTER_DISCOUNT_TITLE = "\n<할인 후 예상 결제 금액>"
        const val EVENT_BADGE_TITLE = "\n<12월 이벤트 배지>"

        const val CHAMPAGNE = "샴페인 1개"
        const val NONE = "없음"
        const val DECEMBER = "12월"
        const val NUM = "개"
        const val WON = "원"
        const val MINUS = "-"

        const val ZERO = 0
        const val COMPLIMENTARY_CRITERIA = 120000
    }
}