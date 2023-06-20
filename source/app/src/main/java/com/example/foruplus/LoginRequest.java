package com.example.foruplus;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{

    //서버 URL 설정 (PHP파일연동)
    final static private String URL = "http://gustn5848.ivyro.net/Login.php";
    private Map<String,String> map;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listner){
        super(Method.POST, URL, listner,null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);

    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
