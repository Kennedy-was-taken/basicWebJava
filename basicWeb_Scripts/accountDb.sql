USE master

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'accountDb')
DROP DATABASE accountDb

CREATE DATABASE accountDb

USE accountDb

--CREATING TABLES

	--USER TABLE
	CREATE TABLE users(
		user_id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
		username VARCHAR(50) NOT NULL,
		password VARCHAR(100) NOT NULL,
	);
	
	--ACCOUNT TABLE
	CREATE TABLE accounts(
		account_id INT NOT NULL IDENTITY(1,1),
		user_ref_id INT NOT NULL,
		PRIMARY KEY(account_id),
		FOREIGN KEY(user_ref_id) REFERENCES users(user_id) ON DELETE CASCADE
	);

	--ROLES TABLE
	CREATE TABLE account_role(
		role_id INT NOT NULL IDENTITY (1,1),
		role_name VARCHAR(13) NOT NULL,
		account_ref_id INT NOT NULL,
		PRIMARY KEY(role_id),
		FOREIGN KEY(account_ref_id) REFERENCES accounts(account_id) ON DELETE CASCADE
	);

	--REFRESH TOKEN
	CREATE TABLE refresh_token(
		token_id INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
		token VARCHAR(350) NOT NULL,
		account_ref_id INT NOT NULL,
		FOREIGN KEY(account_ref_id) REFERENCES accounts(account_id) ON DELETE CASCADE
	);
