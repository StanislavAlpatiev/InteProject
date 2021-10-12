package se.su.dsv.RegisterSystem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MockBank {
    public static double getRate(Currency from, Currency to) throws IOException {
        // Setting URL
        String url_str = ("https://v6.exchangerate-api.com/v6/3f192049848a3da4ed3985ce/pair/" + from + "/" + to);

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonobj.get("result").getAsString();
        if(req_result.equals("error")) {
            throw new IllegalStateException();
        }

        return jsonobj.get("conversion_rate").getAsDouble();
    }

    public static void main(String[] args) throws IOException {
        double rate = getRate(Currency.USD, Currency.SEK);;
        System.out.println(rate);
    }
}
