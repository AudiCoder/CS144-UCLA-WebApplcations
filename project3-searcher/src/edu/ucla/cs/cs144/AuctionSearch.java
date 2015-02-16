package edu.ucla.cs.cs144;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import javax.swing.text.html.parser.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.sql.PreparedStatement;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;






//import org.w3c.dom.Attr;
import org.w3c.dom.Attr;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) throws IOException ,ParseException {
		String itemID;
        String name;
        int cFlag=0;
        ArrayList<SearchResult> array= new ArrayList<SearchResult>();
       
		IndexSearcher searcher = null;
	    QueryParser parser = null;
	    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
	   // searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory"))));
        parser = new QueryParser("Content", new StandardAnalyzer());
        Query q1=null;
	
       // System.out.println("query" + query);
			q1 = parser.parse(query);
		 
        TopDocs topDocs=searcher.search(q1,numResultsToReturn+numResultsToSkip);
        ScoreDoc[] hits = topDocs.scoreDocs;
      //  System.out.println("q1 " + q1);
      //  System.out.println(hits.length);
        for (int i = 0; i < hits.length; i++) {
        	if(cFlag >=numResultsToSkip)
        	{
            Document doc = searcher.doc(hits[i].doc);
            array.add(new SearchResult(doc.get("id"),doc.get("name")));
        	}
        	cFlag++;
        }
		// TODO: Your code here!
        SearchResult[] sr=new SearchResult[array.size()];
        sr= array.toArray(sr);
		return sr;
	}
	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		if(numResultsToSkip > numResultsToReturn)
        {
            return null;
        }
		
	    HashMap<String,Boolean> match=new HashMap<String,Boolean>();
	    Connection conn = null;
	    
	    double lx=region.getLx();
	    double ly=region.getLy();
	    double rx=region.getRx();
	    double ry=region.getRy();
	    
	        try{
	        	
	        	//Spatial search   
		         try 
		        {
		               conn = DbManager.getConnection(true);
		        }
		        catch (SQLException ex) 
		        {
		        System.out.println(ex);
		        }
				int check=0;
		        Statement st=conn.createStatement();
		        ResultSet rs=st.executeQuery("SELECT Itemid FROM ItemGeo WHERE MBRCONTAINS( GEOMFROMTEXT(  'POLYGON(("+lx+" "+ly+"),("+lx+""+ry+"),("+rx+" "+ry+"),("+rx+" "+ly+"),("+lx+" "+ly+")))' ) , lat_long ) ");
		        while(rs.next())
		        {
					check++;
		                    match.put(rs.getString("Itemid"),true);
		            
		        }
		     //   System.out.println("executing start basic search, check :"+check);
		       
	            int cFlag=0;
	            ArrayList<SearchResult> array= new ArrayList<SearchResult>();
	       
	    		IndexSearcher searcher = null;
	    	    QueryParser parser = null;
				 searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
	    	   // searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory"))));
	            parser = new QueryParser("Content", new StandardAnalyzer());
	            Query q1 = parser.parse(query);
	            TopDocs topDocs=searcher.search(q1,Integer.MAX_VALUE);
	            ScoreDoc[] hits = topDocs.scoreDocs;
	            int i = 0;
	            
	           while ( i < hits.length) {
	               	   Document doc1 = searcher.doc(hits[i].doc);
	               //	  System.out.println(doc1.get("id"));
	               	   if(match.containsKey(doc1.get("id")))
	               	   {
	               		//  System.out.println("INNNN");
	               		  array.add(new SearchResult(doc1.get("id"),doc1.get("name")));
	                 }
	               i++;
	            }
	            SearchResult basic_search[]=new SearchResult[array.size()];
	            basic_search= array.toArray(basic_search);
	        
		        
		      
	        int final_c=0;
	        int number_to_return;
	        if(basic_search.length < numResultsToReturn)
	        {
	            number_to_return=basic_search.length;
	        }
	        else
	        {
	            number_to_return=numResultsToReturn;
	        }
	        
	        SearchResult[] f_ans=new SearchResult[number_to_return];
	     //   int i=numResultsToSkip;
	       // while(i < number_to_return )
	        int j=numResultsToSkip;
	     while( j < number_to_return )
	        {
	        	f_ans[final_c]=new SearchResult();
	            f_ans[final_c].setItemId(basic_search[j].getItemId());
	            f_ans[final_c].setName(basic_search[j].getName());
	            final_c++;
	          j++;
	      }
	        
	        return f_ans;
	    }
	    catch(Exception e){
	        System.out.println("Error is : "+e.getMessage());
	    }
	      return null;  
}


	static String convertDate(String dateString, String oldFormat, String newFormat) {


        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        String out = "";
        try {
            java.util.Date d = sdf.parse(dateString);
            sdf.applyPattern(newFormat);
            out = sdf.format(d);
        } catch (Exception e) {
            System.out.println("Could not format date");
        }

        return out;
    }
	
	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		StringWriter outStr=new StringWriter();
	try
	{
		Connection conn= null;
		conn = DbManager.getConnection(true);
        Statement stat = conn.createStatement();
        
        
        ResultSet r = stat.executeQuery("SELECT * FROM Items "
                                                + "WHERE Items.ItemID = " + itemId);
        
        
        r.first();
        
        
        if(r.getRow()!=0)
        {
        	 int nbid = Integer.parseInt(r.getString("No_of_bids"));
            String[] bidderid = new String[nbid];
            String[] brating = new String[nbid];
            String[] bidtime = new String[nbid];
            String[] bamount = new String[nbid];
            String[] bloc = new String[nbid];
            String[] bcountry = new String[nbid];
            String loctxt=r.getString("Location_Text");
            String strt=r.getString("Started");
            String endr=r.getString("Ends");
            String cntry=r.getString("Country");
            String descc = r.getString("Description");
            String sellr=r.getString("Sellerid");
            String buyprice= r.getString("Buyprice");
            
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.newDocument();
		
		
		//Document doc = docBuilder.newDocument(); Root Created
		org.w3c.dom.Element rootElement = doc.createElement("Item");
		rootElement.setAttribute("ItemID", itemId);
		doc.appendChild(rootElement);
		
		org.w3c.dom.Element e_name = doc.createElement("Name");
		//e_name.setAttribute("Name", r.getString("Name"));
		e_name.appendChild(doc.createTextNode(r.getString("Name")));
        rootElement.appendChild(e_name);
        
        Statement cstatement = conn.createStatement();
        ResultSet cresult = cstatement.executeQuery("SELECT ItemCategory.Category "
                                                      + "FROM Items,ItemCategory "
                                                      + "WHERE ItemCategory.Itemid = " + itemId + " "
                                                      + "AND ItemCategory.Itemid = Items.Itemid");

        while (cresult.next()) {
        	org.w3c.dom.Element c_element= doc.createElement("Category");
        	//c_element.setAttribute("Category", cresult.getString("Category"));
        	//e_name.appendChild(doc.createTextNode(r.getString("Name")));
        	 c_element.appendChild(doc.createTextNode(cresult.getString("Category")));
            rootElement.appendChild(c_element);
        	}

        cresult.close();
        cstatement.close();

        org.w3c.dom.Element current_element= doc.createElement("Currently");
    	//current_element.setAttribute("Currently", r.getString("Currently"));
		current_element.appendChild(doc.createTextNode("$" + r.getString("Currently")));
        rootElement.appendChild(current_element);
        
   //     String buyprice= r.getString("Buyprice");
    //    System.out.println("Anks"+ buyprice);
        if(buyprice!=null)
        {
        org.w3c.dom.Element buy_element= doc.createElement("Buy_Price");
    	//current_element.setAttribute("Currently", r.getString("Currently"));
		buy_element.appendChild(doc.createTextNode("$" + r.getString("Buyprice")));
        rootElement.appendChild(buy_element);
        }
        org.w3c.dom.Element f_bid= doc.createElement("First_Bid");
    	//f_bid.setAttribute("First_Bid", r.getString("First_Bid"));
		f_bid.appendChild(doc.createTextNode("$" + r.getString("First_Bid")));
        rootElement.appendChild(f_bid);
        
        org.w3c.dom.Element n_bid= doc.createElement("No_of_Bids");
    	//n_bid.setAttribute("Number_of_Bids", r.getString("First_Bid"));
		 n_bid.appendChild(doc.createTextNode(r.getString("No_of_Bids")));
        rootElement.appendChild(n_bid);
   
       
        
                
   
      
        
   
       // Statement stat1 = conn.createStatement();
        ResultSet rs6 = stat.executeQuery("SELECT * from Bids WHERE Itemid ="+ itemId);
        
        PreparedStatement pt3=conn.prepareStatement("SELECT rating from Bidder WHERE Userid =?");
        int i=0;
        while(rs6.next()){
            bidderid[i] = rs6.getString("Userid");
            bidtime[i] = rs6.getString("Time");
            bamount[i] = rs6.getString("Amount");
            pt3.setString(1, bidderid[i]);
          //  System.out.println("herloo" + bidderid[i]);
            
            ResultSet rs4 = pt3.executeQuery();
            while(rs4.next()){
                brating[i] = rs4.getString("rating");
              //  System.out.println("seller rating "+bidderid[i]+""+brating[i]);
            }
            
    //        System.out.println("bidder "+bidderid[i]);
            i++;
        }
        PreparedStatement pt1=conn.prepareStatement("SELECT * from BidderLocation where userid=?");
        for(int loop=0;loop<nbid;loop++){
            pt1.setString(1, bidderid[loop]);
            ResultSet bidloc=pt1.executeQuery();
           
            while(bidloc.next())
            {
                bloc[loop] = bidloc.getString("Location");
                if(bidloc.wasNull()){
                    bloc[loop]   = null;    
                    //bcountry[loop]   = null;    
                }
                bcountry[loop] = bidloc.getString("Country");  
                if(bidloc.wasNull()){
                    //bloc[loop]   = null;    
                    bcountry[loop]   = null;    
                }
                //category+=rs_category.getString("category")+" ";
            }
            
        }
        
        
        //addDoc( itemid, name, category, descr);
        //c++;
        
        org.w3c.dom.Element bids = doc.createElement("Bids");
		rootElement.appendChild(bids);
		//String tem[]={"1","2"};
	//	System.out.println("tetsin" + brating.length);
		//int bidscount=10;
		for (int f = 0; f < brating.length; f++) {
			
			org.w3c.dom.Element bid1 = doc.createElement("Bid");
			bids.appendChild(bid1);
			
			org.w3c.dom.Element bidder = doc.createElement("Bidder");
			Attr brat = doc.createAttribute("Rating");
			brat.setValue(brating[f]);// set value
			Attr buid = doc.createAttribute("UserID");
			buid.setValue(bidderid[f]);// set value
			bidder.setAttributeNode(brat);
			bidder.setAttributeNode(buid);
			
			if(bloc[f]!=null)
			{
				org.w3c.dom.Element bloc1 = doc.createElement("Location");
			bloc1.appendChild(doc.createTextNode(bloc[f]));
			bidder.appendChild(bloc1);
			}
			if(bcountry[f]!=null)
			{
				org.w3c.dom.Element bcon = doc.createElement("Country");
			bcon.appendChild(doc.createTextNode(bcountry[f]));
			bidder.appendChild(bcon);
			}
			//bid.appendChild(doc.createTextNode(state));
			bid1.appendChild(bidder);
			
			org.w3c.dom.Element time = doc.createElement("Time");
			time.appendChild(doc.createTextNode(convertDate(bidtime[f], "yyyy-MM-dd HH:mm:ss", "MMM-dd-yy HH:mm:ss")));
		//	time.appendChild(doc.createTextNode(bidtime[f]));
			bid1.appendChild(time);
			

			org.w3c.dom.Element amount = doc.createElement("Amount");
			amount.appendChild(doc.createTextNode("$"+bamount[f]));
			bid1.appendChild(amount);
			
		}
		
		rs6.close();
		
		
		//Connection conn1= null;
		//conn1 = DbManager.getConnection(true);
		  String latitude="",longitude="";
	        java.sql.PreparedStatement pt4=conn.prepareStatement("SELECT Latitude,Longitude from ItemLocation WHERE Itemid =?");
	       
	        pt4.setString(1, itemId);
	        
	        ResultSet rs5 = pt4.executeQuery();
	        rs5.first();
	        
	        if(rs5.getRow()!=0){
	            latitude = rs5.getString("latitude");
	            longitude = rs5.getString("longitude");
	        }
	        else{
	            latitude = null;
	            longitude = null;
	        } 
	        
	        org.w3c.dom.Element i_loc= doc.createElement("Location");
	        if(latitude!=null)
	        {
	    	i_loc.setAttribute("Latitude", latitude);
	    	i_loc.setAttribute("Longitude", longitude);
	        }
			 i_loc.appendChild(doc.createTextNode(loctxt));
	        rootElement.appendChild(i_loc);
		
		org.w3c.dom.Element country= doc.createElement("Country");
        //	end.setAttribute("Number_of_Bids", r.getString("First_Bid"));
    		 country.appendChild(doc.createTextNode(cntry));
            rootElement.appendChild(country);

        
        rs5.close();
      
   ////////
        org.w3c.dom.Element start= doc.createElement("Started");
    //	start.setAttribute("Started", r.getString("Started"));
    //	 start.appendChild(doc.createTextNode(r.getString("Started")));
    	 start.appendChild(doc.createTextNode(convertDate(strt, "yyyy-MM-dd HH:mm:ss", "MMM-dd-yy HH:mm:ss")));
		// element_name.appendChild(doc.createTextNode(r.getString("Name")));
        rootElement.appendChild(start);
        
        org.w3c.dom.Element end= doc.createElement("Ends");
    //	end.setAttribute("Number_of_Bids", r.getString("First_Bid"));
		// end.appendChild(doc.createTextNode(r.getString("Ends")));
		 end.appendChild(doc.createTextNode(convertDate(endr, "yyyy-MM-dd HH:mm:ss", "MMM-dd-yy HH:mm:ss")));
        rootElement.appendChild(end);
        
        
            
            Statement sellerstatement = conn.createStatement();
         //   System.out.println(r.getString("Sellerid"));
            
          //  String query1="SELECT rating  FROM Seller,Items WHERE Items.Sellerid = '"+ sellr + "' " + "AND Items.Sellerid = Seller.Userid";
            
            String query1="SELECT rating  FROM Seller WHERE Userid= '" + sellr + "'" ;
           
        //    System.out.println(query1);
            ResultSet sresult = sellerstatement.executeQuery(query1);
         //   sresult.first();
           // ResultSet cresult = cstatement.executeQuery("SELECT ItemCategory.Category "
           //         + "FROM Items,ItemCategory "
            //        + "WHERE ItemCategory.Itemid = " + itemId + " "
            //        + "AND ItemCategory.Itemid = Items.Itemid");
            
          //  System.out.println("Reached 1");
            while(sresult.next())
            {
        //    System.out.println(sresult.getString("rating"));
            org.w3c.dom.Element Seller= doc.createElement("Seller");
            	Seller.setAttribute("Rating", sresult.getString("rating"));
            	Seller.setAttribute("UserID", sellr);
            	
           // 	System.out.println("Reached 2");
        		// desc.appendChild(doc.createTextNode(r.getString("Description")));
                rootElement.appendChild(Seller);
          
            }   
                sresult.close();
                sellerstatement.close();
                

            org.w3c.dom.Element desc= doc.createElement("Description");
            //	end.setAttribute("Number_of_Bids", r.getString("First_Bid"));
        		 desc.appendChild(doc.createTextNode(descc));
                rootElement.appendChild(desc);
                
                
                
          
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
        		Transformer transformer = transformerFactory.newTransformer();
        		
        	
        		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
     		
        	 transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        		DOMSource source = new DOMSource(doc);
        		StreamResult result = new StreamResult(outStr);
        
        		// Output to console for testing
        		// StreamResult result = new StreamResult(System.out);
         
        		transformer.transform(source, result);
         
        	//	System.out.println("File saved!");
                
               
                
        }
       
	}
	catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return outStr.toString();
	}
	
	public String echo(String message) {
		return message;
	}

}
