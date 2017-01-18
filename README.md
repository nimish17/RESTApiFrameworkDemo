# RESTApiFrameworkDemo

/*==========================================================================================================================*/
        // if(check internet connection){
        // GET request
        Attribute attributeGet = new Attribute();
        attributeGet.setUrl("Your webservice url"); //you can append param to url otherwise you can also set param in add method
        attributeGet.addHeader("Key", "Value"); // you can set header param (like authorization, cookie etc..) by using addHeader methods
        attributeGet.addHeader("Key", "Value");
        attributeGet.add("Key", "value");
        attributeGet.add("Key", "value");
        attributeGet.setRequestMethod(Attribute.GET); // if you don't want to set this param please set  dafaultMethod = 2 in Attribute class
        attributeGet.setProgressMessage("You custom progress message"); // If you not set you will see default message "Please wait..."
        new ApiClient(MainActivity.this/* context reference*/, attributeGet, true /* for progressbar visibility*/, ApiModel.class /* Your model class which is mapping with api response*/) {
            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result); // if your progressbar visibility false you can remove this line
                if (null != result) {
                    ApiModel apiModel = (ApiModel) result; //Your response is ready
                }
            }
        }.execute();
        //}


        /*==========================================================================================================================*/
        // if(check internet connection){
        // POST request
        Attribute attributePost = new Attribute();
        attributePost.setUrl("Your webservice url"); //you can append param to url otherwise you can also set param in add method
        attributePost.addHeader("Key", "Value"); // you can set header param (like authorization, cookie etc..) by using addHeader methods
        attributePost.addHeader("Key", "Value");
        attributePost.add("Key", "value");
        attributePost.setJsonData("YOUR JSON DATA"); // use setJsonData when you need to send JSON request instead of add method, you can not set both same time
        attributePost.add("Key", new File("")); // use this add method if you need to sent file, if you add minimum 1 file MultiPart request will be called automatically
        attributePost.setRequestMethod(Attribute.GET); // if you don't want to set this param please set  dafaultMethod = 1 in Attribute class
        attributePost.setProgressMessage("You custom progress message"); // If you not set you will see default message "Please wait..."
        ApiClient apiClient = new ApiClient(MainActivity.this/* context reference*/, attributePost, true /* for progressbar visibility*/, ApiModel.class /* Your model class which is mapping with api response*/) {
            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result); // if your progressbar visibility false you can remove this line
                if (null != result) {
                    ApiModel apiModel = (ApiModel) result; //Your response is ready
                }
            }
        };
        apiClient.fileTransferInfo(true); //set fileTransferInfo true when you want see Percentage of uploading file
        apiClient.execute();
        // apiClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //you can also use THREAD_POOL_EXECUTOR
        //}


        /*==========================================================================================================================*/
        //if you have many params and you want to set all param in background thread.

        // if(check internet connection){
        // POST request
        final Attribute attrPost = new Attribute();
        attributeGet.setProgressMessage("You custom progress message"); // If you not set you will see default message "Please wait..."
        new ApiClient(MainActivity.this/* context reference*/, attrPost, true /* for progressbar visibility*/, ApiModel.class /* Your model class which is mapping with api response*/) {
            @Override
            protected Object doInBackground(Void... params) {
                attrPost.setUrl("Your webservice url"); //you can append param to url otherwise you can also set param in add method
                attrPost.addHeader("Key", "Value"); // you can set header param (like authorization, cookie etc..) by using addHeader methods
                attrPost.addHeader("Key", "Value");
                attrPost.add("Key", "value");
                attrPost.setJsonData("YOUR JSON DATA"); // use setJsonData when you need to send JSON request instead of add method, you can not set both same time
                attrPost.add("Key", new File("")); // use this add method if you need to sent file, if you add minimum 1 file MultiPart request will be called automatically
                attrPost.setRequestMethod(Attribute.GET); // if you don't want to set this param please set  dafaultMethod = 1 in Attribute class
                return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result); // if your progressbar visibility false you can remove this line
                if (null != result) {
                    ApiModel apiModel = (ApiModel) result; //Your response is ready
                }
            }
        }.execute();
        // apiClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //you can also use THREAD_POOL_EXECUTOR
        //}
