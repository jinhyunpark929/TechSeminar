# 알아두면 언젠가 쓸모 있을 GitHub Actions
<img width="10240" height="5760" alt="TechSeminar_main" src="https://github.com/user-attachments/assets/37b2aad5-7c14-453f-a68e-fe4b4a02af32" />

본 발표는 CI/CD 도구로서의 **Jenkins와 GitHub Actions 비교**, 그리고 **GitHub Actions 실습 사례 및 도입 전략**을 중심으로 진행되었습니다.

---

## 1. DevOps와 CI/CD의 중요성

- 현대 소프트웨어 개발에서는 **개발 → 테스트 → 빌드 → 배포 → 운영**의 전 과정을 자동화하는 것이 핵심 경쟁력임  
- **DORA Report(2024)**에 따르면, 단순히 배포 속도가 빠른 팀이 아니라 **꾸준한 개선을 실현하는 팀**이 높은 성과를 달성함

---

## 2. Jenkins vs GitHub Actions

### Jenkins
- **장점**: 유연성과 확장성, 방대한 플러그인 생태계  
- **적합 환경**: 대규모 프로젝트, 온프레미스/보안 요구가 강한 조직  

### GitHub Actions
- **장점**: GitHub와의 강력한 연동성, 빠른 실행 속도, 인프라 관리 불필요  
- **특징**: `.github/workflows`에 YAML 정의만으로 실행 가능  

👉 **결론**: Jenkins는 정교한 전문가용 도구, Actions는 가볍고 빠른 자동화 도입에 강점

---

## 3. GitHub Actions 실습 사례

### 3.1 Docker 이미지 빌드 및 DockerHub 푸시

- Spring Boot 애플리케이션 → Docker 이미지 빌드 → DockerHub 푸시  
- Slack 연동으로 성공/실패 알림  

```yaml
name: CI - Build & Push Docker
on:
  push:
    branches: [ main ]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: temurin
          java-version: 17
      - name: Build with Gradle
        run: ./gradlew build
      - name: Build Docker image
        run: docker build -t ${{ secrets.DOCKER_USER }}/spotlink:latest .
      - name: Push Docker image
        run: |
          echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u ${{ secrets.DOCKER_USER }} --password-stdin
          docker push ${{ secrets.DOCKER_USER }}/spotlink:latest
```

### 3.2 IaC 기반 S3 정적 웹사이트 배포

- Terraform으로 S3 버킷 정의 및 정적 호스팅 설정
- GitHub Actions에서 workflow_dispatch로 수동 트리거 가능
- terraform fmt → validate → plan → apply 전 과정 자동화

```yaml
name: Deploy Static Website
on:
  workflow_dispatch:
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up Terraform
        uses: hashicorp/setup-terraform@v3
        with:
          terraform_version: 1.6.6
      - name: Terraform Init
        run: terraform init
      - name: Terraform Validate
        run: terraform validate
      - name: Terraform Plan
        run: terraform plan
      - name: Terraform Apply
        run: terraform apply -auto-approve
```

---

## 4. 점진적 도입 전략

### 혼합 도입
- Jenkins는 유지
- GitHub Actions는 Webhook/알림 등 일부 영역 활용

### 저위험 영역부터 시작
- Lint, 포맷터, PR 템플릿, README 자동화 등

### IaC 워크플로우 이전
- Terraform/Pulumi 등 인프라 코드를 Actions로 실행
- 운영 단순화 및 관리 효율성 확보

---

## 5. 워크플로우 구성 시 고려 사항

- YAML 문법 및 Job 간 의존성(needs:) 관리
- upload-artifact / download-artifact를 통한 파일 공유
- 액션 버전 불일치 문제 → Fixed Version 사용 권장
- Slack Webhook + Repository Secrets 기반 알림 구성

---

## 결론 및 인사이트

GitHub Actions는 단순 CI/CD 도구를 넘어 DevOps 자동화 플랫폼으로서 가치가 있음

소규모 팀도 **"코드에 집중하고, 인프라 관리 부담을 최소화"**할 수 있음

중요한 것은 도구 자체가 아니라, 지속적인 개선과 팀 문화라는 점을 다시 확인할 수 있었음

---

## 참고 자료
- [2024 DORA Report](https://dora.dev/research/2024/dora-report/)  
- [GitHub Actions 공식 문서](https://docs.github.com/ko/actions)  
