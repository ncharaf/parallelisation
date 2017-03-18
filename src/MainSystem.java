import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


/*
 * Class de test qui permet de trouver les fonctions nécessaire pour 
 * déterminer les ressources
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class MainSystem {

	public static void main (String[] args){
		/*
		//Runtime.getRuntime().gc();

		/*long totalMemory = (long) (Runtime.getRuntime().totalMemory()/ (1024.0 * 1024.0 ));
		System.out.printf("%.3fGiB\n", Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0 * 1024.0));
		//System.out.println();
		System.out.println(totalMemory);
		Runtime.getRuntime().gc();*/
		//OperatingSystemMXBean mxbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		System.setProperty("file.encoding", "UTF-8");
		/*Runtime runtime = Runtime.getRuntime();
		System.out.println(runtime.freeMemory()/(1024*1024));
       Process process;
		try {
			//process = runtime.exec("systeminfo");
			process = runtime.exec("wmic MemoryChip get Capacity");

		//	System.out.println(process.isAlive());

        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();
        String line=line = systemInformationReader.readLine();

        while ((line = systemInformationReader.readLine()) != null)
        {
            stringBuilder.append(line);
          //  stringBuilder.append(System.lineSeparator());
        }
        process.destroy();*/

		try{
			Runtime runtime = Runtime.getRuntime();
			String[] cmd = {"/bin/sh","-c","free | tr -s \" \""};

			Process process;
			process=runtime.exec(cmd);
			//System.out.println(process.);
			BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			//On supprime la première ligne qui ne sert à rien 

			String line= systemInformationReader.readLine();

			//	System.out.println(line);
			line= systemInformationReader.readLine();
			System.out.println(line);

			System.out.println(line.length());

			String [] line_sep=line.split(" ");
			Integer max_memory=Integer.parseInt(line_sep[1]);
			//	System.out.println(line_sep[1]);
			Integer memory_avalaible=Integer.parseInt(line_sep[6]);
			//	System.out.println(line_sep[6]);
			Double  memory = (memory_avalaible*1.0)/max_memory;
			System.out.println(memory);


			/*
			String result_command []=stringBuilder.toString().trim().split("  ");
			//System.out.println(result_command.length);
			float max_memory=0;
			for (int i=0;i<result_command.length;i++){
				max_memory+=Float.parseFloat(result_command[i]);
				//System.out.println(result_command[i]);
			}
			//Linux memory cat  /proc/meminfo
			//memory avalaible wmic os get freephysicalmemory
			System.out.println("Memoire maximale :"+max_memory/(1024*1024*1024*1.0));*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//System.out.println(((Object) mxbean).getTotalPhysicalMemorySize() + " Bytes "); 

		Map<String,String> param=System.getenv();
		for (Entry<String, String> entry : param.entrySet()) {
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
		System.out.println("Coucééeé");
		//System.setProperty("sun.jnu.encoding", "UTF-8");
		System.out.println("Property-------------");
		Properties property=System.getProperties();

		for (Object entry : property.keySet()) {
			System.out.println(entry+"\t"+property.getProperty((String) entry));
		}

	}
	//		long freeMemory = Runtime.getRuntime().freeMemory();
	//long usedMemory = totalMemory - freeMemory;






}

