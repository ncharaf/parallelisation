import java.io.Serializable;

/*
 * 
 * Cette classe va servir à stocker les ressources du client
 * 
 * On est pas obligé de la faire mais on a pensé à le faire  au cas ou 
 * 
 * Apres vous avez besoin que le client envoie ces ressource au serveur
 * 
 * pour les afficher 
 * 
 * 
 */



public class Ressource implements Serializable {

	/**
	 * On a besoin de :
	 * -Pourcentage du CPU  c'est à dire frequence actuelle/Frequence MAx
	 * -Pourcentage Ram  c'est à dire memoire utilisé actuellement/ memoire max
	 * On a rajoute des autres attributs mais ils seront pas pris en compte 
	 * 
	 */
	private static final long serialVersionUID = -8767826457823666928L;

	private String hostname;
	private  double frequency;
	private  double memory;
	private  int core_available;
	private String os;
	private String architecture;
	public Ressource(String hostname, double frequency, double memory, int core_available, String os,String architecture) {
		this.hostname = hostname;
		this.frequency = frequency;
		this.memory = memory;
		this.core_available = core_available;
		this.os = os;
		this.architecture = architecture;
	}

	public String getHostname() {
		return hostname;
	}
	public double getMemory_avalaible() {
		return frequency;
	}
	public double getMax_memory() {
		return memory;
	}
	public int getCore_available() {
		return core_available;
	}
	public String getOs() {
		return os;
	}
	public String getArchitecture() {
		return architecture;
	}

	@Override
	public String toString() {
		return "Ressource [hostname=" + hostname + ", frequency=" + frequency + ", memory=" + memory
				+ ", core_available=" + core_available + ", os=" + os + ", architecture=" + architecture + "]";
	}





}
