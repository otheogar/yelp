package yelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;


import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class YelpJsonParser {

	public YelpJsonParser(){

	}
	
	private static void reviewRatings(String uidBid){
		HashMap<String, String> ratings = new HashMap<String, String>();
		try{
			
			InputStream fileStream = YelpJsonParser.class.getClassLoader().getResourceAsStream("yelp_academic_dataset_review.json");
			InputStreamReader reader = new InputStreamReader(fileStream);
			Gson myGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			JsonStreamParser jsonParser = new JsonStreamParser(reader);
			JsonElement element;
			String idRow;
			String idCol;
			int i=0;
			synchronized (jsonParser) {  
				while(jsonParser.hasNext()) {
					element = jsonParser.next();
					i++;
					if (i%1000==0)
						System.out.println(i);
					Review review = myGson.fromJson(element, Review.class);
					if(uidBid=="user"){
						idRow = review.getUser_id();
						idCol = review.getBusiness_id();
					}
					else{
						idRow = review.getBusiness_id();
						idCol = review.getUser_id();
					}
					
					if(ratings.containsKey(idRow)) {
						String old = ratings.get(idRow); 
						ratings.put(idRow, old+";"+idCol+","+review.getStars());
					}
					else {
						ratings.put(idRow, idCol+","+review.getStars());
					}
					
				}
			}

			System.out.println(i);
			String out;
			
			if(uidBid == "user")
				out="user_ratings.txt";
			else
				out = "business_ratings.txt";
			FileWriter writer = new FileWriter(out);
			Iterator<Entry<String,String>> iter = ratings.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> kv = iter.next();
				writer.write(kv.getKey() + "\t" + kv.getValue()+"\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ratings.clear();
		}
	}

	public static void main(String[] args){

		//read reviews  and output as userid....businesId, rating
		reviewRatings("user");
		reviewRatings("business");
	}
}

