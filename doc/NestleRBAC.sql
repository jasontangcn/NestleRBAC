CREATE DATABASE ACS;
GO

USE ACS;

CREATE TABLE ACCOUNT(
  id int NOT NULL IDENTITY(1,1),
  name varchar(18) NOT NULL,
  pwd varchar(18) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE PRIVILEGE(
  id int NOT NULL IDENTITY(1,1), 
  name varchar(50) NOT NULL UNIQUE,/*[Class-Name]:[Class-Method], in fact they are all the methods of the business classes */
  PRIMARY KEY(id)
);

CREATE TABLE ROLE(
  id int NOT NULL IDENTITY(1,1),
  name varchar(18) NOT NULL UNIQUE,
  PRIMARY KEY(id) 
);
GO
INSERT INTO ROLE(name) VALUES ('root');

CREATE TABLE ROLE_ROLE(
  id int NOT NULL IDENTITY(1,1),
  child_id int NOT NULL,
  parent_id int NOT NULL DEFAULT 1,
  PRIMARY KEY(id),  
  FOREIGN KEY(child_id) REFERENCES ROLE(id) ON DELETE CASCADE, /* If you create some roles and build the parent-clild relationship, do not rebuild the relationship, otherwise it may cause an infinite loop. */
  FOREIGN KEY(parent_id) REFERENCES ROLE(id) ON DELETE CASCADE
);

CREATE TABLE ROLE_PRIVILEGE(
  id int NOT NULL IDENTITY(1,1),
  role_id int NOT NULL,
  privilege_id int NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (role_id) REFERENCES ROLE(id) ON DELETE CASCADE, 
  FOREIGN KEY (privilege_id) REFERENCES PRIVILEGE(id) ON DELETE CASCADE/* Generately when the system is done privileges(functionalities) should be fixed(without adding or removing), so there is no restrictions: delete cascade. */ 
);

CREATE TABLE ACCOUNT_ROLE(
  id int NOT NULL IDENTITY(1,1),
  account_id int NOT NULL,
  role_id int NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (account_id) REFERENCES ACCOUNT(id) ON DELETE CASCADE, 
  FOREIGN KEY (role_id) REFERENCES ROLE(id) ON DELETE CASCADE
);

