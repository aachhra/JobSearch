/**
 * 
 */
package AI.jobserch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
/**
 * @author archana.achhra
 *
 */
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.stream.JsonReader;
import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Content;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;
import com.ibm.watson.developer_cloud.util.GsonSingleton;
 

 
@RestController
@RequestMapping("/api")
public class RestApiController {
	 public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
	 
	// https://watson-api-explorer.mybluemix.net/apis/personality-insights-v3  
	 @RequestMapping(value = "/jobSerach", method = RequestMethod.GET)
	    public void getJobs(@RequestParam(value="name") String name ,@RequestParam(value="desc") String userDesc) {
		
		 
		 
		 PersonalityInsights service = new PersonalityInsights("2017-10-13","ce431ce2-c7b3-45a3-8481-3963a6556b47", "IWqKLZzIuKRM");
		 Map<String, String> headers = new HashMap<String, String>();
		 headers.put("X-Watson-Learning-Opt-Out", "true");
		 service.setDefaultHeaders(headers);
		 
		 try {
			  JsonReader jReader = new JsonReader(new FileReader("C:\\Users\\archana.achhra\\profile.json"));
			  Content content =
					  GsonSingleton.getGson().fromJson(jReader, Content.class);
			  ProfileOptions options = new ProfileOptions.Builder()
			    .contentItems(content.getContentItems())
			    .consumptionPreferences(true)
			    .rawScores(true)
			    .build();
			  Profile profile = service.getProfile(options).execute();
			  System.out.println(profile);
			} catch (FileNotFoundException e) {
			  e.printStackTrace();
			}
		// return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	    }
	
}
 
   