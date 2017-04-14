package br.ufes.SDMan.readIaas;

public class Server {
	
	private String hostname;
	private String ip;
	private int camada;
	private int nagiosMonitor;
	private String uuid;
	private boolean active;
	private NodeDao nos;

	public NodeDao getNos() {
		return nos;
	}
	public void setNos(NodeDao nos) {
		this.nos = nos;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getCamada() {
		return camada;
	}
	public void setCamada(int camada) {
		this.camada = camada;
	}
	public int getNagiosMonitor() {
		return nagiosMonitor;
	}
	public void setNagiosMonitor(int nagiosMonitor) {
		this.nagiosMonitor = nagiosMonitor;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
