package ochie.dataport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

public class DataPort {
  protected Socket socket;
  private BufferedReader binput;
  private PrintWriter out;

  void init() throws IOException {

    binput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream());
  }

  void send(String data) throws IOException {
    out.println(data);
    out.flush();
  }

  String receive() throws IOException {
    String result = binput.readLine();
    return result;
  }

  void write(JSONObject data) throws IOException {
    String res = data.toString();
    send(res);
  }

  JSONObject read() throws IOException {

    String output = receive();
    JSONObject obj = null;
    try {
      obj = new JSONObject(output);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return obj;
  }
}
