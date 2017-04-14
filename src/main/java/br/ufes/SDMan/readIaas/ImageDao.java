package br.ufes.SDMan.readIaas;

import java.util.ArrayList;

public interface ImageDao {
	
	public ArrayList<Image> getAllImages();
	public Image getImage(int index);
	public void updateImage(Image image);
	public void deleteImage(Image image);
	public void addImage(Image image);
}
