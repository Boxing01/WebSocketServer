package boxing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 程序入口
 */
public class WebSocketMainMethod {
	private static int PORT = 2018;

    public static void main(String[] args) throws IOException {
        MWebSocketService socketServer = new MWebSocketService(PORT);
        socketServer.start();

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = socketServer.getPort();
            System.out.println(String.format("服务器启动: %s:%d", ip, port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(in);

        while (true) {
            try {
                String msg = reader.readLine();
                socketServer.sendToAll(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
