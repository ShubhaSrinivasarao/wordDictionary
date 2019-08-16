package com;

import com.dto.ResponseDto;

public interface IDictionary {
	/**
	 * API to load the words to dictionary from the path
	 * @param path
	 */
	ResponseDto loadWords(String path);
	
	/**
	 * API to load the stop words which is used to filter the words 
	 * @param path
	 */
	void loadStopWords(String path);
	
	/**
	 * API to get the search word from the dictionary
	 * @param word
	 * @return
	 */
	boolean searchWord(String word);
	
	/**
	 * 
	 * @param mode
	 * @return
	 */
	void setPersistMode(String mode);
}
