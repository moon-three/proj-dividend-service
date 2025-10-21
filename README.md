# Dividend Service
해외 주식의 배당금 데이터를 스크래핑하고 DB에 저장하는 Spring Boot 기반 배당금 관리 서비스

<br>

## 기술 스택
<table>
  <tr>
    <td><b>Framework</b></td>
    <td>Spring Boot</td>
  </tr> 
  <tr>
    <td><b>Language</b></td>
    <td>Java 21</td>
  </tr> 
  <tr>
    <td><b>Build Tool</b></td>
    <td>Gradle</td>
  </tr> 
  <tr>
    <td><b>ORM</b></td>
    <td>Spring Data JPA</td>
  </tr> 
  <tr>
    <td><b>Web Scraping</b></td>
    <td>Selenium</td>
  </tr> 
  <tr>
    <td><b>Cache</b></td>
    <td>Redis</td>
  </tr> 
  <tr>
    <td><b>Scheduler</b></td>
    <td>Spring Scheduler</td>
  </tr> 
  <tr>
    <td><b>Logging</b></td>
    <td>Logback</td>
  </tr> 
  <tr>
    <td><b>Etc</b></td>
    <td>Lombok</td>
  </tr> 
</table>

<br>

## 주요 기능
- **회사 & 배당금 등록 / 삭제**
  - Selenium으로 배당금 정보를 스크래핑 후 DB에 저장
- **회사 조회**
  - Trie 기반 자동완성 기능 제공
- **배당금 조회**
  - Redis 캐시를 활용하여 조회 성능 향상
- **배당금 데이터 스케줄링**
  - Spring Scheduler로 주기적인 데이터 갱신
- **회원가입 / 로그인**
  - JWT 기반 인증 및 권한 관리 구현
