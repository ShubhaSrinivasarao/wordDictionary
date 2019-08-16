package com;

import com.dao.FilePersist;
import com.dao.IPersist;

public class PersistentContext {

	public IPersist getPersistent(String mode){
		IPersist persist = null;
		if("file".equals(mode)){
			persist= new FilePersist();
		}
		return persist;
	}
}
