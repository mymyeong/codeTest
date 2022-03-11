# 사용자 포인트 적립 / 사용 시스템

+ 회원별 포인트 합계 조회  
    - url : /user-point/{userNo}
    - parameter : 없음
    - return : Long(사용자의 사용 가능 포인트)
    
     
+ 회원별 포인트 적립/사용 내역 조회
    - url : /user-point/{userNo}/list
    - parameter : Pageable
    - return : JSON Array
```jsonc
/**return */
{  
  "pointStatus": {String}, /** 포인트 충전 사용 여부 (CHARGE(충전) or USE(사용)) */
  "pointAmount": {Long}, /** 포인트 사용/충전 금액 */
  "processDate": {Date}, /** 처리일자 */
}
````
    
+ 회원별 포인트 적립
    - url : /user-point/{userNo}/chargePoint
    - parameter : JSON 
    - return : JSON
```jsonc
/** parameter */
{
  "chargePointAmount": {Long} /** 적립 금액 */
} 
```

```jsonc
/**return */
{
  "resultCode": int, /** 응답코드 */
  "resultMsg": String, /** 응답메시지 */
  "responseDateTime": Date, /** 응답일자 */
}
```
    
    
+ 회원별 포인트 사용
    - url : /user-point/{userNo}/usePoint
    - parameter : JSON 
    - return : JSON
```jsonc
/** parameter */
{
  "usePointAmount": {Long} /** 사용 금액 */
} 
```

```jsonc
/**return */
{
  "resultCode": int, /** 응답코드 */
  "resultMsg": String, /** 응답메시지 */
  "responseDateTime": Date, /** 응답일자 */
}
```
