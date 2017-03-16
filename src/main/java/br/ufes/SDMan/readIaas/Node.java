package br.ufes.SDMan.readIaas;

public class Node{
	private String hostname;
	private String host;
	private String ip;
	private String iface;
	private String mac;
	private ServiceDao services;
	private int camada;
	private int nagiosMonitor;
	private String uuid;
	private String projeto;
	private String locatario;
	private String roteador;
	private String image;
	private boolean active;
	//getters and setters
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	//getters and setters
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getIface() {
		return iface;
	}
	
	public void setIface(String iface) {
		this.iface = iface;
	}
	
	public String getMac() {
		return mac;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public ServiceDao getServices() {
		return services;
	}
	
	public void setServices(ServiceDao services) {
		this.services = services;
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
	
	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getLocatario() {
		return locatario;
	}

	public void setLocatario(String locatario) {
		this.locatario = locatario;
	}

	public String getRoteador() {
		return roteador;
	}

	public void setRoteador(String roteador) {
		this.roteador = roteador;
	}


}
