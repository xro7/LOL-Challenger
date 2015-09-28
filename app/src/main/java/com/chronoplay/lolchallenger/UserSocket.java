package com.chronoplay.lolchallenger;

import android.app.Application;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by on 28/9/2015.
 */
public class UserSocket extends Application {

    private Socket uSocket;

    public void initSocket(String serverUrl){
        try {
            uSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {}

    }


    public Socket getuSocket() {
        return uSocket;
    }

}
