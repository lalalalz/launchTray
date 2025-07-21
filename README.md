Lunch Tray Practice Problem - Starter Code
==================================

Starter code for the Jetpack Compose Navigation practice problems

가능한 원인들
1. MySQL sql_mode 차이 (가장 가능성 높음)
로컬은 관대한 모드, 서버는 엄격한 모드일 수 있습니다.
sql-- 서버에서 sql_mode를 관대하게 변경 (임시)
SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION';
2. 다른 데이터베이스에 연결
sql-- 실제 연결된 곳 확인
SELECT DATABASE(), USER();

-- longstone 데이터베이스 존재 확인
USE longstone;
3. 네트워크 지연으로 인한 연결 끊김
yaml# application.yml에 타임아웃 설정 추가
datasource:
  url: ${MYSQL_HOST}/longstone?connectTimeout=60000&socketTimeout=60000
  hikari:
    connection-timeout: 60000
    maximum-pool-size: 10
4. MySQL 컨테이너 리소스 부족
bash# MySQL Pod 리소스 확인
kubectl describe pod mysql-pod-name
kubectl top pod mysql-pod-name
즉시 확인해볼 것들
1. 애플리케이션 로그에서 실제 URL 확인
yaml# 로깅 레벨 올려서 실제 연결 URL 확인
logging:
  level:
    com.zaxxer.hikari: DEBUG
    org.springframework.jdbc: DEBUG
2. MySQL 프로세스 리스트 실시간 확인
sql-- MySQL에서 실시간으로 연결 모니터링
SHOW PROCESSLIST;
3. 간단한 연결 테스트
bash# Pod 내에서 직접 MySQL 연결 테스트
kubectl exec -it your-app-pod -- mysql -h mysql-cluster.kwt-dev.svc.cluster.local -u app_user -p -e "SELECT DATABASE(), USER();"
어떤 결과가 나오는지 알려주세요! 특히 MySQL 버전과 sql_mode를 먼저 확인해봅시다.

