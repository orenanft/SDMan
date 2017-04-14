package br.ufes.SDMan.readIaas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class NodeDaoImpl implements NodeDao {
	
	ArrayList<Node> nodes;
	
	//constructor
	public NodeDaoImpl() throws ClassNotFoundException, SQLException {
		nodes = new ArrayList<Node>();
		
		
		//SQL Query to get some node data
		String selectSql = "select host, hostname, uuid, image_ref, project_id,"+ 
				   "user_id, network_info from instances i "+
				   "join instance_info_caches ic on i.uuid = ic.instance_uuid "+
				   "where vm_state='active';";
	    ResultSet resultSet = lerMysql(selectSql,"root","kaio22","nova");
	    
	    while (resultSet.next()) {
	    	
	    	int indexI, indexF;
			String[] networkArray, ifaceArray, ipArray, macArray, routerArray;
			String iface = null, ip = null, mac  = null, roteador=null;
			
	    	Node guest = new Node();
	    	
		    if (resultSet.getString(7).contains("bridge") && resultSet.getString(7).contains("address") && resultSet.getString(5).contains("network")){
	    		networkArray = resultSet.getString(7).split(",");
		    	/*No quarto(3) vetor encontramos a interface (iface) 
		    	 *No nono(8) vetor encontramos o ip (ip) 
		    	 *No vigesimo nono(29) vetor encontramos o MAC address (mac) 
		    	 */
	    		
	    		//Collect Iface
	    		ifaceArray = networkArray[3].split(":");
	    		indexI = ifaceArray[2].indexOf('"');
	    		indexF = ifaceArray[2].lastIndexOf('"');
	    		iface = ifaceArray[2].substring(indexI + 1,indexF);
	    		//Collect Ip
	    		ipArray = networkArray[8].split(": ");
	    		indexI = ipArray[1].indexOf('"');
	    		indexF = ipArray[1].lastIndexOf('"');
	    		ip = ipArray[1].substring(indexI + 1,indexF);
	    		//Collect MAC
	    		macArray = networkArray[28].split(": ");
	    		indexI = macArray[1].indexOf('"');
	    		indexF = macArray[1].lastIndexOf('"');
	    		mac = macArray[1].substring(indexI + 1,indexF);
	    		//Collect Router
	    		routerArray = networkArray[1].split(": ");
	    		indexI = routerArray[2].indexOf('"');
	    		indexF = routerArray[2].lastIndexOf('"');
	    		roteador = routerArray[2].substring(indexI + 1,indexF);
	    	}
	    	
	    	//Set Node Properties
	    	guest.setHost(resultSet.getString(1));
	    	guest.setHostname(resultSet.getString(2));
	    	guest.setIp(ip);
	    	guest.setIface(iface);
	    	guest.setMac(mac);
	    	guest.setCamada(72);
	    	guest.setNagiosMonitor(0);
	    	guest.setUuid(resultSet.getString(3));
	    	guest.setImage(resultSet.getString(4));
	    	guest.setProjeto(resultSet.getString(5));
	    	guest.setLocatario(resultSet.getString(6));
	    	guest.setRoteador(roteador);
	    	guest.setActive(true);
	    	//finding services
	    	guest.setServices(procuraServicos(ip));	    	    	
		    //
	    	this.nodes.add(guest);
	    }
	    
	}

	public void findNewNodes() throws SQLException, ClassNotFoundException{
		//SQL Query to get some node data
		String selectSql = "select host, hostname, uuid, image_ref, project_id,"+ 
				   "user_id, network_info from instances i "+
				   "join instance_info_caches ic on i.uuid = ic.instance_uuid "+
				   "where vm_state='active';";
	    ResultSet resultSet = lerMysql(selectSql,"root","kaio22","nova");
	    
	    while (resultSet.next()) {
	    	
	    	//verifica se instancia já está catalogada
			if(this.getAllNodes()!=null){
				for(Node searchNode: this.getAllNodes()){
					if(searchNode.getUuid().equals(resultSet.getString(3))){
						//existing instance
						continue;
					}
				}
			}
	    	
	    	int indexI, indexF;
			String[] networkArray, ifaceArray, ipArray, macArray, routerArray;
			String iface = null, ip = null, mac  = null, roteador=null;
			Node guest = new Node();
	    	
	    	
		    if (resultSet.getString(7).contains("bridge") && resultSet.getString(7).contains("address") && resultSet.getString(5).contains("network")){
	    		networkArray = resultSet.getString(7).split(",");
		    	/*No quarto(3) vetor encontramos a interface (iface) 
		    	 *No nono(8) vetor encontramos o ip (ip) 
		    	 *No vigesimo nono(29) vetor encontramos o MAC address (mac) 
		    	 */
	    		
	    		//Collect Iface
	    		ifaceArray = networkArray[3].split(":");
	    		indexI = ifaceArray[2].indexOf('"');
	    		indexF = ifaceArray[2].lastIndexOf('"');
	    		iface = ifaceArray[2].substring(indexI + 1,indexF);
	    		//Collect Ip
	    		ipArray = networkArray[8].split(": ");
	    		indexI = ipArray[1].indexOf('"');
	    		indexF = ipArray[1].lastIndexOf('"');
	    		ip = ipArray[1].substring(indexI + 1,indexF);
	    		//Collect MAC
	    		macArray = networkArray[28].split(": ");
	    		indexI = macArray[1].indexOf('"');
	    		indexF = macArray[1].lastIndexOf('"');
	    		mac = macArray[1].substring(indexI + 1,indexF);
	    		//Collect Router
	    		routerArray = networkArray[1].split(": ");
	    		indexI = routerArray[2].indexOf('"');
	    		indexF = routerArray[2].lastIndexOf('"');
	    		roteador = routerArray[2].substring(indexI + 1,indexF);
	    	}
	    	
	    	//Set Node Properties
	    	guest.setHost(resultSet.getString(1));
	    	guest.setHostname(resultSet.getString(2));
	    	guest.setIp(ip);
	    	guest.setIface(iface);
	    	guest.setMac(mac);
	    	guest.setCamada(72);
	    	guest.setNagiosMonitor(0);
	    	guest.setUuid(resultSet.getString(3));
	    	guest.setImage(resultSet.getString(4));
	    	guest.setProjeto(resultSet.getString(5));
	    	guest.setLocatario(resultSet.getString(6));
	    	guest.setRoteador(roteador);
	    	guest.setActive(true);
	    	//finding services
	    	guest.setServices(procuraServicos(ip));	    	    	
		    //
	    	this.nodes.add(guest);
	    }
	    
}

private static ServiceDao procuraServicos(String ip) {
	// TODO Auto-generated method stub
	//exec nmap to find services
	System.out.println ("\t");
	ArrayList<Service> services = new ArrayList<Service>();
	Nmap4j nmap4j = new Nmap4j( ip ) ;
	ArrayList<String> getOutput = new ArrayList<String>();
	if( !nmap4j.hasError() ) {
		getOutput = nmap4j.getResult();
	}
	for(String service: getOutput){
		Service srv = new Service();
		srv.setName(service);
		srv.setNagiosMonitor(0);
		srv.setUuid(UUID.randomUUID().toString());
		srv.setDadoAvaliado(null);
		srv.setActive(true);
		//add service
		services.add(srv);		
	}
	
	ServiceDao servicesDao = new ServiceDaoImpl(services);
	
	return servicesDao;
}

//implemented methods from NodeDao

	//get all nodes (self-explained)
	public ArrayList<Node> getAllNodes() {
		// TODO Auto-generated method stub
		return this.nodes;
	}

	//get a specific node by uuid
	public Node getNode(String uuid) {
		// TODO Auto-generated method stub
		Node node = new Node();
    	for(Node varNode: this.nodes){
    		if(varNode.getUuid().equals(uuid)){
    			node = varNode;
    			break;
    		}
    	}
    	return node;
	}

	//update a specific node by uuid
	public void updateNode(Node node) {
		// TODO Auto-generated method stub
		int i=0;
    	for(Node varNode: this.nodes){
    		if(varNode.getUuid().equals(node.getUuid())){
    			nodes.set(i, node);
    			break;
    		}
    		i++;
    	}
	}

	//delete a specific node by uuid
	public void deleteNode(Node node) {
		// TODO Auto-generated method stub
    	for(Node varNode: this.nodes){
    		if(varNode.getUuid().equals(node.getUuid()))
    			nodes.remove(varNode);
    	}
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
