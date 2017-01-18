package nimish.restapiframework;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import nimish.restapiframework.ApiClient.DataTransferListener;
import nimish.restapiframework.Attribute.KeyFileRequest;
import nimish.restapiframework.Attribute.KeyValueRequest;

public class WebConnector {

    private String restApiTag = "==>";
    private int CONNECTION_TIMEOUT = 10000;

    public String sendGetReq(Attribute attribute) {
        try {
            String urlParams = getUrlParams(attribute);
            URL url;
            if (urlParams.length() > 0)
                url = new URL(new StringBuilder().append(attribute.getUrl()).append("?").append(urlParams).toString());
            else url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            setHeaderParam(connection, attribute);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            return fetchData(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendDeleteReq(Attribute attribute) {
        try {
            String urlParams = getUrlParams(attribute);
            URL url;
            if (urlParams.length() > 0)
                url = new URL(new StringBuilder().append(attribute.getUrl()).append("?").append(urlParams).toString());
            else url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            setHeaderParam(connection, attribute);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            return fetchData(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String sendPostReq(Attribute attribute) {
        try {
            URL url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            setHeaderParam(connection, attribute);
            String urlParams = getUrlParams(attribute);

            connection.setRequestProperty("Content-Length", Integer.toString(urlParams.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParams);
            wr.flush();
            wr.close();

            return fetchData(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String sendPutReq(Attribute attribute) {
        try {
            URL url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            setHeaderParam(connection, attribute);
            String urlParams = getUrlParams(attribute);

            connection.setRequestProperty("Content-Length", Integer.toString(urlParams.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParams);
            wr.flush();
            wr.close();

            return fetchData(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public String sendJsonPostReq(Attribute attribute) {

        try {
            URL url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            setHeaderParam(connection, attribute);

            connection.setFixedLengthStreamingMode(attribute.getJsonData().getBytes().length);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(attribute.getJsonData());
            out.close();
            connection.connect();

            return fetchData(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String sendJsonPutReq(Attribute attribute) {
        try {
            URL url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            setHeaderParam(connection, attribute);

            connection.setFixedLengthStreamingMode(attribute.getJsonData().getBytes().length);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(attribute.getJsonData());
            out.close();
            connection.connect();

            return fetchData(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public String multipartRequest(Attribute attribute, String type, DataTransferListener dataTransferListener) {


        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = null;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

        try {

            URL url = new URL(attribute.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(type);
            connection.setChunkedStreamingMode(0);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // Upload POST Data
            int max = attribute.getArrayListParam().size();
            for (int i = 0; i < max; i++) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + attribute.getArrayListParam().get(i).getParamKey() + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(attribute.getArrayListParam().get(i).getParamValue());
                outputStream.writeBytes(lineEnd);
            }

            for (KeyFileRequest keyFileRequest : attribute.getArrayListFile()) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + keyFileRequest.getParamKey() + "\"; filename=\"" + keyFileRequest.getFile().getName() + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                outputStream.writeBytes(lineEnd);

                try {
                    FileInputStream fileInputStream = new FileInputStream(keyFileRequest.getFile());
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    long transferred = 0;
                    if (null != dataTransferListener) {
                        String text = new StringBuilder().append("Uploading file ")
                                .append(attribute.getArrayListFile().indexOf(keyFileRequest) + 1).append(" of ")
                                .append(attribute.getArrayListFile().size()).append(" (0%)").toString();
                        dataTransferListener.transferred(text);
                    }
                    while (bytesRead > 0) {
                        outputStream.write(buffer);
                        outputStream.flush();
                        if (null != dataTransferListener) {
                            transferred += bytesRead;
                            String text = new StringBuilder().append("Uploading file ")
                                    .append(attribute.getArrayListFile().indexOf(keyFileRequest) + 1).append(" of ")
                                    .append(attribute.getArrayListFile().size())
                                    .append(" (").append(transferred * 100 / keyFileRequest.getFile().length()).append("%)").toString();
                            dataTransferListener.transferred(text);
                        }
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    outputStream.writeBytes(lineEnd);
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_ACCEPTED ||
                    responseCode == HttpURLConnection.HTTP_NOT_AUTHORITATIVE || responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_RESET ||
                    responseCode == HttpURLConnection.HTTP_PARTIAL)
                inputStream = connection.getInputStream();
            else
                inputStream = connection.getErrorStream();

            result = convertStreamToString(inputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            connection.disconnect();
            Log.i("responseMessage", restApiTag + String.valueOf(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private void setHeaderParam(HttpURLConnection conn, Attribute attribute) {
        for (KeyValueRequest keyValueRequest : attribute.getArrayListHeaderParam()) {
            conn.setRequestProperty(keyValueRequest.getParamKey(), keyValueRequest.getParamValue());
        }
    }

    private String getUrlParams(Attribute attribute) {
        StringBuilder urlParameters = new StringBuilder();
        for (int i = 0; i < attribute.getArrayListParam().size(); i++) {
            String value = attribute.getArrayListParam().get(i).getParamValue();
            if (null != value) {
                try {
                    urlParameters.append(attribute.getArrayListParam().get(i).getParamKey()).append("=").append(URLEncoder.encode(value, "UTF-8"));
                    if (i != attribute.getArrayListParam().size() - 1)
                        urlParameters.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("params==>", urlParameters.toString());
        return urlParameters.toString();
    }

    private String fetchData(HttpURLConnection connection) {
        try {
            int responseCode = connection.getResponseCode();
            InputStream is;
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_ACCEPTED ||
                    responseCode == HttpURLConnection.HTTP_NOT_AUTHORITATIVE || responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_RESET ||
                    responseCode == HttpURLConnection.HTTP_PARTIAL)
                is = new BufferedInputStream(connection.getInputStream());
            else
                is = new BufferedInputStream(connection.getErrorStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();

            if (null != reader)
                reader.close();

            if (null != connection)
                connection.disconnect();

            Log.i("responseMessage", restApiTag + sb.toString());
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}

