package com.example.usrgam.codigobarras;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://192.168.1.2:8080/UpdateAsistencia.php";
    private Map<String, String> params;

    public UpdateRequest(String codigo, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("codigo", codigo);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
