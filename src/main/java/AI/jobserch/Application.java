package AI.jobserch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.stream.JsonReader;
import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Content;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;
import com.ibm.watson.developer_cloud.util.GsonSingleton;
 
 
@SpringBootApplication(scanBasePackages={"AI.jobserch"})
public class Application implements CommandLineRunner {
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
 
    public void run(String... strings) throws Exception {
    	
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
    }
}