-- 직급 테이블 기본 더미 데이터
INSERT INTO mushroom.org_pos (POS_NM, POS_DESC, CREATE_DT, CREATE_ID, UPDATE_DT, UPDATE_ID)
VALUES
  ('사원', '신입 또는 일반 사원', NOW(), 'system', NOW(), 'system'),
  ('대리', '주니어 매니저급', NOW(), 'system', NOW(), 'system'),
  ('과장', '중간 관리자', NOW(), 'system', NOW(), 'system'),
  ('차장', '수석 관리자', NOW(), 'system', NOW(), 'system'),
  ('부장', '부서 책임자', NOW(), 'system', NOW(), 'system');

--회원 테이블 테스트 데이터
INSERT INTO mushroom.org_usr (
  LOGIN_ID, USR_NM, PWD, PWD_DATE,
  LOCK_YN, STATUS,
  EMP_NO, EMAIL, POS_NO, EXTENSION_NO, JOB_DESC,
  PROFILE_BIO, JOIN_DATE,
  CREATE_DT, CREATE_ID, UPDATE_DT, UPDATE_ID
)
VALUES
  (
    'admin01', '관리자1', 'encrypted_password_01', '20240101',
    'N', 'A',
    'E001', 'admin01@example.com', 1, '1001', '시스템 운영 총괄',
    '버섯상사 최고 관리자', '20240101',
    NOW(), 'system', NOW(), 'system'
  ),
  (
    'user02', '홍길동', 'encrypted_password_02', '20240315',
    'N', 'A',
    'E002', 'hong@example.com', 2, '1023', '전자결재 개발 및 유지보수',
    '열정 넘치는 대리', '20240315',
    NOW(), 'system', NOW(), 'system'
  );
