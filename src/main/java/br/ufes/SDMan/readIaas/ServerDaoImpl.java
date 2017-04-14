package br.ufes.SDMan.readIaas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

public class ServerDaoImpl implements ServerDao {

	Server server;

	public ServerDaoImpl() throws ClassNotFoundException, SQLException {
		
		//Consulta SQL para a Coleta das Informações dos Nós de Computação
		String selectSql = "select host, host_ip from compute_nodes;";
		
		ResultSet resultSet = lerMysql(selectSql,"root","kaio22","nova"); 
		Server serverFound = new Server();
		
		if (resultSet.next()) {
			
			
		    serverFound.setHostname(resultSet.getString(1));
		    serverFound.setIp(resultSet.getString(2));
		    serverFound.setCamada(4);
		    serverFound.setNagiosMonitor(0);
		    serverFound.setUuid(UUID.randomUUID().toString());
		    serverFound.setActive(true);
		}
		
		this.server = serverFound;
		
	}

	
	public Server getServer() {
		// TODO Auto-generated method stub
		return server;
	}

	public void updateServer(Server server) {
		// TODO Auto-generated method stub
		this.server = server;
	}

	public void deleteServer() {
		// TODO Auto-generated method stub
		this.server=null;
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

}
