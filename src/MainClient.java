import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import searchEngine.Test;


public class MainClient {



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Double memory=getMemory();
		String hostname=System.getProperty("user.name");
		//System.out.println(hostname);
		Double frequency=getFrequency();
		int cores = Runtime.getRuntime().availableProcessors();
		String os=System.getProperty("os.name");
		String architecture=System.getProperty("os.arch");
		Ressource r = new Ressource(hostname, frequency, memory, cores, os, architecture);
		System.out.println(r);

		Client  client = new Client(10000,"localhost");
		System.out.println("testSocketClient\t"+client.getSocket());
		/*try {
			ObjectOutputStream toServer = new ObjectOutputStream(client.getSocket().getOutputStream());
			ObjectInputStream fromServer = new ObjectInputStream(client.getSocket().getInputStream());
			Object obj;
			try {
				obj = fromServer.readObject();

				if(obj.getClass().equals(String.class)){
					String req=(String) obj;
					Test essai=new Test();
					HashMap<Integer, Double> classement=essai.calcul(req);

					toServer.writeObject(classement);
					String msg="FIN";
					toServer.writeObject(msg);
					toServer.flush();

					client.closeConnexion();
				}else{
					System.out.println("Object inattendu");
				}

			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/


	}



	public static Double  getMemory(){
		//	os.name	Linux

		if(System.getProperty("os.name").equals("Linux")){
			Runtime runtime = Runtime.getRuntime();
			String[] cmd = {"/bin/sh","-c","free | tr -s \" \""};

			Process process;
			try {
				process=runtime.exec(cmd);
				BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				//On supprime la première ligne qui ne sert à rien 
				line = systemInformationReader.readLine();
				line= systemInformationReader.readLine();
				String [] line_sep=line.split(" ");
				Integer max_memory=Integer.parseInt(line_sep[1]);
				//	System.out.println(line_sep[1]);
				Integer memory_avalaible=Integer.parseInt(line_sep[2]);
				//	System.out.println(line_sep[6]);
				Double  memory = (memory_avalaible*1.0)/max_memory;
				if(memory>1 || memory<=0){
					throw new Error("Erreur calcul ram");				
				}
				return memory;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}




		}else{

			System.out.println("Votre OS n'est pas encore géré pour le moment ");
			return null;

		}
	}

	public static Double getFrequency(){

		if(System.getProperty("os.name").equals("Linux")){
			Runtime runtime = Runtime.getRuntime();
			String[] cmd = {"/bin/sh","-c","lscpu | grep MHz | tr -s  \" \" | tr -s \",\" \".\""};

			Process process;
			try {
				process=runtime.exec(cmd);
				BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				//Première ligne pour récuperer la vitesse actuelle

				String line;
				line = systemInformationReader.readLine();
				String [] first_line=line.split(":");
				Double current_frequency= Double.parseDouble(first_line[1].trim());

				//2ème ligne pour récuperer la vitesse max
				line= systemInformationReader.readLine();
				Double max_frequency;
				if(line!=null){
				String [] second_line=line.split(":");
				max_frequency= Double.parseDouble(second_line[1].trim());
				}else{
				String[] cmd1 = {"/bin/sh","-c","cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
				process=runtime.exec(cmd1);
				systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				line= systemInformationReader.readLine();
				max_frequency=Double.parseDouble(line);
				max_frequency/=1000;
				}
				
				Double  frequency = (current_frequency*1.0)/max_frequency;
				if(frequency>1 || frequency<=0){
					throw new Error("Erreur calcul frequence");				
				}

				return frequency;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}else{

			System.out.println("Votre OS n'est pas encore géré pour le moment ");
			return null;

		}

	}

}
