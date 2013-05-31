package yelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;


import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class YelpJsonParser {

	public static String RECORDS_DELIMETER = ",";
	public static String BCOUNTFILE = "resources/business_reviewCounts.txt";
	public static String UCOUNTFILE = "resources/users_reviewCounts.txt";
	public static int cutoffUser=5;
	public static int cutoffBusiness=5;
	private static HashMap<String,Integer> bReviews = new HashMap<String,Integer>();
	private static HashMap<String,Integer> uReviews = new HashMap<String,Integer>();
	
	public YelpJsonParser(){

	}

	private static void count(){
		

			InputStream fileStream = YelpJsonParser.class.getClassLoader().getResourceAsStream("resources/review.json");
			InputStreamReader reader = new InputStreamReader(fileStream);
			Gson myGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			JsonStreamParser jsonParser = new JsonStreamParser(reader);
			JsonElement element;
			String idUser;
			String idBusiness;
			int i=0;

			synchronized (jsonParser) {  
				while(jsonParser.hasNext()) {
					element = jsonParser.next();
					i++;
					if (i%1000==0)
						System.out.println(i);
					Review review = myGson.fromJson(element, Review.class);
					idUser = review.getUser_id();
					idBusiness = review.getBusiness_id();
					if(uReviews.containsKey(idUser)){
						uReviews.put(idUser, uReviews.get(idUser)+1);
					}
					else{
						uReviews.put(idUser, 1);
					}
					if(bReviews.containsKey(idBusiness)){
						bReviews.put(idBusiness, bReviews.get(idBusiness)+1);
					}
					else{
						bReviews.put(idBusiness, 1);
					}
				}
			}
			
		
		
	}
	
	private static void reviewRatings(String uidBid){
		System.out.println(uReviews.size());
		System.out.println(bReviews.size());
		HashMap<String, String> ratings = new HashMap<String, String>();
		try{
			
			InputStream fileStream = YelpJsonParser.class.getClassLoader().getResourceAsStream("resources/review.json");
			InputStreamReader reader = new InputStreamReader(fileStream);
			Gson myGson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			JsonStreamParser jsonParser = new JsonStreamParser(reader);
			JsonElement element;
			String idRow;
			String idCol;
			int i=0;
			boolean freq;
			int nReviews = 0;
			synchronized (jsonParser) {  
				while(jsonParser.hasNext()) {
					element = jsonParser.next();
					freq=false;
					i++;
					if (i%1000==0)
						System.out.println(i);
					Review review = myGson.fromJson(element, Review.class);
					if(uidBid=="user"){
						idRow = review.getUser_id();
						idCol = review.getBusiness_id();
						if(!uReviews.containsKey(idRow)){
							System.out.println("no reviewcount for user " + idRow);
						}
						if (uReviews.get(idRow) >= cutoffUser && bReviews.get(idCol) >= cutoffBusiness) {
							freq = true;
						}
					}
					else{
						idRow = review.getBusiness_id();
						idCol = review.getUser_id();
						if (uReviews.get(idCol) >= cutoffUser && bReviews.get(idRow) >= cutoffBusiness) {
							freq = true;
						}
					}
					
					if(freq){
						nReviews++;
						if(ratings.containsKey(idRow)) {
							String old = ratings.get(idRow); 
							ratings.put(idRow, old+";"+idCol+","+review.getStars());
						}
						else {
							ratings.put(idRow, idCol+","+review.getStars());
						}
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
			System.out.println("nReviews: " + nReviews);
			System.out.println(uidBid + " : " + ratings.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ratings.clear();
		}
	}

	public static void main(String[] args){
		
		/*
		
		URL bFile = YelpJsonParser.class.getClassLoader().getResource(BCOUNTFILE);
		URL uFile = YelpJsonParser.class.getClassLoader().getResource(UCOUNTFILE);
		BufferedReader bCountIn = null;
		BufferedReader uCountIn = null;
		String line=null;
		//read files with business review counts and user review counts
		try {
		      bCountIn = new BufferedReader(new InputStreamReader(new FileInputStream(bFile.toString().replace("file:", ""))));
		} catch (FileNotFoundException e1) {
		      e1.printStackTrace();
		}

		try {
			while((line=bCountIn.readLine())!=null){
				String[] entries = line.split(RECORDS_DELIMETER);
				bReviews.put(entries[0],Integer.parseInt(entries[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
		      uCountIn = new BufferedReader(new InputStreamReader(new FileInputStream(uFile.toString().replace("file:", ""))));
		} catch (FileNotFoundException e1) {
		      e1.printStackTrace();
		}

		try {
			while((line=uCountIn.readLine())!=null){
				String[] entries = line.split(RECORDS_DELIMETER);
				uReviews.put(entries[0],Integer.parseInt(entries[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		*/
		
		if(args.length!=2){
			System.out.println("usage: YelpJsonParser cutoffUserNReviews cutoffBusinessNReviews");
			return;
		}
		
		cutoffUser = Integer.parseInt(args[0]);
		cutoffBusiness = Integer.parseInt(args[1]);
		count();
		System.out.println(uReviews.size());
		System.out.println(bReviews.size());
		
		//read reviews  and output as userid....businesId, rating
		reviewRatings("user");
		reviewRatings("business");
	}
}

