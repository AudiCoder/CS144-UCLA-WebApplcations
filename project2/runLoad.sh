#!/bin/bash


ant run-all

sort Items1.csv | uniq > Items.csv
sort ItemCategory1.csv | uniq > ItemCategory.csv
sort Seller1.csv | uniq > Seller.csv
sort Bidder1.csv | uniq > Bidder.csv
sort BidderLocation1.csv | uniq > BidderLocation.csv
sort Bids1.csv | uniq > Bids.csv
sort ItemLocation1.csv | uniq > ItemLocation.csv

mysql CS144 < drop.sql

mysql CS144 < create.sql

mysql CS144 < load.sql

rm -f Items1.csv;
rm -f ItemCategory1.csv 
rm -f Seller1.csv
rm -f Bidder1.csv
rm -f BidderLocation1.csv
rm -f Bids1.csv
rm -f ItemLocation1.csv 

rm -f Items.csv;
rm -f ItemCategory.csv 
rm -f Seller.csv
rm -f Bidder.csv
rm -f BidderLocation.csv
rm -f Bids.csv
rm -f ItemLocation.csv 