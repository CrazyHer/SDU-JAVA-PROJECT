package server.dataObjs;

import java.io.Serializable;

//用户登录或注册都用这个类传输用户基本信息
public class UserData implements Serializable {
    private String username = null, ID = null, password = null;

    public UserData(String username, String ID, String password) {
        this.username = username;
        this.ID = ID;
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
