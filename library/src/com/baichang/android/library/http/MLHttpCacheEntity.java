package com.baichang.android.library.http;



public class MLHttpCacheEntity {
	 
    public MLHttpCacheEntity() {
		super();
	}

	
    public MLHttpCacheEntity(String key, String value,long expiry) {
		super();
		this.key = key;
		this.value = value;
		if(expiry!=0){
			this.expiry =System.currentTimeMillis()/1000+expiry/1000;
		}
	}

    public String key;

    public String value;

    public long expiry;
}
