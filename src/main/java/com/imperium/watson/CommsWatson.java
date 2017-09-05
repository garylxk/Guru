package com.imperium.watson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

/**
 * Servlet implementation class CommsWatson
 */
@WebServlet("/CommsWatson")
public class CommsWatson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ConversationService service;
	private String version = "2017-05-26";
	private String username = "38e95977-72fb-4629-866a-6724ccc701c7";
	private String password = "5b8m2hpjeAQp";
	private String workspaceId = "61f50108-996e-48c0-a2f1-eb03d86ddd5e";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommsWatson() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Establish Watson Connection
		establishConn();
		
		String bodyContent = "";
		if ("POST".equalsIgnoreCase(request.getMethod())) 
		{
			bodyContent = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		}
		
		response.setContentType("text/html;charset=UTF-8");

		try {
			JSONObject json = (JSONObject) parseJson(bodyContent);

			String text = ((JSONObject) json.get("input")).get("text").toString();
			
			Map<String, Object> context = null;
			try {
				context = (Map) json.get("context");
			} catch (Exception e) {
			}
			;

			MessageRequest newMessage = new MessageRequest.Builder().inputText(text)
					// Replace with the context obtained from the initial request
					.context(context).build();

			MessageResponse content = service.message(workspaceId, newMessage).execute();
			response.getWriter().append(content.toString());	

		} catch (Exception e) {

			//return "Http Error 500";
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
