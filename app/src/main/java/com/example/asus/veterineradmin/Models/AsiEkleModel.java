package com.example.asus.veterineradmin.Models;

public class AsiEkleModel{
	private boolean tf;
	private String text;

	public void setTf(boolean tf){
		this.tf = tf;
	}

	public boolean isTf(){
		return tf;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	@Override
 	public String toString(){
		return 
			"AsiEkleModel{" + 
			"tf = '" + tf + '\'' + 
			",text = '" + text + '\'' + 
			"}";
		}
}
