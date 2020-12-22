package dao;

import entity.Reference;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RefDao {
    private Connection connection;

    public RefDao(FileInputStream filepath) throws IOException {
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

    public List<Reference> getReferences(String filter) throws  SQLException{
        if (filter == null)
            filter = "";
        PreparedStatement preparedStatement = connection.prepareStatement("select * from `references` where description like ?");
        preparedStatement.setString(1,"%"+filter+"%");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Reference> references = new ArrayList<Reference>();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String url = resultSet.getString(2);
            String description = resultSet.getString(3);
            int minus = resultSet.getInt(4);
            int plus = resultSet.getInt(5);
            references.add(new Reference(id,url,description,minus,plus));
        }
        return  references;
    }

    public void addReference(Reference reference) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("{call AddReference(?,?,?,?)}");
//        callableStatement.setInt(1,reference.getId());
        callableStatement.setString(1, reference.getUrl());
        callableStatement.setString(2,reference.getDescription());
        callableStatement.setInt(3,reference.getMinus());
        callableStatement.setInt(4,reference.getPlus());
        callableStatement.execute();
    }

    public void  updateReference(Reference reference) throws SQLException{
        String sqlRequest = "update `references` set ";
        if (reference.getUrl() != null && reference.getDescription() != null)
            sqlRequest += "url = '"+reference.getUrl()+"', description = '"+reference.getDescription()+"'";
        if (reference.getPlus() != -1)
            sqlRequest += " plus = "+(reference.getPlus()+1);
        if (reference.getMinus() != -1)
            sqlRequest += " minus = "+(reference.getMinus()+1);
        sqlRequest += " where id ="+reference.getId();
        connection.createStatement().executeUpdate(sqlRequest);
    }

    public void  deleteReference(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("delete from `comment` where refId = "+id);
        statement.executeUpdate("delete from `references` where id = "+id);
    }
}