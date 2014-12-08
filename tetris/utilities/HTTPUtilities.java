package tetris.utilities;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HTTPUtilities {

    @SuppressWarnings("resource")
    public static void jsonArrayGetRequest(String url, JsonHandler handler) {
        (new AsyncHttpClient())
        .prepareGet(url)
        .execute(new AsyncCompletionHandler<Response>(){
            @Override
            public Response onCompleted(Response response) throws Exception{
                // HTTP response code 200 = success
                if (response.getStatusCode() != 200) {
                    int err = response.getStatusCode();
                    throw new RuntimeException("HTTP Error: "+err);
                }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(response.getResponseBodyAsStream())
                );

                StringBuffer jsonBuffer = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    jsonBuffer.append(line);
                }
                String jsonString = jsonBuffer.toString();

                JsonParser parser = new JsonParser();
                JsonArray json = (JsonArray) parser.parse(jsonString);
                handler.handleJsonArray(json);
                return response;
            }

            @Override
            public void onThrowable(Throwable t){
                // Something wrong happened.
            }
        });
    }
}
