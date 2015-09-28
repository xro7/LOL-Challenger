package com.chronoplay.lolchallenger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import javax.xml.transform.Templates;


/**
 * A placeholder fragment containing a simple view.
 */
public class ActiveGamesFragment extends Fragment {


    TextView text3;
    TextView text2;

    private Socket mSocket;



    public ActiveGamesFragment() {
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.off("game created",gameCreated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_games, container, false);
        text2= (TextView)view.findViewById(R.id.text2);
        text3= (TextView)view.findViewById(R.id.text3);
        //mSocket = SocketHandler.getSocket();
        String name = "xronis";
        UserSocket userSocket = (UserSocket)getActivity().getApplicationContext();
        mSocket =  userSocket.getuSocket();
        mSocket.emit("new game",name);
        mSocket.on("game created",gameCreated);

        return view;
    }

    private Emitter.Listener gameCreated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int gameId;
                    String socketId;
                    try {
                        gameId =  data.getInt("gameId");
                        socketId =  data.getString("mySocketId");
                    } catch (JSONException e) {
                        return;
                    }
                    Log.e("game created","server send a game greated event");
                    // add the message to view
                    addMessage(gameId,socketId);
                  //  addMessage(gameId);
                }
            });
        }
    };

    private void addMessage(int gameId,String socketId){
        text2.setText("new game with id  " + gameId+"in socket with id "+socketId);
    }
    private void addMessage(int gameId){
        text2.setText("new game with id  " + gameId);
    }
}
