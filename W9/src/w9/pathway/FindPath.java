package w9.pathway;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import w9.keyLL.Levenshtein;

/**********************************************
Workshop # 9
Course: JAC444 - Semester Winter 2023
Last Name: Siu
First Name: Chun Kit Jacky
ID: 134663186
Section: NAA
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature
Date: 2023-04-05
**********************************************/

/**
 * This program computes the edit distance (Levenshtein distance) between two 
 * words and find the path consisting of real words of how one word
 * can transform from one word to the next 
 * by substitution, deletion or insertion into another word
 * with the shortest edit distance.
 * 
 *
 */
public class FindPath {
	
	static int dictionarysize = 0;
	/**
	 * consist of the main program which loads the all the words from a dictionary
	 * text file into a list called dictionary2
	 * 
	 * @param args type String (not used)
	 */
    public static void main(String[] args) {
    	
      
        
    	Task t = new Task();
        Thread t0 = new Thread() {public void run() { dictionarysize = t.getDictionaryFromFile();}};
        try {
        	t0.start();
        	t0.join();
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
        int indexInterval = dictionarysize/4;
        int endIndex2 = indexInterval + indexInterval;
        int endIndex3 = endIndex2 + indexInterval;
        int endIndex4 = dictionarysize;
        
        Thread t1 = new Thread(){public void run() {t.createHashMap2(0, indexInterval);}};
        Thread t2 = new Thread(){public void run() {t.createHashMap2(indexInterval, endIndex2);}};
        Thread t3 = new Thread(){public void run() {t.createHashMap2(endIndex2, endIndex3);}};
        Thread t4 = new Thread(){public void run() {t.createHashMap2(endIndex3, endIndex4);}};
        Thread t5 = new Thread(){public void run(){t.runProgram();}};
        	
        try {
            
	        t1.start();
	        t2.start();
	        t3.start();
	        t4.start();
	        t1.join();
	        t2.join();
	        t3.join();
	        t4.join();
	        t5.start();
	        t5.join();
	        
	        }catch(InterruptedException e) {
	        	e.printStackTrace();
	        }

    }
}
/**
 * The class Task is use for inserting 
 * its varies of method into multiple threads
 * so the program load faster as specially
 * when creating a hashmap of keys and values pairs
 * of key words and value a (LinkedList) of words that belongs
 * to the key hundreds  of words in the text file.
 * 
 *
 */
class Task{
	 
	List<String> dictionary2 = new ArrayList<>();

	HashMap<String, LinkedList<String>> wordsDistance1Map2 = new HashMap<String, LinkedList<String>>();
     
	
    Scanner scan = new Scanner(System.in);
    
    /**
     * Responsible for opening a dictionary text file. It
     * first ask the user for the txt.file name.
     * And then it loads its contents (words) one line at a time
     * into the a list call dictionary2
     * It will return a the word List size of type Integer 
     * 
     * @return dictionary.size() - integer type
     */
    synchronized int getDictionaryFromFile(){
		boolean fileNameIsCorrect = false;
    	String txtFileName  = "";
    	
        do {
        	System.out.println("This Program uses a dictionary to compute the edit distances\n"
        			+ "between pairs of words.\n\n");
        	do {
        	System.out.println("What is in the dictionary file? ");
        	Pattern filePattern = Pattern.compile("[A-Za-z]+\\.txt");
        	txtFileName = scan.nextLine();
        	Matcher fileMatcher = filePattern.matcher(txtFileName);
        	fileNameIsCorrect = fileMatcher.find();
        	}while(!fileNameIsCorrect);
        	
        	
        	Path p = Paths.get(txtFileName);
    		Path folder = p.toAbsolutePath();
    		System.out.println(folder);
        	try {
        		
        		
        		File myObj = new File(folder.toString());
        	        
        		Scanner myReader = new Scanner(myObj);
        		
        		while(myReader.hasNextLine()) {
        			String line = myReader.nextLine();
        			dictionary2.add(line);	
        		}
        		myReader.close();
            	break;
        	}catch(FileNotFoundException e) {
        		e.printStackTrace();
        	}
        	
        }while(true);
        return dictionary2.size();
	}

	/**
	 * This method ask for a first word and a second word. Determine whether the dictionary List contain 
	 * those words, if not it will ask for them again. It will exit the program if the user enters
	 * an empty string.
	 * It will first calculate the Edit Distance between the two words. The two word is the same
	 * it will print out the word and an Edit Distance of 0 to the console.
	 * Otherwise it will determine the homeInDistance from the first word to the second word. Which is determine
	 * base on the edit distance between the two words. It will input a testword as the firstword and the secondword
	 * into the method editDistance. Determine its calculated distance. If the calculated distance is equal
	 * to the homeInDistance then the homeInDistance is decremented by 1.  The homeInDistance is
	 * the edit Distance (the right amount of distance) of a Word to the second word given 
	 * the edit position it should be in towards the Second word.  The testword is added to LinkedList of words
	 * called closingInWords which will be the result. the testword is key gotten from the Value (the LinkedList of 
	 * words). The testword that is the right word will be use to get the next List of words from the HashMap to be tested.
	 * The process would continue until the HomeInDistance equals -1 which means the second word has been found or
	 * reached. If the is no Path is found then it will print out on the the console "No path exist between the two words!"
	 * else it will print out the firstword and then the list of the closing in word in the right order including the second
	 * word that has been found or reached. Lastly it will output the Edit Distance between the firstword and the secondword.
	 * This process is in a loop and until the user enters an empty String at the beginning of the program to exit the
	 * progam. 
	 *  
	 * 
	 */
	 void runProgram() {
			System.out.println(wordsDistance1Map2);
	    	boolean quit = false;
	    	boolean firstWordPass = false;
	    	boolean secondWordPass = false;
	    	String firstWord ="";
	    	String secondWord="";
	    	int editDistance = -1;
	    		    	
	    	while(!quit) {
	    		
	    		
	    		System.out.println("\nLet's find an edit distance between words.");
	    		while(!secondWordPass) {	
	    			while(!firstWordPass) {
	        			System.out.println("	first word (enter to quit)?");
	        			firstWord = scan.nextLine();
	        	       
	        	        
	        	        	quit =  firstWord.isEmpty();
	        	        if(quit == true) {
	        	        	break;
	        	        }
	        	        boolean pass = dictionary2.contains(firstWord);
	        	        if(!pass) {
	        	        	System.out.println(firstWord + " is not in dictionary");
	        	        }
	        	        firstWordPass = pass;
	    			}
	    			if(quit == true) {
	    	        	break;
	    	        }
	        		
	    			System.out.println("	Second word (enter to quit)?");
	    			secondWord = scan.nextLine();
	    	        
	    			
	    	        	quit =  secondWord.isEmpty();
	    	        if(quit == true) {
	        	        	break;
	        	    }
	    	        boolean pass2 = dictionary2.contains(secondWord);
	    	        if(!pass2) {
	    	        	System.out.println(secondWord + " is not in dictionary");
	    	        }
	    	        secondWordPass = pass2;
	        	        
	        	        
	        		
	    		}
	    		firstWordPass=false;
	    		secondWordPass = false;
	    		
	    		
	    		
	    		if ( firstWord.equals(secondWord)) {
	    			System.out.println(firstWord);
	    			System.out.println("\nEdit distance = 0");
	    		}else {
		    		boolean finished = false;
		    		LinkedList<String> unProcessedWords = new LinkedList<String>();
		    		
		    		LinkedList<String> closingInWords = new LinkedList<String>();
		    		unProcessedWords = wordsDistance1Map2.get(firstWord);
		    		boolean secondWordFound = false;
		    		boolean hasNext = false;
		    		String testWord ="";		
	        		editDistance = editDistance2(firstWord, secondWord);
	        		
	        		int sCalculatedDistance = -1;
	        		
	        		int shomeInDistance = editDistance - 1;
	        		
	        		while(!finished) {
		        		while(unProcessedWords != null) {
		        				
		        			
		        			ListIterator<String> word = unProcessedWords.listIterator();
		        			
		        			while(hasNext = word.hasNext()) {
		        				testWord = String.valueOf(word.next());
		        				sCalculatedDistance = editDistance2(testWord, secondWord);
		        				
		        				if (sCalculatedDistance == shomeInDistance ) {
		        					unProcessedWords = wordsDistance1Map2.get(testWord);
		        					shomeInDistance--;
		        					
		        					break;
		        				}
		        			}
		        			
		        			closingInWords.add(testWord);
		        			if(shomeInDistance == -1 ) {
		        				secondWordFound = true;
		        				
		        				finished = true;
		        				break;
		        			}else if (!hasNext) {
		        				unProcessedWords = null;
		        				
		        			}
		        		}
		        		finished = true;
	        		}
		    		
		    		if(secondWordFound && !closingInWords.equals(null)) {
		    			System.out.print(firstWord);
		        		ListIterator<String>  matchedWord2 = closingInWords.listIterator();
		        		while(matchedWord2.hasNext()) {
		        			System.out.print(", " + matchedWord2.next() );
		        		}
		        		
		        		System.out.println("\nEdit distance = " + editDistance);
		        		closingInWords.clear();
		    		}else if(!quit) {
		    			//System.out.println(wordsDistance1Map2);
		    			System.out.println("No path exist between the two words!");
		    		}
		    		
		    	}
	    	}
	    	scan.close();	
	    }
	    
		
    
	/**
	 * Responsible for finding out the a number of words in the dictionary of words
	 * that is of edit distance 1 from the key a (word) from the dictionary
	 * and adding them to a LinkedList belonging to that word the key. It does this for 
	 * every word in the text file. Every time a key(Word) and a Value a LinkedList(a list of words).
	 * It add that key and value (pair) into a hashmap called wordDistance1Map2 which is a property of
	 * the class Task. Slowly be surely it creates the Whole Map.
	 * @param startIndex - type integer the starting index for a List of words not necessary 
	 * the beginning of the list could be anywhere in between the beginning and the end
	 * @param endIndex - type integer the ending index  for a List of words not necessary 
	 * the ending of the List, could be anywhere in between the beginning and the end
	 */
	 void createHashMap2(int startIndex, int endIndex) {
   	 String string_i = "";
    	 String string_j = "";
    
        for(int i = startIndex; i < endIndex; i++) {
        	
        	  string_i = String.valueOf(dictionary2.get(i));
        	Levenshtein levenshtein2 = new Levenshtein();
        	
        	for(int j = 0; j < dictionary2.size(); j++) {
        		
        		string_j = String.valueOf(dictionary2.get(j));
        		
        		if(editDistance2(string_i, string_j) == 1 ){
        			int num = editDistance2(string_i, string_j);
    				System.out.println(string_i);
    				System.out.println(string_j);
        			System.out.println(num);
        			levenshtein2.setWordsEditDistance1(string_j);		
        		}
        	
        	}
        			
        	levenshtein2.setKey(string_i);
        	
        		
        	wordsDistance1Map2.put(levenshtein2.getKey(), levenshtein2.getWordsEditDistance1());
        			
        
        	
        }
   }
	
  /**
   * take two string the firstWord and the secondWord and compute the shortest editDistance between them.
   * @param firstWord - a String
   * @param secondWord - a String
   * @return calculateDistance[firstWord.length()][secondWord.length()];- an element in the two dimensional array that 
   * contain an integer(type int) value of the shortest edit distance between the two words.
   */
  int editDistance2 (String firstWord, String secondWord) {
    	
    	
		int[][] calculateDistance = new int[(firstWord.length()) + 1][secondWord.length() + 1];
		for(int row = 0; row < firstWord.length() + 1; row++){
			for(int col = 0; col < secondWord.length()+ 1; col++) {
				if (row == 0 && col == 0) {
					calculateDistance[row][col] = 0;
				}
				else if (row == 0) {
					calculateDistance[row][col] = calculateDistance[row][col - 1] + 1;
				}
				else if (col == 0) {
					calculateDistance[row][col] = calculateDistance[row - 1][col] + 1;
				}
				else { 
    				if(firstWord.charAt(row -1) == secondWord.charAt(col - 1)) {
    					calculateDistance[row][col] = calculateDistance[row - 1][col - 1];
    				}else {
    					int[] array ={(calculateDistance[row - 1][col] + 1),
                                (calculateDistance[row][col - 1] + 1),
                                (calculateDistance[row - 1][col - 1] + 1)};
        					calculateDistance[row][col] = getMinValue2(array);  
    				}
				}		
				
			}
			
		}
		return calculateDistance[firstWord.length()][secondWord.length()];
    }
 
  /**
   * Recieve an array of integers and find out the Min value of the integer that
   * is in the list and returns it.
   * @param array - an (int) array
   * @return minValue - type Integer (The smallest integer in the array)
   */
    int getMinValue2(int[] array) {
	 	int minValue = array[0];
	 	for(int i = 1; i < array.length; i++) {
	 		if(array[i] < minValue) {
	 			minValue = array[i];
	 		}
	 	}
	 	return minValue;
	 }
	
}
