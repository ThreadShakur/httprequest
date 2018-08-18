package requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Request {
	public CookieManager cookies = new CookieManager();
	public Map<String, String> params = new HashMap<>();
	public Map<String, String> headers = new HashMap<>();
	public String text;
	
	public int connectTimeout = 5000;
	public int readTimeout = 5000;
	
	public boolean storeCookies = true;
	public boolean followRedirects = true;
	
	private HttpURLConnection request(String url, String method) throws IOException, MalformedURLException {
		String cookies_parsed = "";
		
		// Converting params map to string
		if (params.size() != 0 && method == "GET") {
			url += "?" + ParameterStringBuilder.getParamsString(params);
		}
		
		// Opening connection
		URL url_ = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url_.openConnection();
		
		// Setting properties
		conn.setRequestMethod(method);
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		conn.setInstanceFollowRedirects(followRedirects);
		
		
		// Manage cookies
		for (HttpCookie cookie : cookies.getCookieStore().getCookies()) {
			cookies_parsed += cookie.toString() + ";";
		}
		conn.setRequestProperty("Cookie", cookies_parsed);
		
		// Setting headers
		if (headers.size() != 0) {
			for (Map.Entry<String, String> data : headers.entrySet()) {
				conn.setRequestProperty(data.getKey(), data.getValue());
			}
		}
		
		if (params != null && method == "POST") {
			conn.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			out.writeBytes(ParameterStringBuilder.getParamsString(params));
			out.flush();
			out.close();
		}
		
		// Reading response
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) content.append(inputLine);
		in.close();
		text = content.toString();

		// Store cookies
		if (storeCookies && conn.getHeaderField("Set-Cookie") != null) {
			for (HttpCookie cookie : HttpCookie.parse(conn.getHeaderField("Set-Cookie"))) {
				cookies.getCookieStore().add(null, cookie);
			}
		}
		
		return conn;
	}
	
	public HttpURLConnection get(String url) throws MalformedURLException, IOException {
		return request(url, "GET");
	}
	
	public HttpURLConnection post(String url) throws MalformedURLException, IOException {
		return request(url, "POST");
	}
	
}
