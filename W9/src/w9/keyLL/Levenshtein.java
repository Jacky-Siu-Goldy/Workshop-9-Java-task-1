package w9.keyLL;

import java.util.LinkedList;
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
 * 
 * Contain the property named key which is a String
 * and the property wordsEditDistance1 which is a LinkedList of words that belongs to the key
 * which is a word
 *
 */
public class Levenshtein {
	private String key;
	private LinkedList<String> wordsEditDistance1;
	public String getKey() {
		return key;
	}
	/**
	 * setter for setting the key which is a String
	 * @param key- type String
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * returns a LinkedList of type String wordEditDistance1 which is a LinkedList of words that belongs to the key
	 * @return wordEditDistance1 a LinkedList type
	 */
	public LinkedList<String> getWordsEditDistance1() {
		return wordsEditDistance1;
	}
	
	/**
	 * add an element a word of type String to the LinkedList wordEditDistance1
	 * @param wordsEditDistance1 - type LinkedList
	 */
	public void setWordsEditDistance1(String wordsEditDistance1) {
		this.wordsEditDistance1.add(wordsEditDistance1);
	}
	
	/**
	 * Default constructor which initialize key to an empty String
	 * and wordEditDistance1 to a new LinkedList of type String
	 */
	public Levenshtein() {
		key = "";
		wordsEditDistance1 = new LinkedList<String>();
	}

}
