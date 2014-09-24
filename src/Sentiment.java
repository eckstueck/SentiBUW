import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;

public class Sentiment {
	
	static ArrayList<String> stopWordList = new ArrayList<String>();
//	static Map<ArrayList<String>, String> tweetList = new HashMap<ArrayList<String>, String>();
	static Set<Tweet> tweetList = new HashSet<Tweet>();
	static String mode;
	static String PATH = "";
//	static int featurecount = 0;
	
	public static void main(String[] args) throws Exception{
//		Map<String, Double> hashUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
//		Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
//    	Tagger tagger = new Tagger();
//    	tagger.loadModel("resources/tagger/model.20120919.txt");
//		Tweet tweet = new Tweet("I don't think I will enjoy it: it might be too spicy. But I will never find her. Or will I?", "positive");
//		tweet.tokenizeAndTag(tagger);
//		tweet.getCharNGrams();
//		tweet.tokenizeAndTag(tagger);
//		tweet.getPosTags();
		
		int numberOfTweets = 0; //0 = all
		boolean useNeutral = true;
		boolean useStopwordList = true;
		int numberOfNGrams = 1;
		String nameOfTrain = "";
		String nameOfTrain2 = "";
		String nameOfTrain3 = "";
		int evalmodelmode = 0;
		Options options = new Options();
		
		options.addOption("nt", true, "Number of Tweets");
		options.addOption("nn", false, "no Neutral");
		options.addOption("ns", false, "no Stopwordlist");
		options.addOption("ng", true, "Number of nGrams");
		options.addOption("on", true, "output Name");
		options.addOption("tf", true, "Name of the Trainfile");
		options.addOption("tf2", true, "Name of the Trainfile2");
		options.addOption("tf3", true, "Name of the Trainfile3");
		options.addOption("em", true, "Eval Modelmode");
		
		CommandLineParser parser = new GnuParser();
		try {
			String name = "";
			CommandLine line = parser.parse(options, args);
			if(line.hasOption("nt")){
				numberOfTweets = Integer.parseInt(line.getOptionValue("nt"));
				name = name + "" + numberOfTweets;
			}
			else{
				name = name + "all";
			}
			if(line.hasOption("nn")){
				useNeutral = false;
				name = name + "_nn";
			}
			if(line.hasOption("ns")){
				useStopwordList = false;
				name = name + "_ns";
			}
			if(line.hasOption("ng")){
				numberOfNGrams = Integer.parseInt(line.getOptionValue("ng"));
				name = name + "_NG" + numberOfNGrams;
			}
			else{
				name = name + "_NG1";
			}
			if(line.hasOption("on")){
				name = "_" + line.getOptionValue("on");
			}
			if(line.hasOption("tf")){
				nameOfTrain = line.getOptionValue("tf");
			}
			if(line.hasOption("tf2")){
				nameOfTrain2 = line.getOptionValue("tf2");
			}
			
			if(line.hasOption("tf3")){
				nameOfTrain3 = line.getOptionValue("tf3");
			}
			
			if(line.hasOption("em")){
				evalmodelmode = Integer.parseInt(line.getOptionValue("em"));
			}
			
			String[] argList = line.getArgs();
			PATH = argList[1];
			
			if (useStopwordList) loadStopWordList();
			
			if (!argList[0].equals("eval")){
				loadTweets(argList[1], numberOfTweets, useNeutral, useStopwordList, numberOfNGrams);
				if (argList[0].equals("train")){
					SentimentWeka.trainNRC(tweetList, name);
				}
				else {
					if (argList[0].equals("test")){
						SentimentWeka.testNRC(tweetList, name, nameOfTrain);
					}
					else{
						if (argList[0].equals("printPred")){
//							printPred(nameOfTrain);
						}
						else{
							if (argList[0].equals("evalall")){
								evalAllModels(nameOfTrain, nameOfTrain2, nameOfTrain3);
//								evalModel(nameOfTrain3);
							}
							else{
								if (argList[0].equals("trainall")){
									SentimentWeka.trainNRC(tweetList, name);
									SentimentWeka.trainGUMLTLT(tweetList, name);
									SentimentWeka.trainKLUE(tweetList, name);
								}
							}
						}
					}
					
				}
			}
			else{
					loadTweets(argList[1], numberOfTweets, useNeutral, useStopwordList, numberOfNGrams);
					evalModel(nameOfTrain, evalmodelmode);
				}

		}
		catch(ParseException exp){
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
				
//		if (args[2].equals("weka")){
//			mode = "train";
//		}
//		else{
//			mode = args[1];
//		}
		
//		loadStopWordList();
//		loadTweets(args[0]);
////		System.out.println(featurecount);
//		
//		if (args[1].equals("train")){
//			if (args[2].equals("simple")){
////				SentimentSimple.trainSimple(tweetList);
//			}
//			if (args[2].equals("weka")){
//				if (args[3].equals("String")){
////					SentimentWeka.trainString(tweetList);
//				}
//				else{
//					SentimentWeka.train(tweetList, args[3]);
//				}
//				
//			}
//		}
//		else{
//			if (args[1].equals("test")){
//				if (args[2].equals("simple")){
////					SentimentSimple.testSimple(tweetList);
//				}
//				if (args[2].equals("weka")){
//					SentimentWeka.test(tweetList, args[3]);
//				}
//			}
//			else{
//				if (args[2].equals("weka")){
//					SentimentWeka.eval();
//				}
//			}
//		}
		
//		String oldString = "know\u002c";
//		String newString = new String(oldString.getBytes("UTF-8"), "UTF-8");
//		System.out.println(newString);
	}
	
//	private static void printPred(String nameOfTrain) throws Exception {
//		System.out.println("Starting print Pred");
//		System.out.println("Tweets: " +  tweetList.size());
//		PrintStream positive_neutral = new PrintStream(new File("resources/erg/positive_neutral.txt"));
//		PrintStream positive_negative = new PrintStream(new File("resources/erg/positive_negative.txt"));
//		PrintStream neutral_positive = new PrintStream(new File("resources/erg/neutral_positive.txt"));
//		PrintStream neutral_negativ = new PrintStream(new File("resources/erg/neutral_negative.txt"));
//		PrintStream negative_neutral = new PrintStream(new File("resources/erg/negative_neutral.txt"));
//		PrintStream negative_positive = new PrintStream(new File("resources/erg/negative_positive.txt"));
//		
//		Map<Tweet, Integer> resultMap = new HashMap<Tweet, Integer>();
//		resultMap = SentimentWeka.printPredNRC(tweetList, nameOfTrain);
//	    ArrayList<String> fvClassVal = new ArrayList<String>();
//	    fvClassVal.add("positive");
//	    fvClassVal.add("neutral");
//	    fvClassVal.add("negative");
//		int tweetcount = 0;
//		if(resultMap != null){
//			for (Map.Entry<Tweet, Integer> tweet : resultMap.entrySet()){
//				String actualSenti = tweet.getKey().getSentiment();
//				String predSenti = fvClassVal.get(tweet.getValue());
//				if (!actualSenti.equals(predSenti)){
//					if (predSenti.equals("positive")){
//						if (actualSenti.equals("neutral")){
//							positive_neutral.println(tweet.getKey().getTweetString());
//						}
//						if (actualSenti.equals("negative")){
//							positive_negative.println(tweet.getKey().getTweetString());
//						}
//					}
//					if (predSenti.equals("neutral")){
//						if (actualSenti.equals("positive")){
//							neutral_positive.println(tweet.getKey().getTweetString());
//						}
//						if (actualSenti.equals("negative")){
//							neutral_negativ.println(tweet.getKey().getTweetString());
//						}
//					}
//					if (predSenti.equals("negative")){
//						if (actualSenti.equals("neutral")){
//							negative_neutral.println(tweet.getKey().getTweetString());
//							
//						}
//						if (actualSenti.equals("positive")){
//							negative_positive.println(tweet.getKey().getTweetString());
//						}
//					}
//				}
//				if (tweetcount % 100 == 0) System.out.println("Tweet: " +  tweetcount);
//				tweetcount++;
//			}
//		}
//		positive_neutral.close();
//		positive_negative.close();
//		neutral_positive.close();
//		neutral_negativ.close();
//		negative_neutral.close();
//		negative_positive.close();
//		System.out.println("Finished print Pred");
//	}
		
		private static void printTweets(List<ClassificationResult> tweetPrintList) throws Exception {
			System.out.println("Starting print Tweets");
			System.out.println("Tweets: " +  tweetPrintList.size());
			PrintStream tweetPrintStream = new PrintStream(new File("resources/erg/printedTweets.txt"));
			
			for (ClassificationResult result : tweetPrintList){
//				tweetPrintStream.print(result.getResultDistribution()[0] + " | " + result.getResultDistribution()[1] + " | " + result.getResultDistribution()[2]);
//				tweetPrintStream.print(" : ");
				tweetPrintStream.print(result.getTweet().getRawTweetString());
				tweetPrintStream.println();
			}
			tweetPrintStream.close();
			System.out.println("Finished print Tweets");
	}
		
//	private static void evalModel(String nameOfTrainNRC) throws Exception {
//		System.out.println("Starting print Pred");
//		System.out.println("Tweets: " +  tweetList.size());
//		double[][] matrix = new double[3][3];
//		Map<String, Integer> classValue = new HashMap<String, Integer>();
//		classValue.put("positive", 0);
//		classValue.put("neutral", 1);
//		classValue.put("negative", 2);
//		Map<Tweet,ClassificationResult> resultListNRC = new HashMap<Tweet,ClassificationResult>();
//		List<ClassificationResult> wrongClassified = new ArrayList<ClassificationResult>();
//		System.out.println("1");
//		resultListNRC = SentimentWeka.printPredKLUE(tweetList, nameOfTrainNRC);
//		System.out.println("2");
//		if(resultListNRC != null ){
//			for (Map.Entry<Tweet, ClassificationResult> result : resultListNRC.entrySet()){
//				Integer actualSenti = result.getValue().getTweet().getSentimentAsInt();
//				Integer nRCSenti = (int) result.getValue().getResult();
//				matrix[nRCSenti][actualSenti]++;
//				if (actualSenti == 0 && nRCSenti == 2){
//				    wrongClassified.add(result.getValue());
//				}
//			}
//		}
//		else{
//			System.out.println("resultMaps null or diffrent size");
//		}
//		System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
//		System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
//		System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
//		score(matrix);
//		printTweets(wrongClassified);
//	}
		private static void evalModel(String nameOfTrain, int model) throws Exception {
			System.out.println("Starting eval Model");
			System.out.println("Tweets: " +  tweetList.size());
			double[][] matrix = new double[3][3];
			Map<String, Integer> classValue = new HashMap<String, Integer>();
			classValue.put("positive", 0);
			classValue.put("neutral", 1);
			classValue.put("negative", 2);
			Map<Tweet, ClassificationResult> resultMap = new HashMap<Tweet, ClassificationResult>();
			Map<String, Integer> resultMapToPrint = new HashMap<String, Integer>();
			if (model == 0){
				resultMap = SentimentWeka.printPredNRCNew(tweetList, nameOfTrain);
			}
			else{
				if (model == 1){
					resultMap = SentimentWeka.printPredGUMLTLT(tweetList, nameOfTrain);
				}
				else{
					resultMap = SentimentWeka.printPredKLUE(tweetList, nameOfTrain);
				}
			}
			for (Map.Entry<Tweet, ClassificationResult> tweet : resultMap.entrySet()){
				Integer actualSenti = classValue.get(tweet.getKey().getSentiment());
				String tweetID = tweet.getKey().getTweetID();
				ClassificationResult nRCSenti = tweet.getValue();
				double[] useSentiArray = {0,0,0};
				for (int i = 0; i < 3; i++){
					useSentiArray[i] = (nRCSenti.getResultDistribution()[i]);
				}
				int useSenti = 0;
				if(useSentiArray[1] > useSentiArray[0] && useSentiArray[1] > useSentiArray[2]){
					useSenti = 1;
				}
				if(useSentiArray[2] > useSentiArray[0] && useSentiArray[2] > useSentiArray[1]){
					useSenti = 2;
				}
				resultMapToPrint.put(tweetID, useSenti);
				matrix[actualSenti][useSenti]++;
			}
			System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
			System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
			System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
			score(matrix);
			printResultToFile(resultMapToPrint);
		}
			
	private static void evalAllModels(String nameOfTrainNRC, String nameOfTrainGUMLTLT, String nameOfTrainKLUE) throws Exception {
		System.out.println("Starting print Pred");
		System.out.println("Tweets: " +  tweetList.size());
		double[][] matrix = new double[3][3];
		Map<String, Integer> classValue = new HashMap<String, Integer>();
		classValue.put("positive", 0);
		classValue.put("neutral", 1);
		classValue.put("negative", 2);
//		List<ClassificationResult> tweetsToPrint = new ArrayList<ClassificationResult>();
		Map<Tweet, ClassificationResult> resultMapNRC = new HashMap<Tweet, ClassificationResult>();
		Map<Tweet, ClassificationResult> resultMapGUMLTLT = new HashMap<Tweet, ClassificationResult>();
		Map<Tweet, ClassificationResult> resultMapKLUE = new HashMap<Tweet, ClassificationResult>();
		Map<String, Integer> resultMapToPrint = new HashMap<String, Integer>();
		System.out.println("1");
		resultMapNRC = SentimentWeka.printPredNRCNew(tweetList, nameOfTrainNRC);
		System.out.println("1.1");
		resultMapGUMLTLT = SentimentWeka.printPredGUMLTLT(tweetList, nameOfTrainGUMLTLT);
		System.out.println("1.2");
		resultMapKLUE = SentimentWeka.printPredKLUE(tweetList, nameOfTrainKLUE);
		System.out.println("2");
//		int tweetcount = 0;
//		int nrcright = 0;
//		int gumltltright = 0;
//		int klueright = 0;
//		int wrong = 0;
//		int correctedWasPos = 0;
//		int correctedWasNeg = 0;
//		int correctedWasNeu = 0;
		if((resultMapNRC != null && resultMapGUMLTLT != null && resultMapKLUE != null)  && (resultMapNRC.size() == resultMapGUMLTLT.size()) && (resultMapNRC.size() == resultMapKLUE.size())){
			for (Map.Entry<Tweet, ClassificationResult> tweet : resultMapNRC.entrySet()){
				Integer actualSenti = classValue.get(tweet.getKey().getSentiment());
				String tweetID = tweet.getKey().getTweetID();
				ClassificationResult nRCSenti = tweet.getValue();
				ClassificationResult gUMLTLTSenti = resultMapGUMLTLT.get(tweet.getKey());
				ClassificationResult kLUESenti = resultMapKLUE.get(tweet.getKey());
//				Integer nRCSenti = (int) tweet.getValue().getResult();
//				Integer gUMLTLTSenti = (int)resultMapGUMLTLT.get(tweet.getKey()).getResult();
//				Integer kLUESenti = (int) resultMapKLUE.get(tweet.getKey()).getResult();
				if(gUMLTLTSenti != null && kLUESenti != null){
					double[] useSentiArray = {0,0,0};
					for (int i = 0; i < 3; i++){
						useSentiArray[i] = (nRCSenti.getResultDistribution()[i] + gUMLTLTSenti.getResultDistribution()[i] + kLUESenti.getResultDistribution()[i]) / 3;
						//useSentiArray[i] = (nRCSenti.getResultDistribution()[i]);
					}
					int useSenti = 0;
					if(useSentiArray[1] > useSentiArray[0] && useSentiArray[1] > useSentiArray[2]){
						useSenti = 1;
					}
					if(useSentiArray[2] > useSentiArray[0] && useSentiArray[2] > useSentiArray[1]){
						useSenti = 2;
					}					
//					System.out.println(useSentiArray[0] + ":" + useSentiArray[1] + ":" + useSentiArray[2]);
					//System.out.println(useSenti);
					resultMapToPrint.put(tweetID, useSenti);
//					Integer useSenti = nRCSenti;
//					if ((nRCSenti != gUMLTLTSenti && nRCSenti != kLUESenti) && (kLUESenti == gUMLTLTSenti)){
//						Double sentiScore = (nRCSenti - 1) / 0.5 + (gUMLTLTSenti - 1) / 0.3 + (kLUESenti - 1) / 0.2;
//						useSenti = 1;
//						if (sentiScore < -0.8){
//							useSenti = 0;
//						}
//						if (sentiScore > 0.8){
//							useSenti = 2;
//						}
//					}
//					Double sentiScore = (nRCSenti - 1) / 0.5 + (gUMLTLTSenti - 1) / 0.3 + (kLUESenti - 1) / 0.2;
//					if (sentiScore < -0.5){
//						useSenti = 0;
//					}
//					if (sentiScore > 0.5){
//						useSenti = 2;
//					}
					
					
					//70.13
//					if ((nRCSenti == 0 && kLUESenti != 0 && gUMLTLTSenti != 0) && (kLUESenti == gUMLTLTSenti)){
//						useSenti = kLUESenti;
//						tweetcount++;
//						if (actualSenti != useSenti){
//							wrong++;
//						}
//					}
					
					
					//use this
//					if ((nRCSenti != gUMLTLTSenti && nRCSenti != kLUESenti) && (kLUESenti == gUMLTLTSenti)){
////					if ((kLUESenti == gUMLTLTSenti) && ((nRCSenti == 0 && gUMLTLTSenti != 2) || (nRCSenti == 2 && gUMLTLTSenti != 0))){
//						useSenti = kLUESenti;
//						tweetcount++;
//						if (actualSenti != useSenti){
//							wrong++;
//						}
//						else{
//							if (nRCSenti == 0){
//								correctedWasPos++;
//							}
//							if (nRCSenti == 1){
//								correctedWasNeu++;
//							}
//							if (nRCSenti == 2){
//								correctedWasNeg++;
//							}
//						}
//					}
//					if(actualSenti == nRCSenti){
//						nrcright++;
//					}
//					if(actualSenti == gUMLTLTSenti){
//						gumltltright++;
//					}
//					if(actualSenti == kLUESenti){
//						klueright++;
//					}
					
					
//					if (nRCSenti == 0 && gUMLTLTSenti != 0){
//						useSenti = gUMLTLTSenti;
//					}
//					if (nRCSenti != gUMLTLTSenti){
//						tweetcount++;
//						System.out.println(tweetcount);
//						if(actualSenti == nRCSenti){
//							System.out.println("NRC");
//							nrcright++;
//						}
//						if(actualSenti == gUMLTLTSenti){
//							System.out.println("GUMLTLT");
//							gumltltright++;
//						}
//						System.out.println("----------------------");
//					}
					
//					if (useSenti == 2 && actualSenti == 0){
//						tweetsToPrint.add(tweet.getValue());
//					}
					matrix[actualSenti][useSenti]++;
				}
				else{
					System.out.println(tweet.getKey().getTweetString());
				}
//				if (tweetcount % 100 == 0) System.out.println("Tweet: " +  tweetcount);
//				tweetcount++;
			}
		}
		else{
			System.out.println("resultMaps null or diffrent size");
		}
//		System.out.println("3");
//		System.out.println("NRC corrected: "  + tweetcount);
//		System.out.println("NRC wrong corrected: "  + wrong);
//		System.out.println("NRC was pos: "  + correctedWasPos);
//		System.out.println("NRC was neu: "  + correctedWasNeu);
//		System.out.println("NRC was neg: "  + correctedWasNeg);
//		System.out.println("NRC right: " + nrcright);
//		System.out.println("GUMLTLT right: " + gumltltright);
//		System.out.println("KLUE right: " + klueright);
		System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
		System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
		System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
		score(matrix);
//		printTweets(tweetsToPrint);
		printResultToFile(resultMapToPrint);
	}

	private static void printResultToFile(Map<String, Integer> resultMapToPrint) throws FileNotFoundException {    
        int errorcount = 0;
        Map<Integer, String> classValue = new HashMap<Integer, String>();
        classValue.put(0, "positive");
        classValue.put(1, "neutral");
        classValue.put(2, "negative");
        File file = new File("resources/tweets/" + PATH + ".txt");
        PrintStream tweetPrintStream = new PrintStream(new File("resources/erg/result.txt"));
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t");
            if (line.length == 4){        
                String senti = classValue.get(resultMapToPrint.get(line[0]));
                if (senti != null){
                    line[2] = senti;
                }
                else{
                    System.out.println("Error while printResultToFile: tweetID:" + line[0]);
                    errorcount++;
                    line[2] = "neutral";
                }
            }
            else{
                errorcount++;
                System.out.println(line[0]);
            }           
            tweetPrintStream.print(StringUtils.join(line, "\t"));
            tweetPrintStream.println();
        }
        scanner.close();
        tweetPrintStream.close();
        if (errorcount != 0) System.out.println("Errors while printResultToFile: " + errorcount);
	}

    private static void loadStopWordList() throws FileNotFoundException {
		File file = new File("resources/stopwords.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			stopWordList.add(scanner.nextLine());
		}
		scanner.close();
	}
	
	private static void loadTweets(String path, int nofT, Boolean useN, Boolean useS, int nofNG) throws FileNotFoundException, UnsupportedEncodingException{
		int tweetcount = 0;
		if (nofT == 0) tweetcount = -1;
		File file = new File("resources/tweets/" + path + ".txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine() && tweetcount < nofT) {
//		    i++;
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 4){			
				if(line[2].equals("neutral") && !useN){
					
				}
				else {
					if(storeTweetUni(line[3], line[2], line[0], nofNG) && nofT != 0){
						tweetcount++;
					}
				}
			}
			else{
			    System.out.println(line[0]);
			}
		}
		scanner.close();
		//System.out.println(tweetcount);
	}

	private static String filterTweet(String tweet) throws UnsupportedEncodingException{
//		tweet = new String(tweet.getBytes("UTF-8"), "UTF-8");
//		System.out.println(tweet);
		//lowerCase
//		tweet = tweet.toLowerCase();
		//filter Usernames
		//tweet = tweet.replaceAll("@[^\\s]+", "@someuser");
		tweet = tweet.replaceAll("@[^\\s]+", "SOMEURL");
		//filter Urls
		//tweet = tweet.replaceAll("((www\\.[^\\s]+)|(https?://[^\\s]+))", "http://someurl");
		tweet = tweet.replaceAll("((www\\.[^\\s]+)|(https?://[^\\s]+))", "SOMEUSER");
		//filter #
//		Matcher m = Pattern.compile("#[^\\s]+").matcher(tweet);
//		while (m.find()) {
//			int index = m.start();
//			System.out.println(tweet);
//			System.out.println("Index: " + index);
//			System.out.println("length: " + tweet.length());
//			if (tweet.length() > index+1){
//				tweet = tweet.substring(0, index) + tweet.substring(index+1);
//			}
//			else{
//				tweet = tweet.substring(0, index) + tweet.substring(tweet.length());
//			}
//			System.out.println("end");
//		}
		return tweet.trim();
	}
	
	private static String filterWord(String word, Boolean useS){
//		Matcher m = Pattern.compile("(.)\\1{2,}").matcher(word);
//		while (m.find()) {
//			word = word.substring(0, m.start()+2) + word.substring(m.end());
//		}
		if(useS && stopWordList.contains(word)){
			word = "";
		}
		if(!word.matches("^[A-Za-z][A-Za-z0-9]*")){
			return "";
		}
		else{
			return word;
		}
	}
	
	private static boolean storeTweetUni(String tweetString, String senti, String tweetID, int ngram) throws UnsupportedEncodingException{
		Tweet tweet = new Tweet(tweetString, senti, tweetID);
		
//	    ArrayList<String> wordlist = new ArrayList<String>();
//	    NGramTokenizer tokenizer = new NGramTokenizer();
//	    tokenizer.setNGramMaxSize(ngram);
//	    tokenizer.setNGramMinSize(1);
//	    tokenizer.tokenize(tweet);
//	    while(tokenizer.hasMoreElements()){
//	        wordlist.add((String) tokenizer.nextElement());
////	        featurecount++;
//	    }
    	
//		for (String word: tweet.split("[\\p{P} \\t\\n\\r]")){
//			String filterdWord = filterWord(word);
//            if (filterdWord.equals("")){
//            	continue;
//            }
//            else{        
//            	wordlist.add(filterdWord);
//            }	            
//		}
		
	    if(tweetList.add(tweet)){
	    	return true;
	    }
	    else {
			return false;
		}
    	
    }
	
	private static Map<String, Double> loadLexicon(String path) throws FileNotFoundException{
		Map<String, Double> lexiMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/" + path + ".txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 4){
				lexiMap.put(line[0], Double.parseDouble(line[1]));
			}
		}
		scanner.close();
		return lexiMap;
	}
	
	private static void score(double[][] matrix){
		double precisionA = matrix[0][0] / (matrix[0][0] + matrix[1][0] + matrix[2][0]);
		double precisionB = matrix[1][1] / (matrix[1][1] + matrix[2][1] + matrix[0][1]);
		double precisionC = matrix[2][2] / (matrix[2][2] + matrix[0][2] + matrix[1][2]);

		double precision = (precisionA + precisionB + precisionC) / 3;
		
		double recallA = matrix[0][0] / (matrix[0][0] + matrix[0][1] + matrix[0][2]);
		double recallB = matrix[1][1] / (matrix[1][1] + matrix[1][2] + matrix[1][0]);
		double recallC = matrix[2][2] / (matrix[2][2] + matrix[2][0] + matrix[2][1]);
		double recall = (recallA + recallB + recallC) / 3;
		
		double f1 = 2 * ((precision * recall) / (precision + recall));
		double f1A = 2 * ((precisionA * recallA) / (precisionA + recallA));
		double f1B = 2 * ((precisionB * recallB) / (precisionB + recallB));
		double f1C = 2 * ((precisionC * recallC) / (precisionC + recallC));
		
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("f1: " + f1);
		System.out.println("f1 without neu: " + (f1A + f1C) /2);
	}
}	
