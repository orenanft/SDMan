package br.ufes.SDMan.neo4j;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import br.ufes.SDMan.readIaas.DescobreOpenstack;
import br.ufes.SDMan.readIaas.Node;
import br.ufes.SDMan.readIaas.NodeDao;
import br.ufes.SDMan.readIaas.Server;
import br.ufes.SDMan.readIaas.ServerDao;
import br.ufes.SDMan.readIaas.Service;

public class Neo4j {

	private static final String createComputeNode = "CREATE (a:ComputeNode {Name: {name}, IP: {ip}, Layer:{layer}, UUID:{uuid}, Active:{active}})";
	private static final String createInstance = "CREATE (a:Instance {Name: {name}, IP: {ip}, MAC: {mac}, Interface:{iface}, Layer:{layer}, Project:{project}, Tenant:{tenant}, Router:{router}, UUID:{uuid}, Active:{active}})";

    public Neo4j() {
		// TODO Auto-generated constructor stub
    	System.out.println ("Writing in NEO4J...");
    	criaBanco();
	}

    private void runQueriesNeo4j(Object object){
    	Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "sdman" ) );
    	Session session = driver.session();
    	
    	if(object instanceof Server){
    		session.run( createComputeNode,
    	    		parameters("name",((Server) object).getHostname(), "ip",((Server) object).getIp(), "layer",((Server) object).getCamada(), "uuid",((Server) object).getUuid(), "active",((Server) object).isActive() ));
    	}else if(object instanceof Node){
    		session.run( createInstance ,
        	        parameters("name",((Node) object).getHostname(), "ip",((Node) object).getIp(), "mac",((Node) object).getMac(), "layer",((Node) object).getCamada(), "project",((Node) object).getProjeto(), "tenant",((Node) object).getLocatario(), "router",((Node) object).getRoteador(), "uuid",((Node) object).getUuid(), "active",((Node) object).isActive() ));
    	}else if (object instanceof Service){
    		
    	}
    	/*
    	 * session.run( "CREATE (a:Person {name: {name}, title: {title}})",
        parameters( "name", "Arthur", "title", "King" ) );

StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
                                      "RETURN a.name AS name, a.title AS title",
        parameters( "name", "Arthur" ) );
while ( result.hasNext() )
{
    Record record = result.next();
    System.out.println( record.get( "title" ).asString() + " " + record.get( "name" ).asString() );
}
    	 * */
    	    	
    	session.close();
    	driver.close();
    }
    
    //funcao responsavel por criar o banco
	private void criaBanco() {
        System.out.println ("\t");
	//DELETING DATA
	insere("MATCH (n)-[r]-(ns) delete n,r,ns"); 
    insere("MATCH (n) delete n");
    //Service Stats
    insere("create(n:ServiceStats)");
	
	ServerDao servidor = DescobreOpenstack.getServidores();
	NodeDao virtuals = DescobreOpenstack.getNos(); 
	Server server = servidor.getServer();
        //create physical server
    if (server != null){
    	
    	runQueriesNeo4j(server);

        for (Node virtual : virtuals.getAllNodes() ){
        	if(virtual.getHost().equalsIgnoreCase(server.getHostname())){
        	//Criação das Instâncias (virtual)	
        		


        	insere("CREATE (a:Instance{Name:'"+virtual.getHostname()+
        	           "',IP:'"+virtual.getIp()+"',MAC:'"+virtual.getMac()+
        	           "',Interface:'"+virtual.getIface()+
        		   "',Layer:"+virtual.getCamada()+",uuid:'"+virtual.getUuid()+
        		   "',Project:'"+virtual.getProjeto()+
        		   "',Tenant:'"+virtual.getLocatario()+
        		   "',Router:'"+virtual.getRoteador()+
        		   "',Active:'"+virtual.isActive()+"'})");
	        //create relationship
	       	insere("MATCH (a:Compute),(b:Instance) WHERE a.uuid='"+server.getUuid()+
	       			"' AND b.uuid='"+virtual.getUuid()+"' CREATE (a)-[r:Hosts]->(b)");

	        //getting services
	       	for (Service service :  virtual.getServices().getAllServices()){
	       		insere("CREATE (a:Service{Name:'"+service.getName()+
	       	       		"',uuid:'"+service.getUuid()+"'})");
	        	//create relationship
	        	insere("MATCH (a:Instance),(b:Service) WHERE a.uuid='"+virtual.getUuid()+
	       			"' AND b.uuid='"+service.getUuid()+"'CREATE (a)-[r:Provides]->(b)");
	        	//Service Stats (todos os serviços iniciados com false)
	        	adiciona("ServiceStats",service.getName(),"false");
		   	}
         }
	    }
    }
        
    }
    
	//função para (ligar/desligar/religar) o neo4j
	public static void neo4jServidor(String status){
    	System.out.println ("\t");
	String [] command = {"/neo4j/bin/neo4j",status};
	    Process p;
	    try {
	        // run the command
	        p = Runtime.getRuntime().exec(command);
	        // get the result
	        p.waitFor();
	        if(p.exitValue() == 1 ){
	        	System.out.println ("Erro ao executar comando " + command);
	        }
	        p.destroy();
	    } catch (Exception e) {}
    }
	
	//função consulta uuid de um nó
	public static String consultaUuid(String mode) {
		// TODO Auto-generated method stub
	System.out.println ("\t");
    	ArrayList <String> result= new ArrayList<String>();
    	Properties props = new Properties();
    	props.setProperty("user","neo4j");
    	props.setProperty("password","kaio22");
    	
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs=null;
		try {
			rs = con.createStatement().executeQuery("MATCH (a:"+mode+") WHERE not ((a)-[]-()) RETURN a.uuid");
		   	while(rs.next()){
		   		//System.out.println(rs.getString("n."+property));
		    	result.add(rs.getString("a.uuid"));
		        }
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result.size()>1){
				System.out.println("ERROR: There are two similar nodes!!!");
				for(String res: result)
					System.out.println(res);
			}else if(result.isEmpty())
				System.out.println("ERROR: Query is empty");
    	
		return result.get(0);
	}
    
	//função de consulta (retorna todos os valores de propriedade de nós com rótulo mode)
    public static ArrayList <String> consulta(String mode, String property){
    	// Make sure Neo4j Driver is registered
    	//Class.forName("org.neo4j.jdbc.Driver");
	System.out.println ("\t");
    	ArrayList <String> result= new ArrayList<String>();
    	Properties props = new Properties();
    	props.setProperty("user","neo4j");
    	props.setProperty("password","kaio22");
    	
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs=null;
		try {
				rs = con.createStatement().executeQuery("MATCH (n:"+mode+") RETURN n."+property);
		   	while(rs.next()){
		   		//System.out.println(rs.getString("n."+property));
		    	result.add(rs.getString("n."+property));
		        }
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if(result.isEmpty())
			System.out.println("CAUTION! Query is empty");
			
		return result;	
    }
  //função de consulta (retorna todos os valores de propriedade de nós com rótulo mode dada uma condição)
public static ArrayList <String> consulta(String mode, String property, String propertyParam, String valueParam){
	System.out.println ("\t");
    	ArrayList <String> result= new ArrayList<String>();
    	Properties props = new Properties();
    	props.setProperty("user","neo4j");
    	props.setProperty("password","kaio22");
    	
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs=null;
		try {
			if(mode.equalsIgnoreCase("instance")){
				rs = con.createStatement().executeQuery("MATCH (n:"+mode+") WHERE n."+propertyParam+"='"+valueParam+"' RETURN n."+property);
			while(rs.next()){
                        result.add(rs.getString("n."+property));
                        }
			}
			else if(mode.equalsIgnoreCase("service")){
				rs = con.createStatement().executeQuery("MATCH (n)-[]-(ns:"+mode+") WHERE n."+propertyParam+"='"+valueParam+"' RETURN ns."+property);
			while(rs.next()){
                        result.add(rs.getString("ns."+property));
                        }
			}else if((valueParam != null && propertyParam == null)||(valueParam == null && propertyParam != null)){
				System.out.println("Param invalid");
			}
			else{
				System.out.println("QueryNeo4j: Unavailable Mode");
			}
		}catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
		
			if(result.isEmpty())
				System.out.println("CAUTION! Query is empty");
			
		return result;	
    }   
 
    @SuppressWarnings("unused")
    //função de inserção ao neo4j (executa a query passada por parametro)
    public static void insere(String query){  

	System.out.println ("\t");
    	Properties props = new Properties();
    	props.setProperty("user","neo4j");
    	props.setProperty("password","kaio22");
    	
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs;
    	try {
    		rs = con.createStatement().executeQuery(query);
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
  
    @SuppressWarnings("unused")
    // metodo para deletar (instancias ou serviços (mode))
    public static void deleteNeo4j(String mode, String property, String value){
		System.out.println ("\t");
		Properties props = new Properties();
		props.setProperty("user","neo4j");
		props.setProperty("password","kaio22");
		
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs;
		try {
			if(mode.equalsIgnoreCase("instance")){
				rs = con.createStatement().executeQuery("MATCH ()-[r1]-(n) MATCH (n)-[r2]-(ns) WHERE n."+property+"='"+value+"' DELETE n,r1,r2,ns");
			}	
			else if(mode.equalsIgnoreCase("service")){
				rs = con.createStatement().executeQuery("MATCH ()-[r]-(n) WHERE n."+property+"='"+value+"' DELETE n,r");
			}
			else{
				System.out.println("DelNeo4j: Unavailable Mode");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }	
  
    @SuppressWarnings("unused")
  //função para adicionar/atualizar valor
	public static void adiciona(String uuid, String property, String oldValue, String newValue){

		System.out.println ("\t");
		Properties props = new Properties();
		props.setProperty("user","neo4j");
		props.setProperty("password","kaio22");
		
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs;
		try {
			rs = con.createStatement().executeQuery("MATCH (n) WHERE n."+property+"='"+oldValue+"' AND n.uuid='"+uuid+"' SET n."+property+"='"+newValue+"'");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("unused")
    //função para adicionar valor
	public static void adiciona(String label, String property, String value){
    	System.out.println ("\t");
		Properties props = new Properties();
		props.setProperty("user","neo4j");
		props.setProperty("password","kaio22");
		// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs;
		try {
			rs = con.createStatement().executeQuery("match (n:"+label+") set n."+property+"="+value.toLowerCase()+"");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //função de consulta por parametro especifico
    public static String consultaParam(String mode, String property){
	System.out.println ("\t");
    	String result=null;
    	Properties props = new Properties();
    	props.setProperty("user","neo4j");
    	props.setProperty("password","kaio22");
    	
    	// Connect		
    	Neo4jConnection con=null;
		try {
			con = new Driver().connect("jdbc:neo4j://localhost:7474/", props);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Querying
     	ResultSet rs=null;
		try {
			rs = con.createStatement().executeQuery("MATCH (n:"+mode+") RETURN n."+property);
	   		//System.out.println(rs.getString("n."+property));
	    	if(rs.next())
				result= rs.getString("n."+property);
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if(result.isEmpty())
			System.out.println("CAUTION! Query is empty");
			
		return result;	
    }

}

