# spring-gift-order
> 주문하기
## 기능 요구 사항
* 이전 리뷰 반영
  * Api Client Service에서 분리
  * Social 로그인 마다 따로 table을 만들어야하는 문제 해결
  * rest client time out 설정
- 상품 구매 request 받기
  - 상품 옵션과 해당 수량을 선택 주분 → 그만큼 수량 차감
  - 해당 상품이 위시 리스트에 있었으면 → 위시 리스트 삭제
  - 구매도 Database에 저장 필요
- 주문할 때 수령인에세 메시지 전송
  - [나에게 보내기](https://developers.kakao.com/docs/latest/ko/kakaotalk-message/rest-api#default-template-msg-me)