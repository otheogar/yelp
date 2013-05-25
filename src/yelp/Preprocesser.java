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
		
		if(args.length!=1){
			System.out.println("Usage: Preprocesser filename");
			return;
		}
		String file = args[0];
		String name = file.substring(Math.max(0,file.lastIndexOf(FileSystems.getDefault().getSeparator())), file.lastIndexOf("_"));
		InputStreamReader in = new InputStreamReader(Preprocesser.class.getClassLoader().getResourceAsStream(file));
		BufferedReader br = new BufferedReader(in);
		String line;
		int i=0;
		
		try{
			FileWriter train = new FileWriter(name+"_train.txt");
			FileWriter test = new FileWriter(name+"_test.txt");
			while((line=br.readLine())!=null){
				String[] s = line.split("\\t");
				String[] list = s[1].split(";");
				if (list.length>1){
					//put all but last rating in train
					test.write(s[0]+"\t"+list[list.length-1]+"\n");
					StringBuffer train_line_bf = new StringBuffer(s[0]+"\t"+list[0]);
					for(int j=1;j<list.length-1;j++)
						train_line_bf.append(";"+list[j]);
					train_line_bf.append("\n");
					train.write(train_line_bf.toString());
				}
				else{
					i++;
					//put one in 10 users/businesses with only one rating in test set
					if (i%10==0) 
						test.write(line+"\n");
					else
						train.write(line+"\n");
				}
				
			}
			train.close();
			test.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
}
