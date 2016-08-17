package ourfood.extservice.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ourfood.utils.CommonUtil;

public class SmsService {

    private static final String MSG91_URL = "https://control.msg91.com/api/sendhttp.php?";
    private static final String MSG91_AUTH_KEY = "115124AUsui56jh57559c6b";
    // Sender ID - 6 Character
    private static final String MSG91_SENDER_ID = "APLITE";
    // (1 -Promotional 4 - Transactional)
    private static final String MSG91_ROUTE_DEFAULT = "4";

    public void postMessage(SmsParam param) {

        try {

            // Prepare parameter string
            StringBuilder sbPostData = new StringBuilder(MSG91_URL);
            sbPostData.append("authkey=" + MSG91_AUTH_KEY);
            sbPostData.append("&mobiles=" + param.getRegistrationIdsStr());
            sbPostData.append("&message=" + URLEncoder.encode(param.getData()));
            sbPostData.append("&route=" + MSG91_ROUTE_DEFAULT);
            sbPostData.append("&sender=" + MSG91_SENDER_ID);

            URL url = new URL(sbPostData.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("\nSending 'GET' request to URL : " + url);
            int responseCode = conn.getResponseCode();            
            System.out.println("Response Code : " + responseCode);

            // Read SMS gateway response.
            InputStream inputStream = conn.getInputStream();
            String resp = CommonUtil.getStringFromInputStream(inputStream);

            System.out.println(resp);

        } catch (IOException e) {
            System.out.println("Unable to send SMS message.");
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Unable to send SMS message.");
        }
    }

    public void postMessage() {

        // Your authentication key
        String authkey = "YourAuthKey";
        // Multiple mobiles numbers separated by comma
        String mobiles = "9999999";
        // Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "102234";
        // Your message to send, Add URL encoding here.
        String message = "Test message";
        // define route
        String route = "default";

        // Prepare Url
        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        // encoding message
        String encoded_message = URLEncoder.encode(message);

        // Send SMS API
        String mainUrl = "https://control.msg91.com/api/sendhttp.php?";

        // Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("authkey=" + authkey);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);

        // final string
        mainUrl = sbPostData.toString();
        try {
            // prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            // reading response
            String response;
            while ((response = reader.readLine()) != null)
                // print response
                System.out.println(response);

            // finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}