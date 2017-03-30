import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 
 * This class extends OS, it is associated with the OS MACINTOSH.
 * Its aim his to set resources such as CPU or RAM.
 * 
 */


public class Mac extends Os {

	public Mac(String os) {
		super(os);
		this.cmd_memory_rate = new String[3];
		this.cmd_memory_rate[0] = "/bin/sh";
		this.cmd_memory_rate[1] = "-c";
		this.cmd_memory_rate[2] = "top -l 1 -s 0 | grep PhysMem";
		this.cmd_frequency = new String[3];
		this.cmd_frequency[0] = "/bin/sh";
		this.cmd_frequency[1] = "-c";
		this.cmd_frequency[2] = "ps -A -o %cpu | awk '{s+=$1} END {print s}'";
		this.cmd_memory_available = new String[3];
		this.cmd_memory_available[0] = "/bin/sh";
		this.cmd_memory_available[1] = "-c";
		this.cmd_memory_available[2] ="top -l 1 -s 0 | grep PhysMem";
		this.setMemoryRate();
		this.setMemoryAvailable();
		this.setFrequency();
	}

	@SuppressWarnings("null")
	@Override
	public void setMemoryRate() {
		Runtime runtime = Runtime.getRuntime();
		Process process;
		String line;
		try {
			process=runtime.exec(this.cmd_memory_rate);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line= systemInformationReader.readLine();
			String [] line_sep=line.split(" ");
			String unused_memory = line_sep[5];
			unused_memory = unused_memory.substring(0, unused_memory.length()-1);
			Integer free_memory=Integer.parseInt(unused_memory);
			String used_memory = line_sep[1];
			used_memory = used_memory.substring(0, used_memory.length()-1);
			Integer max_memory=Integer.parseInt(used_memory)+free_memory;
			Double memoryRate = 1-(free_memory*1.0)/max_memory;
			if(memoryRate>1 || memoryRate<=0){
				// Memory check
				throw new Error("Trouble in getting RAM");
			}
			this.memory_rate=memoryRate;


		} catch (IOException e) {
			this.memory_rate=(Double) null;
		}

	}

	@SuppressWarnings("null")
	@Override
	public void setFrequency() {
		Runtime runtime = Runtime.getRuntime();
		Process process;
		String line;
		try {
			process=runtime.exec(this.cmd_frequency);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line= systemInformationReader.readLine();
			String [] line_sep=line.split(" ");
			String frequency_trim = line_sep[0];
			Double frequency=Double.parseDouble(frequency_trim);
			this.frequency=frequency*1.0;


		} catch (IOException e) {
			this.frequency=(Double) null;
		}
	}


	@SuppressWarnings("null")
	@Override
	public void setMemoryAvailable() {
		Runtime runtime = Runtime.getRuntime();
		Process process;
		String line;
		try {
			process=runtime.exec(this.cmd_memory_available);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line= systemInformationReader.readLine();
			String [] line_sep=line.split(" ");
			String unused_memory = line_sep[5];
			unused_memory = unused_memory.substring(0, unused_memory.length()-1);
			Integer free_memory=Integer.parseInt(unused_memory);
			this.memory_available=free_memory*1.0;


		} catch (IOException e) {
			this.memory_available=(Double) null;
		}
	}

}