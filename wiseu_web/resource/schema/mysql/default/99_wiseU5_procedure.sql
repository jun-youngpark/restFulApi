DELIMITER $$
DROP PROCEDURE IF EXISTS copy_t_gen
$$
CREATE PROCEDURE copy_t_gen()
BEGIN
  DECLARE   var1 INT DEFAULT 1;
  
  TRUNCATE TABLE COPY_T;
   WHILE (var1 <= 100) DO
    INSERT INTO COPY_T(NO, CHR) VALUES(var1, var1);
    SELECT var1;
    SET var1 = var1 + 1;
   END WHILE;
END
$$
DELIMITER ;

-- 위 프로시저 생성 후 실행
call copy_t_gen();