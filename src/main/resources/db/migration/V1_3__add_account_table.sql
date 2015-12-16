CREATE TABLE ACCOUNTS (
	ID BIGSERIAL PRIMARY KEY,
	ACCOUNT_ID VARCHAR(255) NOT NULL,
	ACCOUNT_SECRET VARCHAR(255),
	RESOURCE_IDS VARCHAR(255),
	SCOPES VARCHAR(255),
	GRANT_TYPES VARCHAR(255),
	REDIRECT_URIS VARCHAR(255),
	AUTHORITIES VARCHAR(255)
);