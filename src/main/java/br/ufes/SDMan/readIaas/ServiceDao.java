package br.ufes.SDMan.readIaas;

import java.util.ArrayList;

public interface ServiceDao {
	public ArrayList<Service> getAllServices();
	public Service getService(String uuid);
	public void updateService(Service service);
	public void deleteService(Service service);
	public void addService(Service service);
}
