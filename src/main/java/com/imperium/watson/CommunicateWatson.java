package com.imperium.watson;

import java.util.Map;

//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@Path("CommunicateWatson")
public class CommunicateWatson {

	private ConversationService service;
	private String version = "2017-05-26";
	private String username = "38e95977-72fb-4629-866a-6724ccc701c7";
	private String password = "5b8m2hpjeAQp";
	private String workspaceId = "61f50108-996e-48c0-a2f1-eb03d86ddd5e";

	@POST
	@Produces(MediaType.TEXT_PLAIN)

	public String getIt(String x) {
		establishConn();

		try {
			JSONObject json = (JSONObject)parseJson(x);
			
			String text = ((JSONObject)json.get("input")).get("text").toString();;
			
			Map<String,Object> context = null;
			try {
				context = (Map)json.get("context");
			}catch(Exception e){};
			
			
			MessageRequest newMessage = new MessageRequest.Builder().inputText(text)
					// Replace with the context obtained from the initial request
					.context(context)
					.build();

			MessageResponse response = service.message(workspaceId, newMessage).execute();

			System.out.println(response);

			return response.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Http Error 400";
		} catch (Exception e) {
			
			return "Http Error 500";
		}

	}

	private Object parseJson(String json) throws ParseException, Exception {

		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(json);
		return obj;
	}

	private void establishConn() {
		service = new ConversationService(version);
		service.setUsernameAndPassword(username, password);
	}

}
