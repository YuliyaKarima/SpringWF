package controller;

import static org.apache.log4j.Logger.getLogger;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import facebook.FBConnection;
import facebook.FBGraph;

@Component
@Controller
@RequestMapping("/login/facebook")
public class CallBackControllerFacebook extends AbstractController {

	private static final Logger LOGGER = getLogger(CallBackControllerFacebook.class);		
	
	@Override
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		LOGGER.warn("Get redirect");
		String code = request.getParameter("code");
		LOGGER.warn("Code: " + code);
		if (code == null || code.equals("")) {
			throw new RuntimeException(
					"ERROR: Didn't get code parameter in callback.");
		}
		FBConnection fbConnection = new FBConnection();
		String accessToken = fbConnection.getAccessToken(code);
		FBGraph fbGraph = new FBGraph(accessToken);
		String graph = fbGraph.getFBGraph();
		Map<String, String> fbProfileData = fbGraph.getGraphData(graph);		
		ModelAndView model = new ModelAndView("hello");
		model.addObject("message", fbProfileData.get("name"));
		return model;
	}
}