package com.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FilePersist implements IPersist {

	@Override
	public String save(List<String> words) {
		FileOutputStream fos;
		try {
			File file = new File("./words.tmp");
			file.createNewFile(); 
			fos = new FileOutputStream("./words.tmp", false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(words);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getWords(){
		List<String> words = new ArrayList<String>();
		FileInputStream fis;
		try {
			File file = new File("./words.tmp");
			if(!file.exists()){
				file.createNewFile(); 
			}
			fis = new FileInputStream("./words.tmp");
			ObjectInputStream ois = new ObjectInputStream(fis);
			words = (List<String>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return words;
	}
}
