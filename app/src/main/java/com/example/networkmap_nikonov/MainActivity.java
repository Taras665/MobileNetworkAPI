package com.example.networkmap_nikonov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText tb_login;
    EditText tb_password;

    Toast msg_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_login = findViewById(R.id.tb_Login);
        tb_password = findViewById(R.id.tb_Password);

        msg_error = Toast.makeText(this,"",Toast.LENGTH_SHORT);
    }

    public void onLogin(View v) throws JSONException
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
                Log.i("Data", data);
                if(data.equals("null"))
                {
                    msg_error.setText("Invalid credentials");
                    msg_error.show();
                }
                else
                {
                    Param.token = data.replace("\"", "");
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        JSONObject object = new JSONObject();
        object.put("uname", tb_login.getText().toString());
        object.put("usecret", tb_password.getText().toString());
        request.post("http://v2.fxnode.ru:8081/rpc/open_session", object.toString());
    }

    public void ShowRegistration(View v)
    {
        Intent intent = new Intent(this, ActivityRegistration.class);
        startActivity(intent);
        finish();
    }

}
