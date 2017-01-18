/**
 *
 */
package nimish.restapiframework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;


public class ApiClient extends AsyncTask<Void, Void, Object> {


    private Context context;
    private Attribute attribute;
    private boolean isShownProgress;
    private Class<?> type;

    private WebConnector webConnector;
    private Gson gson = new Gson();
    private ProgressDialog progressDialog;

    public ApiClient(Context context, Attribute attribute, boolean isShownProgress, Class<?> type) {
        this.context = context;
        this.attribute = attribute;
        this.isShownProgress = isShownProgress;
        this.type = type;
        webConnector = new WebConnector();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShownProgress)
            showProgress();
    }

    @Override
    protected Object doInBackground(Void... params) {

        String response = null;
        try {
            if (attribute.getDafaultMethod() == Attribute.GET) {
                response = webConnector.sendGetReq(attribute);
            } else if (attribute.getDafaultMethod() == Attribute.POST) {
                if (attribute.getArrayListFile().size() == 0) {
                    if (attribute.getJsonData() == null)
                        response = webConnector.sendPostReq(attribute);
                    else
                        response = webConnector.sendJsonPostReq(attribute);
                } else {
                    response = webConnector.multipartRequest(attribute, "POST", isFileTransferInfo ? new DataTransferListener() {
                        @Override
                        public void transferred(final String text) {
                            if (null != progressDialog && isShownProgress) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage(String.valueOf(text));
                                    }
                                });
                            }
                        }
                    } : null);
                }
            } else if (attribute.getDafaultMethod() == Attribute.PUT) {
                if (attribute.getArrayListFile().size() == 0) {
                    if (attribute.getJsonData() == null)
                        response = webConnector.sendPutReq(attribute);
                    else
                        response = webConnector.sendJsonPutReq(attribute);
                } else {
                    response = webConnector.multipartRequest(attribute, "PUT", isFileTransferInfo ? new DataTransferListener() {
                        @Override
                        public void transferred(final String text) {
                            if (null != progressDialog && isShownProgress) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage(String.valueOf(text));
                                    }
                                });
                            }
                        }
                    } : null);
                }
            } else if (attribute.getDafaultMethod() == Attribute.DELETE) {
                response = webConnector.sendDeleteReq(attribute);
            }

            if (null != response)
                return gson.fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            if (isShownProgress) {
                hideProgress();
                if (result == null)
                    showAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert() {
        if (null != context)
            new AlertDialog.Builder(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.style.Theme_Material_Light_Dialog_Alert : -1)
                    .setTitle(context.getString(R.string.alert_network_error))
                    .setMessage(context.getString(R.string.alert_unable_connect_server))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(true).show();
    }

    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.style.Theme_Material_Light_Dialog_Alert : -1);
            if (null == attribute.getProgressMessage())
                progressDialog.setMessage(context.getString(R.string.please_wait));
            else
                progressDialog.setMessage(attribute.getProgressMessage());
            progressDialog.setCancelable(false);
        }
        this.progressDialog.show();
    }

    public void hideProgress() {
        try {
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFileTransferInfo;

    public void fileTransferInfo(boolean isFileTransferInfo) {
        this.isFileTransferInfo = isFileTransferInfo;
    }

    public interface DataTransferListener {
        public void transferred(String fileName);
    }
}
