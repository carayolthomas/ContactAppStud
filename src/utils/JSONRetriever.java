package utils;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * A class which define a static method in order to retrieve JSON file
 * @author Thomas
 *
 */
public class JSONRetriever {
	
	/**
	 * Retrieve a JSON file given a URL
	 * @param url
	 * @return JSON in String
	 */
	public static String getContactsAppStud(String url) {
		String lToReturn = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;

        HttpGet httpGet = new HttpGet(url);
        try {
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
            lToReturn = EntityUtils.toString(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return lToReturn;
	}
}
