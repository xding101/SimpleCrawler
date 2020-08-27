package Crawler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


public class Main {
	final static String url = "https://en.wikipedia.org/wiki/Microsoft";
	final static String inputFileName = "allWords.txt";
	final static String outputFileName = "output.txt";
	public static void main(String[] args) {
		Set<String> excluded = new HashSet<>();
		Scanner scan = new Scanner(System.in);
		try {
			// Crawl words from web page
			JsoupCrawler.crawl(url, inputFileName);
			// Count words
			Map<String, Integer> map = WordCount.count(inputFileName);
			
			

			// set excluded words
			System.out.println("How many words do you want to exclude? Number must postive");
			int ex = scan.nextInt();
			while (ex < 0) {
				System.out.println("How many words do you want to exclude? Number must postive");
				ex = scan.nextInt();
			}
			if (ex > 0) {
				System.out.println("Please enter the " + ex + " words you would like to exclue");
				for (int i = 0; i < ex; i++) {
					excluded.add(scan.next());
					
				}
			}

			// set range
			int size = map.size() - ex;
			System.out.println("After excluding words you specified, " + size + " distinct words found!");
			System.out.println("Please enter how many top used words you would like to see.\n"
					+ "Number must be bigger than 0 and smaller than " + size);
			int k = scan.nextInt();
			while (k <= 0 || k > size) {
				System.out.println("Please enter how many top used words you would like to see.\n"
						+ "Number must postive and no larger than " + size);
				k = scan.nextInt();
			}
			
			scan.close();
			
			// get result
			Deque<Entry<String, Integer>> result = new ArrayDeque<>();
			if (ex > 0) {
				result = filter(k, map, excluded);
			} else {
				result = filter(k, map, null);
			}
			
			// print result and write to file
			PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
			while (!result.isEmpty()) {
				Entry<String, Integer> cur = result.pollFirst();
				System.out.printf("%-30s%d\n",cur.getKey(), cur.getValue());
				writer.printf("%-30s%d\n",cur.getKey(), cur.getValue());
			}
			writer.close();
			System.out.println("Done writing to output file!");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Deque<Entry<String, Integer>> filter(int k, Map<String, Integer> map, Set<String> excluded) {
		PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(k, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (o1.getValue() == o2.getValue()) {
					if (o1.getKey().length() == o2.getKey().length()) {
						return o1.getKey().compareTo(o2.getKey());
					}
					return o1.getKey().length() < o2.getKey().length() ? -1 : 1;
				}
				return o1.getValue().compareTo(o2.getValue());
			}
			
		});

		int count = 0;
		for (Entry<String, Integer> e : map.entrySet()) {
			if (excluded != null && excluded.contains(e.getKey())) {
				continue;
			}
			if (count < k) {
				minHeap.offer(e);
				
			} else {
				Map.Entry<String, Integer> cur = minHeap.peek();
				if (e.getValue() > cur.getValue()) {
					minHeap.poll();
					minHeap.offer(e);
				} else if (e.getValue() == cur.getValue()) {
					if (e.getKey().length() < cur.getKey().length() 
							|| (e.getKey().length() == cur.getKey().length() && e.getKey().compareTo(cur.getKey()) < 0)) {
						minHeap.poll();
						minHeap.offer(e);
					}
				}
			}
			count++;
		}
		Deque<Entry<String, Integer>> stack = new ArrayDeque<>();
		while (!minHeap.isEmpty()) {
			stack.offerFirst((Entry<String, Integer>) minHeap.poll());
		}
		return stack;
	}
	

}
