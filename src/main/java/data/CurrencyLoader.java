package data;

import okhttp3.*;
import org.json.simple.JSONArray;

import java.io.IOException;

public class CurrencyLoader {
    private final String apiURL = "https://xecdapi.xe.com/v1/";
    private final OkHttpClient httpClient;

    public CurrencyLoader() {
        this.httpClient = CurrencyLoader.getClient();
    }

    public static OkHttpClient getClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic("usyd509488321", "gs2aa6rv1lqpk5pvtpj5pgfpe7");
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();
        return httpClient;
    }

    public void fetchCurrencyData() {
        String rAction = "currencies";
        String rForm = ".json/";
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(apiURL + rAction + rForm)
                .newBuilder();
        urlBuilder.addQueryParameter("obsolete", "true");

        String rURL = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(rURL)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Not good");
            } else {
                System.out.println(response.code());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CurrencyLoader cLoader = new CurrencyLoader();
        cLoader.fetchCurrencyData();
    }
}
