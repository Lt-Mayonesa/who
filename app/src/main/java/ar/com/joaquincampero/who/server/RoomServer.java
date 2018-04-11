package ar.com.joaquincampero.who.server;

import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by joaco on 4/8/18.
 */

public class RoomServer extends NanoHTTPD {
    private final String TAG = this.getClass().getCanonicalName();

    public RoomServer(int port) {
        super(port);
    }

    @Override
    public void start() throws IOException {
        super.start();
        Log.d(TAG, "Server started");
    }

    @Override
    public void stop() {
        super.stop();
        Log.d(TAG, "Server stopped");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello Server!</h1>";
        msg += "<p>We serve " + session.getUri() + " at " + session.getRemoteIpAddress() + "</p>";
        msg += "</body></html>";
        return newFixedLengthResponse(msg);
    }
}
