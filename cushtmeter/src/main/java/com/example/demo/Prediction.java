package com.example.demo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prediction {
	
	@JsonProperty("Recomendation") 
    public ArrayList<String> recomendation;
    public int prediction;
	public ArrayList<String> getRecomendation() {
		return recomendation;
	}
	public void setRecomendation(ArrayList<String> recomendation) {
		this.recomendation = recomendation;
	}
	public int getPrediction() {
		return prediction;
	}
	public void setPrediction(int prediction) {
		this.prediction = prediction;
	}
    
    

}
