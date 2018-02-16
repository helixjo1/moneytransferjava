CREATE TABLE IF NOT EXISTS accounts
(
  id        TEXT UNIQUE NOT NULL,
  balance   DECIMAL(19, 2) NOT NULL DEFAULT 0,
  CONSTRAINT pk_accounts_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
  username	TEXT UNIQUE NOT NULL,
  pass		TEXT NOT NULL,
  account_id	TEXT UNIQUE,
  role		TEXT NOT NULL,
  CONSTRAINT pk_users_id PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS transfers
(
  id	 BIGSERIAL PRIMARY KEY,
  originatorId		TEXT NOT NULL,
  destinationId		TEXT NOT NULL,
  amount   DECIMAL(19, 2) NOT NULL,
  result	TEXT NOT NULL,
  error		TEXT,
  message	TEXT NOT NULL,
  user_id		TEXT NOT NULL,
  timestamp timestamp default current_timestamp
);


INSERT INTO accounts
(id, balance)
VALUES
('1', 100.00),
('2', 120.00),
('3', 700.00),
('4', 1000.00);


INSERT INTO users
(username, pass, account_id, role)
VALUES
('user1', 'userpass', '1', 'USER'),
('user2', 'userpass', '2', 'USER' ),
('user3', 'userpass', '3', 'USER'),
('user4', 'userpass', '4', 'USER'),
('ADMINUSER', 'ADMIN', null, 'ADMIN');