package br.ufes.SDMan;

import java.util.ArrayList;


import br.ufes.SDMan.nagios.NagiosMonitor;
import br.ufes.SDMan.neo4j.Neo4j;
import br.ufes.SDMan.readIaas.*;

public class App 
{
	
	public static void main(String[] args) throws Exception {
		Nmap4j nmap4j = new Nmap4j( "localhost" ) ;
		ArrayList<String> getOutput = new ArrayList<String>();
		if( !nmap4j.hasError() ) {
			getOutput = nmap4j.getResult();
		}
    	for(String var: getOutput){
    		System.out.println(var);
    	}
	}
	/*System.out.println("Selecione a IaaS...");
	System.out.println("No momento, somente OPENSTACK é suportado");
	new DescobreOpenstack();
	Neo4j.neo4jServidor("start");
        new Neo4j();
        System.out.println ("NAGIOS MONITOR");        
        //A execução do programa termina com um ctrl+c
        createShutDownHook();
		while(true){
			DescobreOpenstack.procuraNovasIntancias();
			try {
			//Executa nagios monitor
			ArrayList <String> ips;
			ips= Neo4j.consulta("INSTANCE", "IP");
    		        for(String ipNeo4j: ips)
				new NagiosMonitor(ipNeo4j);					
			Thread.sleep(30000); //1000 milisegundos = 1 segundo	
			}catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}			
		}
	}
	
	private static void createShutDownHook()
	{
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
	    {
	        public void run()
	        {   System.out.println();
	            System.out.println("Obrigado por utilizar a aplicação");
	            System.out.println("Saindo...");}
	    }));
}*/
}
