import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;
import cmu.arktweetnlp.Tagger;

public class SentimentWeka {
	
//	private static List<String> featureList;

//	public static void train(Map<HashMap<String, String>, String> tweetList) throws Exception {
//		System.out.println("Starting Weka Train");
//		System.out.println("Tweets: " +  tweetList.size());
//		for (Map.Entry<HashMap<String,String>, String> tweet : tweetList.entrySet()){
//			for (Map.Entry<String, String> wordMap : tweet.getKey().entrySet()){
//				String word = wordMap.getKey();
//				if (word.equals("") || featureList.contains(word)){
//					continue;
//				}
//				else{
//					featureList.add(word);
//				}		
//			}
//		}
//		System.out.println("1");
//		FastVector attributeList = new FastVector(featureList.size() + 1);
//		for (String feature : featureList){
//			Attribute attribute = new Attribute(feature);
//			attributeList.addElement(attribute);
//		}
//		System.out.println("2");
//		FastVector fvClassVal = new FastVector(3);
//		fvClassVal.addElement("positive");
//		fvClassVal.addElement("neutral");
//		fvClassVal.addElement("negative");
//		Attribute classAttribute = new Attribute("Class", fvClassVal);
//		attributeList.addElement(classAttribute);
//		System.out.println("3");
//		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
//		trainingSet.setClassIndex(featureList.size());
//		System.out.println("4");
//		int tweetcount = 0;
//		for (Map.Entry<HashMap<String,String>, String> tweet : tweetList.entrySet()){
//			String sentiValue = tweet.getValue();
//			ArrayList<Integer> tweetVector = new ArrayList<Integer>(Collections.nCopies(featureList.size(), 0));
//			for (Map.Entry<String, String> wordMap : tweet.getKey().entrySet()){
//				String word = wordMap.getKey();
//				if (word.equals("")) continue;
//				int index = featureList.indexOf(word);
//				if (index != -1){
//					tweetVector.set(index, 1);
//				}
//			}
//			Instance instance = new Instance(featureList.size() + 1);
//			int i = 0;
//			for (int val : tweetVector){
//				instance.setValue((Attribute) attributeList.elementAt(i) , val);
//				i++;
//			}
//			instance.setValue((Attribute) attributeList.elementAt(featureList.size()), sentiValue);
//			trainingSet.add(instance);
//			tweetcount++;
//			if (tweetcount % 50 == 0) System.out.println("Tweet: " + tweetcount);
//		}
//		System.out.println("5");
//		Classifier classi = (Classifier) new NaiveBayes();
//		classi.buildClassifier(trainingSet);
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(trainingSet);
//		saver.setFile(new File("resources/arff/train1000.arff"));
//		saver.writeBatch();
//	}
	
//	public static void train(Map<ArrayList<String>, String> tweetList, String savename) throws Exception {
//		System.out.println("Starting Weka Train");
//		System.out.println("Tweets: " +  tweetList.size());
//		int featurecount = 0;
//		int tweetcount = 0;
//		Set<String> featureset = new LinkedHashSet<String>();
//	    ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
//		Map<SparseInstance,String> instanceList = new HashMap<SparseInstance, String>();
//		System.out.println("1");
//		for (Entry<ArrayList<String>, String> tweet : tweetList.entrySet()){
////		    String sentiValue = tweet.getValue();
//		    SparseInstance instance = new SparseInstance(0);
////		    instance.setValue(attributeList.get(0), sentiValue);
////		    System.out.println("1.1");
//			for (String word : tweet.getKey()){
//				if (word.equals("")){
//					continue;
//				}
//				else{
//				    if(featureset.add(word)){
//						Attribute attribute = new Attribute(word);
//						attributeList.add(attribute);
//				        instance.setValue(featurecount, 1); 
//				        featurecount++;
//				    }
//				    else{
//				        int index = getIndex(featureset, word);
//				        if(index != -1){
//				            instance.setValue(index, 1);
//				        }
//				    }
//				}
//			}        
////	        System.out.println("1.3");
//			instanceList.put(instance, tweet.getValue());
//			tweetcount++;
//	        if (tweetcount % 100 == 0) System.out.println("tweet: " + tweetcount);
//		}
////	    System.out.println("2");     
////		for (String feature : featureset){
////			Attribute attribute = new Attribute(feature);
////			attributeList.add(attribute);
////		}
//		System.out.println("2");
//	    ArrayList<String> fvClassVal = new ArrayList<String>();
//        fvClassVal.add("positive");
//        fvClassVal.add("neutral");
//        fvClassVal.add("negative");
//        Attribute classAttribute = new Attribute("Class", fvClassVal);
//        attributeList.add(classAttribute);
//		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
//		trainingSet.setClassIndex(attributeList.size() - 1);
//		
//		System.out.println("3"); 
//		for(Entry<SparseInstance,String> inst : instanceList.entrySet()){
//			SparseInstance instance = inst.getKey();
//			instance.setValue(attributeList.get(attributeList.size() - 1), inst.getValue());
//		    trainingSet.add(instance);
//		    
//		}
//		
////		System.out.println("4");
////		featureList = new ArrayList<String>(featureset);
////		int tweetcount = 0;
////		for (Entry<ArrayList<String>, String> tweet : tweetList.entrySet()){
////			String sentiValue = tweet.getValue();
////			SparseInstance instance = new SparseInstance(0);
////			for (String word : tweet.getKey()){
////				if (word.equals("")) continue;
////				int index = featureList.indexOf(word);
////				if (index != -1){
////					instance.setValue(index, 1);
////				}
////			}
////			instance.setValue((Attribute) attributeList.elementAt(featureList.size()), sentiValue);
////			trainingSet.add(instance);
////			tweetcount++;
////			if (tweetcount % 50 == 0) System.out.println("Tweet: " + tweetcount);
////		}
////		System.out.println("4");
////        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/arff/train_de_wr.arff"));
////        writer.write(trainingSet.toString());
////        writer.flush();
////        writer.close();
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(trainingSet);
//		saver.setFile(new File("resources/arff/train_" + savename + ".arff"));
//		saver.writeBatch();
//		System.out.println("train_" + savename + " saved");
//	}
	
	public static void trainNRC(Set<Tweet> tweetList, String savename) throws IOException{
		System.out.println("Starting Weka Train");
		System.out.println("Tweets: " +  tweetList.size());
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
    	
    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
    	Map<String, Double> MPQALexi = loadMPQA();
    	Map<String, Double> BingLiuLexi = loadBingLiu();
    	Map<String, Double> NRCLexi = loadNRC();
		
		int featurecount = 0;
//		int tweetcount = 0;
		Map<String, Integer> nGramMap = new HashMap<String, Integer>();
		Map<String, Integer> CharNGramMap = new HashMap<String, Integer>();
		Map<String, Integer> posMap = new HashMap<String, Integer>();
		Map<String, Integer> clusterMap = new HashMap<String, Integer>();
		Map<String, Integer> emoticonMap = new HashMap<String, Integer>();
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			Set<String> nGramSet = tweet.getNGrams(4);
			for (String nGram : nGramSet){
				if(!nGramMap.containsKey(nGram)){
					nGramMap.put(nGram, featurecount++);
					attributeList.add(new Attribute("NGRAM_" + nGram));
				}
			}
			Set<String> CharNGramSet = tweet.getCharNGrams();
			for (String nGram : CharNGramSet){
				if(!CharNGramMap.containsKey(nGram)){
					CharNGramMap.put(nGram, featurecount++);
					attributeList.add(new Attribute("CHARNGRAM_" + nGram));
				}
			}	
			Map<String, Integer> posTags = tweet.getPosTags();
			for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
				if(!posMap.containsKey(posTag.getKey())){
					posMap.put(posTag.getKey(), featurecount++);
					attributeList.add(new Attribute("POS_" + posTag.getKey()));
				}
			}
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				if(!clusterMap.containsKey(cluster)){
					clusterMap.put(cluster, featurecount++);
					attributeList.add(new Attribute("CLUSTER_" + cluster));
				}
			}
			Set<String> emoticonSet = tweet.getEmoticons();
			for(String emoticon : emoticonSet){
				if(!emoticonMap.containsKey(emoticon)){
					emoticonMap.put(emoticon, featurecount++);
					attributeList.add(new Attribute("EMO_" + emoticon));
				}
			}
		}
		Attribute allCaps = new Attribute("allCaps");
		attributeList.add(allCaps);
		featurecount++;
		
		Attribute hashtags = new Attribute("hashtags");
		attributeList.add(hashtags);
		featurecount++;
		
		Attribute punctuationCount = new Attribute("punctuationCount");
		attributeList.add(punctuationCount);
		featurecount++;
		
		Attribute punctuationLast = new Attribute("punctuationLast");
		attributeList.add(punctuationLast);
		featurecount++;
		
		Attribute emoticonLast = new Attribute("emoticonLast");
		attributeList.add(emoticonLast);
		featurecount++;
		
		Attribute elongatedWords = new Attribute("elongatedWords");
		attributeList.add(elongatedWords);
		featurecount++;
		
//		Attribute negationCount = new Attribute("negationCount");
//		attributeList.add(negationCount);
//		featurecount++;
		
		//senti140Uni
		Attribute senti140UniTotalCount = new Attribute("senti140UniTotalCount");
		attributeList.add(senti140UniTotalCount);
		featurecount++;
		
		Attribute senti140UniTotalScore = new Attribute("senti140UniTotalScore");
		attributeList.add(senti140UniTotalScore);
		featurecount++;
		
		Attribute senti140UniMaxScore = new Attribute("senti140UniMaxScore");
		attributeList.add(senti140UniMaxScore);
		featurecount++;
		
		Attribute senti140UniLastScore = new Attribute("senti140UniLastScore");
		attributeList.add(senti140UniLastScore);
		featurecount++;
		
		//hashtagUni
		Attribute hashtagUniTotalCount = new Attribute("hashtagUniTotalCount");
		attributeList.add(hashtagUniTotalCount);
		featurecount++;
		
		Attribute hashtagUniTotalScore = new Attribute("hashtagUniTotalScore");
		attributeList.add(hashtagUniTotalScore);
		featurecount++;
		
		Attribute hashtagUniMaxScore = new Attribute("hashtagUniMaxScore");
		attributeList.add(hashtagUniMaxScore);
		featurecount++;
		
		Attribute hashtagUniLastScore = new Attribute("hashtagUniLastScore");
		attributeList.add(hashtagUniLastScore);
		featurecount++;
		
		//senti140Bi
		Attribute senti140BiTotalCount = new Attribute("senti140BiTotalCount");
		attributeList.add(senti140BiTotalCount);
		featurecount++;
		
		Attribute senti140BiTotalScore = new Attribute("senti140BiTotalScore");
		attributeList.add(senti140BiTotalScore);
		featurecount++;
		
		Attribute senti140BiMaxScore = new Attribute("senti140BiMaxScore");
		attributeList.add(senti140BiMaxScore);
		featurecount++;
		
		Attribute senti140BiLastScore = new Attribute("senti140BiLastScore");
		attributeList.add(senti140BiLastScore);
		featurecount++;
		
		//hashtagBi
		Attribute hashtagBiTotalCount = new Attribute("hashtagBiTotalCount");
		attributeList.add(hashtagBiTotalCount);
		featurecount++;
		
		Attribute hashtagBiTotalScore = new Attribute("hashtagBiTotalScore");
		attributeList.add(hashtagBiTotalScore);
		featurecount++;
		
		Attribute hashtagBiMaxScore = new Attribute("hashtagBiMaxScore");
		attributeList.add(hashtagBiMaxScore);
		featurecount++;
		
		Attribute hashtagBiLastScore = new Attribute("hashtagBiLastScore");
		attributeList.add(hashtagBiLastScore);
		featurecount++;
		
		//MPQA
		Attribute MPQATotalCount = new Attribute("MPQATotalCount");
		attributeList.add(MPQATotalCount);
		featurecount++;
		
		Attribute MPQATotalScore = new Attribute("MPQATotalScore");
		attributeList.add(MPQATotalScore);
		featurecount++;
		
		Attribute MPQAMaxScore = new Attribute("MPQAMaxScore");
		attributeList.add(MPQAMaxScore);
		featurecount++;
		
		Attribute MPQALastScore = new Attribute("MPQALastScore");
		attributeList.add(MPQALastScore);
		featurecount++;
		
		//BingLiu
		Attribute BingLiuTotalCount = new Attribute("BingLiuTotalCount");
		attributeList.add(BingLiuTotalCount);
		featurecount++;
		
		Attribute BingLiuTotalScore = new Attribute("BingLiuTotalScore");
		attributeList.add(BingLiuTotalScore);
		featurecount++;
		
		Attribute BingLiuMaxScore = new Attribute("BingLiuMaxScore");
		attributeList.add(BingLiuMaxScore);
		featurecount++;
		
		Attribute BingLiuLastScore = new Attribute("BingLiuLastScore");
		attributeList.add(BingLiuLastScore);
		featurecount++;
		
		//NRC
		Attribute NRCTotalCount = new Attribute("NRCTotalCount");
		attributeList.add(NRCTotalCount);
		featurecount++;
		
		Attribute NRCTotalScore = new Attribute("NRCTotalScore");
		attributeList.add(NRCTotalScore);
		featurecount++;
		
		Attribute NRCMaxScore = new Attribute("NRCMaxScore");
		attributeList.add(NRCMaxScore);
		featurecount++;
		
		Attribute NRCLastScore = new Attribute("NRCLastScore");
		attributeList.add(NRCLastScore);
		featurecount++;
		
	    ArrayList<String> fvClassVal = new ArrayList<String>();
	    fvClassVal.add("positive");
	    fvClassVal.add("neutral");
	    fvClassVal.add("negative");
	    Attribute classAttribute = new Attribute("Class", fvClassVal);
	    attributeList.add(classAttribute);
		featurecount++;
		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
		trainingSet.setClassIndex(classAttribute.index());
		
		for(Tweet tweet : tweetList){
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
			Set<String> nGramSet = tweet.getNGrams(4);
			for (String nGram : nGramSet){
				Integer index = nGramMap.get(nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}			
			Set<String> CharNGramSet = tweet.getCharNGrams();
			for (String nGram : CharNGramSet){
				Integer index = CharNGramMap.get(nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Map<String, Integer> posTags = tweet.getPosTags();
			for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
				Integer index = posMap.get(posTag.getKey());
				if(index != null){
					instance.setValue(index, posTag.getValue());
				}
			}
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = clusterMap.get(cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> emoticonSet = tweet.getEmoticons();
			for(String emoticon : emoticonSet){
				Integer index = emoticonMap.get(emoticon);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			instance.setValue(allCaps, tweet.getAllCapsCount());
			instance.setValue(hashtags, tweet.getHashtagCount());
			instance.setValue(punctuationCount, tweet.getPunctuation());
			if(tweet.isLastPunctuation()){
				instance.setValue(punctuationLast, 1);
			}
			else{
				instance.setValue(punctuationLast, 0);
			}
			if(tweet.isLastEmoticon()){
				instance.setValue(emoticonLast, 1);
			}
			else{
				instance.setValue(emoticonLast, 0);
			}
			instance.setValue(elongatedWords, tweet.getElongatedCount());
//			instance.setValue(negationCount, tweet.getNegationCount());
			List<Double> senti140Uni = tweet.getLexiScores(senti140UniLexi);
			instance.setValue(senti140UniTotalCount, senti140Uni.get(0));
			instance.setValue(senti140UniTotalScore, senti140Uni.get(1));
			instance.setValue(senti140UniMaxScore, senti140Uni.get(2));
			instance.setValue(senti140UniLastScore, senti140Uni.get(3));
			List<Double> hashtagUni = tweet.getLexiScores(hashtagUniLexi);
			instance.setValue(hashtagUniTotalCount, hashtagUni.get(0));
			instance.setValue(hashtagUniTotalScore, hashtagUni.get(1));
			instance.setValue(hashtagUniMaxScore, hashtagUni.get(2));
			instance.setValue(hashtagUniLastScore, hashtagUni.get(3));
			
			List<Double> senti140Bi = tweet.getLexiScores(senti140BiLexi);
			instance.setValue(senti140BiTotalCount, senti140Bi.get(0));
			instance.setValue(senti140BiTotalScore, senti140Bi.get(1));
			instance.setValue(senti140BiMaxScore, senti140Bi.get(2));
			instance.setValue(senti140BiLastScore, senti140Bi.get(3));
			List<Double> hashtagBi = tweet.getLexiScores(hashtagBiLexi);
			instance.setValue(hashtagBiTotalCount, hashtagBi.get(0));
			instance.setValue(hashtagBiTotalScore, hashtagBi.get(1));
			instance.setValue(hashtagBiMaxScore, hashtagBi.get(2));
			instance.setValue(hashtagBiLastScore, hashtagBi.get(3));
			
			List<Double> MPQA = tweet.getLexiScores(MPQALexi);
			instance.setValue(MPQATotalCount, MPQA.get(0));
			instance.setValue(MPQATotalScore, MPQA.get(1));
			instance.setValue(MPQAMaxScore, MPQA.get(2));
			instance.setValue(MPQALastScore, MPQA.get(3));
			
			List<Double> BingLiu = tweet.getLexiScores(BingLiuLexi);
			instance.setValue(BingLiuTotalCount, BingLiu.get(0));
			instance.setValue(BingLiuTotalScore, BingLiu.get(1));
			instance.setValue(BingLiuMaxScore, BingLiu.get(2));
			instance.setValue(BingLiuLastScore, BingLiu.get(3));
			
			List<Double> NRC = tweet.getLexiScores(NRCLexi);
			instance.setValue(NRCTotalCount, NRC.get(0));
			instance.setValue(NRCTotalScore, NRC.get(1));
			instance.setValue(NRCMaxScore, NRC.get(2));
			instance.setValue(NRCLastScore, NRC.get(3));
			
			instance.setValue(classAttribute, tweet.getSentiment());
			
			
			trainingSet.add(instance);
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(trainingSet);
		saver.setFile(new File("resources/arff/train_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("train_" + savename + " saved");
	}
	
	public static void trainGUMLTLT(Set<Tweet> tweetList, String savename) throws IOException{
		System.out.println("Starting Weka Train");
		System.out.println("Tweets: " +  tweetList.size());
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
    	
//    	System.out.println("1");
    	Map<String, Double> sentiWordNet = loadSentiWordNet(); 
//    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
//    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
//    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
//    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
//    	Map<String, Double> MPQALexi = loadMPQA();
//    	Map<String, Double> BingLiuLexi = loadBingLiu();
//    	Map<String, Double> NRCLexi = loadNRC();
		
		int featurecount = 0;
//		int tweetcount = 0;
		Map<String, Integer> nGramMap = new HashMap<String, Integer>();
		Map<String, Integer> stemMap = new HashMap<String, Integer>();
		Map<String, Integer> clusterMap = new HashMap<String, Integer>();
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
//		System.out.println("2");
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
//			System.out.println("3");
			Set<String> nGramSet = tweet.getNGrams(1);
			for (String nGram : nGramSet){
				if(!nGramMap.containsKey(nGram)){
					nGramMap.put(nGram, featurecount++);
					attributeList.add(new Attribute("NGRAM_" + nGram));
				}
			}
//			System.out.println("4");
			Set<String> stemSet = tweet.getStems();
			for (String stem : stemSet){
				if(!stemMap.containsKey(stem)){
					stemMap.put(stem, featurecount++);
					attributeList.add(new Attribute("STEM_" + stem));
				}
			}
//			System.out.println("5");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				if(!clusterMap.containsKey(cluster)){
					clusterMap.put(cluster, featurecount++);
					attributeList.add(new Attribute("CLUSTER_" + cluster));
				}
			}
//			System.out.println("6");
			Set<String> clusterRawSet = tweet.getClusters(tweet.getRawWordList());
			for(String cluster : clusterRawSet){
				if(!clusterMap.containsKey(cluster)){
					clusterMap.put(cluster, featurecount++);
					attributeList.add(new Attribute("CLUSTER_" + cluster));
				}
			}
//			System.out.println("7");
			Set<String> clusterCollapsedSet = tweet.getClusters(tweet.getCollapsedWordList());
			for(String cluster : clusterCollapsedSet){
				if(!clusterMap.containsKey(cluster)){
					clusterMap.put(cluster, featurecount++);
					attributeList.add(new Attribute("CLUSTER_" + cluster));
				}
			}
		}
//		System.out.println("8");
		Attribute sentiWordNetPos = new Attribute("sentiWordNetPos");
		attributeList.add(sentiWordNetPos);
		featurecount++;
		Attribute sentiWordNetNeg = new Attribute("sentiWordNetNeg");
		attributeList.add(sentiWordNetNeg);
		featurecount++;
//		System.out.println("9");
		
		
	    ArrayList<String> fvClassVal = new ArrayList<String>();
	    fvClassVal.add("positive");
	    fvClassVal.add("neutral");
	    fvClassVal.add("negative");
	    Attribute classAttribute = new Attribute("Class", fvClassVal);
	    attributeList.add(classAttribute);
		featurecount++;
		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
		trainingSet.setClassIndex(classAttribute.index());
		
		for(Tweet tweet : tweetList){
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
			Set<String> nGramSet = tweet.getNGrams(1);
			for (String nGram : nGramSet){
				Integer index = nGramMap.get(nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}			
			Set<String> stemSet = tweet.getStems();
			for (String stem : stemSet){
				Integer index = stemMap.get(stem);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = clusterMap.get(cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> clusterSetRaw = tweet.getClusters(tweet.getRawWordList());
			for(String cluster : clusterSetRaw){
				Integer index = clusterMap.get(cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> clusterSetCollapsed = tweet.getClusters(tweet.getCollapsedWordList());
			for(String cluster : clusterSetCollapsed){
				Integer index = clusterMap.get(cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			instance.setValue(sentiWordNetPos, tweet.getSentiWordNetScore("+", sentiWordNet));
			instance.setValue(sentiWordNetNeg, tweet.getSentiWordNetScore("-", sentiWordNet));
			
			instance.setValue(classAttribute, tweet.getSentiment());
			
			
			trainingSet.add(instance);
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(trainingSet);
		saver.setFile(new File("resources/arff/train_GUMLTLT_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("train_GUMLTLT_" + savename + " saved");
	}
	
	public static void trainKLUE(Set<Tweet> tweetList, String savename) throws IOException{
		System.out.println("Starting Weka Train");
		System.out.println("Tweets: " +  tweetList.size());
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
    	
//    	System.out.println("1");
    	Map<String, Double> afinnLexi = loadAFINN();
//    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
//    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
//    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
//    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
//    	Map<String, Double> MPQALexi = loadMPQA();
//    	Map<String, Double> BingLiuLexi = loadBingLiu();
//    	Map<String, Double> NRCLexi = loadNRC();
		
		int featurecount = 0;
//		int tweetcount = 0;
		Map<String, Integer> nGramMap = new HashMap<String, Integer>();
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
//		System.out.println("2");
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
//			System.out.println("3");
			Set<String> nGramSet = tweet.getNGrams(2);
			for (String nGram : nGramSet){
				if(!nGramMap.containsKey(nGram)){
					nGramMap.put(nGram, featurecount++);
					attributeList.add(new Attribute("NGRAM_" + nGram));
				}
			}
		}
//		System.out.println("8");
		Attribute sentiAFINNPos = new Attribute("sentiAFINNPos");
		attributeList.add(sentiAFINNPos);
		featurecount++;
		Attribute sentiAFINNNeg = new Attribute("sentiAFINNNeg");
		attributeList.add(sentiAFINNNeg);
		featurecount++;
		Attribute sentiAFINNTotal = new Attribute("sentiAFINNTotal");
		attributeList.add(sentiAFINNTotal);
		featurecount++;
		Attribute sentiAFINNScore = new Attribute("sentiAFINNScore");
		attributeList.add(sentiAFINNScore);
		featurecount++;
		
		Attribute sentiEmoPos = new Attribute("sentiEmoPos");
		attributeList.add(sentiEmoPos);
		featurecount++;
		Attribute sentiEmoNeg = new Attribute("sentiEmoNeg");
		attributeList.add(sentiEmoNeg);
		featurecount++;
		Attribute sentiEmoTotal = new Attribute("sentiEmoTotal");
		attributeList.add(sentiEmoTotal);
		featurecount++;
		Attribute sentiEmoScore = new Attribute("sentiEmoScore");
		attributeList.add(sentiEmoScore);
		featurecount++;
//		System.out.println("9");
		
		
	    ArrayList<String> fvClassVal = new ArrayList<String>();
	    fvClassVal.add("positive");
	    fvClassVal.add("neutral");
	    fvClassVal.add("negative");
	    Attribute classAttribute = new Attribute("Class", fvClassVal);
	    attributeList.add(classAttribute);
		featurecount++;
		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
		trainingSet.setClassIndex(classAttribute.index());
		
		for(Tweet tweet : tweetList){
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
			Set<String> nGramSet = tweet.getNGrams(2);
			for (String nGram : nGramSet){
				Integer index = nGramMap.get(nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			List<Double> afinnScore = tweet.getAFINNScore(afinnLexi);
			instance.setValue(sentiAFINNPos, afinnScore.get(0));
			instance.setValue(sentiAFINNNeg, afinnScore.get(1));
			instance.setValue(sentiAFINNTotal, afinnScore.get(2));
			instance.setValue(sentiAFINNScore, afinnScore.get(3));
			
			List<Double> emoScore = tweet.getEmoScore();
			instance.setValue(sentiEmoPos, emoScore.get(0));
			instance.setValue(sentiEmoNeg, emoScore.get(1));
			instance.setValue(sentiEmoTotal, emoScore.get(2));
			instance.setValue(sentiEmoScore, emoScore.get(3));
			
			instance.setValue(classAttribute, tweet.getSentiment());
			
			
			trainingSet.add(instance);
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(trainingSet);
		saver.setFile(new File("resources/arff/train_KLUE_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("train_KLUE_" + savename + " saved");
	}
	
//	public static void trainString(Map<HashMap<String, String>, String> tweetList) throws Exception {
//		System.out.println("Starting Weka Train");
//		System.out.println("Tweets: " +  tweetList.size());
//		FastVector attributeList = new FastVector(2);
//		Attribute attribute = new Attribute("text",  (FastVector) null);
//		attributeList.addElement(attribute);		
//		FastVector fvClassVal = new FastVector(3);
//		fvClassVal.addElement("positive");
//		fvClassVal.addElement("neutral");
//		fvClassVal.addElement("negative");
//		Attribute classAttribute = new Attribute("Class", fvClassVal);
//		attributeList.addElement(classAttribute);
//		Instances trainingSet = new Instances("test", attributeList, tweetList.size());
//		trainingSet.setClassIndex(featureList.size());
//		System.out.println("1");
//		int tweetcount = 0;
//		for (Map.Entry<HashMap<String,String>, String> tweet : tweetList.entrySet()){
//			Instance instance = new Instance(2);
//			String tweetString = "";
//			for (Map.Entry<String, String> wordMap : tweet.getKey().entrySet()){
//				String word = wordMap.getKey();
//				if (word.equals("") || featureList.contains(word)){
//					continue;
//				}
//				else{
//					tweetString = tweetString + word + " ";
//				}		
//			}
//			System.out.println("1.1");
//			instance.setValue((Attribute) attributeList.elementAt(0), tweetString);
//			instance.setValue((Attribute) attributeList.elementAt(1), tweet.getValue());
//			trainingSet.add(instance);
//			System.out.println("1.2");
//			tweetcount++;
//			if (tweetcount % 50 == 0) System.out.println("Tweet: " + tweetcount);
//		}
//		System.out.println("2");
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(trainingSet);
//		saver.setFile(new File("resources/arff/trainString1000.arff"));
//		saver.writeBatch();
//	}
	
//	public static void test(Map<ArrayList<String>, String> tweetList, String savename, String nameOfTrain) throws Exception {
//		System.out.println("Starting Weka Test");
//		System.out.println("Tweets: " +  tweetList.size());
//		String trainname = "train_" + savename;
//		if(!nameOfTrain.equals("")) trainname = nameOfTrain;
//		
//		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
//		Instances train = new Instances(reader);
//		train.setClassIndex(train.numAttributes() - 1);
//		reader.close();
//		
//		train.delete();
//		
//		Map<String, Integer> featureMap = new HashMap<String, Integer>();
//		for (int i = 0; i < train.numAttributes() - 1; i++){
//			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
//		}
//
////		System.out.println("1");
////		FastVector attributeList = new FastVector(featureList.size() + 1);
////		for (String feature : featureList){
////			Attribute attribute = new Attribute(feature);
////			attributeList.addElement(attribute);
////		}
////		System.out.println("2");
////		FastVector fvClassVal = new FastVector(3);
////		fvClassVal.addElement("positive");
////		fvClassVal.addElement("neutral");
////		fvClassVal.addElement("negative");
////		Attribute classAttribute = new Attribute("Class", fvClassVal);
////		attributeList.addElement(classAttribute);
////		System.out.println("3");
////		Instances testSet = new Instances("test", attributeList, tweetList.size());
////		testSet.setClassIndex(featureList.size());
//		System.out.println("1");
//		int tweetcount = 0;
//		for (Map.Entry<ArrayList<String>, String> tweet : tweetList.entrySet()){
//			String sentiValue = tweet.getValue();
//			SparseInstance instance = new SparseInstance(0);
//			for (String word : tweet.getKey()){
//				if (word.equals("")) continue;
//				Integer index = featureMap.get(word);
//				if (index != null){
//				instance.setValue(index, 1);
//				}
////				Attribute attr = train.attribute(word);
////				if (attr != null){
////					instance.setValue(attr.index(), 1);
////				}
//			}
//			instance.setValue(train.classAttribute(), sentiValue);
//			train.add(instance);
//			tweetcount++;
//			if (tweetcount % 1000 == 0) System.out.println("Tweet: " + tweetcount);
//		}
//		System.out.println("2");
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(train);
//		saver.setFile(new File("resources/arff/test_" + savename + ".arff"));
//		saver.writeBatch();
//		System.out.println("test_" + savename + " saved");
//	}
	
	public static void testNRC(Set<Tweet> tweetList, String savename, String nameOfTrain) throws Exception {
		System.out.println("Starting Weka Test");
		System.out.println("Tweets: " +  tweetList.size());
		String trainname = "train_" + savename;
		if(!nameOfTrain.equals("")) trainname = nameOfTrain;
		
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		train.delete();
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
    	Map<String, Double> MPQALexi = loadMPQA();
    	Map<String, Double> BingLiuLexi = loadBingLiu();
    	Map<String, Double> NRCLexi = loadNRC();
		
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}

		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(4);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> CharNGramSet = tweet.getCharNGrams();
			for (String nGram : CharNGramSet){
				Integer index = featureMap.get("CHARNGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("3");
			Map<String, Integer> posTags = tweet.getPosTags();
			for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
				Integer index = featureMap.get("POS_" +posTag.getKey());
				if(index != null){
					instance.setValue(index, posTag.getValue());
				}
			}
//			System.out.println("4");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("5");
			Set<String> emoticonSet = tweet.getEmoticons();
			for(String emoticon : emoticonSet){
				Integer index = featureMap.get("EMO_" + emoticon);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			instance.setValue(featureMap.get("allCaps"), tweet.getAllCapsCount());
			instance.setValue(featureMap.get("hashtags"), tweet.getHashtagCount());
			instance.setValue(featureMap.get("punctuationCount"), tweet.getPunctuation());
			if(tweet.isLastPunctuation()){
				instance.setValue(featureMap.get("punctuationLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("punctuationLast"), 0);
			}
			if(tweet.isLastEmoticon()){
				instance.setValue(featureMap.get("emoticonLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("emoticonLast"), 0);
			}
//			System.out.println("7");
			instance.setValue(featureMap.get("elongatedWords"), tweet.getElongatedCount());
			List<Double> senti140Uni = tweet.getLexiScores(senti140UniLexi);
			instance.setValue(featureMap.get("senti140UniTotalCount"), senti140Uni.get(0));
			instance.setValue(featureMap.get("senti140UniTotalScore"), senti140Uni.get(1));
			instance.setValue(featureMap.get("senti140UniMaxScore"), senti140Uni.get(2));
			instance.setValue(featureMap.get("senti140UniLastScore"), senti140Uni.get(3));
			List<Double> hashtagUni = tweet.getLexiScores(hashtagUniLexi);
			instance.setValue(featureMap.get("hashtagUniTotalCount"), hashtagUni.get(0));
			instance.setValue(featureMap.get("hashtagUniTotalScore"), hashtagUni.get(1));
			instance.setValue(featureMap.get("hashtagUniMaxScore"), hashtagUni.get(2));
			instance.setValue(featureMap.get("hashtagUniLastScore"), hashtagUni.get(3));
			
			List<Double> senti140Bi = tweet.getLexiScores(senti140BiLexi);
			instance.setValue(featureMap.get("senti140BiTotalCount"), senti140Bi.get(0));
			instance.setValue(featureMap.get("senti140BiTotalScore"), senti140Bi.get(1));
			instance.setValue(featureMap.get("senti140BiMaxScore"), senti140Bi.get(2));
			instance.setValue(featureMap.get("senti140BiLastScore"), senti140Bi.get(3));
			List<Double> hashtagBi = tweet.getLexiScores(hashtagBiLexi);
			instance.setValue(featureMap.get("hashtagBiTotalCount"), hashtagBi.get(0));
			instance.setValue(featureMap.get("hashtagBiTotalScore"), hashtagBi.get(1));
			instance.setValue(featureMap.get("hashtagBiMaxScore"), hashtagBi.get(2));
			instance.setValue(featureMap.get("hashtagBiLastScore"), hashtagBi.get(3));
			
			List<Double> MPQA = tweet.getLexiScores(MPQALexi);
			instance.setValue(featureMap.get("MPQATotalCount"), MPQA.get(0));
			instance.setValue(featureMap.get("MPQATotalScore"), MPQA.get(1));
			instance.setValue(featureMap.get("MPQAMaxScore"), MPQA.get(2));
			instance.setValue(featureMap.get("MPQALastScore"), MPQA.get(3));
			
			List<Double> BingLiu = tweet.getLexiScores(BingLiuLexi);
			instance.setValue(featureMap.get("BingLiuTotalCount"), BingLiu.get(0));
			instance.setValue(featureMap.get("BingLiuTotalScore"), BingLiu.get(1));
			instance.setValue(featureMap.get("BingLiuMaxScore"), BingLiu.get(2));
			instance.setValue(featureMap.get("BingLiuLastScore"), BingLiu.get(3));
			
			List<Double> NRC = tweet.getLexiScores(NRCLexi);
			instance.setValue(featureMap.get("NRCTotalCount"), NRC.get(0));
			instance.setValue(featureMap.get("NRCTotalScore"), NRC.get(1));
			instance.setValue(featureMap.get("NRCMaxScore"), NRC.get(2));
			instance.setValue(featureMap.get("NRCLastScore"), NRC.get(3));
			
//			System.out.println("8");
			instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
//			System.out.println("10");
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(train);
		saver.setFile(new File("resources/arff/test_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("test_" + savename + " saved");
	}
	
	public static void testGUMLTLT(Set<Tweet> tweetList, String savename, String nameOfTrain) throws Exception {
		System.out.println("Starting Weka Test");
		System.out.println("Tweets: " +  tweetList.size());
		String trainname = "train_GUMLTLT_" + savename;
		if(!nameOfTrain.equals("")) trainname = nameOfTrain;
		System.out.println("Open File: " +  trainname);
		
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		train.delete();
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> sentiWordNet = loadSentiWordNet(); 
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}

		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(1);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> stemSet = tweet.getStems();
			for (String stem : stemSet){
				Integer index = featureMap.get("STEM_" + stem);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("4");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			
			Set<String> clusterSetRaw = tweet.getClusters(tweet.getRawWordList());
			for(String cluster : clusterSetRaw){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			
			Set<String> clusterSetCollapsed = tweet.getClusters(tweet.getCollapsedWordList());
			for(String cluster : clusterSetCollapsed){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			instance.setValue(featureMap.get("sentiWordNetPos"), tweet.getSentiWordNetScore("+", sentiWordNet));
			instance.setValue(featureMap.get("sentiWordNetNeg"), tweet.getSentiWordNetScore("-", sentiWordNet));

			
//			System.out.println("8");
			instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
//			System.out.println("10");
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(train);
		saver.setFile(new File("resources/arff/test_GUMLTLT_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("test_GUMLTLT_" + savename + " saved");
	}
	
	public static void testKLUE(Set<Tweet> tweetList, String savename, String nameOfTrain) throws Exception {
		System.out.println("Starting Weka Test");
		System.out.println("Tweets: " +  tweetList.size());
		String trainname = "train_KLUE_" + savename;
		if(!nameOfTrain.equals("")) trainname = nameOfTrain;
		System.out.println("Open File: " +  trainname);
		
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		train.delete();
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> afinnLexi = loadAFINN(); 
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}

		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(2);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			List<Double> afinnScore = tweet.getAFINNScore(afinnLexi);
			instance.setValue(featureMap.get("sentiAFINNPos"), afinnScore.get(0));
			instance.setValue(featureMap.get("sentiAFINNNeg"), afinnScore.get(1));
			instance.setValue(featureMap.get("sentiAFINNTotal"), afinnScore.get(2));
			instance.setValue(featureMap.get("sentiAFINNScore"), afinnScore.get(3));
			
			List<Double> emoScore = tweet.getEmoScore();
			instance.setValue(featureMap.get("sentiEmoPos"), emoScore.get(0));
			instance.setValue(featureMap.get("sentiEmoNeg"), emoScore.get(1));
			instance.setValue(featureMap.get("sentiEmoTotal"), emoScore.get(2));
			instance.setValue(featureMap.get("sentiEmoScore"), emoScore.get(3));

			
//			System.out.println("8");
			instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
//			System.out.println("10");
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(train);
		saver.setFile(new File("resources/arff/test_KLUE_" + savename + ".arff"));
		saver.writeBatch();
		System.out.println("test_KLUE_" + savename + " saved");
	}
	
	public static void eval(String trainfile, String testfile) throws Exception{
		System.out.println("Starting Weka Eval");
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainfile +".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		BufferedReader reader2 = new BufferedReader(new FileReader("resources/arff/" + testfile +".arff"));
		Instances test = new Instances(reader2);
		test.setClassIndex(test.numAttributes() - 1);
		reader2.close();
		
		
//		Classifier classi = (Classifier) new NaiveBayes();
		LibLINEAR classi = new LibLINEAR();
//		Logistic classi = new Logistic();
//		Classifier classi = (Classifier) new LibLINEAR();
//		Classifier classi = (Classifier) new SMO();
		classi.setCost(0.42); //NRC 0.005 //GUMLTLT 0.12 //KLUE 0.42
//		classi.setSVMType(new SelectedTag(3, LibLINEAR.TAGS_SVMTYPE));
		classi.buildClassifier(train);
//		classi.setCost(0.001);
		
//		System.out.println(test.size());
//		for (int i = 0; i < test.size(); i++){
//			System.out.println(test.get(i).classValue());
//		}
//		classi.classifyInstance(test.get(0));
		
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(classi, test);
		
		String strSummary = eval.toSummaryString();
		System.out.println(strSummary);
		System.out.println("F1 / pre / rec pos: " + eval.fMeasure(0) + " / " + eval.precision(0) + " / " + eval.recall(0));
		System.out.println("F1 / pre / rec neu: " + eval.fMeasure(1) + " / " + eval.precision(1) + " / " + eval.recall(1));
		System.out.println("F1 / pre / rec neg: " + eval.fMeasure(2) + " / " + eval.precision(2) + " / " + eval.recall(2));
		System.out.println();
		double[][] matrix = eval.confusionMatrix();
		System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
		System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
		System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
		System.out.println();
		System.out.println("TestPos: " + (int) matrix[0][0] + " / " +  (int) (matrix[0][0] +  matrix[0][1] +  matrix[0][2]));
		System.out.println("TestNeu: " + (int) matrix[1][1] + " / " +  (int) (matrix[1][0] +  matrix[1][1] +  matrix[1][2]));
		System.out.println("TestNeg: " + (int) matrix[2][2] + " / " +  (int) (matrix[2][0] +  matrix[2][1] +  matrix[2][2]));
		System.out.println();
		double precisionA = matrix[0][0] / (matrix[0][0] + matrix[1][0] + matrix[2][0]);
		double precisionB = matrix[1][1] / (matrix[1][1] + matrix[2][1] + matrix[0][1]);
		double precisionC = matrix[2][2] / (matrix[2][2] + matrix[0][2] + matrix[1][2]);
		double precision = (precisionA + precisionB + precisionC) / 3;
		
		double recallA = matrix[0][0] / (matrix[0][0] + matrix[0][1] + matrix[0][2]);
		double recallB = matrix[1][1] / (matrix[1][1] + matrix[1][2] + matrix[1][0]);
		double recallC = matrix[2][2] / (matrix[2][2] + matrix[2][0] + matrix[2][1]);
		double recall = (recallA + recallB + recallC) / 3;
		
		double f1 = 2 * ((precision * recall) / (precision + recall));
		
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("f1: " + f1);
		System.out.println("f1 without neu: " + (eval.fMeasure(0) + eval.fMeasure(2)) /2);
	}
    public static Map<Tweet, Integer> printPredNRC(Set<Tweet> tweetList, String nameOfTrain) throws Exception{
        String trainname = "";
        if(!nameOfTrain.equals("")){
            trainname = nameOfTrain;
        }
        else{
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
        Instances train = new Instances(reader);
        train.setClassIndex(train.numAttributes() - 1);
        reader.close();
        
        LibLINEAR classifier = new LibLINEAR();
        classifier.setCost(0.005);
        classifier.buildClassifier(train);
        
        train.delete();
        
        Tagger tagger = new Tagger();
        tagger.loadModel("resources/tagger/model.20120919.txt");
        
        Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
        Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
        Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
        Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
        Map<String, Double> MPQALexi = loadMPQA();
        Map<String, Double> BingLiuLexi = loadBingLiu();
        Map<String, Double> NRCLexi = loadNRC();
        
        Map<String, Integer> featureMap = new HashMap<String, Integer>();
        for (int i = 0; i < train.numAttributes(); i++){
            featureMap.put(train.attribute(i).name(), train.attribute(i).index());
        }
        
        Map<Tweet, Integer> resultMap = new HashMap<Tweet, Integer>();
        for(Tweet tweet : tweetList){
            tweet.tokenizeAndTag(tagger);
            tweet.negate();
            SparseInstance instance = new SparseInstance(0);
//          instance.setValue();
//          System.out.println("1");
            Set<String> nGramSet = tweet.getNGrams(4);
//          System.out.println("2");
            for (String nGram : nGramSet){
                Integer index = featureMap.get("NGRAM_" + nGram);
                if(index != null){
                    instance.setValue(index, 1);
                }
            }
            Set<String> CharNGramSet = tweet.getCharNGrams();
            for (String nGram : CharNGramSet){
                Integer index = featureMap.get("CHARNGRAM_" + nGram);
                if(index != null){
                    instance.setValue(index, 1);
                }
            }
//          System.out.println("3");
            Map<String, Integer> posTags = tweet.getPosTags();
            for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
                Integer index = featureMap.get("POS_" +posTag.getKey());
                if(index != null){
                    instance.setValue(index, posTag.getValue());
                }
            }
//          System.out.println("4");
            Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
            for(String cluster : clusterSet){
                Integer index = featureMap.get("CLUSTER_" + cluster);
                if(index != null){
                    instance.setValue(index, 1);
                }
            }
//          System.out.println("5");
            Set<String> emoticonSet = tweet.getEmoticons();
            for(String emoticon : emoticonSet){
                Integer index = featureMap.get("EMO_" + emoticon);
                if(index != null){
                    instance.setValue(index, 1);
                }
            }
//          System.out.println("6");
            instance.setValue(featureMap.get("allCaps"), tweet.getAllCapsCount());
            instance.setValue(featureMap.get("hashtags"), tweet.getHashtagCount());
            instance.setValue(featureMap.get("punctuationCount"), tweet.getPunctuation());
            if(tweet.isLastPunctuation()){
                instance.setValue(featureMap.get("punctuationLast"), 1);
            }
            else{
                instance.setValue(featureMap.get("punctuationLast"), 0);
            }
            if(tweet.isLastEmoticon()){
                instance.setValue(featureMap.get("emoticonLast"), 1);
            }
            else{
                instance.setValue(featureMap.get("emoticonLast"), 0);
            }
//          System.out.println("7");
            instance.setValue(featureMap.get("elongatedWords"), tweet.getElongatedCount());
            List<Double> senti140Uni = tweet.getLexiScores(senti140UniLexi);
            instance.setValue(featureMap.get("senti140UniTotalCount"), senti140Uni.get(0));
            instance.setValue(featureMap.get("senti140UniTotalScore"), senti140Uni.get(1));
            instance.setValue(featureMap.get("senti140UniMaxScore"), senti140Uni.get(2));
            instance.setValue(featureMap.get("senti140UniLastScore"), senti140Uni.get(3));
            List<Double> hashtagUni = tweet.getLexiScores(hashtagUniLexi);
            instance.setValue(featureMap.get("hashtagUniTotalCount"), hashtagUni.get(0));
            instance.setValue(featureMap.get("hashtagUniTotalScore"), hashtagUni.get(1));
            instance.setValue(featureMap.get("hashtagUniMaxScore"), hashtagUni.get(2));
            instance.setValue(featureMap.get("hashtagUniLastScore"), hashtagUni.get(3));
            
            List<Double> senti140Bi = tweet.getLexiScores(senti140BiLexi);
            instance.setValue(featureMap.get("senti140BiTotalCount"), senti140Bi.get(0));
            instance.setValue(featureMap.get("senti140BiTotalScore"), senti140Bi.get(1));
            instance.setValue(featureMap.get("senti140BiMaxScore"), senti140Bi.get(2));
            instance.setValue(featureMap.get("senti140BiLastScore"), senti140Bi.get(3));
            List<Double> hashtagBi = tweet.getLexiScores(hashtagBiLexi);
            instance.setValue(featureMap.get("hashtagBiTotalCount"), hashtagBi.get(0));
            instance.setValue(featureMap.get("hashtagBiTotalScore"), hashtagBi.get(1));
            instance.setValue(featureMap.get("hashtagBiMaxScore"), hashtagBi.get(2));
            instance.setValue(featureMap.get("hashtagBiLastScore"), hashtagBi.get(3));
            
            List<Double> MPQA = tweet.getLexiScores(MPQALexi);
            instance.setValue(featureMap.get("MPQATotalCount"), MPQA.get(0));
            instance.setValue(featureMap.get("MPQATotalScore"), MPQA.get(1));
            instance.setValue(featureMap.get("MPQAMaxScore"), MPQA.get(2));
            instance.setValue(featureMap.get("MPQALastScore"), MPQA.get(3));
            
            List<Double> BingLiu = tweet.getLexiScores(BingLiuLexi);
            instance.setValue(featureMap.get("BingLiuTotalCount"), BingLiu.get(0));
            instance.setValue(featureMap.get("BingLiuTotalScore"), BingLiu.get(1));
            instance.setValue(featureMap.get("BingLiuMaxScore"), BingLiu.get(2));
            instance.setValue(featureMap.get("BingLiuLastScore"), BingLiu.get(3));
            
            List<Double> NRC = tweet.getLexiScores(NRCLexi);
            instance.setValue(featureMap.get("NRCTotalCount"), NRC.get(0));
            instance.setValue(featureMap.get("NRCTotalScore"), NRC.get(1));
            instance.setValue(featureMap.get("NRCMaxScore"), NRC.get(2));
            instance.setValue(featureMap.get("NRCLastScore"), NRC.get(3));
            
//          System.out.println("8");
            instance.setValue(train.classAttribute(), tweet.getSentiment());
            
//          System.out.println("9");
            train.add(instance);
            
//          classifier.setSVMType();;
            
            resultMap.put(tweet, (int) classifier.classifyInstance(train.lastInstance()));
//          System.out.println("10");
        }
        
//      Iterator<Instance> iterator = train.iterator();
//      while (iterator.hasNext()){
//          iterator.getClass()
//      }
        return resultMap;
    }	
	
	public static Map<Tweet,ClassificationResult> printPredNRCNew(Set<Tweet> tweetList, String nameOfTrain) throws Exception{
		String trainname = "";
		if(!nameOfTrain.equals("")){
			trainname = nameOfTrain;
		}
		else{
			return null;
		}
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
//        NaiveBayes classifier = new NaiveBayes();
		LibLINEAR classifier = new LibLINEAR();
        classifier.setProbabilityEstimates(true);
        classifier.setSVMType(new SelectedTag(0, LibLINEAR.TAGS_SVMTYPE));
//        classifier.setCost(0.005);
        classifier.setCost(0.05);
        classifier.buildClassifier(train);
        
		train.delete();
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
    	Map<String, Double> MPQALexi = loadMPQA();
    	Map<String, Double> BingLiuLexi = loadBingLiu();
    	Map<String, Double> NRCLexi = loadNRC();
		
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}
	    
		Map<Tweet,ClassificationResult> resultMap = new HashMap<Tweet,ClassificationResult>();
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(4);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> CharNGramSet = tweet.getCharNGrams();
			for (String nGram : CharNGramSet){
				Integer index = featureMap.get("CHARNGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("3");
			Map<String, Integer> posTags = tweet.getPosTags();
			for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
				Integer index = featureMap.get("POS_" +posTag.getKey());
				if(index != null){
					instance.setValue(index, posTag.getValue());
				}
			}
//			System.out.println("4");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("5");
			Set<String> emoticonSet = tweet.getEmoticons();
			for(String emoticon : emoticonSet){
				Integer index = featureMap.get("EMO_" + emoticon);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			instance.setValue(featureMap.get("allCaps"), tweet.getAllCapsCount());
			instance.setValue(featureMap.get("hashtags"), tweet.getHashtagCount());
			instance.setValue(featureMap.get("punctuationCount"), tweet.getPunctuation());
			if(tweet.isLastPunctuation()){
				instance.setValue(featureMap.get("punctuationLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("punctuationLast"), 0);
			}
			if(tweet.isLastEmoticon()){
				instance.setValue(featureMap.get("emoticonLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("emoticonLast"), 0);
			}
//			System.out.println("7");
			instance.setValue(featureMap.get("elongatedWords"), tweet.getElongatedCount());
//			instance.setValue(featureMap.get("negationCount"), tweet.getNegationCount());
			List<Double> senti140Uni = tweet.getLexiScores(senti140UniLexi);
			instance.setValue(featureMap.get("senti140UniTotalCount"), senti140Uni.get(0));
			instance.setValue(featureMap.get("senti140UniTotalScore"), senti140Uni.get(1));
			instance.setValue(featureMap.get("senti140UniMaxScore"), senti140Uni.get(2));
			instance.setValue(featureMap.get("senti140UniLastScore"), senti140Uni.get(3));
			List<Double> hashtagUni = tweet.getLexiScores(hashtagUniLexi);
			instance.setValue(featureMap.get("hashtagUniTotalCount"), hashtagUni.get(0));
			instance.setValue(featureMap.get("hashtagUniTotalScore"), hashtagUni.get(1));
			instance.setValue(featureMap.get("hashtagUniMaxScore"), hashtagUni.get(2));
			instance.setValue(featureMap.get("hashtagUniLastScore"), hashtagUni.get(3));
			
			List<Double> senti140Bi = tweet.getLexiScores(senti140BiLexi);
			instance.setValue(featureMap.get("senti140BiTotalCount"), senti140Bi.get(0));
			instance.setValue(featureMap.get("senti140BiTotalScore"), senti140Bi.get(1));
			instance.setValue(featureMap.get("senti140BiMaxScore"), senti140Bi.get(2));
			instance.setValue(featureMap.get("senti140BiLastScore"), senti140Bi.get(3));
			List<Double> hashtagBi = tweet.getLexiScores(hashtagBiLexi);
			instance.setValue(featureMap.get("hashtagBiTotalCount"), hashtagBi.get(0));
			instance.setValue(featureMap.get("hashtagBiTotalScore"), hashtagBi.get(1));
			instance.setValue(featureMap.get("hashtagBiMaxScore"), hashtagBi.get(2));
			instance.setValue(featureMap.get("hashtagBiLastScore"), hashtagBi.get(3));
			
			List<Double> MPQA = tweet.getLexiScores(MPQALexi);
			instance.setValue(featureMap.get("MPQATotalCount"), MPQA.get(0));
			instance.setValue(featureMap.get("MPQATotalScore"), MPQA.get(1));
			instance.setValue(featureMap.get("MPQAMaxScore"), MPQA.get(2));
			instance.setValue(featureMap.get("MPQALastScore"), MPQA.get(3));
			
			List<Double> BingLiu = tweet.getLexiScores(BingLiuLexi);
			instance.setValue(featureMap.get("BingLiuTotalCount"), BingLiu.get(0));
			instance.setValue(featureMap.get("BingLiuTotalScore"), BingLiu.get(1));
			instance.setValue(featureMap.get("BingLiuMaxScore"), BingLiu.get(2));
			instance.setValue(featureMap.get("BingLiuLastScore"), BingLiu.get(3));
			
			List<Double> NRC = tweet.getLexiScores(NRCLexi);
			instance.setValue(featureMap.get("NRCTotalCount"), NRC.get(0));
			instance.setValue(featureMap.get("NRCTotalScore"), NRC.get(1));
			instance.setValue(featureMap.get("NRCMaxScore"), NRC.get(2));
			instance.setValue(featureMap.get("NRCLastScore"), NRC.get(3));
			
			//System.out.println("8");
			//System.out.println(tweet.getSentiment());
			
			//instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
			
//			classifier.setSVMType();;
			
			resultMap.put(tweet, new ClassificationResult(tweet,classifier.distributionForInstance(train.lastInstance()), classifier.classifyInstance(train.lastInstance())));
//			System.out.println("10");
		}
		
//		Iterator<Instance> iterator = train.iterator();
//		while (iterator.hasNext()){
//			iterator.getClass()
//		}
		return resultMap;
	}
		
	public static Map<Tweet,ClassificationResult> printPredGUMLTLT(Set<Tweet> tweetList, String nameOfTrain) throws Exception{
		String trainname = "";
		if(!nameOfTrain.equals("")){
			trainname = nameOfTrain;
		}
		else{
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		LibLINEAR classifier = new LibLINEAR();
        classifier.setProbabilityEstimates(true);
        classifier.setSVMType(new SelectedTag(0, LibLINEAR.TAGS_SVMTYPE));
		classifier.setCost(0.15);
//		classifier.setCost(0.12);
		classifier.buildClassifier(train);
		
		train.delete();
		
        Tagger tagger = new Tagger();
        tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> sentiWordNet = loadSentiWordNet(); 
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}
		
		Map<Tweet,ClassificationResult> resultMap = new HashMap<Tweet,ClassificationResult>();
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(1);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> stemSet = tweet.getStems();
			for (String stem : stemSet){
				Integer index = featureMap.get("STEM_" + stem);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("4");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			
			Set<String> clusterSetRaw = tweet.getClusters(tweet.getRawWordList());
			for(String cluster : clusterSetRaw){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			
			Set<String> clusterSetCollapsed = tweet.getClusters(tweet.getCollapsedWordList());
			for(String cluster : clusterSetCollapsed){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			instance.setValue(featureMap.get("sentiWordNetPos"), tweet.getSentiWordNetScore("+", sentiWordNet));
			instance.setValue(featureMap.get("sentiWordNetNeg"), tweet.getSentiWordNetScore("-", sentiWordNet));

			
//			System.out.println("8");
			//instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
			
//			classifier.setSVMType();;
			
			resultMap.put(tweet, new ClassificationResult(tweet, classifier.distributionForInstance(train.lastInstance()), classifier.classifyInstance(train.lastInstance())));
//			System.out.println("10");
		}
		
//		Iterator<Instance> iterator = train.iterator();
//		while (iterator.hasNext()){
//			iterator.getClass()
//		}
		return resultMap;
	}
	
	public static Map<Tweet,ClassificationResult> printPredKLUE(Set<Tweet> tweetList, String nameOfTrain) throws Exception{
		String trainname = "";
		if(!nameOfTrain.equals("")){
			trainname = nameOfTrain;
		}
		else{
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(reader);
		train.setClassIndex(train.numAttributes() - 1);
		reader.close();
		
		LibLINEAR classifier = new LibLINEAR();
        classifier.setProbabilityEstimates(true);
        classifier.setSVMType(new SelectedTag(0, LibLINEAR.TAGS_SVMTYPE));
		//classifier.setCost(0.15);
        classifier.setCost(0.09);
//		classifier.setCost(0.42);
		classifier.buildClassifier(train);
		
		train.delete();
		
        Tagger tagger = new Tagger();
        tagger.loadModel("resources/tagger/model.20120919.txt");
		
    	Map<String, Double> afinnLexi = loadAFINN();
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}
		
		Map<Tweet,ClassificationResult> resultMap = new HashMap<Tweet,ClassificationResult>();
		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(2);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			List<Double> afinnScore = tweet.getAFINNScore(afinnLexi);
//			System.out.println("bla: " + afinnScore.size());
			instance.setValue(featureMap.get("sentiAFINNPos"), afinnScore.get(0));
			instance.setValue(featureMap.get("sentiAFINNNeg"), afinnScore.get(1));
			instance.setValue(featureMap.get("sentiAFINNTotal"), afinnScore.get(2));
			instance.setValue(featureMap.get("sentiAFINNScore"), afinnScore.get(3));
//			System.out.println("7");
			List<Double> emoScore = tweet.getEmoScore();
			instance.setValue(featureMap.get("sentiEmoPos"), emoScore.get(0));
			instance.setValue(featureMap.get("sentiEmoNeg"), emoScore.get(1));
			instance.setValue(featureMap.get("sentiEmoTotal"), emoScore.get(2));
			instance.setValue(featureMap.get("sentiEmoScore"), emoScore.get(3));

			
//			System.out.println("8");
			//instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
			
//			classifier.setSVMType();;
			
			resultMap.put(tweet, new ClassificationResult(tweet, classifier.distributionForInstance(train.lastInstance()), classifier.classifyInstance(train.lastInstance())));
//			System.out.println("10");
		}
		
//		Iterator<Instance> iterator = train.iterator();
//		while (iterator.hasNext()){
//			iterator.getClass()
//		}
		return resultMap;
	}
	
//	 private static int getIndex(Set<? extends Object> set, Object value) {
//	     int result = 0;
//	     for (Object entry:set) {
//	       if (entry.equals(value)) return result;
//	       result++;
//	     }
//	     return -1;
//	   }
	 
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
	
	private static Map<String, Double> loadMPQA() throws FileNotFoundException{
		Map<String, Double> lexiMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/subjclueslen1-HLTEMNLP05.tff");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split(" ");
			if (line.length == 6){
				String word = line[2].replaceFirst("word1=", "");
				Double val = 0.0;
				if(line[5].replaceFirst("priorpolarity=", "").equals("positive")){
					val = 1.0;
				}
				else{
					if(line[5].replaceFirst("priorpolarity=", "").equals("negative")){
						val = -1.0;
					}
				}
				if(line[0].replaceFirst("type=", "").equals("strongsubj")){
					val = val * 5;
				}
				lexiMap.put(word, val);
			}
		}
		scanner.close();
		return lexiMap;
	}
	
	private static Map<String, Double> loadBingLiu() throws FileNotFoundException{
		Map<String, Double> lexiMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/bingliu/positive-words.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
				lexiMap.put(scanner.nextLine(), 1.0);
		}
		scanner.close();
		File file2 = new File("resources/lexi/bingliu/negative-words.txt");
		Scanner scanner2 = new Scanner(file2);
		while (scanner2.hasNextLine()) {
				lexiMap.put(scanner2.nextLine(), -1.0);
		}
		scanner2.close();
		return lexiMap;
	}
	
	private static Map<String, Double> loadNRC() throws FileNotFoundException{
		Map<String, Double> lexiMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/NRC-emotion-lexicon-wordlevel-alphabetized-v0.92.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 3){
				if (line[1].equals("negative") && line[2].equals("1")){
					lexiMap.put(line[0], -1.0);
				}
				else{
					if (line[1].equals("positive") && line[2].equals("1")){
						lexiMap.put(line[0], 1.0);
					}
				}
			}
		}
		scanner.close();
		return lexiMap;
	}
	
	private static Map<String, Double> loadSentiWordNet() throws FileNotFoundException{
		Map<String, Double> sentiWordMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/SentiWordNet_3.0.0.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 6){
				for (String word : line[4].split(" ")){
					sentiWordMap.put(word.split("#")[0] + "+", Double.valueOf(line[2]));
					sentiWordMap.put(word.split("#")[0] + "-", Double.valueOf(line[3]));
				}
			}
		}
		scanner.close();
		return sentiWordMap;
	}
	
	private static Map<String, Double> loadAFINN() throws FileNotFoundException{
		Map<String, Double> afinnMap = new HashMap<String, Double>();
		File file = new File("resources/lexi/AFINN-111.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 2){
				afinnMap.put(line[0], Double.valueOf(line[1]));
			}
		}
		scanner.close();
		return afinnMap;
	}
	
	public static Instances getTestInstance(Set<Tweet> tweetList, Instances trainInstance, String resourcePath) throws Exception {
		System.out.println("Starting Weka Test");
		System.out.println("Tweets: " +  tweetList.size());
//		String trainname = "train_" + savename;
//		if(!nameOfTrain.equals("")) trainname = nameOfTrain;
		
//		BufferedReader reader = new BufferedReader(new FileReader("resources/arff/" + trainname + ".arff"));
		Instances train = new Instances(trainInstance);
//		train.setClassIndex(train.numAttributes() - 1);
//		reader.close();
		
		train.delete();
		
    	Tagger tagger = new Tagger();
    	tagger.loadModel(resourcePath + "resources/tagger/model.20120919.txt");
		
    	Map<String, Double> senti140UniLexi = loadLexicon("sentiment140/unigrams-pmilexicon");
    	Map<String, Double> hashtagUniLexi = loadLexicon("hashtag/unigrams-pmilexicon");
    	Map<String, Double> senti140BiLexi = loadLexicon("sentiment140/bigrams-pmilexicon");
    	Map<String, Double> hashtagBiLexi = loadLexicon("hashtag/bigrams-pmilexicon");
    	Map<String, Double> MPQALexi = loadMPQA();
    	Map<String, Double> BingLiuLexi = loadBingLiu();
    	Map<String, Double> NRCLexi = loadNRC();
		
		Map<String, Integer> featureMap = new HashMap<String, Integer>();
		for (int i = 0; i < train.numAttributes(); i++){
			featureMap.put(train.attribute(i).name(), train.attribute(i).index());
		}

		for(Tweet tweet : tweetList){
			tweet.tokenizeAndTag(tagger);
			tweet.negate();
			SparseInstance instance = new SparseInstance(0);
//			instance.setValue();
//			System.out.println("1");
			Set<String> nGramSet = tweet.getNGrams(4);
//			System.out.println("2");
			for (String nGram : nGramSet){
				Integer index = featureMap.get("NGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
			Set<String> CharNGramSet = tweet.getCharNGrams();
			for (String nGram : CharNGramSet){
				Integer index = featureMap.get("CHARNGRAM_" + nGram);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("3");
			Map<String, Integer> posTags = tweet.getPosTags();
			for (Map.Entry<String, Integer> posTag : posTags.entrySet()){
				Integer index = featureMap.get("POS_" +posTag.getKey());
				if(index != null){
					instance.setValue(index, posTag.getValue());
				}
			}
//			System.out.println("4");
			Set<String> clusterSet = tweet.getClusters(tweet.getWordList());
			for(String cluster : clusterSet){
				Integer index = featureMap.get("CLUSTER_" + cluster);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("5");
			Set<String> emoticonSet = tweet.getEmoticons();
			for(String emoticon : emoticonSet){
				Integer index = featureMap.get("EMO_" + emoticon);
				if(index != null){
					instance.setValue(index, 1);
				}
			}
//			System.out.println("6");
			instance.setValue(featureMap.get("allCaps"), tweet.getAllCapsCount());
			instance.setValue(featureMap.get("hashtags"), tweet.getHashtagCount());
			instance.setValue(featureMap.get("punctuationCount"), tweet.getPunctuation());
			if(tweet.isLastPunctuation()){
				instance.setValue(featureMap.get("punctuationLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("punctuationLast"), 0);
			}
			if(tweet.isLastEmoticon()){
				instance.setValue(featureMap.get("emoticonLast"), 1);
			}
			else{
				instance.setValue(featureMap.get("emoticonLast"), 0);
			}
//			System.out.println("7");
			instance.setValue(featureMap.get("elongatedWords"), tweet.getElongatedCount());
			List<Double> senti140Uni = tweet.getLexiScores(senti140UniLexi);
			instance.setValue(featureMap.get("senti140UniTotalCount"), senti140Uni.get(0));
			instance.setValue(featureMap.get("senti140UniTotalScore"), senti140Uni.get(1));
			instance.setValue(featureMap.get("senti140UniMaxScore"), senti140Uni.get(2));
			instance.setValue(featureMap.get("senti140UniLastScore"), senti140Uni.get(3));
			List<Double> hashtagUni = tweet.getLexiScores(hashtagUniLexi);
			instance.setValue(featureMap.get("hashtagUniTotalCount"), hashtagUni.get(0));
			instance.setValue(featureMap.get("hashtagUniTotalScore"), hashtagUni.get(1));
			instance.setValue(featureMap.get("hashtagUniMaxScore"), hashtagUni.get(2));
			instance.setValue(featureMap.get("hashtagUniLastScore"), hashtagUni.get(3));
			
			List<Double> senti140Bi = tweet.getLexiScores(senti140BiLexi);
			instance.setValue(featureMap.get("senti140BiTotalCount"), senti140Bi.get(0));
			instance.setValue(featureMap.get("senti140BiTotalScore"), senti140Bi.get(1));
			instance.setValue(featureMap.get("senti140BiMaxScore"), senti140Bi.get(2));
			instance.setValue(featureMap.get("senti140BiLastScore"), senti140Bi.get(3));
			List<Double> hashtagBi = tweet.getLexiScores(hashtagBiLexi);
			instance.setValue(featureMap.get("hashtagBiTotalCount"), hashtagBi.get(0));
			instance.setValue(featureMap.get("hashtagBiTotalScore"), hashtagBi.get(1));
			instance.setValue(featureMap.get("hashtagBiMaxScore"), hashtagBi.get(2));
			instance.setValue(featureMap.get("hashtagBiLastScore"), hashtagBi.get(3));
			
			List<Double> MPQA = tweet.getLexiScores(MPQALexi);
			instance.setValue(featureMap.get("MPQATotalCount"), MPQA.get(0));
			instance.setValue(featureMap.get("MPQATotalScore"), MPQA.get(1));
			instance.setValue(featureMap.get("MPQAMaxScore"), MPQA.get(2));
			instance.setValue(featureMap.get("MPQALastScore"), MPQA.get(3));
			
			List<Double> BingLiu = tweet.getLexiScores(BingLiuLexi);
			instance.setValue(featureMap.get("BingLiuTotalCount"), BingLiu.get(0));
			instance.setValue(featureMap.get("BingLiuTotalScore"), BingLiu.get(1));
			instance.setValue(featureMap.get("BingLiuMaxScore"), BingLiu.get(2));
			instance.setValue(featureMap.get("BingLiuLastScore"), BingLiu.get(3));
			
			List<Double> NRC = tweet.getLexiScores(NRCLexi);
			instance.setValue(featureMap.get("NRCTotalCount"), NRC.get(0));
			instance.setValue(featureMap.get("NRCTotalScore"), NRC.get(1));
			instance.setValue(featureMap.get("NRCMaxScore"), NRC.get(2));
			instance.setValue(featureMap.get("NRCLastScore"), NRC.get(3));
			
//			System.out.println("8");
			instance.setValue(train.classAttribute(), tweet.getSentiment());
			
//			System.out.println("9");
			train.add(instance);
//			System.out.println("10");
		}
		
		return train;
	}
}
