package ochie.dataport;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import ochie.dataport.Handler;

public class Server extends DataPort {
  private HashMap<String, Handler> handlers = new HashMap<String, Handler>();

  public void serve(int port) throws IOException {
    try (ServerSocket server = new ServerSocket(port)) {
      while (true) {
        socket = server.accept();
        init();
        while (true) {
          String context = receive();
          Handler handler = handlers.get(context);
          if (handler != null) {
            send("ok");
            write(handler.handle(read()));
          } else {
            break;
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }

  public void addContext(String key, Handler handler) {
    handlers.put(key, handler);
  }

  public void close() throws IOException {
    socket.close();
  }
}
