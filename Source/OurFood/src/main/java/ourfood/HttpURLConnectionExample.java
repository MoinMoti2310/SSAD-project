import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

	private final String USER_AGENT = "Mozilla/5.0";
	double Olat,Olong,Dlat,Dlong;

	public HttpURLConnectionExample() {
		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080"); 
	}

	public static void main(String[] args) throws Exception {

		HttpURLConnectionExample http = new HttpURLConnectionExample();

		http.sendGet(17.444792,78.348310,17.596589,78.124105);


	}

	// HTTP GET request
	private void sendGet(double Olat,double Olong,double Dlat,double Dlong ) throws Exception {
		//Olat = 17.444792;
		//Olong =	78.348310;
		//Dlat = 17.596589;
		//Dlong = 78.124105;

		String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Olat + "," + Olong + "&destination=" + Dlat + "," + Dlong + "&key=AIzaSyCxfMjWup0K4Dg_T7qkA4mLZ1FW-lOBdVQ";

		//String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + 17.444792 + "," + 78.348310 + "&destination=" + 17.596589 + "," + 78.124105 + "&key=AIzaSyCxfMjWup0K4Dg_T7qkA4mLZ1FW-lOBdVQ";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
}
