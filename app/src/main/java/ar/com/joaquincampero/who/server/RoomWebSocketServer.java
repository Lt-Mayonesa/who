package ar.com.joaquincampero.who.server;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fi.iki.elonen.NanoWSD;

/**
 * Created by joaco on 4/8/18.
 */

public class RoomWebSocketServer extends NanoWSD {
    private final String TAG = this.getClass().getCanonicalName();

    private ArrayList<RoomWebSocket> sockets = new ArrayList<>();

    public RoomWebSocketServer(int port) {
        super(port);
    }

    @Override
    public void start() throws IOException {
        super.start();
        new CountDownTimer(10 * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                new PingSocketsTask().execute();
            }

            public void onFinish() {
                stop();
            }
        }.start();
        Log.d(TAG, "Server started");
    }

    @Override
    public void stop() {
        super.stop();
        Log.d(TAG, "Server stopped");
    }

    @Override
    protected WebSocket openWebSocket(IHTTPSession handshake) {
        RoomWebSocket s = new RoomWebSocket(this, handshake);
        addSocket(s);
        return s;
    }

    public ArrayList<RoomWebSocket> getSockets() {
        return sockets;
    }

    public void setSockets(ArrayList<RoomWebSocket> sockets) {
        this.sockets = sockets;
    }

    private void addSocket(RoomWebSocket socket) {
        this.sockets.add(socket);
    }

    public void broadcastMessage(RoomWebSocket sender, NanoWSD.WebSocketFrame message) throws IOException {
        for (RoomWebSocket socket : this.sockets) {
            if (!socket.equals(sender) && socket.isOpen())
                socket.sendFrame(message);
        }
    }

    private void keepAlive() throws IOException {
        for (RoomWebSocket socket : this.sockets) {
            if (socket.isOpen())
                socket.ping("keepAlive".getBytes());
        }
    }

    private class PingSocketsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                keepAlive();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
