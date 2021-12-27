create table "USER" (USER_ID varchar(10) NOT NULL, NAME varchar(20), ACTIVE bool, primary key(USER_ID));
create table USER_TRANSACTION (TRANS_ID varchar(40) NOT NULL , USER_ID varchar(10) , AMOUNT NUMERIC(10), TYPE VARCHAR(10), TRANSACTION_DATE TIMESTAMP  , STATUS varchar(10), foreign key(USER_ID) references USER(USER_ID), primary key(TRANS_ID)) ;
create table WALLET (WALLET_ID varchar(40) NOT NULL, USER_ID varchar(10), BALANCE NUMERIC(10), ACTIVE bool, foreign key(USER_ID) references USER(USER_ID),primary key(WALLET_ID));

insert into "USER" values('jay123', 'Jay Patel', TRUE);
insert into "USER" values('jasmin123', 'Jasmin Patel', FALSE);

insert into WALLET values('wa01212sdwd', 'jay123', 3000, TRUE);
insert into WALLET values('wa01212sdwk', 'jasmin123', 3000, FALSE);


