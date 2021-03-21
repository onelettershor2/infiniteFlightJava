package com.etcox.infiniteFlightConnect.objects;

public class ManifestObject {

	private final int id;
	private final int type;
	private final String path;
	
	// init
	public ManifestObject(int id, int type, String path){
		this.id = id;
		this.type = type;
		this.path = path;
	}
	
	// getters
	
	public int getID(){
		return id;
	}
	
	public int getType(){
		return type;
	}
	
	public String getPath(){
		return path;
	}
	
}
