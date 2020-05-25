package server.dataBase;

import server.ServerMain;

import java.io.IOException;
import java.sql.*;

public class DB {
    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String dbURL = "jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    static final String username = "root";
    static final String password = "lzxhr";
    /*
    //  Ubuntu测试环境
        static final String driver = "com.mysql.cj.jdbc.Driver";
        static final String dbURL = "jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
        static final String username = "javaProject";
        static final String password = "lzxhr";
     */
    Connection connection;
    Statement statement;
    ResultSet resultSet;


    public DB() throws IOException { //连接数据库，直接提供操作方法

        try {//加载驱动
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("载入数据库驱动失败");
            e.printStackTrace();
            ServerMain.closeServer();
        }
        try {//连接数据库
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
            ServerMain.closeServer();
        }
    }

    public ResultSet query(String sql) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public void update(String sql) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }
}
