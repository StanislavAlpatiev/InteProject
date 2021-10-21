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


public class Bank implements BankService {

    /*GetRate communicates with an API that and returns the exchange rate between two currencies
    The exchange rate is one sided meaning that it is only from currency 'A' to currency 'B'*/
    @Override
    public BigDecimal getRate(Currency from, Currency to) {
        // Setting URL
        String urlStr = ("https://v6.exchangerate-api.com/v6/3f192049848a3da4ed3985ce/pair/" + from + "/" + to);

        try {
            //Making Request
            URL url = new URL(urlStr);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to JSON
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();
            //Accessing object
            String reqResults = jsonobj.get("result").getAsString();
            if (reqResults.equals("error")) {
                throw new IllegalStateException();
            }

            return jsonobj.get("conversion_rate").getAsBigDecimal();

        } catch (IOException e) {
            System.err.print(e.getMessage());
            return null;
        }
    }

    //Converts a given money object of Currency 'A' to a money object with same value in currency 'B'
    /*Example: Exchange rate from SEK to USD is 10 SEK for every USD, Passing SEKMoney with value 10
    will return USDMoney with value 1*/
    @Override
    public Money exchange(Money money, Currency currency) {
        BigDecimal rate = getRate(money.getCurrency(), currency);
        return exchange(money, currency, rate);
    }

    //Converts a given money object of Currency 'A' to a money object with same value in currency 'B' as per supplied rate
    /*Example: Supplied Exchange rate from SEK to USD is 10 SEK for every USD, Passing SEKMoney with value 10
    will return USDMoney with value 1*/
    @Override
    public Money exchange(Money money, Currency currency, BigDecimal rate) {
        BigDecimal newAmount = money.getAmount().multiply(rate);
        return new Money(newAmount, currency);
    }
}
