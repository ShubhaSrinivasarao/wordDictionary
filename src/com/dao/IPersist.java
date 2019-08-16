package com.dao;

import java.util.List;

public interface IPersist {
	String save(List<String> words);
	List<String> getWords();
}
