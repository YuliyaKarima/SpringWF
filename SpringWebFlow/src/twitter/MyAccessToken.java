package twitter;

import static org.apache.log4j.Logger.getLogger;

import org.apache.log4j.Logger;

import flow.PizzaFlowActions;

public class MyAccessToken {
	private static final Logger LOGGER = getLogger(PizzaFlowActions.class);
	private String token;
	private String tokensecret;

	public String getTokensecret() {
		return tokensecret;
	}

	public MyAccessToken() {
		LOGGER.warn("New access token");
		LOGGER.warn(this.hashCode());
	}

	public void setTokensecret(String tokensecret) {
		this.tokensecret = tokensecret;
	}

	public String getToken() {		
		return token;
	}

	public void setToken(String token) {
		LOGGER.warn("Set token");
		this.token = token;
	}
}
