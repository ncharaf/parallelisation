/*
 * Abstract class that will be extended by all kind of OS. 
 */

public abstract class Os {

	protected String os_name;
	protected String[] cmd_memory_rate;
	protected String[] cmd_memory_available;
	protected String[] cmd_frequency;
	protected double memory_rate;
	protected double frequency;
	protected double memory_available;

	public Os (String os) {
		this.os_name=os;
	}


	public abstract void setMemoryRate();
	public abstract void setFrequency();
	public abstract void setMemoryAvailable();

	public double getMemoryRate() {
		return this.memory_rate;
	}

	public double getFrequency() {
		return this.frequency;
	}

	public double getMemoryAvailable() {
		return this.memory_available;
	}




}
