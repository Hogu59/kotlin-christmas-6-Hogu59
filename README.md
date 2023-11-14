# 미션 - 크리스마스 프로모션

## ✅ 기능목록
### - 입력
#### 1. 예상 방문 날짜 입력
 - [x] If input number is not a number between 1~31, print error message `[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.` 
#### 2. 주문할 메뉴와 개수 입력 `(e.g. 해산물파스타-2,레드와인-1,초코케이크-1)`
 - [x] If input has any menu not in the menu-list, print error message `[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.`
 - [x] The number of ordered menu should be not less than "1". Or, print error message `[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.`
 - [x] If the input form is different with e.g., print error message `[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.`
 - [x] If there is any duplicate menu, print error message `[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.`

### - 출력
#### 1. 주문 메뉴 출력 순서 제한 없음
#### 2. 할인 전 총주문 금액 출력
#### 3. 증정 메뉴 출력
- 미해당시 "없음" 출력 
#### 4. 총 혜택 금액(총 할인 금액 + 증정 메뉴 가격) 출력
- 적용되는 이벤트 내역 출력 (출력 순서 자유)
- 적용된 이벤트가 하나도 없다면 "없음 출력"
#### 5. 할인 후 예상 결제 금액 출력
#### 6. 총 혜택 금액(총 할인 금액 + 증정 메뉴 가격)에 따라 이벤트 배지 출력 (12월 이벤트 배지)
- 배지가 부여되지 않는 경우, "없음" 출력

*증정메뉴 : 샴페인(25,000원) 총주문금액 12만원 이상시 증정

### - 이벤트 상세
#### 1. 크리스마스 디데이 할인 (총 주문 금액 할인)
 - 12월 1일; 1000원 할인
 - 하루가 지날수록 100원씩 할인 금액 증가
 - 12월 25일; 3,400원 할인
 - 이후 할인 종료

#### 2. 평일 할인 (일~목)
 - 디저트 메뉴 1개당 2,023원 할인

#### 3. 주말 할인 (금, 토)
 - 메인 메뉴 1개당 2,023원 할인

#### 4. 특별 할인
 - 매주 일요일, 크리스마스 당일 총 주문금액에서 1,000원 할인

#### 5. 증정이벤트
 - 총 주문 금액(할인 전 금액)이 12만원 이상일 때, 샴페인(25,000원) 1개 증정

### - 주의 사항
#### 1. 총 주문 금액 10,000원 이상부터 이벤트 적용
#### 2. 음료만 주문 불가
#### 3. 메뉴는 최대 20개까지 주문 가능. 20개 초과 불가.

### - 기타
- [x] Indent depth : 2 or less
- [x] Method length(number of lines) : 15 or less
- [x] Avoid `else` statement as much as possible (Follow coding convention) - used twice.
 * OrderingService.getBadge(); Inevitable because of when statement.
 * OutputView.printComplimentaryMenu(); Thought it is an optimal code.
- [x] Throw `IllegalArgumentException` if input is wrong
  - Print error message
  - Get input again
- [x] Make `InputView` and `OutputView` classes.
