package com.chronoplay.lolchallenger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * A placeholder fragment containing a simple view.
 */
public class JoinGameFragment extends Fragment {

    private Socket mSocket;
    TextView game;

    public JoinGameFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        mSocket.off("available games",availableGames);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_game, container, false);
        game = (TextView) view.findViewById(R.id.game);
        UserSocket userSocket = (UserSocket)getActivity().getApplicationContext();
        mSocket = userSocket.getuSocket();
        mSocket.emit("join game");

        mSocket.on("available games",availableGames);
        return view;

    }

    private Emitter.Listener availableGames = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray array = (JSONArray) args[0];
                    if (array.length() != 0) {
                        JSONObject data = null;
                        try {
                            data = array.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int gameId;
                        String socketId;
                        try {
                            gameId = data.getInt("gameId");
                            socketId = data.getString("mySocketId");
                        } catch (JSONException e) {
                            return;
                        }
                        Log.e("game created", "server send a game greated event");
                        // add the message to view
                        game.setText("with id  " + gameId + "in socket with id " + socketId);
                        //  addMessage(gameId);
                    }
                }
            });
        }
    };
}
