import java.io.Serializable;

/*
 * 
 * Classe Génerique
 * Classe Data elle correspond à la donnée qu'on veut traiter
 * 
 * Cette classe va etre utiliser par le serveur en utilisant un socket 
 * pour envoyer les données au Client
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class Data implements Serializable  {

	/**
	 * Les atttibuts ont servi juste au test
	 */
	private static final long serialVersionUID = -8714977058852359053L;
	private String nom;
	private int id;
	private float moyenne;

	public Data(String nom, int id, float moyenne) {

		this.nom = nom;
		this.id = id;
		this.moyenne = moyenne;
	}

	public String getNom() {
		return nom;
	}

	public int getId() {
		return id;
	}

	public float getMoyenne() {
		return moyenne;
	}



}
