package br.ufes.SDMan.neo4j;

import java.util.ArrayList;

public interface Neo4jDAO {

	public void insert(Object o);
	public void delete(Object o);
	public ArrayList<Object> get(Object o);
	public void createRelationships(Object objSrc, Object objTrgt);
	public void deleteRelationships(Object objSrc, Object objTrgt);
	public void getAttr(Object objSrc, Object attr);
	public void deleteAttr(Object objSrc, Object objAttr);
	public void setAttr(Object objSrc, Object objAttr);
	public void startNeo4j();
	
	
}
