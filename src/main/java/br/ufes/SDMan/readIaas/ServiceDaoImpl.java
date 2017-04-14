package br.ufes.SDMan.readIaas;

import java.util.ArrayList;

public class ServiceDaoImpl implements ServiceDao {

	ArrayList<Service> services;
	
	public ServiceDaoImpl(ArrayList<Service> services) {
		
		this.services = services;
	}
	
	public ArrayList<Service> getAllServices() {
		// TODO Auto-generated method stub
		return this.services;
	}

	public Service getService(String uuid) {
		// TODO Auto-generated method stub
		Service service = new Service();
    	for(Service varService: this.services){
    		if(varService.getUuid().equals(uuid)){
    			service = varService;
    			break;
    		}
    	}
    	return service;
	}

	public void updateService(Service service) {
		// TODO Auto-generated method stub
		int i=0;
    	for(Service varService: this.services){
    		if(varService.getUuid().equals(service.getUuid())){
    			services.set(i, service);
    			break;
    		}
    		i++;
    	}
	}

	public void deleteService(Service service) {
		// TODO Auto-generated method stub
    	for(Service varService: this.services){
    		if(varService.getUuid().equals(service.getUuid()))
    			services.remove(varService);
    	}
	}

	public void addService(Service service) {
		// TODO Auto-generated method stub
		this.services.add(service);
	}

}
