package christmas

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.text.DecimalFormat


class ApplicationTest : NsTest() {
    private val inputView = InputView()
    private val outputView = OutputView()
    private val orderingService = OrderingService()

    @Test
    fun `모든 타이틀 출력`() {
        assertSimpleTest {
            run("3", "티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1")
            assertThat(output()).contains(
                "<주문 메뉴>", "<할인 전 총주문 금액>", "<증정 메뉴>", "<혜택 내역>", "<총혜택 금액>", "<할인 후 예상 결제 금액>", "<12월 이벤트 배지>"
            )
        }
    }

    @Test
    fun `혜택 내역 없음 출력`() {
        assertSimpleTest {
            run("26", "타파스-1,제로콜라-1")
            assertThat(output()).contains("<혜택 내역>$LINE_SEPARATOR".toString() + "없음")
        }
    }

    @Test
    fun `날짜 예외 테스트`() {
        assertSimpleTest {
            runException("a")
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트`() {
        assertSimpleTest {
            runException("3", "제로콜라-a")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    //테스트 코드 작성
    @Test
    fun `주문 예외 테스트 (날짜가 31 초과인 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateVisitDate("32")
        }
        assertSimpleTest {
            runException("32", "제로콜라-2")
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (날짜가 음수인 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateVisitDate("-1")
        }
        assertSimpleTest {
            runException("-1", "제로콜라-2")
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (날짜가 문자인 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateVisitDate("a")
        }
        assertSimpleTest {
            runException("!", "제로콜라-2")
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (날짜입력이 공백인 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateVisitDate("")
        }
        assertSimpleTest {
            runException("\n", "제로콜라-2")
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (주문 입력이 공백인 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("")
        }
        assertSimpleTest {
            runException("3", "\n")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (주문 입력 형식이 맞지 않는 경우 - 공백 존재)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("제로콜라- 1")
        }
        assertSimpleTest {
            runException("3", "제로콜라- 1")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (주문 입력 형식이 맞지 않는 경우 - 완전 오기입)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("^*%*ㅆ훂ㄴ(*)-ㄴㄷㄹㅁㄹ")
        }
        assertSimpleTest {
            runException("3", "^*%*ㅆ훂ㄴ(*)-ㄴㄷㄹㅁㄹ")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (주문 입력 형식이 맞지 않는 경우 - 문자가 있는 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("제로콜라-*1")
        }
        assertSimpleTest {
            runException("3", "제로콜라-/1")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (메뉴판에 없는 메뉴 주문)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("사이다-3")
        }
        assertSimpleTest {
            runException("3", "사이다-3")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (중복된 메뉴 주문)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("제로콜라-1,제로콜라-1")
        }
        assertSimpleTest {
            runException("3", "제로콜라-1,제로콜라-1")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (음료만 주문한 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("제로콜라-1")
        }
        assertSimpleTest {
            runException("3", "레드와인-1")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (메뉴를 20개 초과 주문할 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("티본스테이크-30")
        }
        assertSimpleTest {
            runException("3", "티본스테이크-30")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `주문 예외 테스트 (메뉴 개수의 총 합이 20개 초과 주문할 경우)`() {
        assertThrows<IllegalArgumentException> {
            inputView.validateOrders("시저샐러드-1, 티본스테이크-1, 크리스마스파스타-1, 제로콜라-3, 아이스크림-17")
        }
        assertSimpleTest {
            runException("3", "시저샐러드-1, 티본스테이크-1, 크리스마스파스타-1, 제로콜라-3, 아이스크림-17")
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
        }
    }

    @Test
    fun `출력 확인 테스트 (주문 메뉴 출력 확인)`() {
        outputView.printOrderList(orders)
        assertThat(output()).contains("시저샐러드 1개${LINE_SEPARATOR}티본스테이크 1개${LINE_SEPARATOR}크리스마스파스타 1개${LINE_SEPARATOR}제로콜라 3개${LINE_SEPARATOR}아이스크림 1개")
    }

    @Test
    fun `출력 확인 테스트 (총주문 금액 출력 확인)`() {
        outputView.printPriceBeforeDiscount(orderingService.getTotalPrice(orders))
        assertThat(output()).contains("102,000")

        outputView.printPriceBeforeDiscount(orderingService.getTotalPrice(ordersOver120000))
        assertThat(output()).contains("127,000")
    }

    @Test
    fun `출력 확인 테스트 (증정 메뉴 출력 확인 - 샴페인 1개)`() {
        outputView.printComplimentaryMenu(orderingService.getTotalPrice(ordersOver120000))
        assertThat(output()).contains("샴페인 1개")
    }

    @Test
    fun `출력 확인 테스트 (증정 메뉴 출력 확인 - 없음)`() {
        outputView.printComplimentaryMenu(orderingService.getTotalPrice(orders))
        assertThat(output()).contains("없음")
    }

    @Test
    fun `출력 확인 테스트 (혜택 내역 출력 - 만원 이하)`() {
        outputView.printBenefitList(orderingService.getBenifitList(3, ordersUnder10000))
        assertThat(output()).isEqualTo("<혜택 내역>${LINE_SEPARATOR}없음")
    }

    @Test
    fun `기능 확인 테스트 (혜택 내역 출력 - 디데이 할인)`() {
        val res = orderingService.getDDayBenefitString(3)
        assertThat(res.trim()).isEqualTo("크리스마스 디데이 할인: -1,200원")
    }

    @Test
    fun `기능 확인 테스트 (혜택 내역 출력 - 주말 할인)`() {
        val res = orderingService.getWeeklyBenefitString(2, orders)
        assertThat(res.trim()).isEqualTo("주말 할인: -4,046원")
    }

    @Test
    fun `기능 확인 테스트 (혜택 내역 출력 - 평일 할인)`() {
        val res = orderingService.getWeeklyBenefitString(3, orders)
        assertThat(res.trim()).isEqualTo("평일 할인: -2,023원")
    }

    @Test
    fun `기능 확인 테스트 (혜택 내역 출력 - 특별 할인)`() {
        for (i in 1..32) {
            val res = orderingService.getSpecialBenefitString(i)
            if (i % 7 == 3 || i == 25) assertThat(res.trim()).isEqualTo("특별 할인: -1,000원")
            else assertThat(res.trim()).isEqualTo("")
        }
    }

    @Test
    fun `기능 확인 테스트 (혜택 내역 출력 - 증정 이벤트)`() {
        val getComplimentary = orderingService.getComplimentaryBenefitString(ordersOver120000)
        assertThat(getComplimentary.trim()).isEqualTo("증정 이벤트: -25,000원")

        val noComplimentary = orderingService.getComplimentaryBenefitString(orders)
        assertThat(noComplimentary.trim()).isEqualTo("")
    }

    @Test
    fun `기능 확인 테스트 (총혜택 금액 - 만원 이하 주문시 0원)`() {
        val res = orderingService.getTotalBenefitAmount(2, ordersUnder10000)
        assertThat(res).isEqualTo(0)

        assertSimpleTest {
            runException("2", "시저샐러드-1")
            assertThat(output()).contains("<총혜택 금액>${LINE_SEPARATOR}0원")
        }
    }

    @Test
    fun `기능 확인 테스트 (총혜택 금액 - 주말, 디데이 할인)`() {
        val res = orderingService.getTotalBenefitAmount(2, orders)
        assertThat(res).isEqualTo(5146)

        assertSimpleTest {
            runException("2", "시저샐러드-1,티본스테이크-1,크리스마스파스타-1,제로콜라-3,아이스크림-1")
            assertThat(output()).contains("<총혜택 금액>${LINE_SEPARATOR}-5,146원")
        }
    }

    @Test
    fun `기능 확인 테스트 (총혜택 금액 - 평일, 디데이, 특별, 증정이벤트)`() {
        val res = orderingService.getTotalBenefitAmount(3, ordersOver120000)
        assertThat(res).isEqualTo(29223)

        assertSimpleTest {
            runException("3", "시저샐러드-1,티본스테이크-1,크리스마스파스타-2,제로콜라-3,아이스크림-1")
            assertThat(output()).contains("<총혜택 금액>${LINE_SEPARATOR}-29,223원")
        }
    }

    @Test
    fun `기능 확인 테스트 (할인 후 예상 결제 금액 - 만원 이하)`() {
        outputView.printExpectedPriceAfterDiscount(
            orderingService.getTotalPrice(ordersUnder10000), orderingService.getTotalDiscountAmount(3, ordersUnder10000)
        )
        assertThat(output().trim()).isEqualTo(
            "<할인 후 예상 결제 금액>${LINE_SEPARATOR}${
                decimalFormat.format(orderingService.getTotalPrice(ordersUnder10000))
            }원"
        )
    }

    @Test
    fun `기능 확인 테스트 (할인 후 예상 결제 금액 - 12만원 이상 (샴페인 가격 안 빠지는지 확인))`() {
        outputView.printExpectedPriceAfterDiscount(
            orderingService.getTotalPrice(ordersOver120000), orderingService.getTotalDiscountAmount(3, ordersOver120000)
        )
        assertThat(output().trim()).isEqualTo(
            "<할인 후 예상 결제 금액>${LINE_SEPARATOR}${
                decimalFormat.format(orderingService.getTotalPrice(ordersOver120000) - 4223)
            }원"
        )
    }

    @Test
    fun `기능 확인 테스트 (배지 정상 반환 확인)`() {
        val benefitUnder5000 = orderingService.getBadge(2, ordersUnder10000)
        assertThat(benefitUnder5000).isEqualTo("없음")

        val benefitUnder10000 = orderingService.getBadge(2, listOf(Order("크리스마스파스타", 2)))
        assertThat(benefitUnder10000).isEqualTo("별")

        val benefitUnder20000 = orderingService.getBadge(22, listOf(Order("크리스마스파스타", 4)))
        assertThat(benefitUnder20000).isEqualTo("트리")

        val benefitOver20000 = orderingService.getBadge(3, ordersOver120000)
        assertThat(benefitOver20000).isEqualTo("산타")
    }


    override fun runMain() {
        main()
    }

    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()

        //시저샐러드-1,티본스테이크-1,크리스마스파스타-1,제로콜라-3,아이스크림-1
        private val orders =
            listOf(Order("시저샐러드", 1), Order("티본스테이크", 1), Order("크리스마스파스타", 1), Order("제로콜라", 3), Order("아이스크림", 1))

        //시저샐러드-1
        private val ordersUnder10000 = listOf(Order("시저샐러드", 1))

        //시저샐러드-1,티본스테이크-1,크리스마스파스타-2,제로콜라-3,아이스크림-1
        private val ordersOver120000 =
            listOf(Order("시저샐러드", 1), Order("티본스테이크", 1), Order("크리스마스파스타", 2), Order("제로콜라", 3), Order("아이스크림", 1))

        private val decimalFormat = DecimalFormat("#,###")
    }
}
