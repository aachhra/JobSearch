/**
 * 
 */
package AI.jobserch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Content;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ContentItem;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Trait;
 

 
@RestController
@RequestMapping("/api")
public class RestApiController {
	 public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
	 
	 @RequestMapping(value = "/jobSerach", method = RequestMethod.GET)
	    public ResponseEntity<List<LinkedHashMap<String, String>>>getJobs(@RequestParam(value="name") String name ,@RequestParam(value="desc") String userDesc) {
		
		 
		 List<String> jobs = new ArrayList<String>();
		 PersonalityInsights service = new PersonalityInsights("2017-10-13","ce431ce2-c7b3-45a3-8481-3963a6556b47", "IWqKLZzIuKRM");
		 Map<String, String> headers = new HashMap<String, String>();
		 headers.put("X-Watson-Learning-Opt-Out", "true");
		 service.setDefaultHeaders(headers);
		 
		 try {
			 Content content = new Content();
			  ContentItem item=  new ContentItem();
			  item.setContent(userDesc);
			  item.setContentType("text/plain");
			  item.setLanguage("en");
			  content.addContentItem(item);	
			  ProfileOptions options = new ProfileOptions.Builder()
			    .contentItems(content.getContentItems())
			    .consumptionPreferences(true)
			    .rawScores(true)
			    .build();
			  Profile profile = service.getProfile(options).execute();
			  System.out.println(profile);
			  //callJobAlogorithm(profile);
			  
			  List<LinkedHashMap<String, String>> jobmap = new ArrayList<LinkedHashMap<String, String>>();
			  LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			  map.put("Job Name", "Teacher");
			  map.put("Job Description", "This is first job");
			  map.put("Match percentage", "80%");
			  jobmap.add(map);
			  return new ResponseEntity<List<LinkedHashMap<String, String>>>(jobmap, HttpStatus.OK);
			} catch (Exception e) {
			  e.printStackTrace();
			}
		return null;
		
	    }
	 
public void callJobAlogorithm(Profile profile){
		 
		 final String uri = new String("http://localhost:8080/api/jobs");
		    RestTemplate restTemplate = new RestTemplate();
		    // Add the Jackson message converter
		    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		    

		    // set headers
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("Authorization", "Basic " + "xxxxxxxxxxxx");
		    HttpEntity<Profile> entity = new HttpEntity<Profile>(profile, headers);

		    // send request and parse result
		    ResponseEntity<String> response = restTemplate
		            .exchange(uri, HttpMethod.POST, entity, String.class);

		    
		 
		 
	 }
	 @RequestMapping(value = "/jobs", method = RequestMethod.POST)
	    @ResponseBody
	    public void updateCustomer(@RequestBody String s) {
		 System.out.println("HI..." +s);
	        
	       
	    }
	
}
 
   