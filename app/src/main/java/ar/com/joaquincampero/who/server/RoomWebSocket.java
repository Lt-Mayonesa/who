package ar.com.joaquincampero.who.server;

import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

public class RoomWebSocket extends NanoWSD.WebSocket {
    private final String TAG = this.getClass().getCanonicalName();
    private final RoomWebSocketServer server;

    public RoomWebSocket(RoomWebSocketServer server, NanoHTTPD.IHTTPSession handshakeRequest) {
        super(handshakeRequest);
        this.server = server;
    }

    @Override
    protected void onOpen() {

    }

    @Override
    protected void onClose(NanoWSD.WebSocketFrame.CloseCode code, String reason, boolean initiatedByRemote) {
        Log.d(TAG, code + " reason: " + reason + " by remote: " + initiatedByRemote);
    }

    @Override
    protected void onMessage(NanoWSD.WebSocketFrame message) {
        Log.d(TAG, message.toString());
        try {
            message.setUnmasked();
            this.server.broadcastMessage(this, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPong(NanoWSD.WebSocketFrame pong) {
        Log.d(TAG, "P " + pong);
    }

    @Override
    protected void onException(IOException exception) {

    }
}
