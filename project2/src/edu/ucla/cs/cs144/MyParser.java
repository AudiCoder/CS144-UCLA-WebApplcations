/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
    static DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
//    static DateFormat timestampFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        /**************************************************************/
        String itemId;
        String name;
        String category;
        String currently;
        
        String firstBid;
        String numberOfBids;
        String bidderUserId;
        String bidderRating;
        
        String time;
        String amount;
        String itemLocationValue;
        
        String itemCountry;
        String started;
        String ends;
        String sellerUserId;
        String sellerRating;
        String description;
        Element Categories[];

        Element root=doc.getDocumentElement();

        Element items[]=MyParser.getElementsByTagNameNR(root,"Item");

        System.out.println("Length "+items.length);
        for(int i=0;i<items.length;i++)
        {
        	String buyPrice = "\\N";
        	String itemLocationLatitude=null;
            String itemLocationLongitude=null;
            String bidderLocation = "\\N";
            String bidderCountry = "\\N";
            
        	itemId=items[i].getAttribute("ItemID");

        	//System.out.println(" Item ID : " +itemId); 

        	Element Name=MyParser.getElementByTagNameNR(items[i],"Name");
        	name=MyParser.getElementText(Name);

        	//System.out.println(" Item Name : " +name); 


        	Categories=MyParser.getElementsByTagNameNR(items[i],"Category");
        	for(int j=0;j<Categories.length;j++)
        	{
        		category=MyParser.getElementText(Categories[j]);
        		//System.out.println(" Category : " +category); 
        	}


        	Element Currently=MyParser.getElementByTagNameNR(items[i],"Currently");
        	currently= strip(MyParser.getElementText(Currently));                

        	//System.out.println(" Currently " +currently); 

        	Element BuyPrice=MyParser.getElementByTagNameNR(items[i],"Buy_Price");
        	if(BuyPrice!=null)
        	{		
        		buyPrice=strip(MyParser.getElementText(BuyPrice));
        		//System.out.println(" BuyPrice : " +buyPrice); 
        	}

        	Element First_Bid=MyParser.getElementByTagNameNR(items[i],"First_Bid");
        	firstBid=strip(MyParser.getElementText(First_Bid));

        	//System.out.println(" First Bid : " +firstBid);

        	Element Number_of_Bids=MyParser.getElementByTagNameNR(items[i],"Number_of_Bids");
        	numberOfBids=MyParser.getElementText(Number_of_Bids);

        	//System.out.println(" Number of Bids : " +numberOfBids);         

        	if(!(numberOfBids.equals("0")))
        	{
        		Element Bids=MyParser.getElementByTagNameNR(items[i],"Bids");

        		Element Bid[]=MyParser.getElementsByTagNameNR(Bids,"Bid");

        		for(int j=0;j<Bid.length;j++)
        		{
        			//System.out.println("Bidder #"+j);
        			Element Bidder=MyParser.getElementByTagNameNR(Bid[j],"Bidder");
        			bidderUserId=Bidder.getAttribute("UserID");
        			bidderRating=Bidder.getAttribute("Rating");

        			//System.out.println(" UserID : " +bidderUserId);         
        			//System.out.println(" Rating : " +bidderRating);         
        			
        			Element Bidder_Location=MyParser.getElementByTagNameNR(Bidder,"Location");
        			if(Bidder_Location!=null)
        			{
        				bidderLocation=MyParser.getElementText(Bidder_Location);
        				//System.out.println(" Bidder_Location : " +bidderLocation);         
        			}
        			
        			Element Bidder_Country=MyParser.getElementByTagNameNR(Bidder,"Country");
        			if(Bidder_Country !=null)
        			{
        				bidderCountry=MyParser.getElementText(Bidder_Country);
        				//System.out.println(" Bidder_Country : " +bidderCountry);
        			}    

        			Element Time=MyParser.getElementByTagNameNR(Bid[j],"Time");    
        			time=MyParser.getElementText(Time);
        			try{
        			    time  = timestampFormat.format(dateFormat.parse(time));
        			}catch(Exception e){   			  
        				System.out.println("TIME PARSING ERROR");
        			}
        			//System.out.println(" Time Content : " +time);         

        			Element Amount=MyParser.getElementByTagNameNR(Bid[j],"Amount");    
        			amount=strip(MyParser.getElementText(Amount));

        			//System.out.println(" Amount  : " +amount);    
        			
        			insertIntoBidsTable(itemId, bidderUserId, time, amount);	
        			insertIntoBidderTable(bidderUserId, bidderRating);
        			insertIntoBidderLocationTable(bidderUserId, bidderLocation, bidderCountry);
        		
        		}
        	}    

        	Element Item_Location=MyParser.getElementByTagNameNR(items[i],"Location");
        	itemLocationValue=MyParser.getElementText(Item_Location);  
        	//System.out.println(" Item_Location : " +itemLocationValue);         

        	if(!(Item_Location.getAttribute("Latitude").isEmpty()))
        	{
        		itemLocationLatitude=Item_Location.getAttribute("Latitude");
        		//System.out.println(" Item_Lat : " +itemLocationLatitude);         
        	}

        	if(!(Item_Location.getAttribute("Longitude").isEmpty()))
        	{
        		itemLocationLongitude=Item_Location.getAttribute("Longitude");  
        		//System.out.println(" Item_Long : " +itemLocationLongitude);   
        	}



        	/* End of Element -- Location ( ITEM ) -- */
        	Element Item_Country=MyParser.getElementByTagNameNR(items[i],"Country");
        	itemCountry=MyParser.getElementText(Item_Country);  

        	//System.out.println(" Item_Country : " +itemCountry);         

        	Element Item_Started=MyParser.getElementByTagNameNR(items[i],"Started");
        	started=MyParser.getElementText(Item_Started);  
        	try{
			    started  = timestampFormat.format(dateFormat.parse(started));
			}catch(Exception e){   			  
				System.out.println("STARTED PARSING ERROR");
			}

        	//System.out.println(" Item_Started : " +started);         

        	Element Item_Ends=MyParser.getElementByTagNameNR(items[i],"Ends");
        	ends=MyParser.getElementText(Item_Ends);  
        	try{
			    ends  = timestampFormat.format(dateFormat.parse(ends));
			}catch(Exception e){   			  
				System.out.println("DATE PARSING ERROR");
			}

        	//System.out.println(" Item_Ends : " +ends); 

        	Element Item_Seller=MyParser.getElementByTagNameNR(items[i],"Seller");
        	sellerUserId=Item_Seller.getAttribute("UserID");
        	sellerRating=Item_Seller.getAttribute("Rating");

        	//System.out.println(" Item_Seller_UserID : " +sellerUserId); 
        	//System.out.println(" Item_Seller_Rating : " +sellerRating); 


        	Element Item_Description=MyParser.getElementByTagNameNR(items[i],"Description");
        	description=MyParser.getElementText(Item_Description);  
        	//Trim description to 4000 characters
        	if(description.length() > 4000)
        		description = description.substring(0,4000);
        	//System.out.println(" Item_Description : " +description); 
        	
        	insertIntoItemTable(itemId, name, currently, buyPrice, firstBid, numberOfBids, itemLocationValue, itemCountry, started, ends, sellerUserId, description);
        	insertIntoItemCategoryTable(itemId, Categories);
        	insertIntoSellerTable(sellerUserId, sellerRating);
        	if(itemLocationLatitude != null || itemLocationLongitude!=null)
        		insertIntoItemLocationTable(itemId, itemLocationLatitude, itemLocationLongitude);
        }


        /**************************************************************/
        
    }
    
    public static void insertIntoItemTable(String itemId, String name, String currently,String buyPrice,String firstBid,String numberOfBids,String itemLocationValue, String itemCountry, String started, String ends, String sellerUserID, String description )
    {
    	try {
			FileWriter fileWriter   = new FileWriter("Items1.csv", true);
			String buy_price = "";
			if(buyPrice!=null) buy_price = buyPrice; 
			String record = itemId+columnSeparator+name+columnSeparator+currently+columnSeparator+buy_price+columnSeparator+firstBid+columnSeparator+numberOfBids+columnSeparator+itemLocationValue+columnSeparator+itemCountry+columnSeparator+started+columnSeparator+ends+columnSeparator+sellerUserID+columnSeparator+description+"\n";
			fileWriter.write(record);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {e.printStackTrace();}
    	
    }
    
    public static void insertIntoItemCategoryTable(String itemId, Element[] Categories) {
    	try {
			FileWriter fileWriter   = new FileWriter("ItemCategory1.csv", true);
			String record;
			for(int i =0; i< Categories.length;i++)
			{
				record  = itemId+columnSeparator+MyParser.getElementText(Categories[i])+"\n";
				fileWriter.write(record);
				fileWriter.flush();
			}
			fileWriter.close();
    	}catch (IOException e) {e.printStackTrace();}
			
	}
    
    public static void insertIntoBidsTable(String itemId, String bidderUserId,String time, String amount){
    	
    	try {
			FileWriter fileWriter   = new FileWriter("Bids1.csv", true);
			String record;
			record  = itemId+columnSeparator+bidderUserId+columnSeparator+time+columnSeparator+amount+"\n";
			fileWriter.write(record);
			fileWriter.flush();
			fileWriter.close();
    	}catch (IOException e) {e.printStackTrace();}		
    }
    
public static void insertIntoSellerTable(String sellerUserId, String rating){
    	
    	try {
			FileWriter fileWriter   = new FileWriter("Seller1.csv", true);
			String record;
			record  = sellerUserId+columnSeparator+rating+"\n";
			fileWriter.write(record);
			fileWriter.flush();
			fileWriter.close();
    	}catch (IOException e) {e.printStackTrace();}
    }
 public static void insertIntoBidderTable(String bidderUserId, String bidderRating){
    	
    	try {
			FileWriter fileWriter   = new FileWriter("Bidder1.csv", true);
			String record;
			record  = bidderUserId+columnSeparator+bidderRating+"\n";
			fileWriter.write(record);
			fileWriter.flush();
			fileWriter.close();
    	}catch (IOException e) {e.printStackTrace();}
    }
 
 public static void insertIntoBidderLocationTable(String bidderUserId, String bidderLocation, String bidderCountry){
 	
 	try {
			FileWriter fileWriter   = new FileWriter("BidderLocation1.csv", true);
			String record;
			String bidder_location=  "";
			if(bidderLocation!=null) bidder_location =  bidderLocation;
			
			String bidder_country = "";
			if(bidderCountry!=null) bidder_country = bidderCountry;
			record  = bidderUserId+columnSeparator+bidder_location+columnSeparator+bidder_country+"\n";
			fileWriter.write(record);
			fileWriter.flush();
			fileWriter.close();
 	}catch (IOException e) {e.printStackTrace();}
 }
 
 public static void insertIntoItemLocationTable(String itemId, String itemLocationLatitude, String itemLocationLongitude){
	 	
	 if(itemLocationLatitude!=null && itemLocationLongitude!=null){
	 	try {
				FileWriter fileWriter   = new FileWriter("ItemLocation1.csv", true);
				String record;
				String lat = "", lon =  "";
				if(itemLocationLatitude!=null) lat =  itemLocationLatitude;
				if(itemLocationLongitude!=null) lon =  itemLocationLongitude;
				record  = itemId+columnSeparator+lat+columnSeparator+lon+"\n";
				fileWriter.write(record);
				fileWriter.flush();
				fileWriter.close();
	 	}catch (IOException e) {e.printStackTrace();}
	 }
	 }
 
 
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
