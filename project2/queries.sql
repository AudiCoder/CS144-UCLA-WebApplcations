select count(*) from ( select Userid from Bidder UNION select Userid from Seller) as FinalUsers;

select count(*) from Items where Location_Text='New York';

select count(*) from (select Itemid from ItemCategory group by Itemid having count(Category)=4 ) as newtable;

select B.ItemID from Bids B,Items I where B.Itemid = I.Itemid and I.No_of_Bids <> 0 and I.Ends >= '2001-12-20 00:00:01' and B.Amount=(select max(Amount) from Bids);

select count(Userid) from Seller where Rating > 1000;

select count(*) from Bidder as B,Seller as S where B.Userid=S.Userid;

select count(Distinct I.Category) from Bids as B,ItemCategory as I where B.Itemid=I.Itemid and B.Amount > 100;
