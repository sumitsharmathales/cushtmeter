package com.example.demo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriorityIssueCount {
	
	private int startAt;
	private int maxResults;
	private int total;
	private String expand;
	public ArrayList<PriorityIssue> getIssues() {
		return issues;
	}
	public void setIssues(ArrayList<PriorityIssue> issues) {
		this.issues = issues;
	}
	private ArrayList<PriorityIssue> issues;
	public int getStartAt() {
		return startAt;
	}
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	
	
	

}
