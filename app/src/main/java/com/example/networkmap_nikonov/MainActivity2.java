package com.example.networkmap_nikonov;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.transform.Result;

public class MainActivity2 extends AppCompatActivity {

    ListView list;
    String text;
    Toast msg_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Update();
    }

    public void Update()
    {
        msg_error = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        list = findViewById(R.id.list_map);
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

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Param.mapid = adapterView.getId();
                                    try {
                                        Param.itemid = jo.getString("mid");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
        request.post("http://v2.fxnode.ru:8081/rpc/get_maps", object.toString());
    }

    public void Delete(View v)
    {
        HttpRequest request = new HttpRequest(this)
        {
            @Override
            public void onError()
            {
                msg_error.setText("Request failed");
                msg_error.show();
            }

            @Override
            public void onSuccess(String data) {
                Log.e("Data", data);
                if (data.equals("null")) {
                    msg_error.setText("Invalid credentials");
                    msg_error.show();
                }
                else
                {
                    msg_error.setText("Deleted");
                    msg_error.show();
                    Param.token = data.replace("\"", "");
                    Param.itemid = data.replace("\"", "");
                    Log.e("Data", Param.itemid);
                    Update();
                }
            }
        };
        JSONObject object = new JSONObject();
        try {
            object.put("tok", Param.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("mid", Param.itemid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.post("http://v2.fxnode.ru:8081/rpc/delete_map", object.toString());
    }

    public void Edit(View v)
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tv = (TextView) view.findViewById(R.id.list_item);
            text = tv.getText().toString();
            Param.namemap = text;
            long id = adapterView.getId();
            Param.mapid = id;
        }
    });
    }

    public void CreateMap(View v)
    {
        Intent intent = new Intent(this, Createmap.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Update();
    }

    public void history(View v)
    {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
        finish();
    }
}