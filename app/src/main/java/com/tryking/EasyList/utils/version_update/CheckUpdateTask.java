package com.tryking.EasyList.utils.version_update;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.AppUtils;
import com.tryking.EasyList.widgets.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 26011 on 2016/8/8.
 */
public class CheckUpdateTask extends AsyncTask<Void, Void, String> {
    private ProgressDialog dialog;
    private Context mContext;
    private boolean mShowProgressDialog;
    private static final String url = Constants.UPDATE_URL;

    public CheckUpdateTask(Context context, boolean showProgressDialog) {

        this.mContext = context;
        this.mShowProgressDialog = showProgressDialog;

    }


    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("正在检查新版本");
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

            int versionCode = AppUtils.getVersionCode(mContext);

            if (apkCode > versionCode) {
                showDialog(mContext, updateMessage, apkUrl);
            } else if (mShowProgressDialog) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Logger.e("parse json error");
        }
    }


    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }


    @Override
    protected String doInBackground(Void... args) {
        return HttpUtils.get(url);
    }
}
