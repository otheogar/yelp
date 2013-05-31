package yelp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;


public class Preprocesser {
	
	public static void main(String[] args){
		
		if(args.length!=2){
			System.out.println("Usage: Preprocesser filename rename");
			return;
		}
		String file = args[0];
		String name = args[1];
		//String name = file.substring(Math.max(0,file.lastIndexOf(FileSystems.getDefault().getSeparator())), file.lastIndexOf("_"));
		InputStreamReader in = new InputStreamReader(Preprocesser.class.getClassLoader().getResourceAsStream(file));
		BufferedReader br = new BufferedReader(in);
		String line;
		int i=0;
		int freq=0;
		int listLengths=0;
		
		try{
			FileWriter trainAll = new FileWriter(name+"_trainFreqAll.txt"); //put all the ratings of frequent users/businesses in the training set
			FileWriter trainAllButLast = new FileWriter(name+"_trainFreqAllButLast.txt"); ////put all but the last the ratings of frequent users/businesses in the training set
			FileWriter testRare = new FileWriter(name+"_testRare.txt"); //test including only ratings of rare users
			FileWriter testFreq = new FileWriter(name+"_testFreq.txt"); //test including last rating of freq users
			while((line=br.readLine())!=null){
				String[] s = line.split("\\t");
				String[] list = s[1].split(";");
				if (list.length>10){
					freq++;
					listLengths+=list.length;
					System.out.println(list.length);
					
					//put all but last rating in trainAllButLAst - put the last one in testFreq
					testFreq.write(s[0]+"\t"+list[list.length-1]+"\n");
					StringBuffer train_line_bf = new StringBuffer(s[0]+"\t"+list[0]);
					for(int j=1;j<list.length-1;j++)
						train_line_bf.append(";"+list[j]);
					train_line_bf.append("\n");
					trainAllButLast.write(train_line_bf.toString());
					
					//put all in trainAll
					trainAll.write(line+"\n");
					
				}
				else{
					i++;
				
					//put one in 10 users/businesses with only one rating in test set
					/*
					if (i%10==0) 
						test.write(line+"\n");
					else
						train.write(line+"\n");
						*/
					//put all of them in testRare
					testRare.write(line+"\n");
				}
				
			}
			System.out.println("rows: " + freq);
			System.out.println("Total reviews: " + listLengths);
			trainAll.close();
			trainAllButLast.close();
			testRare.close();
			testFreq.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
}
