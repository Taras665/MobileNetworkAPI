package com.example.networkmap_nikonov;

import android.app.Activity;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    Activity activity;

    public void onSuccess(String data)
    {

    }

    public void onError()
    {

    }

    public HttpRequest(Activity activity)
    {
        this.activity = activity;
    }

    public void post (String endpoint, String payload)
    {
        Thread thread = new Thread(() -> {
            try
            {
                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setDoOutput(true);

                OutputStream out_stream = connection.getOutputStream();

                byte[] out = payload.getBytes("utf-8");
                out_stream.write(out);

                InputStream in_stream = connection.getInputStream();
                byte[] in = new byte[1024];

                String data = "";
                while (true)
                {
                    int len = in_stream.read(in);
                    if(len < 0) break;

                    data += new String(in,0,len);
                }
                connection.disconnect();


                data = data.replace("\"", "");
                final String result = data;
                activity.runOnUiThread(() -> {onSuccess(result); });
            }
            catch (Exception ex)
            {
                Log.i("Error", ex.toString());
                activity.runOnUiThread(() -> {onError(); });
            }
        });
        thread.start();
    }
}
