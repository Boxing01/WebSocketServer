package boxing.im;

/**
 * @author Liuyuli
 * @date 2018/5/15.
 */

public class IMMessage {
    // 登录 单发 群聊
    private String type;

    // 发送者id
    private int senderId;

    // 接受者id
    private int receiverId;

    // 消息内容
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
