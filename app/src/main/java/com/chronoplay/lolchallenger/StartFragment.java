package com.chronoplay.lolchallenger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.logging.SocketHandler;

/**
 * A placeholder fragment containing a simple view.
 */


public class StartFragment extends Fragment {

    TextView text;
    private Socket mSocket;


    public StartFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        //mSocket.off("connected",connected);
        mSocket.off("update users",updateUsers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_start, container, false);
        text = (TextView)view.findViewById(R.id.textview);
        UserSocket userSocket = (UserSocket)getActivity().getApplicationContext();
        userSocket.initSocket("http://192.168.1.65:8080");
        mSocket =  userSocket.getuSocket();
        Button button =  (Button)view.findViewById(R.id.button);
        Button button2 =  (Button)view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JoinGame.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActiveGames.class);
                startActivity(intent);

            }
        });

       //mSocket.on("connected", connected);
        mSocket.on("update users",updateUsers);
        mSocket.connect();
        return view;
    }


    private Emitter.Listener connected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int users;
                    try {
                        users =  data.getInt("users");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(users);
                }
            });
        }
    };

    private Emitter.Listener updateUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("Update users even","asd");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int users;
                    try {
                        users =  data.getInt("users");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(users);
                }
            });
        }
    };

    private void addMessage(int users){
       text.setText("-users connected: " + users);
    }
}
