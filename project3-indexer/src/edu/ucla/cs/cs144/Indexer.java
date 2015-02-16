package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

	 private IndexWriter indexWriter = null;

    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
           // Directory indexDir = FSDirectory.open(new File("index-directory"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    public void indexItem(String id, String name, String description, String categories) throws IOException,SQLException  {
        IndexWriter writer = getIndexWriter(false);

        Document doc = new Document();

        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new StringField("name", name, Field.Store.YES));
        doc.add(new TextField("Category", categories, Field.Store.YES));
        doc.add(new TextField("Description", description, Field.Store.YES));
        String search = name +  " " + categories + " " + description;
        doc.add(new TextField("Content", search, Field.Store.NO));
        writer.addDocument(doc);
    }

    public void rebuildIndexes() throws SQLException,IOException {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}


	 getIndexWriter(true);
	 Statement stmt = conn.createStatement();
	 Statement stmt1 = conn.createStatement();
		String query="SELECT Items.Itemid, Items.Name, Items.Description from Items ";;
 		 ResultSet rs = stmt.executeQuery(query);
 		 String category="";
 		String itemid="";
 		String query1="";
 	    String description;
 		PreparedStatement pt=conn.prepareStatement("SELECT category from ItemCategory where itemid=?");

    	          while (rs.next()) {

    	     itemid=   	   rs.getString("Itemid");
    	     category="";
        //	 System.out.println(itemid);
        	 pt.setString(1, itemid);
             ResultSet rs1=pt.executeQuery();
     		while (rs1.next()) {

     			category+=rs1.getString("Category")+" ";
     		}
        	// String string= "rs.Itemid+rs.Name+rs.Description+rs.Category";
        	 indexItem(rs.getString("Itemid"),rs.getString("Name"),rs.getString("Description"),category);
         }
	 closeIndexWriter();
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
//   System.out.println("I am Done");
    }


    public static void main(String args[]) {
        Indexer idx = new Indexer();
		try{
        idx.rebuildIndexes();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
    }
      /*
        int i=0;
        try {
        	//idx.rebuildIndexes();
			AuctionSearch as=new AuctionSearch();
			String query = "superman";
			SearchResult[] basicResults = as.basicSearch(query, 0, 20000);
			System.out.println("Basic Seacrh Query: " + query);
			System.out.println("Received " + basicResults.length + " results");
			for(SearchResult result : basicResults) {
				i++;
				System.out.println(result.getItemId() + " : " + result.getName());
			}
			System.out.println("out :"+i);
			query = "kitchenware";
			basicResults = as.basicSearch(query, 0, 20000);
			System.out.println("Basic Seacrh Query: " + query);
			System.out.println("Received " + basicResults.length + " results");
			for(SearchResult result : basicResults) {
				i++;
				//System.out.println(result.getItemId() + " : " + result.getName());
			}
			System.out.println("out :"+i);
			query = "camera";
			basicResults = as.basicSearch(query, 0, 20000);
			System.out.println("Basic Seacrh Query camera: " + query);
			System.out.println("Received " + basicResults.length + " results");
			for(SearchResult result : basicResults) {
				i++;
				//System.out.println(result.getItemId() + " : " + result.getName());
			}
			System.out.println("out :"+i);


			//
			SearchRegion region =
				    new SearchRegion(33.774, -118.63, 34.201, -117.38);
				SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
				System.out.println("Spatial Seacrh");
				System.out.println("Received " + spatialResults.length + " results");
				for(SearchResult result : spatialResults) {
					System.out.println(result.getItemId() + ": " + result.getName());
				}



			///XML Creation
			System.out.println(" Starting XML Creation");
			String itemid="1045773349";
			String s= as.getXMLDataForItemId(itemid);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
    }


