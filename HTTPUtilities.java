import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HTTPUtilities {

    // This is a helper function for doing asynchronous JSON GET requests
    @SuppressWarnings("resource")
    public static void jsonArrayAsyncGetRequest(String url, JsonHandler handler) {
        final JsonHandler handlerFinal = handler; // To fix compilation issue on linux
        disableLogging();
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
                String jsonString = readResponseContent(response.getResponseBodyAsStream());
                JsonParser parser = new JsonParser();
                JsonArray json = (JsonArray) parser.parse(jsonString);
                handlerFinal.handleJsonArray(json);
                return response;
            }

            @Override
            public void onThrowable(Throwable t){
                // Something wrong happened.
            }
        });
    }

    // This is a helper function for doing synchronous JSON POST requests
    // it returns a string so the caller can check if it was successful
    public static String jsonArrayPostRequest(String url, List<BasicNameValuePair> params) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);
            return readResponseContent(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Concvert response stream into a String
    private static String readResponseContent(InputStream content) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(content)
        );
        StringBuffer jsonBuffer = new StringBuffer();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                jsonBuffer.append(line);
            }
            return jsonBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // We don't want to use Log4j, but somehow it's built into AsyncHttpRequest
    // so we use this helper method to keep it from logging meaningless errors
    // Source: http://stackoverflow.com/questions/571960/disabling-log4j-output-in-java
    private static void disableLogging() {
        @SuppressWarnings("unchecked")
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for ( Logger logger : loggers ) {
            logger.setLevel(Level.OFF);
        }
    }
}
