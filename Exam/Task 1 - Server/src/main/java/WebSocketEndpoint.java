import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/socket")
public class WebSocketEndpoint {
    @OnMessage
    public void onMessage(Session session, String msg) {


    }
}
