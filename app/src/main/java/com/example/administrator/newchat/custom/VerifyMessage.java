
package com.example.administrator.newchat.custom;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;


@AVIMMessageType(
        type = 10
)
public class VerifyMessage extends AVIMTypedMessage {

    @AVIMMessageField(name = "type")
    String type;
    public static final String REQUEST = "request";
    public static final String AGREE = "agree";

    public static final Creator<VerifyMessage> CREATOR = new AVIMMessageCreator(VerifyMessage.class);

    public VerifyMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
