import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 
 * This class extends OS, it is associated with the OS MACINTOSH.
 * Its aim his to set resources such as CPU or RAM.
 * 
 */

public class Windows extends Os {

	public Windows(String os) {
		super(os);
		this.cmd_memory_rate=new String[2];
		this.cmd_memory_rate[0]="wmic ComputerSystem get TotalPhysicalMemory";
		this.cmd_memory_rate[1]="wmic OS get FreePhysicalMemory";
		this.cmd_memory_available=new String[1];
		this.cmd_memory_available[0]="wmic OS get FreePhysicalMemory"; this.cmd_frequency=new String[1];
		this.cmd_frequency[0]="wmic cpu get loadpercentage";
	}

	@SuppressWarnings("null")
	@Override
	public void setMemoryRate() {
		Runtime runtime = Runtime.getRuntime();
		Process process1;
		Process process2;
		String line;

		try {
			process1=runtime.exec(this.cmd_memory_rate[0]);
			process2=runtime.exec(this.cmd_memory_rate[1]);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
			System.out.println("Done");
			//First 2 are useless
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			//Remove useless blank
			String [] line_sep=line.split(" ");
			//System.out.println("Max"+line_sep[0]);
			long max_memory=Long.parseLong(line_sep[0]);

			/*
			 * Value returned by wmic ComputerSystem get TotalPhysicalMemory is in bytes. Value returned by wmic OS get FreePhysicalMemory is in KB.
			 */

			systemInformationReader = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			//System.out.println("Free"+line);
			String [] line_sep1=line.split(" ");
			long free_memory=Long.parseLong(line_sep1[0])*1000;
			System.out.println(free_memory+":Total"+max_memory);
			Double memoryRate = 1-((free_memory*1.0)/max_memory);
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
			process=runtime.exec(this.cmd_frequency[0]);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//First 2 are useless
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			//Remove useless blank
			String [] line_sep=line.split(" ");
			long frequency=Long.parseLong(line_sep[0]);
			this.frequency=frequency;

		}catch(IOException e) {
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
			process=runtime.exec(this.cmd_memory_available[0]);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//First 2 are useless
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			line= systemInformationReader.readLine();
			//Remove useless blank
			String [] line_sep=line.split(" ");
			//System.out.println("Max"+line_sep[0]);
			long free_memory=Long.parseLong(line_sep[0])/1000;
			this.memory_available=free_memory;

		}catch(IOException e) {
			this.memory_available=(Double) null;
		}
	}

}