package se.su.dsv.RegisterSystem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class Bank {
    public static BigDecimal getRate(Currency from, Currency to) throws IOException {
        // Setting URL
        String url_str = ("https://v6.exchangerate-api.com/v6/3f192049848a3da4ed3985ce/pair/" + from + "/" + to);

        try {
            // Making Request
            URL url = new URL(url_str);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to JSON
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();
            // Accessing object
            String req_result = jsonobj.get("result").getAsString();
            if (req_result.equals("error")) {
                throw new IllegalStateException();
            }

            return jsonobj.get("conversion_rate").getAsBigDecimal();

        } catch (IOException e) {
            throw new IOException();
        }
    }

    public static Money exchange(Money money, Currency currency) throws IOException {
        BigDecimal rate = getRate(money.getCurrency(), currency);
        BigDecimal newAmount = money.getAmount().multiply(rate);
        return new Money(newAmount, currency);
    }
}
