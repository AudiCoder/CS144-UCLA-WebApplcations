DROP TABLE IF EXISTS ItemCategory;
DROP TABLE IF EXISTS BidderLocation;
DROP TABLE IF EXISTS Bids;
DROP TABLE IF EXISTS ItemLocation;
DROP TABLE IF EXISTS Items;
DROP TABLE IF EXISTS Seller;
DROP TABLE IF EXISTS Bidder;

CREATE TABLE Items ( 
Itemid VARCHAR(50) NOT NULL ,
Name VARCHAR(100) NOT NULL,
Currently DECIMAL(8,2),
Buyprice DECIMAL(8,2),
First_Bid DECIMAL(8,2),
No_of_Bids INTEGER,
Location_Text VARCHAR(100),
Country VARCHAR(50),
Started TIMESTAMP NOT NULL,
Ends TIMESTAMP NOT NULL,
SellerID VARCHAR(50),
Description VARCHAR(4000),
PRIMARY KEY (ItemID)
) charset latin1 collate latin1_general_cs charset latin1 collate latin1_general_cs ENGINE=INNODB;


CREATE table ItemCategory ( Itemid varchar(30) ,
Category varchar(50),
PRIMARY KEY (Itemid,Category),
FOREIGN KEY (Itemid) REFERENCES  Items (Itemid) ON DELETE CASCADE)ENGINE=INNODB DEFAULT CHARSET latin1 collate latin1_general_cs;


CREATE table Seller (Userid varchar(40) ,
rating varchar(10) 
)ENGINE=INNODB DEFAULT CHARSET latin1 collate latin1_general_cs;


CREATE table Bidder (Userid varchar(40),
rating varchar(10),
PRIMARY KEY (Userid))ENGINE=INNODB DEFAULT CHARSET latin1 collate latin1_general_cs;


CREATE TABLE BidderLocation (
Userid   VARCHAR(50),
Location VARCHAR(100),
Country  VARCHAR(50),
PRIMARY KEY (Userid),
FOREIGN KEY (Userid) REFERENCES  Bidder (Userid) ON DELETE CASCADE
) charset latin1 collate latin1_general_cs ENGINE=INNODB;

CREATE table Bids (Itemid varchar(30),
Userid varchar(40),
Time TIMESTAMP NOT NULL,
Amount decimal(8,2),
PRIMARY KEY (Time,Userid) ,
FOREIGN KEY (Itemid) REFERENCES Items (Itemid) ON DELETE CASCADE ,
FOREIGN KEY (Userid) REFERENCES Bidder (Userid) ON DELETE CASCADE)ENGINE=INNODB DEFAULT CHARSET latin1 collate latin1_general_cs;


CREATE table ItemLocation (Itemid varchar(30) ,
Latitude  Decimal(8,6) NOT NULL,
Longitude Decimal(9,6) NOT NULL,
PRIMARY KEY (Itemid) ,
FOREIGN KEY (Itemid) REFERENCES Items (Itemid) ON DELETE CASCADE)ENGINE=INNODB DEFAULT CHARSET latin1 collate latin1_general_cs;