package tetris.utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class HTTPUtilities {

    public static JsonArray jsonArrayGetRequest(String url) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = httpClient.execute(getRequest);

            // HTTP response code 200 = success
            if (response.getStatusLine().getStatusCode() != 200) {
                int err = response.getStatusLine().getStatusCode();
                throw new RuntimeException("HTTP Error: "+err);
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent())
            );
            StringBuffer jsonBuffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonBuffer.append(line);
            }
            String jsonString = jsonBuffer.toString();
            JsonParser parser = new JsonParser();
            JsonArray json = (JsonArray) parser.parse(jsonString);
            System.out.println("Fetched JSON"+json.toString());
            return json;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
