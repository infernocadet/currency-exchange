package currency.data;

import okhttp3.*;

import java.io.IOException;
import java.util.Currency;

public class CurrencyClient {
    private final String apiURL = "https://xecdapi.xe.com/v1/";
    private static CurrencyClient client = new CurrencyClient();
    private final OkHttpClient httpClient;

    private CurrencyClient() {
        this.httpClient = CurrencyClient.getClient();
    }

    public static synchronized CurrencyClient getCurrencyClient(){
        if (client == null) {
            client =  new CurrencyClient();
        }
        return client;
    }

    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder().authenticator(new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic("usyd509488321", "gs2aa6rv1lqpk5pvtpj5pgfpe7");
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();
    }

    public

    public Response fetchCurrencyData() {
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
                throw new IOException("Unexpected code " + response);
            } else {
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
