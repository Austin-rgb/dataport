package ochie.dataport;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

public class Client extends DataPort {
  public Client(int port) throws IOException {
    try {
      socket = new Socket("127.0.0.1", port);
      init();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public JSONObject request(String context, JSONObject data) throws IOException {
    send(context);
    String status = receive();
    write(data);
    return read();
  }

  public void close() throws IOException {
    socket.close();
  }
}
