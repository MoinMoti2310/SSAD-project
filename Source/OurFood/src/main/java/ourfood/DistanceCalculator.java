package ourfood;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceCalculator {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String API_KEY = "AIzaSyCpmt9JgfPw_MJJo6iSB0pB0uVPS89hZ0E";

    //public DistanceCalculator() {
    //	System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
    //	System.setProperty("http.proxyPort", "8080");
    //}

    // HTTP GET request
    private String getGoogleDistanceApiData(LocationHolder[] origins, LocationHolder[] destinations) throws Exception {
        String[] urlTokens = new String[4];
        String url = "";
        int i;

        urlTokens[0] = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        urlTokens[1] = "origins=";
        urlTokens[2] = "destinations=";
        urlTokens[3] = "key=" + API_KEY;

        for (i = 0; i < origins.length - 1; i++)
            urlTokens[1] += origins[i].latitude + "," + origins[i].longitude + "|";
        urlTokens[i] = origins[i].latitude + "," + origins[i].longitude + "&";

        for (i = 0; i < destinations.length - 1; i++)
            urlTokens[1] += destinations[i].latitude + "," + destinations[i].longitude + "|";
        urlTokens[i] = origins[i].latitude + "," + origins[i].longitude + "&";

        for (i = 0; i < 4; i++)
            url += urlTokens[i];

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

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
