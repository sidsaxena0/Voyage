package com.sid.voyage.utils;

import java.io.IOException;

/**
 * Created by sidsa on 11/21/2016.
 */

public interface ResponseCallback {

    public void onResponse(String res);
    public void onFailed(IOException e);
    public void onNoNetwork();



}
