package br.ufes.SDMan.readIaas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class ImageDaoImpl implements ImageDao {
	
	ArrayList<Image> images;

	public ImageDaoImpl() throws SQLException, ClassNotFoundException {
	    images= new ArrayList<Image>();
		String selectSql = "select image_id, value from glance.image_properties "+
				"where name='instance_uuid';";
	     
	    ResultSet resultSet = lerMysql(selectSql,"root","kaio22","glance");
	    Image snap= new Image();
	    while(resultSet.next()){
	    	snap.setId(resultSet.getString(1));
	    	snap.setFromInstance(resultSet.getString(2));
	    	images.add(snap);
	    }
	}

	public ArrayList<Image> getAllImages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getImage(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateImage(Image image) {
		// TODO Auto-generated method stub

	}

	public void deleteImage(Image image) {
		// TODO Auto-generated method stub

	}
	public static ResultSet lerMysql(String selectSql, String user, String passwd, String database) throws ClassNotFoundException, SQLException{
		System.out.println ("\t");
		//SessionFactory factory; 
		  Connection connect = null;
		  Statement statement = null;
		  ResultSet resultSet = null;
		
		//Connection Properties
			Properties connectionProps = new Properties();
		    connectionProps.put("user", user);
		    connectionProps.put("password", passwd);
		 // Load the MySQL driver
		    try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    // Setup the connection with the DB
		    try{
		    connect = DriverManager.getConnection(
	                  "jdbc:" + "mysql" + "://" +
	                  "localhost" +
	                  ":" + "3306" + "/" + database,
	                  connectionProps);
		    
		    // Statements allow to issue SQL queries to the database
		    statement = connect.createStatement();
		 // Result set get the result of the SQL query
		    resultSet = statement.executeQuery(selectSql);
		    
		    }catch(SQLException e){
		        // handle any errors
		        System.out.println("SQLException: " + e.getMessage());
		        System.out.println("SQLState: " + e.getSQLState());
		        System.out.println("VendorError: " + e.getErrorCode());
		    }
		    return resultSet;
	  }

	public void addImage(Image image) {
		// TODO Auto-generated method stub
		this.images.add(image);
	}
}
