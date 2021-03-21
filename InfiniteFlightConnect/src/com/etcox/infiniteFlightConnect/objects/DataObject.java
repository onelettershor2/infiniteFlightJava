package com.etcox.infiniteFlightConnect.objects;

public class DataObject {

	private boolean dataBool = false;
	private int dataInt = -1;
	private float dataFloat = -1;
	private double dataDouble = -1;
	private String dataString = null;
	private long dataLong = -1;
	
	private int dataType = -1;
	
	public DataObject(){}
	
	public DataObject(boolean data){
		this.dataBool = data;
		dataType = 0;
	}
	
	public DataObject(int data){
		this.dataInt = data;
		dataType = 1;
	}
	
	public DataObject(float data){
		this.dataFloat = data;
		dataType = 2;
	}
	
	public DataObject(double data){
		this.dataDouble = data;
		dataType = 3;
	}
	
	public DataObject(String data){
		this.dataString = data;
		dataType = 4;
	}
	
	public DataObject(long data){
		this.dataLong = data;
		dataType = 5;
	}
	
	public int getDataType(){
		return dataType;
	}
	
	public boolean getData0(){
		return dataBool;
	}
	
	public int getData1(){
		return dataInt;
	}
	
	public float getData2(){
		return dataFloat;
	}
	
	public double getData3(){
		return dataDouble;
	}
	
	public String getData4(){
		return dataString;
	}
	
	public long getData5(){
		return dataLong;
	}
	
}
