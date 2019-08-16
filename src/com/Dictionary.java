package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.dao.IPersist;
import com.dto.ResponseDto;

public class Dictionary implements IDictionary{
	
	List<String> persistentModeList = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("file");
		}
	};
	
	private List<String> dic=new ArrayList<>();
	
	private Set<String> stopWords = new HashSet<>();
	private String persistMode;
	
	private static boolean isDataLoaded = false;
	
	private PersistentContext persistentContext = new PersistentContext();
	
	IPersist persist = null;

	public ResponseDto loadWords(String path){
		ResponseDto responseDto = validateInputFile(path);
		if(responseDto.isState()){
			return responseDto;
		}
		try {
			
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String s, word = null;
			List<String> aListWords = new ArrayList<>();
			Scanner scan = null;
			while ((s = br.readLine()) != null) {
				scan = new Scanner(s);
				while (scan.hasNext()) {
					word = scan.next();
					if(stopWords.size()>0){
						if(stopWords.contains(word)){
							//Dont add that word to list
							continue;
						}
					}
					aListWords.add(word.replaceAll("[^a-zA-Z0-9]+", ""));
				}
			}
			br.close();
			fr.close();
			Set<String> filteredWords = new HashSet<String>(aListWords);
			aListWords.clear();
			dic.addAll(filteredWords);
			System.out.println(dic);
		} catch (FileNotFoundException e) {
			System.err.println(path+" file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(path+" file Interupted file operation");
			e.printStackTrace();
		}
		getPersistent();
		persist.save(dic);
		responseDto.setState(true);
		responseDto.setMessage("Successfully persisted to "+persistMode);
		return responseDto;
	}

	private ResponseDto validateInputFile(String path) {
		ResponseDto responseDto = loadWords(); 
		if(responseDto.isState()){
			return responseDto;
		}
		
		//Check the path has file 
		File file = new File(path);
		if(!file.exists()){
			responseDto.setState(true);
			responseDto.setMessage("File Not exists");
			return responseDto;
		}
		
		//Check the file size
		if(!file.isFile()){
			responseDto.setState(true);
			responseDto.setMessage(path + "is not a text file");
			return responseDto;
		}
		
		// Get length of file in bytes
		long fileSizeInBytes = file.length();
		// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
		long fileSizeInMB = fileSizeInBytes / (1024*1024);
		
		if(fileSizeInMB > 200){
			responseDto.setState(true);
			responseDto.setMessage("Allowed File size is 200MB");
			return responseDto;
		}
		

		responseDto.setState(false);
		responseDto.setMessage("Conditions valid");
		return responseDto;
	}
	
	public void loadStopWords(String path) {
		System.out.println("Load stop words started");
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String s, word = null;
			Scanner scan = null;
			while ((s = br.readLine()) != null) {
				scan = new Scanner(s);
				while (scan.hasNext()) {
					word = scan.next();
					stopWords.add(word);
				}
			}
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			System.err.println(path+" file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(path+" file Interupted file operation");
			e.printStackTrace();
		}
		System.out.println("Load stop words ended");
	}

	public boolean searchWord(String word){
		ResponseDto responseDto = loadWords(); 
		if(responseDto.isState()){
			System.out.println(responseDto.getMessage());
			return false;
		}
		if(dic.size()<=0){
			System.out.println("Words not loaded in Dictionary");
			return false;
		}
		boolean isMatch = dic.stream().anyMatch(word::equalsIgnoreCase);
		return isMatch;
	}

	@Override
	public void setPersistMode(String mode) {
		persistMode = mode;
	}
	
	private IPersist getPersistent(){
		if(null == persist){
			persist = persistentContext.getPersistent(persistMode);
		}
		return persist;
	}
	
	private ResponseDto loadWords(){
		ResponseDto responseDto = new ResponseDto(); 
		if (null == persistMode || !persistentModeList.contains(persistMode)) {
			responseDto.setState(true);
			responseDto.setMessage("Persist options are not valid. Valid Persist options are " + persistentModeList);

		} else {
			if (!isDataLoaded) {
				getPersistent();
				persist.getWords();
			}
		}
		return responseDto;
	}
}
