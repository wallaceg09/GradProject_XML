create table Customer(Email varchar(14),Password varchar(5), intOfBids int, RegistrationDate DATE, Title varchar(4),FirstName varchar(15),MidInit varchar(10),LastName varchar(15),Suffix varchar(5),Street varchar(20),State varchar(20),City varchar(20),Country varchar(15),Zip varchar(6),primary key (Email));

create table GoldCustomer ( Email varchar(14), GUDate DATE, Discount int, Primary Key (Email), Foreign key (Email) references Customer ON DELETE CASCADE);

create table PlatinumCustomer ( Email varchar(14), PUDate DATE, URL varchar(30), URLViews int, Primary Key (Email), Foreign key (Email) references Customer ON DELETE CASCADE);

create table Item( itemnumber int, Email varchar(14), ExpectedPrice int, Weight int, Description varchar(10), Deadline date, Filename varchar(10), primary key (itemnumber,Email),  foreign key (Email) references customer ON DELETE CASCADE);

create table Keywords(itemnumber int, Email varchar(14), keyword varchar(10),  primary key(itemnumber,Email,keyword), foreign key (itemnumber,Email) references Item(itemnumber,Email) ON DELETE CASCADE);

create table Transaction(IDNumber int, Type varchar(20), Amount int, tdate DATE, Email varchar(14), Primary key (IDNumber),Foreign key (Email) references Customer ON DELETE CASCADE);

create table Thread (ThreadID int, Title varchar(10), primary key (ThreadID));

create table Message (ThreadID int, MessageID int, createTime TIMESTAMP, Text varchar(50), Email varchar(14),  Primary key (ThreadID,MessageID), Foreign key (Email) references Customer ON DELETE CASCADE, foreign key (ThreadID) references Thread ON DELETE CASCADE);

create table Trade(BEmail varchar(14),SEmail varchar(14),itemnumber int,IEmail varchar(14),Primary key(BEmail,SEmail,itemnumber,IEmail), Foreign key(SEmail) references customer(Email) ON DELETE CASCADE, Foreign key(BEmail) references customer(Email) ON DELETE CASCADE, foreign key(itemnumber,IEmail) references Item(itemnumber,Email) ON DELETE CASCADE); 

create table Bid(BEmail varchar(14), itemnumber int, IEmail varchar(14), BidAmount int, BidTime Timestamp, Primary key(BEmail,IEmail,itemnumber),foreign key(itemnumber,IEmail) references Item(itemnumber,Email) ON DELETE CASCADE,foreign key(BEmail) references Customer(Email) ON DELETE CASCADE); 

create table ItemTransaction(itemnumber int, Email varchar(14), IDNumber int,primary key(itemnumber, Email, IDNumber),foreign key (itemnumber,Email) references Item(itemnumber,Email) ON DELETE CASCADE, foreign key (IDNumber) references Transaction(IDNumber) ON DELETE CASCADE);

create table ItemReview ( REmail varchar(14), IEmail varchar(14), itemnumber int, Rating int NOT NULL, CHECK (Rating BETWEEN 1 AND 5 ), Primary key (REmail , IEmail , itemnumber) , Foreign key (REmail) references GoldCustomer ON DELETE CASCADE, foreign key(itemnumber,IEmail) references Item(itemnumber,Email) ON DELETE CASCADE );

create table CustomerReview ( REmail varchar(14), CEmail varchar(14), Critique varchar(10),CHECK (Critique = 'poor' OR Critique = 'fair' OR  Critique ='good' OR  Critique ='excellent' ),Primary key (REmail , CEmail),  Foreign key (REmail) references PlatinumCustomer(Email) ON DELETE CASCADE, Foreign key (CEmail) references Customer(Email) ON DELETE CASCADE);

create table Moderator ( ThreadID int, Email varchar(14),Primary Key (ThreadID), Foreign Key (ThreadID) references Thread ON DELETE CASCADE, Foreign Key (Email) references PlatinumCustomer ON DELETE CASCADE);

create table MessageRating ( ThreadID int, MessageID int,Email varchar(14), Rating varchar(7), CHECK (Rating = 'like' OR Rating ='dislike'), Primary key( ThreadID, MessageID, Email), Foreign key ( ThreadID, MessageID) references Message ON DELETE CASCADE, Foreign key (Email) references Customer ON DELETE CASCADE);

create table Reply (OThreadID int, OMessageID int, RThreadID int, RMessageID int, Primary key( RThreadID, RMessageID), Foreign key ( OThreadID, OMessageID) references Message(ThreadID,MessageID) ON DELETE CASCADE, Foreign key ( RThreadID, RMessageID) references Message(ThreadID,MessageID) ON DELETE CASCADE);