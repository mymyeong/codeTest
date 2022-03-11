# musinsa 코드 테스트 코드

+ 회원별 포인트 합계 조회  
    - url : /user-point/{userNo}
    - parameter : 없음
    - return : Long(사용자의 사용 가능 포인트)
    
     
+ 회원별 포인트 적립/사용 내역 조회 (페이징 처리 필수, 사용취소된 내역은 조회되지 않음)
    - url : /user-point/{userNo}/list
    - parameter : 없음
    - return : JSON
```json {
        "userNo": {Long},
        "processDate": {DATE},
        "pointStatus": "CHARGE(충전) or USE(사용)",
        "pointAmount": {Long}
      }
````
    
     
+ 회원별 포인트 적립
    - url : /user-point/{userNo}/usePoint
    - parameter : JSON 
```` {
      	 "usePointAmount": {Long}
      }
````
    - return : JSON  
```` {
        "userNo": {Long},
        "processDate": {DATE},
        "pointStatus": "CHARGE(충전) or USE(사용)",
        "pointAmount": {Long}
      }
````
    
    
+ 회원별 포인트 사용
