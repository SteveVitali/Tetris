package tetris.utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HTTPUtilities {

    @SuppressWarnings("resource")
    public static void jsonArrayAsyncGetRequest(String url, JsonHandler handler) {
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
                handler.handleJsonArray(json);
                return response;
            }

            @Override
            public void onThrowable(Throwable t){
                // Something wrong happened.
            }
        });
    }

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
}
