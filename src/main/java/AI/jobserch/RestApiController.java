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
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
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

	@RequestMapping(value = "/jobSearch", method = RequestMethod.POST , consumes =MediaType.APPLICATION_JSON_VALUE)
	public String getJobs(@RequestBody User user) {

		System.out.println("In jobsearch"+ user.getDesc());
		List<String> jobs = new ArrayList<String>();
		PersonalityInsights service = new PersonalityInsights("2017-10-13", "ce431ce2-c7b3-45a3-8481-3963a6556b47",
				"IWqKLZzIuKRM");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Watson-Learning-Opt-Out", "true");
		service.setDefaultHeaders(headers);

		try {
			Content content = new Content();
			ContentItem item = new ContentItem();
			item.setContent(user.getDesc());
			item.setContentType("text/plain");
			item.setLanguage("en");
			content.addContentItem(item);
			ProfileOptions options = new ProfileOptions.Builder().contentItems(content.getContentItems())
					.consumptionPreferences(true).rawScores(true).build();
			Profile profile = service.getProfile(options).execute();
			// System.out.println(profile.toString());

			String jobMatcher = callJobAlogorithm(profile);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(profile.toString());
			json.add("jobMatches", parser.parse(jobMatcher.toString()));
			System.out.println(json);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String callJobAlogorithm(Profile profile) {

		final String uri = new String("https://jobmatcher1.mybluemix.net/matchjob");
		RestTemplate restTemplate = new RestTemplate();
		// Add the Jackson message converter
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Basic " + "xxxxxxxxxxxx");
		HttpEntity<Profile> entity = new HttpEntity<Profile>(profile, headers);

		// send request and parse result
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

		System.out.println(response.getBody());
		return response.getBody();

	}

}
