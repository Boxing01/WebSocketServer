package boxing;

import com.alibaba.fastjson.JSON;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import boxing.data.Constants.MessageType;

public class MWebSocketService extends WebSocketServer {
	private Map<Integer, WebSocket> hashMap = new HashMap<>();

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
				+ clientHandshake.getResourceDescriptor() + webSocket.getClass());
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

		webSocket.close();
		int id = webSocket.getAttachment();
		hashMap.remove(id);

		System.out.println(message);
	}

	// 服务端接收到消息
	@Override
	public void onMessage(WebSocket webSocket, String s) {
		IMMessage imMessage = JSON.parseObject(s, IMMessage.class);
		String type = imMessage.getType();
		if (MessageType.LOGIN.equals(type)) {
			// 登录消息
			int senderId = imMessage.getSenderId();
			hashMap.put(senderId, webSocket);
			// 回复客户端登录消息
			webSocket.send(s);
			// 将id和WebSocket绑定
			webSocket.setAttachment(senderId);
		} else if (MessageType.PRIVATE.equals(type)) {
			// 发送私聊消息到指定客户端
			int receiverId = imMessage.getReceiverId();
			WebSocket receiveSocket = hashMap.get(receiverId);
			if (receiveSocket != null) {
				receiveSocket.send(s);
			} else {
				// 未在线
			}
		} else if (MessageType.GROUP.equals(type)) {
			// TODO 群聊消息
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
			int id = webSocket.getAttachment();
			hashMap.remove(id);
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