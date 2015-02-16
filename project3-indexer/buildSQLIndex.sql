DROP TABLE IF EXISTS ItemGeo;
CREATE TABLE ItemGeo 
(
        Itemid VARCHAR(150),
        lat_long POINT NOT NULL,
        primary key(itemid)
)ENGINE=MyISAM;

INSERT INTO ItemGeo(Itemid, lat_long)
SELECT Itemid, PointFromText(CONCAT('POINT(',ItemLocation.Latitude,' ',ItemLocation.Longitude,')'))
FROM ItemLocation;

ALTER table ItemGeo ADD SPATIAL INDEX(lat_long);


SELECT count(Itemid)
FROM ItemGeo
WHERE MBRCONTAINS( GEOMFROMTEXT(  'POLYGON((33.774 -118.63),(33.774 -117.38),(34.201 -117.38),(34.201 -118.63),(33.774 -118.63)))' ) , lat_long );

