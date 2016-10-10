package com.example.hp.volleytest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 2016/6/24.
 */
public class HttpUtils {
    /**
     * 回调接口
     */
    public interface IHttpResult {

        void onSuccess(String result);

        void onError(Throwable ex);

//        void onCancelled(String canceStr);

//        void onFinished();

    }
    /**
     * 网络请求
     *
     * @param url
     * @param params
     * @param result
     */
    public static void doHttp(Context context,String url, HashMap<String, String> params, IHttpResult result) {
        if (params == null || params.size() == 0) {
            doGet(context,url, result);
        } else if (params.size() > 0) {
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.doPost(context,url, params, result);
        }
    }

    private static void doGet(Context context,String url, final IHttpResult iHttpResult){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                iHttpResult.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                iHttpResult.onError(volleyError);
            }
        });
        queue.add(stringRequest);
    }

    private void doPost(Context context, String url, final HashMap<String,String> parms, final IHttpResult iHttpResult){
        RequestQueue queue = Volley.newRequestQueue(context);
        Request<JSONObject> request = new NormalPostRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String s = jsonObject.toString();
                iHttpResult.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                iHttpResult.onError(volleyError);
            }
        },parms);
        queue.add(request);
    }
    class NormalPostRequest extends Request<JSONObject> {
        private Map<String, String> mMap;
        private Response.Listener<JSONObject> mListener;
        public NormalPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> map) {
            super(Request.Method.POST, url, errorListener);

            mListener = listener;
            mMap = map;
        }

        //mMap是已经按照前面的方式,设置了参数的实例
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return mMap;
        }

        //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

            try {
                String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));

                return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }



        @Override
        protected void deliverResponse(JSONObject response) {
            mListener.onResponse(response);
        }
    }
}
