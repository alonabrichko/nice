DROP TABLE IF EXISTS RICH_PEOPLE;

CREATE TABLE RICH_PEOPLE (
 id INT PRIMARY KEY,
 first_name VARCHAR(250) NOT NULL,
 last_name VARCHAR(250) NOT NULL,
 fortune VARCHAR(250) DEFAULT NULL
);