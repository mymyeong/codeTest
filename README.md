# 사용자 포인트 적립 / 사용 시스템

실행 명령어 : java -jar codeTest-0.0.1-SNAPSHOT.jar    
기본 사용자 NO : 1

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
+ 포인트 사용 취소
    - url : /user-point/{userNo}/usePointCancel
    - parameter : JSON 
    - return : JSON
```jsonc
/** parameter */
{
  "no": {Long} /** UserPointNo */
  "userNo" : {Long} /** userNo */
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
+ 포인트 만료 처리
    - url : /user-point/{userNo}/pointExpired
    - parameter : 없음
    - return : JSON
```jsonc
/**return */
{
  "resultCode": int, /** 응답코드 */
  "resultMsg": String, /** 응답메시지 */
  "responseDateTime": Date, /** 응답일자 */
}
```
    
---
+ 포인트 사용 우선순위 구현
```jsonc
[
    {
        "pointAmount": 500,
        "processDate": "2021-03-15T00:00:00",
        "pointDetailNo": 20003
    },
    {
        "pointAmount": 1000,
        "processDate": "2021-03-17T00:00:00",
        "pointDetailNo": 20005
    }
]
```
700원 사용
```jsonc
[
    {
        "pointAmount": 800,
        "processDate": "2021-03-17T00:00:00",
        "pointDetailNo": 20005
    }
]
```
