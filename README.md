## 🥕 CodeTerian
|분류|내용|
|----|----|
|주제| 대규모 트래픽 처리 기반 티켓 예매 서비스|
|팀원 구성| 👑 [김종규](https://github.com/Kim-Jong-Gyu) [이유빈](https://github.com/asd42270) [백지연](https://github.com/rkoji)|
|개발 기간| 2024.09.24 ~ 2024.10.25|
|API 명세서| [🔗 API 명세서](https://teamsparta.notion.site/API-V1-0-d21343fb5478443c96766b7db35852c0)|
|테이블 명세서|[🔗 테이블 명세서](https://teamsparta.notion.site/v1-31bd05b41d26476f9e32c9291c860d56)|

## 🎫 프로젝트 소개
대규모 트래픽을 안정적으로 처리하며, 사용자에게 정확하고 신뢰성 있는 티켓 예매 경험을 제공하는 MSA기반의 티켓 예매 서비스 입니다.

## 🥅 프로젝트 목표
- MSA 구조를 통해 대용량 트래픽을 안정적으로 처리
- Elasticsearch를 활용한 빠른 검색 기능을 제공
- 대기열 구현을 통해 사용자 요청을 효과적으로 관리하여 시스템 과부화를 방지하고, 예매 과정의 응답성을 향상

## 🚧 인프라 설계도

## 📝 ERD

## 🔧 주요 기능
#### 1. Elasticsearch를 통한 공연 검색
- 키워드 검색 :
    - 사용자는 검색창에 원하는 키워드를 입력하여 공연을 검색할 수 있습니다.
- 검색 결과 :
    - 검색 결과는 공연 제목, 공연 설명, 공연 위치에서 유사한 키워드를 기반으로 반환됩니다.
    - 제목 필드가 가장 높은 가중치를 부여받아, 제목과의 일치 여부가 검색 결과에 큰 영향을 미칩니다.
- 페이징 처리 :
    - 기본 페이지 사이즌 10으로 설정되어 있어, 한 페이지에 최대 10개의 검색 결과만 표시됩니다.
    - 사용자는 페이지를 전환하여 추가적인 검색 결과를 쉽게 탐색할 수 있습니다.
#### 2. 주문 ->  결제 시스템
- Kafka를 활용해 비동기 주문 시스템
- 외부 결제 API를 활용하여, 결제 테스트
#### 3.대기열 시스템
1. 대기 큐와 실행 큐를 구분하여 관리

- 실행 큐에 인원 제한을 둔다.
- 서버 부하를 줄이기 위해 제한된 수의 사용자만이 실제 예매 작업을 실행할 수 있도록 한다.

 2. 새로고침을 할 경우 대기열에서 삭제되고 가장 후순위로 재배치

- 사용자들이 대기열에서 불리한 상황을 방지하기 위해 **무분별한 새로고침을 제한**하고, **공정한 대기 시스템**을 유지하도록 한다.

3. 대기열에 등록된 시간을 score로

- 동일한 시간에 접속한 사용자들이 있을 때 순서 문제를 해결하기 위해 대기 순서를 등록된 시간으로 관리한다.

4. WebSocket을 통해 실시간으로 대기 순서 정보 제공

- 대기 중인 사용자가 자신의 순서를 실시간으로 확인하여 대기 시간을 예측할 수 있게 함으로써 **사용자 만족도**를 높임.

## 💻 적용 기술
##### □ Kafka
> 대용량의 실시간 데이터 스트리밍과 분산 메시지 처리를 안정적이고 확장 가능하게 관리하며, 시스템 간의 비동기 통신과 이벤트 중심 아키텍처를 구현하기 위해 도입했습니다.
##### □  Redis
> 	인메모리 데이터베이스로 높은 성능을 보장하고, 다양한 자료구조와 라이브러리 등을 통해 캐싱, 분산 락 같이 다양한 분야에서 활용이 가능합니다.
##### □  Elasticsearch
> 대용량 데이터를 실시간으로 빠르게 검색하고, 로그 분석 및 검색 쿼리에 최적화된 검색 엔진으로, 공연 검색을 하는데 있어 효율적인 데이터 인덱싱과 고성능 검색을 지원합니다.
##### □  WebSocket
> HTTP 통신에서 발생하는 지속적인 요청/응답 비용을 줄이고, 사용자에게 실시간으로 대기열 순서 정보를 효율적으로 제공하기 위해 도입했습니다. 서버와 클라이언트 간의 지속적인 양방향 통신을 가능하게 하여, 대기열 상태 변화나 알림을 지연 없이 전달할 수 있습니다.

## 🛠️ 개발 환경
- **Backend**
  ![Java](https://img.shields.io/badge/java17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
  ![Spring Boot](https://img.shields.io/badge/spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white) 
  ![Spring Security](https://img.shields.io/badge/spring%20Security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white) 
  ![Spring JPA](https://img.shields.io/badge/spring%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) 
  ![JWT](https://img.shields.io/badge/JWT-000000.svg?style=for-the-badge&logo=jsonwebtokens&logoColor=white) 
  ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white)
  ![Elasticsearch](https://img.shields.io/badge/elasticsearch-005571.svg?style=for-the-badge&logo=elasticsearch&logoColor=white)
![Kafka](https://img.shields.io/badge/apachekafka-231F20.svg?style=for-the-badge&logo=apachekafka&logoColor=white)

- **DB**
![postgresql](https://img.shields.io/badge/postgresql-4169E1.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![redis](https://img.shields.io/badge/redis-FF4438.svg?style=for-the-badge&logo=redis&logoColor=white)

- **Architecture**
  ![Microservices Architecture](https://img.shields.io/badge/MSA-000000.svg?style=for-the-badge&logo=architecture&logoColor=white) 
  ![API Gateway](https://img.shields.io/badge/API%20Gateway-000000.svg?style=for-the-badge&logo=api-gateway&logoColor=white) 
  ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)

- **Tools**
  ![Github](https://img.shields.io/badge/Github-181717.svg?style=for-the-badge&logo=github&logoColor=white)
![intellij IDE](https://img.shields.io/badge/intellij%20IDE-181717.svg?style=for-the-badge&logo=intellijidea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37.svg?style=for-the-badge&logo=postman&logoColor=white)
![Notion](https://img.shields.io/badge/notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white)

- **Monitoring**
![grafana](https://img.shields.io/badge/grafana-F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)
![prometheus](https://img.shields.io/badge/prometheus-E6522C.svg?style=for-the-badge&logo=prometheus&logoColor=white)
- **Test**
![apachejmeter](https://img.shields.io/badge/apachejmeter-D22128.svg?style=for-the-badge&logo=apachejmeter&logoColor=white)
