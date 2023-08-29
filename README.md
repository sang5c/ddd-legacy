# 키친포스

## 퀵 스타트

```sh
cd docker
docker compose -p kitchenpos up -d
```

## 요구 사항

- 치킨포스 프로그램을 구현한다.

### Product (상품)

- [X] 상품은 ID, 이름, 가격을 갖는다.
- [ ] 상품을 등록할 수 있다
    - [ ] 이름은 비어있을 수 없고 욕설을 포함해선 안된다
    - [ ] 가격은 0원 이상이다
- [ ] 상품 가격을 변경할 수 있다.
    - [ ] 존재하는 상품만 변경 가능하다
    - [ ] 메뉴 가격이 상품 가격 * 수량의 총합보다 크면 메뉴를 숨긴다(노출 중지, 클 수 없다)
        - [ ] 가격은 0원 이상이다
- [ ] 상품 목록을 조회할 수 있다.

### MenuProduct (메뉴 상품)

- [ ] 메뉴를 구성하는 상품이다.
- [ ] 상품 정보와 수량을 갖는다.
    - [ ] 0 이상의 수량을 갖는다. (예: 짜장면 10개)

### Menu (메뉴)

- [ ] 메뉴는 ID, 이름, 가격, 메뉴 그룹, 노출 여부, 메뉴 상품 목록을 갖는다.
- [ ] 메뉴를 등록할 수 있다
    - [ ] 메뉴는 메뉴 그룹에 속해야 한다.
    - [ ] 메뉴에는 하나 이상의 메뉴 상품이 포함되어야 한다.
    - [ ] 여러 상품을 하나의 메뉴로 구성 가능하다. (예: 짜장면 2개와 짬뽕 1개를 하나의 메뉴로 묶어 판매할 수 있다)
    - [ ] 메뉴 가격은 (상품 가격 * 수량)의 총합보다 작거나 같아야 한다
        - 예) 메뉴 구성: 짜장면 개당 2000원 * 2개, 짬뽕 개당 1000원 * 1개 => 메뉴의 가격은 5000원 이하여야 한다
    - [ ] 메뉴의 이름은 비어있거나 욕설이 포함될 수 없다
- [ ] 메뉴 가격을 변경할 수 있다
    - [ ] 메뉴 가격은 비어있을 수 없다
    - [ ] 메뉴 가격은 0원 이상이다
    - [ ] 메뉴 가격은 메뉴에 포함된 개별의 (상품 가격 * 수량)보다 작거나 같아야 한다
        - 예) 메뉴 구성: 짜장면 개당 2000원 * 2개, 짬뽕 개당 1000원 * 1개 => 최소값은 짬뽕 1개인 1000원 => 변경하는 메뉴 가격은 1000원 이하여야 한다
- [ ] 메뉴 노출 여부를 변경할 수 있다
    - [ ] 존재하는 메뉴만 변경 가능하다
    - [ ] 메뉴 가격이 개별의 (상품 가격 * 수량)보다 크면 메뉴를 숨긴다
- [ ] 메뉴 전체 목록을 조회할 수 있다

### MenuGroup (메뉴 그룹)

- [ ] 메뉴의 묶음을 메뉴 그룹이라 한다.
- [ ] 메뉴 그룹은 ID, 이름을 갖는다.
- [ ] 메뉴 그룹을 등록할 수 있다
    - [ ] 메뉴 그룹 이름은 비어있거나 공백일 수 없다
- [ ] 메뉴 그룹 목록을 조회할 수 있다.

### Order (주문)

- [ ] 주문은 타입, 상태, 주문시간, 주문 상품 목록, 배송주소, 주문 테이블을 갖는다
    - [ ] 주문 타입은 세 가지이다
        - 배달(DELIVERY)
        - 포장(TAKEOUT)
        - 매장식사(EAT_IN)
    - [ ] 주문 상태는 여섯 가지이다
        - 대기(WAITING)
        - 접수(ACCEPTED)
        - 서빙완료(SERVED)
        - 배달중(DELIVERING)
        - 배달완료(DELIVERED)
        - 완료(COMPLETED)
- [ ] 주문을 등록할 수 있다
    - [ ] 주문 타입은 비어있을 수 없다
    - [ ] 주문 상품은 비어있을 수 없다
    - [ ] 메뉴에 없는 항목은 주문할 수 없다
    - [ ] 매장식사가 아닌 경우 주문 수량이 0 이상이어야 한다
    - [ ] 노출중이 아닌 메뉴는 주문할 수 없다
    - [ ] 주문 상품 가격과 메뉴 가격이 일치해야 한다
    - [ ] 주문의 초기 상태는 대기이다
    - [ ] 주문 타입이 배달이면 배달 주소가 필수이다
    - [ ] 주문 타입이 매장식사면 주문 테이블이 존재하고, 주문 테이블을 점유해야 주문 가능하다
- [ ] 주문 상태를 접수로 변경할 수 있다
    - [ ] 존재하는 주문만 가능하다
    - [ ] 주문 대기중에만 주문 접수가 가능하다
    - [ ] 주문 타입이 배달이면 배달 요청을 보낸다
- [ ] 주문 상태를 서빙완료로 변경할 수 있다
    - [ ] 존재하는 주문만 가능하다
    - [ ] 접수 상태에서만 서빙완료가 가능하다
- [ ] 주문 상태를 배달중으로 변경할 수 있다
    - [ ] 존재하는 주문만 가능하다
    - [ ] 주문 타입이 배달인 경우에만 가능하다
    - [ ] 주문 상태가 서빙 완료인 경우만 가능하다
- [ ] 주문 상태를 배달 완료로 변경할 수 있다.
    - [ ] 존재하는 주문만 가능하다
    - [ ] 주문 상태가 배달중인 경우만 가능하다
- [ ] 주문 상태를 완료로 변경할 수 있다
    - [ ] 존재하는 주문만 가능하다
    - [ ] 주문 타입이 배달일 때 배달 완료인 경우만 가능하다
    - [ ] 주문 타입이 포장/매장식사일 때 서빙 완료인 경우만 가능하다
    - [ ] 주문 타입이 매장식사이면 주문 테이블을 정리해야 한다
- [ ] 주문 목록을 조회할 수 있다

### OrderLineItem (주문 상품)

- [ ] 주문 상품은 ID, 메뉴, 수량(메뉴 주문 수량)을 갖는다

### OrderTable (주문 테이블)

- [ ] 주문 테이블은 ID, 이름, 손님 수, 점유 여부를 갖는다
- [ ] 주문 테이블을 등록할 수 있다
    - [ ] 주문 테이블 이름은 비어있거나 공백일 수 없다
- [ ] 주문 테이블을 점유할 수 있다
    - [ ] 존재하는 테이블만 가능하다
- [ ] 주문 테이블을 정리할 수 있다
    - [ ] 테이블이 존재해야 하고, 주문 완료상태여야 한다
    - [ ] 테이블 손님 수와 점유 여부를 초기화한다
        - [ ] 초기 상태는 테이블에 손님이 없고, 테이블을 점유하지 않은 상태이다
- [ ] 주문 테이블의 손님 수를 변경할 수 있다
    - [ ] 손님 수는 0 이상이다
    - [ ] 점유되지 않은 테이블은 손님 수를 변경할 수 없다 (테이블 점유가 선행되어야 한다)
- [ ] 주문 테이블 목록을 조회할 수 있다

## 용어 사전

| 한글명 | 영문명 | 설명 |
|-----|-----|----|
|     |     |    |

## 모델링

---

# 공통 요구사항

## 객체지향 생활 체조

- 규칙 1: 한 메서드에 오직 한 단계의 들여쓰기만 한다.
- 규칙 2: else 예약어를 쓰지 않는다.
- 규칙 3: 모든 원시 값과 문자열을 포장한다.
- 규칙 4: 한 줄에 점을 하나만 찍는다.
- 규칙 5: 줄여 쓰지 않는다. (축약 금지)
- 규칙 6: 모든 엔티티를 작게 유지한다.
- 규칙 7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
- 규칙 8: 일급 컬렉션을 쓴다.

---

## 2단계 - 요구 사항 정리

## 요구 사항

- 키친포스의 요구 사항을 작성한다.
    - 참고: 코드, HTTP 요청, SQL DDL

---

# 1단계 - 문자열 덧셈 계산기

## 요구 사항

- 구분자로 문자열을 분리하여 각 숫자의 합을 반환한다. 기본 구분자는 콜론(:)과 콤마(,)이다.
    - 예) "" => 0, "1,2" => 3, "1,2,3" => 6, "1,2:3" => 6)
- 커스텀 구분자를 지정할 수 있다. 커스텀 구분자는 문자열 앞부분의 “//”와 “\n” 사이에 위치하는 문자를 커스텀 구분자로 사용한다.
    - 예) "//;\n1;2;3" 구분자는 세미콜론(;)
- 문자열 계산기에 숫자 이외의 값 또는 음수를 전달하는 경우 RuntimeException 예외를 throw 한다.

### 기능 목록 도출

- [X] 문자열을 받아 계산 결과를 반환한다
    - [X] 빈 문자열 또는 null 전달시 0을 반환한다
    - [X] 숫자 하나를 문자열로 입력할 경우 해당 숫자를 반환한다
- [X] 문자열은 구분자로 시작한다
    - [X] 구분자는 //와 \n 사이에 위치한다
- [X] 구분자가 없는 경우 기본 구분자를 사용한다
    - [X] 기본 구분자는 콜론(:)과 콤마(,)이다
    - [X] 1:2:3 = 6
- [X] 구분자가 아닌 문자가 숫자가 아니면 예외가 발생한다
