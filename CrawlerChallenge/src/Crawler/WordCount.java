package Crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// For large file, k-way merge or MapReduce is  recommended.

//k-way merge: separate the entire word file into small partitions (for example: 100 words per time)
//sort small partition in alphabetical order, merge partitions per round to update word count

//MapReduce: separate the entire word file into multiple files to perform MapReduce



public class WordCount {
	static Map<String, Integer> hashMap = new HashMap<>();
	
	public static Map<String, Integer> count(String fileName) throws FileNotFoundException {
		
		Scanner scan = new Scanner(new File("allWords.txt"));
		while (scan.hasNextLine()) {
			String str = scan.nextLine();
			Integer c = hashMap.get(str);
			if (c == null) {
				hashMap.put(str, 1);
			} else {
				hashMap.put(str, c + 1);
			}
			
		}
		scan.close();
		return hashMap;
	}
	
}
