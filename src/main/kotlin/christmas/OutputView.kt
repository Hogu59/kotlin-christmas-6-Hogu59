package christmas

import java.text.DecimalFormat

class OutputView {
    private val decimalFormat = DecimalFormat("#,###")

    fun printGetVisitDateGuideMention() {
        println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.")
        println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
    }

    fun printGetOrderGuideMention() {
        println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)")
    }

    fun printPreviewMention(visitDate: Int) {
        println("12월 ${visitDate}일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n")
    }

    fun printOrderList(orders: List<Order>) {
        println("<주문 메뉴>")
        for (i in orders.indices) {
            println("${orders[i].name} ${orders[i].num}개")
        }
    }

    fun printPriceBeforeDiscount(totalPrice: Int) {
        println("\n<할인 전 총주문 금액>")
        println("${decimalFormat.format(totalPrice)}원")
    }

    fun printComplimentaryMenu(totalPrice: Int) {
        println("\n<증정 메뉴>")
        if (totalPrice >= 120000) println("샴페인 1개")
        else println("없음")
    }

    fun printBenefitList(benefitList: String) {
        println("\n<혜택 내역>")
        print(benefitList)
    }

    fun printTotalBenefitPrice(totalBenefitPrice: Int) {
        println("\n<총혜택 금액>")
        if (totalBenefitPrice == 0) println("0원")
        else println("-${decimalFormat.format(totalBenefitPrice)}원")
    }

    fun printExpectedPriceAfterDiscount(totalPrice: Int, discountAmount: Int) {
        println("\n<할인 후 예상 결제 금액>")
        println("${decimalFormat.format(totalPrice - discountAmount)}원")
    }

    fun printEventBadge(badge: String) {
        println("\n<12월 이벤트 배지>")
        println(badge)
    }
}