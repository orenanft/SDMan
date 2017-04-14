package br.ufes.SDMan.readIaas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Nmap4j {
	
	private ArrayList<String> result;
	private String nmap;
	
	public Nmap4j(String ip){
		System.out.println("\t");
		this.findNmap();
		this.runNmap(ip);
	}

	public ArrayList<String> getResult() {
		return result;
	}
	
	private void setResult(ArrayList<String> res) {
		this.result = res;
	}
	
	public String getNmap() {
		return nmap;
	}
	
	private void setNMap(String nmap) {
		this.nmap = nmap;
	}	
	
	public boolean hasError(){
		if(this.nmap.equals(null)){
			return true;
		}
		return false;
	}

	private void runNmap(String ip){
		String commandReturned = new String();
		ArrayList<String> readLine = new ArrayList<String>();
		String [] command = {this.nmap," -T3 -sV ",ip};
	    Process p;
	    try {
	        // run the command
	        p = Runtime.getRuntime().exec(command);
	        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        // get the result
	        while((commandReturned = br.readLine()) != null){
				readLine.add(commandReturned);
	        }
	        p.waitFor();
	        if(p.exitValue() == 1 ){
	        	System.out.println ("Erro ao executar comando " + command);
	        }
	        p.destroy();
	    } catch (Exception e) {}
	    findServices(readLine);	    
	}
	
	private void findServices(ArrayList<String> readLine) {
		// TODO Auto-generated method stub	
		ArrayList<String> services = new ArrayList<String>();
		for (String read: readLine){
			if(read.contains("open")){
				String [] nmapService = read.split(" ");
				services.add(nmapService[nmapService.length-1]);
			}
		}
		this.setResult(services);
	}

	private void findNmap() {
		// TODO Auto-generated method stub
		String [] command = {"/bin/which", "nmap"};
	    Process p;
	    String commandReturned = new String();
	    ArrayList<String> readLine = new ArrayList<String>();
		try {
	        // run the command
	        p = Runtime.getRuntime().exec(command);
	        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        // get the result
	        while((commandReturned = br.readLine()) != null){
	        	readLine.add(commandReturned);
	        	//System.out.println(commandReturned);
	        }
	        p.waitFor();
	        if(p.exitValue() == 1 ){
	        	System.out.println ("Erro ao executar comando " + command);
	        }
	        p.destroy();
	    } catch (Exception e) {}
		
	    this.setNMap(readLine.get(0));
	}
}
