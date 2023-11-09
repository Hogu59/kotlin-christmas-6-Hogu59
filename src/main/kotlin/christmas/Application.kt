package christmas

import camp.nextstep.edu.missionutils.*
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

fun main() {
    println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.\n")
    println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
    val visitDate = getVisitDate()


}

fun getVisitDate() : Int {
    val visitDate = Console.readLine()
    return try {
        validateVisitDate(visitDate)
    } catch (exception : IllegalArgumentException) {
        println(exception.message)
        getVisitDate()
    }
}

fun validateVisitDate(visitDate : String) : Int {
    try {
        require(visitDate.isNotEmpty()) { "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요." }
        val validVisitDate = visitDate.toInt()
        require(validVisitDate in 1..< 32) { "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요." }
    } catch (exception : NumberFormatException) {
        throw IllegalArgumentException("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
    }
    return visitDate.toInt()
}