package br.ufes.SDMan.readIaas;

public interface ServerDao {
	
	public Server getServer();
	public void updateServer(Server server);
	public void deleteServer();
}
