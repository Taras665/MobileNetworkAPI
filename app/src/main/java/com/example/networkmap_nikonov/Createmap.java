package com.example.networkmap_nikonov;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Createmap extends AppCompatActivity {

    EditText tb_name;

    Toast msg_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmap);

        tb_name = findViewById(R.id.tb_namemap);
    }

    public void Create(View v) throws JSONException
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
            public void onSuccess(String data)
            {
                if(data.equals("null"))
                {
                    msg_error.setText("Invalid credentials");
                    msg_error.show();
                }
                else
                {
                    Intent intent = new Intent(Createmap.this, MainActivity2.class);
                    setResult(2,intent);
                    finish();
                }
            }
        };
        JSONObject object = new JSONObject();
        object.put("mname", tb_name.getText().toString());
        object.put("tok", Param.token);
        request.post("http://v2.fxnode.ru:8081/rpc/add_map", object.toString());
    }

    public void onCancel(View v)
    {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
    }

}