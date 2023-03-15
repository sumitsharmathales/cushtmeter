package com.example.demo;

import java.util.ArrayList;
import java.util.Date;

public class PriorityIssueFields {
	
	public Priority priority;
    public Date resolutiondate;
    public Date created;
    public ArrayList<String> labels;
    public ArrayList<Components> components;
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public Date getResolutiondate() {
		return resolutiondate;
	}
	public void setResolutiondate(Date resolutiondate) {
		this.resolutiondate = resolutiondate;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
	public ArrayList<Components> getComponents() {
		return components;
	}
	public void setComponents(ArrayList<Components> components) {
		this.components = components;
	}
    
    

}
