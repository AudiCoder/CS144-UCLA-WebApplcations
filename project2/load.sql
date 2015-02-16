LOAD DATA LOCAL INFILE 'Items.csv' INTO TABLE Items FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'Bidder.csv' INTO TABLE Bidder FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'BidderLocation.csv' INTO TABLE BidderLocation FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'Seller.csv' INTO TABLE Seller FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'Bids.csv' INTO TABLE Bids FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'ItemCategory.csv' INTO TABLE ItemCategory FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
LOAD DATA LOCAL INFILE 'ItemLocation.csv' INTO TABLE ItemLocation FIELDS TERMINATED BY "|*|" lines terminated BY "\n";
