package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TestController {

	@Autowired
	private RestTemplate restTemplate;
	
	private String jiraURL = "https://<Jira_Server_HostName>";
	private String jiraToken = "token_value";

	@GetMapping("/projectCustomersIssues")
	public Map<String, Integer> fetchMessage(@RequestParam String projectName) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer "+jiraToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(jiraURL+"/rest/api/2/search?jql=project="+projectName+" and (labels is not EMPTY or cf[10519] is not empty)&maxResults=0", HttpMethod.GET, entity, String.class);
		ObjectMapper mapper = new ObjectMapper();
		IssueCount issueCount =  mapper.readValue(response.getBody(), IssueCount.class);
		int totalIssues = issueCount.getTotal();
		System.out.println("Total Issues:"+ totalIssues);
		int totalIssueAPICall = totalIssues>0?1:0;
		if(totalIssues>1000) {
			totalIssueAPICall = totalIssues/1000;
			if(totalIssues%1000!=0) {
				totalIssueAPICall = totalIssueAPICall+1;
			}
		}
		int maxResults = 999;
		int startAt = 0;
		Map<String, Integer> customerIssueMap = new HashMap();
		for(int i=0; i<totalIssueAPICall; i++) {
			ResponseEntity<String> resp = restTemplate.exchange(jiraURL+"/rest/api/2/search?jql=project ="+projectName+" and (labels is not EMPTY or cf[10519] is not empty)&startAt="+startAt+"&maxResults="+maxResults+"&fields=labels,Customer", HttpMethod.GET, entity, String.class);
			System.out.println(jiraURL+"/rest/api/2/search?jql=project ="+projectName+" and (labels is not EMPTY or cf[10519] is not empty)&startAt="+startAt+"&maxResults="+maxResults+"&fields=labels,Customer");	
			IssueCount issues =  mapper.readValue(resp.getBody(), IssueCount.class);
			List<Issue> allIssues = issues.getIssues();
				for(Issue issue: allIssues) {
					List<String> customerLabels = issue.getFields().getLabels();
					for(String label:customerLabels) {
						if(!customerIssueMap.containsKey(label.toUpperCase())) {
							customerIssueMap.put(label.toUpperCase(), 1);
						} else {
							customerIssueMap.put(label.toUpperCase(), customerIssueMap.get(label.toUpperCase())+1);
						}
					}
				}
				startAt = startAt+maxResults+1;	
		}
     	return customerIssueMap;
        
	}
	
	@GetMapping("/allProjects")
	public List<ProjectData> fetchAllProjects() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer "+jiraToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(jiraURL+"/rest/api/2/project", HttpMethod.GET, entity, String.class);
		ObjectMapper mapper = new ObjectMapper();
		Project[] projects =  mapper.readValue(response.getBody(), Project[].class);
		List<ProjectData> projectData = new ArrayList();
		for(int i=0;i<projects.length;i++) {
			ProjectData proj = new ProjectData();
			proj.setId(projects[i].getId());
			proj.setName(projects[i].getName());
			proj.setKey(projects[i].getKey());
			projectData.add(proj);
		}
		return projectData;
        
	}
	
	@GetMapping("/customerP1P2Issues")
	public Prediction fetchP1Issues(@RequestParam String projectName, @RequestParam String customer) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer "+jiraToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(jiraURL+"/rest/api/2/search?jql=priority in (\"Very High\",\"High\") and (issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", labels, \"("+customer+")\") OR issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", Customer, \"("+customer+")\"))&startAt=0&maxResults=999&fields=key,labels,Customer,created,resolutiondate,priority,components", HttpMethod.GET, entity, String.class);
		Map<String, Map<String,Integer>> customerPriorityMap = new HashMap();
		Map<String, Integer> priorityMap = new HashMap();
		Map<String, Integer> timeTakenMap = new HashMap();
		ObjectMapper mapper = new ObjectMapper();
		PriorityIssueCount p1issues =  mapper.readValue(response.getBody(), PriorityIssueCount.class);
		List<PriorityIssue> allIssues = p1issues.getIssues();
		Set<Components> totalComponents = new HashSet();
			for (PriorityIssue issue : allIssues) {
				String priority = issue.getFields().getPriority().getName();
				Date resolutionDate = issue.getFields().getResolutiondate();
				if(resolutionDate==null) {
					resolutionDate = new Date();
				}
				Date createdDate = issue.getFields().getCreated();
				long datediff = resolutionDate.getTime() - createdDate.getTime();
				int days = (int)TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS);
					if (!priorityMap.containsKey(priority.toUpperCase())) {
						priorityMap.put(priority.toUpperCase(), 1);
						timeTakenMap.put(priority.toUpperCase(), days);
					} else {
						priorityMap.put(priority.toUpperCase(), priorityMap.get(priority.toUpperCase()) + 1);
						timeTakenMap.put(priority.toUpperCase(), timeTakenMap.get(priority.toUpperCase()) + days);
					}
					totalComponents.addAll(issue.getFields().getComponents());	
			}
			Set<String> uniqueComponents = new HashSet();
			for (Components comp: totalComponents) {
				uniqueComponents.add(comp.getName().toUpperCase());
			}
			ResponseEntity<String> resp = restTemplate.exchange(jiraURL+"/rest/api/2/search?jql=issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", labels, \"("+customer+")\") OR issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", Customer, \"("+customer+")\")&startAt=0&maxResults=999&fields=key,labels,Customer,created,resolutiondate,priority,components", HttpMethod.GET, entity, String.class);
			PriorityIssueCount totalIssues =  mapper.readValue(resp.getBody(), PriorityIssueCount.class);
			int totalIssuesCount = totalIssues.getTotal();
			List<PriorityIssue> finalIssues = p1issues.getIssues();
			int totalDays = 0;
			for (PriorityIssue issue : finalIssues) {
				Date resolutionDate = issue.getFields().getResolutiondate();
				if(resolutionDate==null) {
					resolutionDate = new Date();
				}
				Date createdDate = issue.getFields().getCreated();
				long datediff = resolutionDate.getTime() - createdDate.getTime();
				int days = (int)TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS);
				totalDays = totalDays + days;
			}
			Set<String> priorityIssueData = priorityMap.keySet();
			for(String priorityIssue:priorityIssueData) {
				Map<String, Integer> keyValueMap = new HashMap();
				keyValueMap.put("totalCount", (priorityMap.get(priorityIssue.toUpperCase())*100)/totalIssuesCount);
				keyValueMap.put("totalTime", (timeTakenMap.get(priorityIssue.toUpperCase())*100)/totalDays);
				customerPriorityMap.put(priorityIssue.toUpperCase(), keyValueMap);
			}
			Map<String, Integer> keyValueMap = new HashMap();
			keyValueMap.put("totalCount", uniqueComponents.size());
			customerPriorityMap.put("impactedComponent", keyValueMap);
			ResponseEntity<String> resp1 = restTemplate.exchange(jiraURL+"/rest/api/2/search?jql=type in (Improvement,\"New Feature\",Requirement,epic) and \"External issue ID\" is not empty and (issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", labels, \"("+customer+")\") OR issueFunction in issueFieldMatch(\"project="+projectName+" and (labels is not EMPTY OR cf[10519] is not empty)\", Customer, \"("+customer+")\"))&maxResults=0", HttpMethod.GET, entity, String.class);
			PriorityIssueCount totalFeatures =  mapper.readValue(resp1.getBody(), PriorityIssueCount.class);
			int totalFeaturesCount = totalIssues.getTotal();
			keyValueMap.put("totalCount", uniqueComponents.size());
			customerPriorityMap.put("newFeatures", keyValueMap);
			HttpEntity<Map> entityFinal = new HttpEntity(customerPriorityMap,headers);
			ResponseEntity<String> respFinal = restTemplate.exchange("http://127.0.0.1:8090/predict", HttpMethod.POST, entityFinal, String.class);
			Prediction prediction =  mapper.readValue(respFinal.getBody(), Prediction.class);
		return prediction;
        
	}
}
