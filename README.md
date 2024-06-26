# 공좌석

공단기 예약 알림 서비스

## 서비스 아키텍처

![](https://github.com/duckbill413/TodayHouse-Clone/assets/86183856/edc74024-55b6-4591-951a-906157414907)

## 앱 구성 화면

<p align="center">
<img src="https://user-images.githubusercontent.com/86183856/218312383-8bb694e5-ffc0-4124-86c2-b4ae414ba4aa.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312389-21408bb7-0718-4a67-8574-ad2ac0fa2209.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312415-45384a77-96a7-4a47-8d75-cb79f64e8c6b.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312396-b60835f8-53d2-4d71-b146-e28f4eee6730.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312408-49e0794c-50cc-4743-88b7-f7258b5bb0c5.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312392-6a9fcf37-68dc-4c39-8dbc-80859abdb755.jpg" width="20%" height="30%">
<img src="https://user-images.githubusercontent.com/86183856/218312405-08fb68cf-6577-477e-8c9e-f1dda69a558d.jpg" width="20%" height="30%">
</p>

## 안드로이드

- 로그인, 회원가입
- 좌석 예약, 변경, 취소
- 좌석 배치도 및 내 자리 확인
- 선호 좌석 선택
- 이전에 좌석 정보 확인 가능
- 알림, 이메일 끄기 켜기 설정
- 회원 정보 변경 기능

## 서버(Spring)

- Selenium을 이용하여 웹 탐색 및 좌석 알림 처리
- 스프링 배치 활용
  - 좌석 예약 시간에 자동 알림 실행
- 좌석 예약 정보 확인 및 좌석 정보 제공
- 안드로이드 연동 구성
- 예약 결과 및 좌석 정보 알림(FCM), 이메일 전송(spring-mail)

### Database - MySQL, RDS

- 이용자 회원정보, 좌석정보, 토큰 정보 등을 관리하기 위하여 사용

### AWS EC2

- AWS EC2의 Ubuntu를 이용하여 Nginx를 이용한 무중단 배포
