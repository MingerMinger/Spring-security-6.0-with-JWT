USE test;
CREATE TABLE my_users (
  id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  PASSWORD VARCHAR(100) NOT NULL,
  enabled TINYINT(1) NOT NULL DEFAULT '1',
  roles VARCHAR(200) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY username_UNIQUE (username)
);

INSERT INTO my_users VALUES('','user',123456,1,'ROLE_USER');
INSERT INTO my_users VALUES('','admin',123456,1,'ROLE_ADMIN');

SELECT * FROM my_users;