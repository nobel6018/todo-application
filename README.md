# Todo Application

# 소개

- Todo 어플리케이션 입니다
- 기능: Todo 조회하기 (내용, 생성날짜, 상태, pagination), 만들기, 선후관계 설정하기
- `Spring Boot`로 API 서버를 작성하였고 `React`로 Front를 작성하였습니다
- 서버에 테스트 코드를 작성하였습니다

# 프로젝트 실행 방법

### 1. API 서버 실행 방법

```bash
$ git clone https://github.com/nobel6018/todo-application.git
$ cd todo-application/backend
$ ./gradlew clean test build
$ java -jar -Dspring.profiles.active=local build/libs/todo-0.0.1-SNAPSHOT.jar
```

- 서버는 h2 In-Memory로 실행합니다
- 서버 모드 (TCP 모드)로 설정하는 방법은 [노션 링크](https://fierce-hydrangea-cb7.notion.site/H2-d7baf9facbed43b8a6de9ac7934d0eb6) 를
  확인해주세요

### 2. Frontend (React) 실행방법

```bash
$ cd todo-application/frontend
$ yarn install
$ yarn start
```

# 실행화면

![screen1](/images/screen1.png)
![screen2](/images/screen2.png)
![screen3](/images/screen3.png)


