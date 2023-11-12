package christmas

import camp.nextstep.edu.missionutils.Console
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

class InputView {
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
            //throw IllegalArgumentException("뭔가 에러임.")
            throw IllegalArgumentException("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
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
}