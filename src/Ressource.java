import java.io.Serializable;


/*
 * 
 * This class will be used to store all resources from the clients, with his hostname, nb of cores .....
 * 
 */



public class Ressource implements Serializable {


	private static final long serialVersionUID = -8767826457823666928L;

	private String hostname;
	private  double frequency;
	private  double memory;
	private Double memory_available;//MegaByte
	private  int core_available;
	private String os;
	private String architecture;
	
	
	public Ressource(String hostname, double frequency, double memory, Double memory_available, int core_available, String os,
		String architecture) {
		this.hostname = hostname;
		this.frequency = frequency;
		this.memory = memory;
		this.core_available = core_available;
		this.os = os;
		this.architecture = architecture;
		this.memory_available = memory_available;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getHostname() {
		return hostname;
	}


	public double getFrequency() {
		return frequency;
	}


	public double getMemory() {
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


	public Double getMemory_available() {
		return memory_available;
	}


	@Override
	public String toString() {
		return "Ressource [hostname=" + hostname + ", frequency=" + frequency + ", memory=" + memory
				+ ", memory_available=" + memory_available + ", core_available=" + core_available + ", os=" + os
				+ ", architecture=" + architecture + "]";
	}








}
