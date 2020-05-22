package dataObjs;

import java.io.Serializable;

//用户登录或注册都用这个类传输用户基本信息
public class UserData implements Serializable {
    private String username = null, ID = null, password = null;

    public UserData(String username, String ID, String password) {//注册用
        this.username = username;
        this.ID = ID;
        this.password = password;
    }

    public UserData(String ID,String password){ //登录用
        this(null,ID,password);
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
