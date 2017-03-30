import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 
 * This class extends OS, it is associated with the OS LINUX.
 * Its aim his to set resources such as CPU or RAM.
 * 
 */

public class Linux extends Os {

	public Linux(String os) {
		super(os);
		this.cmd_memory_rate = new String[3];
		this.cmd_memory_rate[0] = "/bin/sh";
		this.cmd_memory_rate[1] = "-c";
		this.cmd_memory_rate[2] = "free | tr -s \" \"";
		this.cmd_frequency = new String[3];
		this.cmd_frequency[0] = "/bin/sh";
		this.cmd_frequency[1] = "-c";
		this.cmd_frequency[2] = "cat /proc/cpuinfo | tr -s \" \" |grep \"cpu MHz\" ";
		this.cmd_memory_available = new String[3];
		this.cmd_memory_available[0] = "/bin/sh";
		this.cmd_memory_available[1] = "-c";
		this.cmd_memory_available[2] =" free -m  | tr -s \" \" | cut -d \" \" -f7";
		this.setMemoryRate();
		this.setMemoryAvailable();
		this.setFrequency();
	}

	@SuppressWarnings("null")
	@Override
	public void setMemoryRate() {

		Runtime runtime = Runtime.getRuntime();
		Process process;

		try {
			process=runtime.exec(this.cmd_memory_rate);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			//First line useless
			line= systemInformationReader.readLine();
			//Second one with specs
			line= systemInformationReader.readLine();
			String [] line_sep=line.split(" ");
			Integer max_memory=Integer.parseInt(line_sep[1]);
			Integer memory_avalaible=Integer.parseInt(line_sep[2]);
			Double  memory = (memory_avalaible*1.0)/max_memory;

			if(memory>1 || memory<=0){
				// Memory check
				throw new Error("Trouble in getting RAM");				
			}
			this.memory_rate=memory;

		} catch (IOException e) {
			this.memory_rate= (Double) null;
		}

	}



	@SuppressWarnings("null")
	@Override
	public void setFrequency() {
		Runtime runtime = Runtime.getRuntime();
		Process process;

		try {
			process=runtime.exec(this.cmd_frequency);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			//First line containing current speed
			//To define n° of cores and current frequency
			String line;
			int nb_cores=0;
			Double freq=0.0;
			while((line=systemInformationReader.readLine())!=null){
				nb_cores++;
				freq+=Double.parseDouble(line.trim().split(":")[1].trim());
			}
			
			//To define Max frequency
			Double max_frequency;
			this.cmd_frequency[0] = "/bin/sh";
			this.cmd_frequency[1] = "-c";
			this.cmd_frequency[2] = "cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
			process=runtime.exec(this.cmd_frequency);
			systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line= systemInformationReader.readLine();
			max_frequency=Double.parseDouble(line);
			max_frequency/=1000;

			//Calculate the frequency
			Double  frequency = (freq*1.0)/(max_frequency*nb_cores);
			if(frequency>1 || frequency<=0){
				throw new Error("Trouble in getting Frequency");				
			}
			this.frequency=frequency;

		} catch (IOException e) {
			this.frequency = (Double) null;
		}


	}



	@SuppressWarnings("null")
	@Override
	public void setMemoryAvailable() {
		Runtime runtime = Runtime.getRuntime();
		Process process;
		
		try {
			process=runtime.exec(this.cmd_memory_available);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			//Delete first line as it is useless
			line = systemInformationReader.readLine();
			//Line where we have the available memory
			line= systemInformationReader.readLine();
			line=line.trim();
			Integer memory=Integer.parseInt(line);
			if(memory<=0){
				throw new Error("Trouble in getting available memory");				
			}
			this.memory_available= memory*1.0;


		} catch (IOException e) {
			this.memory_available = (Double) null;
		}
	}
}
