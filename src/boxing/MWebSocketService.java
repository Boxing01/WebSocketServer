package boxing;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import boxing.data.Constants.MessageType;

import com.alibaba.fastjson.JSON;

public class MWebSocketService extends WebSocketServer {
	private Map<Integer, WebSocket> hashSet = new HashMap<Integer, WebSocket>();

	public MWebSocketService(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public MWebSocketService(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onStart() {
		System.out.println("服务器启动成功");
	}

	@Override
	public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
		System.out.println("客户端连接成功" + webSocket.getResourceDescriptor() + "==="
				+ clientHandshake.getResourceDescriptor());
		String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
		String message = String.format("(%s) <进入房间！>", address);
		sendToAll(message);
		System.out.println(message);
	}

	@Override
	public void onClose(WebSocket webSocket, int i, String s, boolean b) {
		String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
		String message = String.format("(%s) <退出房间！>", address);
		sendToAll(message);

		System.out.println(message);

	}

	// 服务端接收到消息
	@Override
	public void onMessage(WebSocket webSocket, String s) {
		IMMessage imMessage = JSON.parseObject(s, IMMessage.class);
		String type = imMessage.getType();
		if (MessageType.LOGIN.equals(type)) {
			// 登录消息
			hashSet.put(imMessage.getSenderId(), webSocket);
		} else if (MessageType.PRIVATE.equals(type)) {
			// 私聊消息
			int receiverId = imMessage.getReceiverId();
			WebSocket receiveSocket = hashSet.get(receiverId);
			receiveSocket.send(s);
		} else if (MessageType.GROUP.equals(type)) {
			// 群聊消息
			InetSocketAddress localSocketAddress = webSocket.getLocalSocketAddress();
			InetSocketAddress remoteSocketAddress = webSocket.getRemoteSocketAddress();

			String message = String.format("%s \n(远程host%s) \n本地host%s", s, remoteSocketAddress.toString(),
					localSocketAddress.toString());
			// 将消息发送给所有客户端
			sendToAll(message);
			System.out.println(message);
		}
	}

	private static void print(String msg) {
		System.out.println(String.format("[%d] %s", System.currentTimeMillis(), msg));
	}

	@Override
	public void onError(WebSocket webSocket, Exception e) {
		if (null != webSocket) {
			webSocket.close(0);
		}
		e.printStackTrace();
	}

	public void sendToAll(String message) {
		// 获取所有连接的客户端
		Collection<WebSocket> connections = getConnections();
		// 将消息发送给每一个客户端
		for (WebSocket client : connections) {
			client.send(message);
		}
	}

}