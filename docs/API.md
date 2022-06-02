# API 명세

---

## 유저(Customer) 정보

- **Email**
    - 이메일 형식 예시 : email@email.com
    - 정규식 표현 : `^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$`
- **Password**
    - 비밀번호 형식 예시 : password1!
    - 비밀번호는 영문 + 숫자 + 특수문자로 이루어지며, 8 ~ 12자 사이여야한다.
    - 정규식 표현 : `^.*(?=^.{8,12}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$`
- **Username**
    - 유저이름은 한글, 영어 구분 없이 1 ~ 10자 여야 한다.

---

## 회원가입

- HTTP Method : **POST**
- URI : `/api/customers`
- RequestBody
  ```json
  {
    "email" : "test@gmail.com",
    "password" : "password0!",
    "username" : "디우"
  }
  ```
- ResponseHeader
  - status code : `201 Created`
  - Location: `/login`
- ResponseBody : NONE

## 로그인


- HTTP Method : **POST**
- URI : `/api/auth/login`
- RequestBody
  ```json
  {
    "email" : "test@gmail.com",
    "password" : "password0!"
  }
  ```
- ResponseHeader
  - status code : `200 Ok`
  - Location: `/`
- ResponseBody
  ```json
  {
    "accessToken" : "Bearer XXXXX"
  }
  ```

## 회원 정보 조회

- HTTP Method : **GET**
- URI : `/api/customers/me`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody : NONE
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  {
    "email" : "test@gamil.com",
    "username" : "디우"
  }
  ```

## 비밀번호 수정

- HTTP Method : **PATCH**
- URI : `/api/customers/me?target=password`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody
  ```json
  {
    "oldPassword" : "oldPassword0!",
    "newPassword" : "newPassword0!"
  }
  ```
- ResponseHeader
  - status code : `200 Ok`
  - Location: `/login`
- ResponseBody : NONE

### 회원 일반 정보 수정

- HTTP Method : **PATCH**
- URI : `/api/customers/me?target=generalInfo`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody
  ```json
  {
    "username" : "다오"
  }
  ```
- ResponseHeader
  - status code : `200 Ok`
  - Location: `/login`
- ResponseBody
  ```json
  {
    "email" : "test@gmail.com",
    "username" : "다오"
  }
  ```

## 회원 탈퇴

- HTTP Method : **DELETE**
- URI : `/api/customers/me`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody
  ```json
  {
    "password" : "newPassword0!"
  }
  ```
- ResponseHeader
  - status code : `204 No Content`
  - Location: `/`
- ResponseBody : NONE

---

## 에러 상황 시

### 에러 상태 코드는 헤더에, 예외 메시지는 body 에 실어서 보내준다.
```json
{
  "errorCode" : 1001,
  "message" : "No message abailable"
}
```

## 회원가입 [400, Bad Request]
- ErrorCode는 `1000`번대를 사용한다.
  - `이메일 중복` <br>
    `errorCode` : 1001, `message` : Duplicated Email

## 로그인 [400, Bad Request]
- ErrorCode는 `2000`번대를 사용한다.
  - 이메일 또는 패스워드 불일치 <br>
    `errorCode` : 2001, `message` : Login Fail
  - 로그인 시에는 이메일 형식만 확인한다. <br>
    `errorCode` : 4001, `message` : Invalid Email

## 회원정보 조회/수정/탈퇴 [401, Unauthorized]
- ErrorCode는 `3000`번대를 사용한다.
### 비밀번호 수정
- 기존 패스워드가 불일치 <br>
  `errorCode` : 3001, `message` : Incorrect Password
- 새 비밀번호의 형식이 맞지 않는 경우 <br>
  `errorCode` : 4002, `message` : Invalid Password
- 토큰 만료 혹은 유효하지 않은 경우(없는 경우)
  `errorCode` : 3002, `message` : Invalid Token

## 입력 형식 [400, Bad Request]
- ErrorCode는 `4000`번대를 사용한다.
  - Email 형식 불만족
    `errorCode` : 4001, `message` : Invalid Email
  - Password 형식 불만족
    `errorCode` : 4002, `message` : Invalid Password
  - Username 형식 불만족
    `errorCode` : 4003, `message` : Invalid Username
  
    

