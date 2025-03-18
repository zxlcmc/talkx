package org.bigmouth.gpt.event;

import org.bigmouth.gpt.entity.User;

import java.util.EventObject;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
public class NewUserRegisterEvent extends EventObject {

    private final User inviter;
    private final User newUser;
    private String ip;

    public NewUserRegisterEvent(User inviter, User newUser, Object source) {
        super(source);
        this.inviter = inviter;
        this.newUser = newUser;
    }

    public User getInviter() {
        return inviter;
    }

    public User getNewUser() {
        return newUser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
