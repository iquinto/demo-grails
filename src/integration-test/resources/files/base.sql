--CREATE SCHEMA public
CREATE TABLE BAND (
	id BIGSERIAL,
	name VARCHAR(255) NOT NULL,
	year_formed VARCHAR(4),
	year_dissolution VARCHAR(4),
	style VARCHAR(10) NOT NULL,
	origin VARCHAR(10) NOT NULL,
CONSTRAINT PK_BAND PRIMARY KEY(id));

INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(1,'Metallica',1981,NULL,'Heavy','US');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(2,'The Rolling Stones',1962,NULL,'Rock','England');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(3,'Free',1968,1973,'Rock','England');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(4,'Bad Company',1973,NULL,'Rock','England');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(5,'Ramones',1974,1996,'Punk','US');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(6,'Sau',1987,1999,'Rock','Catalonia');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(7,'Els Pets',1985,NULL,'Rock','Catalonia');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(8,'Mecano',1981,1992,'Pop','Spain');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(9,'Estopa',1999,NULL,'Rock','Catalonia');
INSERT INTO BAND (id, name, year_formed, year_dissolution, style, origin) VALUES(10,'El canto del loco',1994,2010,'Pop','Spain');