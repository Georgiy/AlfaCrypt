package crypt.alfa.com.alfacrypt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Will show the string "data" that holds the results
    TextView results;
    // URL of object to be parsed
    // String JsonURL = "http://gregoryiv.narod.ru/json/Example-Object.JSON";
    String JsonURL = "https://api.cryptonator.com/api/ticker/eth-rur";

    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        WebView wv = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.setWebViewClient(new Callback()); //HERE IS THE MAIN CHANGE.
        wv.loadUrl("https://ru.cryptonator.com/rates/");
        getSupportActionBar().setTitle(R.string.title_rates);

        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);
        // Casts results into the TextView found within the main layout XML with id jsonData
        results = (TextView) findViewById(R.id.jsonData);
        results.setVisibility(View.GONE);
        new JsonHandler().invoke();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            WebView wv = (WebView) findViewById(R.id.webview);
            WebSettings webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);
            switch (item.getItemId()) {
                case R.id.navigation_rates:
                    wv.setWebViewClient(new Callback()); //HERE IS THE MAIN CHANGE.
                    wv.loadUrl("https://ru.cryptonator.com/rates/");
                    getSupportActionBar().setTitle(R.string.title_rates);
                    results.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_eth_rur:
                    wv.setWebViewClient(new Callback()); //HERE IS THE MAIN CHANGE.
                    wv.loadUrl("https://ru.cryptonator.com/rates/ETH-RUR");
                    getSupportActionBar().setTitle(R.string.title_eth_rur);
                    results.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_miner:
                    wv.setWebViewClient(new Callback()); //HERE IS THE MAIN CHANGE.
                    wv.loadUrl("https://ethermine.org/miners/a0AC224BFA8132164c68cbD0bf58b7Ac815Ee2A6");
                    getSupportActionBar().setTitle(R.string.title_miner);
                    results.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }

    };

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            return (false);
        }

    }

    private class JsonHandler {
        public void invoke() {
            // Creating the JsonObjectRequest class called obreq, passing required parameters:
            //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL,
                    // The third parameter Listener overrides the method onResponse() and passes
                    //JSONObject as a parameter
                    new Response.Listener<JSONObject>() {

                        // Takes the response from the JSON request
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject obj = response.getJSONObject("ticker");
                                // Retrieves the string labeled "colorName" and "description" from
                                //the response JSON Object
                                //and converts them into javascript objects
                                String curr = obj.getString("base");
                                String price = obj.getString("price");

                                // Adds strings from object to the "data" string
                                data += "Currency: " + curr +
                                        "\nPrice: " + price +" Rubbles";

                                // Adds the data string to the TextView "results"
                                results.setText(data);
                            }
                            // Try and catch are included to handle any errors due to JSON
                            catch (JSONException e) {
                                // If an error occurs, this prints the error to the log
                                e.printStackTrace();
                            }
                        }
                    },
                    // The final parameter overrides the method onErrorResponse() and passes VolleyError
                    //as a parameter
                    new Response.ErrorListener() {
                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error");
                        }
                    }
            );
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);
        }
    }

}
