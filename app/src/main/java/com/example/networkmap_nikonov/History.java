package com.example.networkmap_nikonov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView list;
    Toast msg_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        msg_error = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        list = findViewById(R.id.list_history);
        ArrayList<String> lstmap = new ArrayList<String>();
        lstmap.clear();
        JSONObject object = new JSONObject();

        HttpRequest request = new HttpRequest(this)
        {
            @Override
            public void onError()
            {
                msg_error.setText("Request failed");
                msg_error.show();
            }

            @Override
            public void onSuccess(String data)
            {
                Log.i("Data", data);
                if(data.equals("null"))
                {
                    msg_error.setText("Invalid credentials");
                    msg_error.show();
                }
                else
                {
                    try
                    {
                        JSONArray array = new JSONArray(data);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, lstmap);
                        for(int i = 0; i < array.length(); i++)
                        {
                            JSONObject jo = array.getJSONObject(i);

                            lstmap.add(array.getJSONObject(i).toString());
                            list.setAdapter(adapter);
                        }
                    }
                    catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        };

        try {
            object.put("tok", Param.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.post("http://v2.fxnode.ru:8081/rpc/get_sessions", object.toString());
    }

    public void Cancel(View v)
    {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
    }




}