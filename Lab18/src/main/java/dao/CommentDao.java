package dao;

import entity.Comment;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CommentDao {
    private Connection connection;

    public CommentDao(FileInputStream filepath) throws IOException {
        try {
            Properties properties = new Properties();
            properties.load(filepath);
            Class.forName(properties.getProperty("db.driverClassName"));

            connection = DriverManager.getConnection(properties.getProperty("db.url"),
                    properties.getProperty("db.username"), properties.getProperty("db.password"));
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Comment> getComments(int refId) throws  SQLException{
        ResultSet resultSet = connection.createStatement().executeQuery("select * from `comment` where refId ="+refId);
        List<Comment> comments = new ArrayList<Comment>();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            refId = resultSet.getInt(2);
            String sessionId = resultSet.getString(3);
            Date stamp = resultSet.getDate(4);
            String comment = resultSet.getString(5);
            comments.add(new Comment(id,refId,sessionId,stamp,comment));
        }
        return  comments;
    }

    public void addComment(Comment comment) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("{call AddComment(?,?,?,?)}");
//        callableStatement.setInt(1,comment.getId());
        callableStatement.setInt(1, comment.getRefId());
        callableStatement.setString(2,comment.getSessionId());
        callableStatement.setDate(3,comment.getStamp());
        callableStatement.setString(4,comment.getComment());
        callableStatement.execute();
    }

    public void updateComment(Comment comment) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("update `comment` set comment = ? where id = ?");
        preparedStatement.setString(1,comment.getComment());
        preparedStatement.setInt(2,comment.getId());
        preparedStatement.executeUpdate();
    }

    public void  deleteComment(int id) throws SQLException {
        connection.createStatement().executeUpdate("delete from `comment` where id = "+id);
    }
}
