package christmas

import camp.nextstep.edu.missionutils.Console
import christmas.MenuList.Companion.drinkMenu
import christmas.MenuList.Companion.menuList
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
            require(visitDate.isNotEmpty()) { ERROR_UNVALID_DATE }
            val validVisitDate = visitDate.toInt()
            require(validVisitDate in 1..<32) { ERROR_UNVALID_DATE }
        } catch (exception: NumberFormatException) {
            throw IllegalArgumentException(ERROR_UNVALID_DATE)
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
        lateinit var validOrderList: List<Order>
        try {
            require(orders.isNotEmpty() || !orders.contains(SPACE)) { ERROR_UNVALID_ORDER }
            val orderList = orders.split(COMMA)
            require(checkNameOfMenu(orderList)) { ERROR_UNVALID_ORDER }
            require(checkDuplicateMenu(orderList)) { ERROR_UNVALID_ORDER }
            require(checkOrderOnlyDrink(orderList)) { ERROR_UNVALID_ORDER }
            require(checkNumberOfMenu(orderList)) { ERROR_UNVALID_ORDER }
            require(checkTotalNumberOfMenu(orderList)) { ERROR_UNVALID_ORDER }
            validOrderList = getValidOrderList(orderList)
        } catch (exception: NumberFormatException) { throw IllegalArgumentException(ERROR_UNVALID_ORDER) }
        return validOrderList
    }

    fun getValidOrderList(orderList: List<String>): List<Order> {
        val validOrderList = mutableListOf<Order>()
        for (i in orderList.indices) {
            val (menu, num) = orderList[i].split(DASH)
            validOrderList.add(Order(menu, num.toInt()))
        }
        return validOrderList
    }

    fun checkNameOfMenu(orderList: List<String>): Boolean {
        for (i in orderList.indices)
            if (!menuList.contains(orderList[i].split(DASH)[0])) return false
        return true
    }

    fun checkDuplicateMenu(orderList: List<String>): Boolean {
        val list = mutableListOf<String>()
        for (i in orderList.indices)
            list.add(orderList[i].split(DASH)[0])
        if (list.distinct().size == orderList.size) return true
        return false
    }

    fun checkOrderOnlyDrink(orderList: List<String>): Boolean {
        orderList.forEach {
            if (!drinkMenu.contains(it.split(DASH)[0])) return true
        }
        return false
    }

    fun checkTotalNumberOfMenu(orderList: List<String>): Boolean {
        var numberOfMenu = 0
        for (i in orderList.indices)
            numberOfMenu = orderList[i].split(DASH)[1].toInt()
        if (numberOfMenu > MAXIMUM_ORDER_NUMBER) return false
        return true
    }

    fun checkNumberOfMenu(orderList: List<String>): Boolean {
        for (i in orderList.indices)
            if (orderList[i].split(DASH)[1].toInt() !in MINIMUM_ORDER_NUMBER..MAXIMUM_ORDER_NUMBER) return false
        return true
    }

    companion object {
        const val ERROR_UNVALID_DATE = "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요."
        const val ERROR_UNVALID_ORDER = "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요."
        const val SPACE = " "
        const val COMMA = ","
        const val DASH = "-"
        const val MINIMUM_ORDER_NUMBER = 1
        const val MAXIMUM_ORDER_NUMBER = 20
    }
}