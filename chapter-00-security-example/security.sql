-- JdbcTokenRepositoryImpl을 사용시 생성 테이블
CREATE TABLE persistent_logins (
	username varchar(64) NOT NULL,
	series varchar(64),
	token varchar(64) NOT NULL,
	last_used timestamp NOT NULL,
	CONSTRAINT persistent_logins_pk PRIMARY KEY(series)
)