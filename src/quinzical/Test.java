package quinzical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Test {
	static String s =  "100";
	private static  File _categoriesFolder = new File("categories/10");
	public static void main(String[] args) throws IOException { 

		System.out.print(s + 100);
		LineNumberReader lineNum = new LineNumberReader(new FileReader( _categoriesFolder));
//		lineNum.skip(Long.MAX_VALUE);
//
//		System.out.print(lineNum.getLineNumber());
	}


	// read the appointed line number of a file. 
	private static String readAppointedLineNumber(File categoryFile, int lineNumber, int totalLines) throws IOException {

		FileReader in = new FileReader(categoryFile);  
		LineNumberReader reader = new LineNumberReader(in);  

		for(int i = 0; i < totalLines; i++) {
			String s = reader.readLine();	
			if (reader.getLineNumber() == lineNumber) {  
				return s;
			} 
		}
		return null;
	}

} 

