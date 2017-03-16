package br.ufes.SDMan.readIaas;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Properties;



public class DescobreOpenstack {

	private static NodeDao nos;
	private static ServerDao servidores;
	private static ImageDao imagens;

	public static NodeDao getNos() {
		return nos;
	}

	public static void setNos(NodeDao nos) {
		DescobreOpenstack.nos = nos;
	}

	public static ServerDao getServidores() {
		return servidores;
	}

	public static void setServidores(ServerDao servidores) {
		DescobreOpenstack.servidores = servidores;
	}

	public static ImageDao getImagens() {
		return imagens;
	}

	public static void setImagens(ImageDao imagens) {
		DescobreOpenstack.imagens = imagens;
	}

	//construtor
	public DescobreOpenstack() throws Exception{
		//Find Openstack images
		procuraImagens();
		
		System.out.println("Finding the Server ");
		procuraServidores();
	
        System.out.println("Finding the Nodes ");
        procuraNos();        
        
	}
	  
	  public static void procuraNos() throws Exception{
		// TODO Auto-generated method stub
		System.out.println ("\t");

		NodeDao node = new NodeDaoImpl();
		
		setNos(node);
		
		getServidores().getServer().setNos(node);
    	/*for(Node nd: getNos().getAllNodes()){
			//add node to array if belongs to server 
			if(nd.getHost().equals(getServidores().getServer().getHostname())){
			   	aux.add(nd);
			}
			else{
				System.out.println ("Instancia não pertence a nenhum servidor" + guest.getHost() + server.getHostname());
			}
    	}*/
	    
		/*while (resultSet.next()) {
			if(getImagens()!=null){
				for(Image img: getImagens() ){
					if(img.getFromInstance().equals(resultSet.getString(3))){
						//found image from snapshot
						System.out.println("Image from snapshot");
					}
				}
			}*/	
	}
	
	private static void procuraServidores() throws SocketException, UnknownHostException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out.println ("\t");
		
		ServerDao servers = new ServerDaoImpl();
		
		setServidores(servers);
	}

	public static void procuraImagens() throws ClassNotFoundException, SQLException{
		//Consulta SQL para a Coleta das Informações das Imagens pelo Método procuraImagens
		
		ImageDao images = new ImageDaoImpl();
	    
	    setImagens(images);
	}
	
	public static void procuraNovasIntancias() throws Exception{

		//Consulta SQL para a Coleta do UUID das Instancias ativas pelo Método procuraNovasInstancias
	    String selectSql = "select uuid from instances "+
				"where vm_state='active';";
		
	    ResultSet resultSet = lerMysql(selectSql,"root","kaio22","nova");
	    while(resultSet.next()){
	    	boolean hasInstance=false;
	    	for(Node newNode: getNos().getAllNodes()){
	    		if(newNode.getUuid().equals(resultSet.getString(1)))
	    			hasInstance=true;
	    			break;
	    	}
	    	if(!hasInstance){
	    		procuraNos();
	    	}
	    }
	    
	}
	
	public static boolean buscaIntancia(String uuid) throws Exception{
		boolean queryOk=false;
		//Consulta SQL para a Coleta do UUID das Instancias pelo Método buscaInstancia
	    String selectSql = "select uuid from instances "+
				"where uuid='"+uuid+"' AND vm_state='active';";
		
	    ResultSet resultSet = lerMysql(selectSql,"root","kaio22","nova");
	    if(resultSet.next()){
	    	queryOk=true;
	    }
	    
	    return queryOk;
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
