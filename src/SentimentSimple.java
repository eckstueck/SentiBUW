import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class SentimentSimple {
	static Map<String, Double> trainMap = new HashMap<String, Double>();
	static Map<String, Integer> countMap = new HashMap<String, Integer>();
	static Map<String, String> resultMap = new HashMap<String, String>();
	
	public SentimentSimple(Map<HashMap<String, String>, String> tList) {
	}
	
	public static void trainSimple(Map<HashMap<String, String>, String> tweetList) throws FileNotFoundException, IOException{
		System.out.println("Start train");
		
		for (Map.Entry<HashMap<String,String>, String> tweet : tweetList.entrySet()){
			double sentiValue = getSentiValue(tweet.getValue());
			for (Map.Entry<String, String> wordMap : tweet.getKey().entrySet()){
				String word = wordMap.getKey();
				if (word.equals("")) continue;
	            if (!trainMap.containsKey(word)){
	            	trainMap.put(word.toLowerCase(), sentiValue);
	            	countMap.put(word.toLowerCase(), 1);
	            }
	            else {
					double sen = trainMap.get(word.toLowerCase());
					int count = countMap.get(word.toLowerCase());
					double newSen = (sen * count + sentiValue) / (count + 1);
					trainMap.put(word.toLowerCase(), newSen);
					countMap.put(word.toLowerCase(), count + 1);				
				}
			}
		}
	
		Properties properties = new Properties();		
		for (Map.Entry<String, Double> entry : trainMap.entrySet()){
			properties.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		properties.store(new FileOutputStream("resources/properties/trainSimple.properties"), null);
		System.out.println("End train");
	}


	private static double getSentiValue(String value) {
		if (value.equals("positive")) {
			return 1.0;
		}
		else{
			if (value.equals("negative")) {
				return -1.0;
			}
			else {
				return 0.0;
			}
		}
	}


	public static void testSimple(Map<HashMap<String, String>, String> tweetList) throws FileNotFoundException, IOException {
		System.out.println("Start test");
		Properties properties = new Properties();
		properties.load(new FileInputStream("resources/properties/trainSimple.properties"));
		for (String key : properties.stringPropertyNames()) {
			   trainMap.put(key, Double.parseDouble(properties.get(key).toString()));
		}
		
		for (Map.Entry<HashMap<String,String>, String> tweet : tweetList.entrySet()){
			resultMap.put(tweet.getValue(), getSentiment(tweet.getKey().entrySet()));
		}
		
		Properties properties2 = new Properties();		
		for (Map.Entry<String, String> entry : resultMap.entrySet()){
			properties2.put(entry.getKey(), entry.getValue());
		}
		properties2.store(new FileOutputStream("resources/properties/testSimple.properties"), null);
		System.out.println("End test");
	}
	
	public static String getSentiment(Set<Entry<String, String>> wordList) {
		int count = 0;
		double sentiSent = 0;
		for (Map.Entry<String, String> wordMap : wordList){
			String word = wordMap.getKey();
			if (word.equals("")) continue;
        	if(trainMap.containsKey(word.toLowerCase())){
            	count++;
            	sentiSent = sentiSent + trainMap.get(word.toLowerCase());
            }
		}
		double sentiment = sentiSent / count;        
        if((sentiment <= -0.30)){
        	return "negative";
        }
        else{
        	if(sentiment <= 0.30){
        		return "neutral";
        	}
        	else{
        		return "positive";
        	}
        }	
	}
}
