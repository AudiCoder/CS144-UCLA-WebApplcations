Team Name - Patriots
Ankush Verma 904435669
Saransh Gupta 104433358

Relations:
Items :PrimaryKey (Itemid)
ItemCategory: PrimaryKey (Itemid,Category), ForeignKey(Itemid)
ItemLocation: PrimaryKey (Itemid), ForeignKey(Itemid)
Bidder: PrimaryKey (Userid)
Bids: PrimaryKey (Userid, time), ForeignKey(Userid, Itemid)
BidderLocation: PrimaryKey (Userid) ForeignKey(Userid)
Seller: PrimaryKey (Userid)

****Tables Like BidderLocation are decomposed to avoid NULL values in the table
****2 seperate table for Item and ItemLocation to avoid NULL
****Latitude,Longitude -> (Country) was the other dependency which we wanted to care of but to avoid decomposing table further so as to avoid Join operations we decided it to upload it in one table itself

Items:
Itemid -> (Name,Category,Description,Location_Text,Country,No_of_Bids,Currently,Started,Ends,First_Bid,Buyprice,Userid)

ItemCategory:
Itemid ->> Category

ItemLocation:
Itemid -> (Latitude,Longitude)

Bids:
(Userid,Time)->Itemid,Amount

Seller:
Userid->Rating

Bidder:
Userid->Rating
// Bidder and Seller can be added to one table but it may result in Null so decomposed the table

BidderLocation:
Userid->(Location,Country) 
//To avoid NULLS location and country are separated out

All tables are in BCNF and 4NF.