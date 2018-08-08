package twitter;

public class OAuthToken {
	public String consumerKey;
	public String consumerSecret;

	public String getConsumerKey() {
		return consumerKey;
	}

	public OAuthToken() {

	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
}
