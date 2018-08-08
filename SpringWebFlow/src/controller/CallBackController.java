package controller;

import static org.apache.log4j.Logger.getLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import flow.PizzaFlowActions;
import twitter.MyAccessToken;
import twitter.OAuthToken;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
@Component
@Controller
@RequestMapping("/login/twitter")
public class CallBackController extends AbstractController {

	private static final Logger LOGGER = getLogger(CallBackController.class);		
	
	@Override
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		LOGGER.warn("Get redirect");
		ApplicationContext context = PizzaFlowActions.getContext();	
		OAuthToken oauthToken = (OAuthToken) context.getBean("oauthToken");
		MyAccessToken accestoken = (MyAccessToken) context.getBean("accessToken");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(oauthToken.getConsumerKey())
				.setOAuthConsumerSecret(oauthToken.getConsumerSecret()).setOAuthAccessToken(null)
				.setOAuthAccessTokenSecret(null).setOAuthRequestTokenURL("https://api.twitter.com/oauth/request_token")
				.setOAuthAuthorizationURL("https://api.twitter.com/oauth/authorize")
				.setOAuthAccessTokenURL("https://api.twitter.com/oauth/access_token");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		LOGGER.warn("Get token " + accestoken.getToken());
		String verifier = request.getParameter("oauth_verifier");
		RequestToken requestToken = new RequestToken(accestoken.getToken(), accestoken.getTokensecret());
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
		twitter.setOAuthAccessToken(accessToken);
		User user = twitter.verifyCredentials();
		ModelAndView model = new ModelAndView("hello");
		PizzaFlowActions.setUser(user);
		response.sendRedirect("../pizzaFlow");
		model.addObject("message", user.getScreenName());
		return model;
	}
}