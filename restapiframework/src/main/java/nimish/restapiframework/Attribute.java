package nimish.restapiframework;


import java.io.File;
import java.util.ArrayList;

public class Attribute {


    public static final int POST = 1;
    public static final int GET = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;

    private int dafaultMethod = 1;
    private String url;

    private ArrayList<KeyValueRequest> arrayListHeaderParam = new ArrayList<>();
    private ArrayList<KeyValueRequest> arrayListParam = new ArrayList<>();
    private ArrayList<KeyFileRequest> arrayListFile = new ArrayList<>();
    private String jsonData;
    private String progressMessage;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getDafaultMethod() {
        return dafaultMethod;
    }

    public void setRequestMethod(int dafaultMethod) {
        this.dafaultMethod = dafaultMethod;
    }


    public ArrayList<KeyValueRequest> getArrayListHeaderParam() {
        return arrayListHeaderParam;
    }

    public ArrayList<KeyValueRequest> getArrayListParam() {
        return arrayListParam;
    }

    public ArrayList<KeyFileRequest> getArrayListFile() {
        return arrayListFile;
    }

    public void addHeader(String key, String value) {
        arrayListHeaderParam.add(new KeyValueRequest(key, value));
    }

    public void add(String key, String value) {
        arrayListParam.add(new KeyValueRequest(key, value));
    }

    public void add(String key, File file) {
        arrayListFile.add(new KeyFileRequest(key, file));
    }


    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    public static class KeyFileRequest {

        private String paramKey;
        private File file;

        public KeyFileRequest(String paramKey, File file) {
            this.paramKey = paramKey;
            this.file = file;
        }

        public String getParamKey() {
            return paramKey;
        }

        public File getFile() {
            return file;
        }
    }

    public static class KeyValueRequest {

        private String paramKey;
        private String paramValue;

        public KeyValueRequest(String paramKey, String paramValue) {
            this.paramKey = paramKey;
            this.paramValue = paramValue;
        }

        public String getParamKey() {
            return paramKey;
        }

        public String getParamValue() {
            return paramValue;
        }

    }
}

