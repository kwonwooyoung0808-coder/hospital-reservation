# 병원 진료 예약 관리 시스템

Java와 Spring Boot를 사용해 구현한 병원 진료 예약 관리 웹 애플리케이션입니다.
환자와 의사 정보를 관리하고, 진료 예약 등록·조회·상태 변경·삭제 기능을 제공합니다.

## 주요 기능

### 대시보드

- 전체 환자 수
- 전체 의사 수
- 전체 예약 수
- 오늘 예약 수
- 예약 일시 기준 최근 예약 5건

### 환자 관리

- 환자 등록, 조회, 수정, 삭제
- 이름 검색
- 전화번호 검색
- 이름 및 전화번호 필수 입력 검증

### 의사 관리

- 의사 등록, 조회, 수정, 삭제
- 이름 검색
- 진료과 필터
- 이름 및 진료과 필수 입력 검증

### 예약 관리

- 환자와 의사를 선택하여 진료 예약 등록
- 예약 목록 조회 및 삭제
- 환자명, 의사명, 예약 상태, 예약일 검색
- 과거 시간 예약 방지
- 동일 의사와 동일 시간의 중복 예약 방지
- 예약 상태 변경

예약 상태는 다음과 같습니다.

| 상태 | 의미 |
|---|---|
| `RESERVED` | 예약 |
| `COMPLETED` | 진료 완료 |
| `CANCELLED` | 취소 |

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Web | Spring MVC |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Template Engine | Thymeleaf |
| Validation | Jakarta Bean Validation |
| UI | HTML, CSS, Thymeleaf |
| Build | Gradle 9.5.1 |
| Test | JUnit 5, Mockito, AssertJ |

## 프로젝트 구조

```text
src
├─ main
│  ├─ java/com/example/hospital
│  │  ├─ controller     # 요청 처리, 화면 데이터 전달
│  │  ├─ dto            # 폼 입력값과 Validation
│  │  ├─ entity         # JPA 엔티티 및 예약 상태 Enum
│  │  ├─ exception      # 비즈니스 커스텀 예외
│  │  ├─ repository     # Spring Data JPA 데이터 접근
│  │  ├─ service        # 비즈니스 로직 및 트랜잭션
│  │  └─ HospitalReservationApplication.java
│  └─ resources
│     ├─ static/css     # 공통 반응형 스타일
│     ├─ templates      # Thymeleaf 화면 및 공통 레이아웃
│     └─ application.properties
└─ test
   └─ java/com/example/hospital/service
      ├─ PatientServiceTest.java
      ├─ DoctorServiceTest.java
      └─ ReservationServiceTest.java
```

요청은 다음 계층을 거쳐 처리됩니다.

```text
Browser → Controller → Service → Repository → MySQL
                       ↓
                 Business Rules
```

## 엔티티 관계

```text
Patient 1 ─── N Reservation N ─── 1 Doctor
```

- 하나의 환자는 여러 예약을 가질 수 있습니다.
- 하나의 의사는 여러 예약을 담당할 수 있습니다.
- 각 예약은 환자 한 명과 의사 한 명을 참조합니다.
- `Reservation`에서 `Patient`, `Doctor`를 `ManyToOne` 단방향 관계로 연결합니다.

## 핵심 설계

### 중복 예약 방지

동일 의사와 동일 시간의 중복 예약은 두 단계로 방지합니다.

1. 서비스에서 기존 예약 존재 여부를 확인합니다.
2. DB의 `(doctor_id, reservation_date_time)` 복합 UNIQUE 제약조건으로 동시 요청을 최종 차단합니다.

서비스 검사만 사용하면 동시에 들어온 요청이 모두 중복 검사를 통과할 수 있습니다. DB 제약조건을 함께 적용해 경쟁 상태에서도 데이터 무결성을 보호합니다.

### 예외 처리

다음 커스텀 예외를 사용합니다.

- `PatientNotFoundException`
- `DoctorNotFoundException`
- `ReservationNotFoundException`
- `DuplicateReservationException`

`@ControllerAdvice` 기반 전역 예외 처리로 stack trace 대신 사용자가 이해할 수 있는 오류 화면을 표시합니다.

### 입력값 검증

엔티티를 화면에 직접 바인딩하지 않고 `PatientForm`, `DoctorForm`, `ReservationForm` DTO를 사용합니다.

- `@NotBlank`: 환자명, 전화번호, 의사명, 진료과
- `@NotNull`: 환자, 의사, 예약 시간
- `@Future`: 현재 이후의 예약 시간

검증에 실패하면 입력값을 유지한 채 해당 폼에 오류 메시지를 표시합니다.

## 실행 방법

### 1. 사전 준비

- JDK 17
- MySQL 8.x
- `hospital_db` 데이터베이스

MySQL에서 데이터베이스를 생성합니다.

```sql
CREATE DATABASE hospital_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 2. 데이터베이스 접속 정보 설정

`src/main/resources/application.properties`의 접속 정보를 자신의 MySQL 환경에 맞게 변경합니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

실제 운영 환경이나 공개 저장소에서는 비밀번호를 소스에 직접 기록하지 말고 환경 변수 또는 별도 프로필로 관리하는 것을 권장합니다.

### 3. 애플리케이션 실행

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

macOS 또는 Linux:

```bash
./gradlew bootRun
```

IntelliJ IDEA에서는 `HospitalReservationApplication`의 `main()` 메서드를 실행해도 됩니다.

### 4. 접속

애플리케이션 실행 후 다음 주소에 접속합니다.

```text
http://localhost:8080/
```

8080 포트가 사용 중이라면 다른 포트로 실행할 수 있습니다.

```powershell
.\gradlew.bat bootRun --args=--server.port=8081
```

## 주요 URL

| URL | 기능 |
|---|---|
| `/` | 대시보드 |
| `/patients` | 환자 관리 |
| `/patients/new` | 환자 등록 |
| `/doctors` | 의사 관리 |
| `/doctors/new` | 의사 등록 |
| `/reservations` | 예약 관리 |
| `/reservations/new` | 예약 등록 |

## 테스트

전체 테스트 실행:

```powershell
.\gradlew.bat test
```

현재 다음 내용을 테스트합니다.

- PatientService 환자 저장
- PatientService 존재하지 않는 ID 조회
- DoctorService 의사 저장
- DoctorService 존재하지 않는 ID 조회
- ReservationService 정상 예약
- 동일 의사와 동일 시간 중복 예약 실패
- ReservationService 존재하지 않는 ID 조회
- Spring 애플리케이션 컨텍스트 로딩

현재 총 8개의 테스트가 통과합니다.

## UI 특징

- 공통 상단 네비게이션
- 대시보드 카드 UI
- 테이블, 버튼, 입력 폼 공통 스타일
- 예약 상태별 배지 색상
- 삭제 버튼 위험 색상 및 확인창
- 빈 목록 안내 메시지
- 모바일 화면 대응

## 향후 개선 아이디어

- Spring Security 기반 로그인과 관리자 권한
- 예약 목록 페이징과 정렬
- 의사별 진료 가능 시간 관리
- 예약 시간 구간 중첩 검사
- 예약 생성 시각과 수정 시각 기록
- 환자와 의사의 물리 삭제 대신 비활성화 처리
- Flyway를 이용한 DB 마이그레이션 관리
- Testcontainers 기반 MySQL 통합 테스트
- 운영 환경의 DB 접속 정보 외부화

## 라이선스

학습 및 포트폴리오 목적으로 작성된 프로젝트입니다.
