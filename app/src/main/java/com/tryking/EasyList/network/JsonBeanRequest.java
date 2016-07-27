package com.tryking.EasyList.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList._bean.BaseBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 */
public class JsonBeanRequest<T> extends Request<T> {
    private Class<T> clazz;
    private Response.Listener<T> listener;
    private Map<String, String> params;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public JsonBeanRequest(String url, Map<String, String> params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, params, clazz, listener, errorListener);
    }

    public JsonBeanRequest(int method, String url, Map<String, String> params, Class<T> clazz, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.clazz = clazz;
        this.listener = listener;
        this.params = params;

        printParams(url, params);
    }

    private void printParams(String url, Map<String, String> params) {
        if (params != null) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            StringBuffer sb = new StringBuffer();
            sb.append(url + "?");
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();

                sb.append((key == null ? "key" : key.toString()) + "=" + (value == null ? "value" : value.toString()) + "&");
            }
            sb.deleteCharAt(sb.length() - 1);
            Logger.i("HttpRequest", sb.toString());
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> header = new HashMap<>();
        header.put("Cookies", "Cookies");
        return header;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookies");
            if (rawCookies != null) {
                Logger.e("JsonRequest", "rawCookies---->" + rawCookies);
                String[] cookieMsgArr = rawCookies.split(";");
                for (String cookie : cookieMsgArr
                        ) {
                    if (cookie.startsWith("JSESSIONID")) {
                        Cookies.JSESSIONID = cookie;
                    }
                }
            }
           /*
            * 拿到返回的数据
            */
            String jsonStr = new String(response.data, "UTF-8");
            Logger.e("返回的数据---->" + jsonStr);

            BaseBean base = gson.fromJson(jsonStr, BaseBean.class);
            Logger.e(base.getMsg());
            if (base.getResult().equals("500")) {
                //登陆超时
                return Response.error(new VolleyError());
            } else if (base.getResult().equals("noToken")) {
                //重新请求token
                return Response.error(new VolleyError());
            } else {
                /*
                 * 转化成对象
                 */
                return Response.success(gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

}
