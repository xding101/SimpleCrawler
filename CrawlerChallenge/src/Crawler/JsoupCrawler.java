package Crawler;

import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// using Jsoup library to parse
public class JsoupCrawler {
	public static void crawl(String url, String fileName) throws IOException {
		Document doc = Jsoup.connect(url).timeout(1000).get();
		// find the div tag with desired content
		Elements div = doc.select("body").select("div#content").select("div#bodyContent")
				.select("div#mw-content-text").select("div.mw-parser-output").first().children();
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		boolean start = false;
		int count = 0;
		
		for (Element e: div) {
			// find the part we want and extract text from html
			// NOTE: Words under the pictures in History section is also included!
			if (e.tagName().equals("h2") && e.text().equals("History")) {
				start = true;
			}
			if (e.tagName().equals("h2") && e.text().equals("Corporate affairs")) {
				break;
			}
			if (start) {
				String[] array = e.text().split(" ");
				for (String s : array) {
					// remove digit and punctuation except "-" and "'"
					s = s.replaceAll("[^a-zA-Z&&[^-']]", "");
					if (s.length() == 0) {
						continue;
					}  else if (s.charAt(0) == '-' || s.charAt(0) == '\'') {
						// if the first character is punctuation, remove the punctuation
						// example: "32-bit" >> "-bit" >> "bit"
						if (s.length() > 1) {
							s = s.substring(1);
							writer.println(s);
							count++;
						}
					} else {
						writer.println(s);
						count++;
					}

				}				
			}
		}
		System.out.println("Success! " + count + " non-digit words crawled.");
		writer.close();
	}

}
