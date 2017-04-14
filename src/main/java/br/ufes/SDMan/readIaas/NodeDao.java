package br.ufes.SDMan.readIaas;

import java.util.ArrayList;

public interface NodeDao {

	public ArrayList<Node> getAllNodes();
	public Node getNode(String uuid);
	public void updateNode(Node node);
	public void deleteNode(Node node);
}
