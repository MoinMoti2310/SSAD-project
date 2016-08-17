package ourfood.extservice.gcm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ourfood.utils.CommonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GcmService {

    private static final String GCM_URL = "https://android.googleapis.com/gcm/send";
    private static final String API_KEY = "AIzaSyDaRrmBwaujNX6ICsZn3uiMm6LTfR_-igU";

    public void postMessage(GcmParam param) {

        try {

            // Prepare JSON containing the GCM message content.

            String jsonString = serialize(param, false);

            // Create connection to send GCM Message request.
            URL url = new URL(GCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonString.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = CommonUtil.getStringFromInputStream(inputStream);

            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for "
                    + "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server "
                    + "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Unable to send GCM message.");
        }
    }

    private String serialize(Object object, boolean pretty) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }

        return mapper.writeValueAsString(object);
    } 
}