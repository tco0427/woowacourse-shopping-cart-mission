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

## 상품 목록 조회

- HTTP Method : **GET**
- URI : `/api/products`
- RequestBody : NONE
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  [
    {
        "id": 1,
        "name": "Apple Watch Series 5",
        "price": 120,
        "stockQuantity": 0,
        "thumbnailImageDto": {
            "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
            "alt": "Apple Watch Series 5"
        }
    },
    {
        "id": 2,
        "name": "Apple Watch Nike",
        "price": 400,
        "stockQuantity": 0,
        "thumbnailImageDto": {
            "url": "https://develoger.kr/wp-content/uploads/apple-watch-nike.jpg",
            "alt": "Apple Watch Nike"
        }
    }
  ]
  ```

## 상품 추가

- HTTP Method : **POST**
- URI : `/api/products`
- RequestBody
  ```json
  {
    "name" : "맥북",
    "price" : 1000,
    "stockQuantity" : 10,
    "thumbnailImageDto" : {
      "url" : "url",
      "alt" : "이미지입니다"
    }
  }
  ```
- ResponseHeader
  - status code : `200 Ok`
  - Location: `/api/products/1`
- ResponseBody
  ```json
  {
    "id" : 1,
    "name" : "맥북",
    "price" : 1000,
    "stockQuantity" : 10,
    "thumbnailImageDto" : {
      "url" : "url",
      "alt" : "이미지입니다"
    }
  }
  ```

## 상품 단건 조회

- HTTP Method : **GET**
- URI : `/api/products/{productId}`
- RequestBody : NONE
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  {
      "id": 1,
      "name": "Apple Watch Series 5",
      "price": 120,
      "stockQuantity": 0,
      "thumbnailImageDto": {
          "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
          "alt": "Apple Watch Series 5"
      }
  }
  ```

## 상품 삭제

- HTTP Method : **DELETE**
- URI : `/api/products/{productId}`
- RequestBody : NONE
- ResponseHeader
  - status code : `204 No Content`
- ResponseBody : NONE

## 장바구니 목록 조회

- HTTP Method : **GET**
- URI : `/api/mycarts`
- RequestBody : NONE
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  [
    {
        "id": 1,
        "productId" : 1,
        "name": "Apple Watch Series 5",
        "price": 120,
        "quantity": 10,
        "thumbnailImageDto": {
            "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
            "alt": "Apple Watch Series 5"
        }
    },
    {
        "id": 2,
        "productId" : 2,
        "name": "Apple Watch Nike",
        "price": 400,
        "quantity": 1,
        "thumbnailImageDto": {
            "url": "https://develoger.kr/wp-content/uploads/apple-watch-nike.jpg",
            "alt": "Apple Watch Nike"
        }
    }
  ]
  ```

## 장바구니 단건 조회

- HTTP Method : **GET**
- URI : `/api/mycarts/{cartItemId}`
- RequestBody : NONE
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  {
      "id": 13,
      "productId" : 20,
      "name": "Apple Watch Series 5",
      "price": 120,
      "quantity": 1,
      "thumbnailImageDto": {
          "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
          "alt": "Apple Watch Series 5"
      }
  }
  ```

## 장바구니 추가 (처음 담기)

- HTTP Method : **POST**
- URI : `/api/mycarts`
- RequestBody
  ```json
  {
    "productId" : 1,
    "quantity" : 1
  }
  ```
- ResponseHeader
  - status code : `201 Created`
  - Location: `/api/mycarts/1`
- ResponseBody
  ```json
  {
    "id" : 1,
    "productId" : 1,
    "name" : "맥북",
    "price" : 1000,
    "quantity" : 10,
    "thumbnailImageDto" : {
      "url" : "url",
      "alt" : "이미지입니다"
    }
  }
  ```

## 장바구니 개수 업데이트

- HTTP Method : **PATCH**
- URI : `/api/mycarts`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody
  ```json
  {
    "cartItemId" : 1,
    "quantity" : 2
  }
  ```
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody : NONE

## 장바구니 항목 삭제

- HTTP Method : **DELETE**
- URI : `/api/mycarts`
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- RequestBody
  ```json
  {
    "cartItemIds" : [1, 2, 3]
  }
  ```
- ResponseHeader
  - status code : `204 No Content`
- ResponseBody : NONE


## 주문 목록 조회

- HTTP Method : **GET**
- URI : `/api/myorders`
- RequestBody : NONE
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  [
    {
        "id": 1,
        "orderedProducts" : [
          {
            "productId" : 1,
            "name": "Apple Watch Series 5",
            "price": 120,
            "quantity": 10,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
              "alt": "Apple Watch Series 5"
            }
          },
          {
            "productId" : 2,
            "name": "Apple Watch Nike",
            "price": 400,
            "quantity": 1,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-nike.jpg",
              "alt": "Apple Watch Nike"
            }
          }],
        "id" : 2,
        "orderedProducts" : [
          {
            "productId" : 1,
            "name": "Apple Watch Series 5",
            "price": 120,
            "quantity": 10,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
              "alt": "Apple Watch Series 5"
            }
          },
          {
            "productId" : 2,
            "name": "Apple Watch Nike",
            "price": 400,
            "quantity": 1,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-nike.jpg",
              "alt": "Apple Watch Nike"
            }
          }]
    }
  ]
  ```

## 주문 추가하기

- HTTP Method : **POST**
- URI : `/api/myorders`
- RequestBody
  ```json
  {
    "cartItemIds" : [1, 2, 3]
  }
  ```
- ResponseHeader
  - status code : `201 Created`
  - Location: `/api/myorders/1`
- ResponseBody : NONE

## 주문 단건조회

- HTTP Method : **GET**
- URI : `/api/myorders/{orderId}`
- RequestBody : NONE
- RequestHeader <br>
  `Authorization : Bearer XXXXXXX`
- ResponseHeader
  - status code : `200 Ok`
- ResponseBody
  ```json
  [
    {
        "id": 1,
        "orderedProducts" : [
          {
            "productId" : 1,
            "name": "Apple Watch Series 5",
            "price": 120,
            "quantity": 10,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-1.jpg",
              "alt": "Apple Watch Series 5"
            }
          },
          {
            "productId" : 2,
            "name": "Apple Watch Nike",
            "price": 400,
            "quantity": 1,
            "thumbnailImageDto": {
              "url": "https://develoger.kr/wp-content/uploads/apple-watch-nike.jpg",
              "alt": "Apple Watch Nike"
            }
          }]
    }
  ]
  ```
  
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

## 장바구니 [400, Bad Request]
- ErrorCode는 `5000`번대를 사용한다.
  - `errorCode` : 5001, `message` : Already Exist
  - `errorCode` : 5002, `message` : Invalid Quantity

## 데이터가 없는 경우 [400, Bad Request]
- ErrorCode는 `6000`번대를 사용한다.
  - `errorCode` : 6001, `message` : Not Exist Product
  - `errorCode` : 6002, `message` : Not Exist CartItem
  - `errorCode` : 6003, `message` : Not Exist Order
  - `errorCode` : 6004, `message` : Not Exist Customer

## 주문 [400, Bad Request]
- ErrorCode는 `7000`번대를 사용한다.
  - `errorCode` : 7001, `message` : Out Of Stock
