package mask.bits.whereabouts;

import java.text.SimpleDateFormat;

/**
 * Created by Technovibe on 17-04-2015.
 */
public class ChatMessage {
    private long id;
    private boolean isMe;
    private String message;
    private Long userId;
    private String dateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this.setDate();
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.dateTime = sdf.format(id);
    }
}
