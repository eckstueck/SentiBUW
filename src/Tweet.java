import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Scrollable;
import javax.swing.text.rtf.RTFEditorKit;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import cmu.arktweetnlp.impl.features.WordClusterPaths;


public class Tweet {
	private String rawTweet;
	private String tweetID;
    private String tweetString;
    private String sentiment;
//    private ArrayList<String> wordList = new ArrayList<String>();
    private List<TaggedToken> wordList;
    private List<TaggedToken> wordListRaw;
    private List<TaggedToken> wordListCollapsed;
    private Set<String> wordListKLUE;
//    private Set<String> negWords = new HashSet<String>();
    private boolean lastEmoticon = false;
    private int negationCount = 0;
//    private boolean lastPunctuation = false;
    
    public Tweet(String tweet, String senti, String tID){
    	rawTweet = tweet;
        sentiment = senti;
    	tweetID = tID;
        tweetString = normalizeTweet(new String(tweet));
    }
    
    public String getTweetID() {
        return tweetID;
    }
    public String getSentiment() {
        return sentiment;
    }
    
    public int getSentimentAsInt() {
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("positive");
        classVal.add("neutral");
        classVal.add("negative");
        return classVal.indexOf(sentiment);
    }

    public String setSentiment(String senti) {
        return sentiment = senti;
    }

    public String getTweetString() {
		return tweetString;
	}
    
    public String getRawTweetString() {
		return rawTweet;
	}
        
    public void tokenizeAndTag(Tagger tagger) throws IOException{
//    	Tagger tagger = new Tagger();
//    	tagger.loadModel("resources/tagger/model.20120919.txt");
    	wordList = tagger.tokenizeAndTag(tweetString);
    	wordListRaw = tagger.tokenizeAndTag(rawTweet);
    	wordListCollapsed = collapseTweet();
    	wordListKLUE = getStems();
//    	String test = WordClusterPaths.wordToPath.get("hallo");
//        NGramTokenizer tokenizer = new NGramTokenizer();
//        tokenizer.setNGramMaxSize(ngram);
//        tokenizer.setNGramMinSize(1);
//        tokenizer.tokenize(tweetString);
//        while(tokenizer.hasMoreElements()){
//            wordList.add((String) tokenizer.nextElement());
//        }
    	
    }
    
    public boolean isTokenized() {
        if (wordList.size() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    
    public List<TaggedToken> getWordList() {
	  	return wordList;
  	}
    
    public List<TaggedToken> getRawWordList() {
	  	return wordListRaw;
  	}
    
    public List<TaggedToken> getCollapsedWordList() {
	  	return wordListCollapsed;
  	}
    
    public Set<String> getNGrams(int n) {
    	Set<String> nGramList = new HashSet<String>();
    	String tokenString = "";
//    	System.out.println("a");
    	for (TaggedToken token : wordList){
//    		System.out.println(token.token);
    		tokenString = tokenString + token.token + " ";
    	}
//    	System.out.println("b");
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMaxSize(n);
        tokenizer.setNGramMinSize(1);
        tokenizer.setDelimiters(" ");
        tokenizer.tokenize(tokenString);
//        System.out.println("c");
        while(tokenizer.hasMoreElements()){
        	nGramList.add((String) tokenizer.nextElement());
        }
//        System.out.println("d");
    	return nGramList;
	}
    
    public Set<String> getCharNGrams(){
    	Set<String> nGramList = new HashSet<String>();
    	for (int i = 0; i < tweetString.length() - 2; i++){
    		nGramList.add(tweetString.substring(i, i + 3));
    		if (i + 4 <= tweetString.length()) nGramList.add(tweetString.substring(i, i + 4));
    		if (i + 5 <= tweetString.length())nGramList.add(tweetString.substring(i, i + 5));
    	}
    	return nGramList;
    }
       
    public int getHashtagCount(){
        String tempString = tweetString;
        return tempString.length() - tempString.replace("#", "").length();
    }
    
    public int getAllCapsCount(){
    	int wordsInCaps = 0;
    	for (String word: tweetString.split("[\\p{P} \\t\\n\\r]")){
    		if(word == word.toUpperCase()) wordsInCaps++;
    	}
    	return wordsInCaps;
    }
        
    public int getElongatedCount(){
    	int elongatedWords = 0;
    	for (String word: tweetString.split("[\\p{P} \\t\\n\\r]")){
    		Matcher m = Pattern.compile("(.)\\1{2,}").matcher(word);    		
    		if(m.find()) elongatedWords++;
    	}
    	return elongatedWords;
    }
    
    public Set<String> getEmoticons(){
    	Set<String> emoticons = new HashSet<String>();
    	String emoticon_string = "(?:[<>]?[:;=8][\\-o\\*\\']?[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\\\]|[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\\\] [\\-o\\*\\']?[:;=8][<>]?)";
    	Matcher m = Pattern.compile(emoticon_string).matcher(tweetString);
    	while (m.find()){
    		emoticons.add(tweetString.substring(m.start(), m.end()));
    		if(m.end() == tweetString.length()) lastEmoticon = true;
    	}
    	return emoticons;
    }
    
    public boolean isLastEmoticon() {
		return lastEmoticon;
	}
    
    public Map<String, Integer> getPosTags(){
    	Map<String,Integer> tagMap = new HashMap<String, Integer>();
    	for (TaggedToken token : wordList){
        	Integer val = tagMap.get(token.tag);
        	if (val != null){
        		tagMap.put(token.tag, ++val);
        	}
        	else{
        		tagMap.put(token.tag, 1);
        	}
    	}
//    	MaxentTagger tagger = new MaxentTagger("resources/tagger/english-bidirectional-distsim.tagger");
//    	List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(tweetString));
//	    for (List<HasWord> sentence : sentences) {
//	        ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
//	        for (TaggedWord word : tSentence){
//	        	if (!word.value().equals("")){
//		        	Integer val = tagMap.get(word.tag());
//		        	if (val != null){
//		        		tagMap.put(word.tag(), ++val);
//		        	}
//		        	else{
//		        		tagMap.put(word.tag(), 1);
//		        	}
//	        	}
//	        }
//	    }
	    return tagMap;
    }
    
    public int getPunctuation() {
    	int punctuations = 0;
    	Matcher m = Pattern.compile("[!,?]{2,}").matcher(tweetString);
    	while (m.find()){
    		punctuations++;
    	}
    	return punctuations;
	}
    
    public boolean isLastPunctuation() {
    	if (tweetString.endsWith("?") || tweetString.endsWith("!")){
    		return true;
    	}
    	return false;
	}
    
    public Set<String >getClusters(List<TaggedToken> list) {
    	Set<String> clusterList = new HashSet<String>();
    	for (TaggedToken token : list){
    		String cluster = WordClusterPaths.wordToPath.get(token.token);
    		if (cluster != null){
    			clusterList.add(cluster);
    		}
    	}
		return clusterList;    	
    }
    public void negate(){
    	boolean neg = false;
    	for (TaggedToken token : wordList){
    		if(neg){
    			if(token.token.matches("^[.:;!?]$")){
    				neg = false;
    				negationCount++;
    			}
    			else{
    				token.token = token.token + "_NEG";
    			}
    		}
    		if(token.token.toLowerCase().matches("^(?:never|no|nothing|nowhere|noone|none|not|havent|hasnt|hadnt|cant|couldnt|shouldnt|wont|wouldnt|dont|doesnt|didnt|isnt|arent|aint)|.*n't")){
    			neg = true;
    		}
    		
    	}
    	if (neg) negationCount++;
    }
    
    public List<Double> getLexiScores(Map<String,Double> lexi) {
    	double totalCount = 0.0;
    	double totalScore = 0.0;
    	double maxScore = 0.0;
    	double lastScore = 0.0;
    	for (TaggedToken token : wordList){
    		Double score = lexi.get(token.token);
    		if(score != null){
    			if(score > 0) totalCount++;
    			totalScore = totalScore + score;
    			if(score > maxScore) maxScore = score;
    			if(score > 0) lastScore = score;
    		}
    	}
    	List<Double> scoreList = new ArrayList<Double>();
    	scoreList.add(totalCount);
    	scoreList.add(totalScore);
    	scoreList.add(maxScore);
    	scoreList.add(lastScore);
		return scoreList;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tweetString == null) ? 0 : tweetString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (tweetString == null) {
			if (other.tweetString != null)
				return false;
		} else if (!(tweetString.equals(other.tweetString) && tweetID.equals(other.tweetID)))
			return false;
		return true;
	}
	
	private String normalizeTweet(String tweet){
//		tweet = new String(tweet.getBytes("UTF-8"), "UTF-8");
//		System.out.println(tweet);
		//lowerCase
		tweet = tweet.toLowerCase();
		//filter Usernames
		//tweet = tweet.replaceAll("@[^\\s]+", "@someuser");
		tweet = tweet.replaceAll("@[^\\s]+", "");
		//filter Urls
		//tweet = tweet.replaceAll("((www\\.[^\\s]+)|(https?://[^\\s]+))", "http://someurl");
		tweet = tweet.replaceAll("((www\\.[^\\s]+)|(https?://[^\\s]+))", "");
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
	
	private List<TaggedToken> collapseTweet(){
//		Matcher matchTweet = Pattern.compile(" ").matcher(tweet);
//		while (m.)
		List<TaggedToken> tokenList = new ArrayList<TaggedToken>(wordList);
		for (TaggedToken token : tokenList){
			Matcher matchWord = Pattern.compile("(.)\\1{2,}").matcher(token.token);
			String tempWord = token.token;
			while (matchWord.find()) {
				if(matchWord.end() < 0 || matchWord.start() < 0){
					
				}
//				System.out.println(token.token);
//				System.out.println(matchWord.end());
//				System.out.println("bla");
				token.token = tempWord.substring(0, matchWord.start()+2) + tempWord.substring(matchWord.end());
			}
    	}
		return tokenList;
	}
	
	public Set<String> getStems() {
//		englishStemmer stemClass = new englishStemmer();
		SnowballStemmer stemmer = new SnowballStemmer("english");
    	Set<String> stemList = new HashSet<String>();
    	for (TaggedToken token : wordList){
    		stemList.add(stemmer.stem(token.token));
    	}
		return stemList;  
	}
	
	public Double getSentiWordNetScore(String posNeg, Map<String,Double> map) {
    	double totalScore = 0.0;
    	for (TaggedToken token : wordListCollapsed){
    		Double score = map.get(token.token + posNeg);
    		if(score != null){
    			totalScore = totalScore + score;
    		}
    	}
    	return totalScore;
	}
	
	public List<Double> getAFINNScore(Map<String,Double> map) {
    	double posCount = 0.0;
    	double negCount = 0.0;
    	double totalCount = 0.0;
    	double totalScore = 0.0;
    	for (String word : wordListKLUE){
    		Double score = map.get(word);
    		if(score != null){
    			totalScore = totalScore + score;
    			if (score > 0){
    				posCount++;
    			}
    			if (score < 0){
    				negCount++;
    			}
    			totalCount++;
    		}
    	}
    	List<Double> scoreList = new ArrayList<Double>();
    	scoreList.add(posCount);
    	scoreList.add(negCount);
    	scoreList.add(totalCount);
    	scoreList.add(totalScore);
    	return scoreList;
	}
	
	public List<Double> getEmoScore() {
    	double posCount = 0.0;
    	double negCount = 0.0;
    	double totalCount = 0.0;
    	double totalScore = 0.0;
    	Set<String> emoticons =  getEmoticons();
    	for (String emo : emoticons){
    		totalCount++;
    		if(emo.endsWith("(") || emo.endsWith("[") || emo.endsWith("<") || emo.endsWith("/") || emo.toLowerCase().endsWith("c") || emo.startsWith(")") || emo.startsWith("]") || emo.startsWith(">") || emo.startsWith("\\") || emo.startsWith("D")){
    			negCount++;
    			totalScore = totalScore - 1;
    		}
    		if(emo.endsWith(")") || emo.endsWith("]") || emo.endsWith(">") || emo.endsWith("D") || emo.startsWith("(") || emo.startsWith("[") || emo.startsWith("<") || emo.toLowerCase().startsWith("c")){    			
    			posCount++;
    			totalScore = totalScore + 1;
    		}
    	}
    	List<Double> scoreList = new ArrayList<Double>();
    	scoreList.add(posCount);
    	scoreList.add(negCount);
    	scoreList.add(totalCount);
    	scoreList.add(totalScore);
    	return scoreList;
	}
	
	public int getNegationCount() {
		return negationCount;
	}
}
