import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Set;
import java.util.TreeSet;

import searchEngine.Index;

/*
 * Cette classe elle permet la création du socket serveur 
 * 
 * 
 * Elle va également permettre de fixer une limite sur  le nombre de connexion 
 * 
 * 
 * Création d'un thread à chaque connexion qui va s'occuper de l'attente de la
 * terminaison du calcul
 * 
 * 
 * 
 * 
 * 
 */


public class MainServer {
	final static int MAX_CONNEXION=50;
	static final String pathStop = "stopwords.txt";

	//args[0]=port args[1]=file

	public static void main(String[] args) {
		//Serveur max priorité
		if(args.length!=0){
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			System.out.println("Taille args"+args.length);
			int port=Integer.parseInt(args[0]);
			//int port=10000;
			ServerSocket serverSocket;
			int nbconnexion=0;


			System.out.println("#########Création du ServerSocket ###########");
			try {

				try{
					try{
						serverSocket = new ServerSocket(port,MAX_CONNEXION);

						System.out.println("Socket Server"+serverSocket);
						Runtime.getRuntime().addShutdownHook(new Thread(""+nbconnexion) {
							public void run() { /*
			       my shutdown code here
							 */
								System.out.println("#########Fermeture du ServerSocket inattendu ###########");
								try {
									System.out.println("#########Fermeture du ServerSocket ###########");
									serverSocket.close();
									System.out.println("Nombre de clients connecté: "+this.getName());
									Thread.currentThread().interrupt();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}

							}
						});
						Index i1 = new Index();
						//Initialisation du keywords et documents
						i1.keywords=i1.loadVocabulary();
						i1.documents=i1.loadDocuments();
						Set<String> stop_words=getStopWord();
						while(true && nbconnexion<MAX_CONNEXION){
							try{
								Thread_server t1=new Thread_server(serverSocket.accept(),i1.keywords,i1.documents,stop_words);
								nbconnexion++;
								System.out.println("Nb Connexion"+nbconnexion);
							} catch(SocketException e){
								//System.out.println(e);

							}

						}
						if(!serverSocket.isClosed()){
							System.out.println("#########Fermeture du ServerSocket ###########");
							serverSocket.close();
							System.out.println("Nombre de client connecté "+nbconnexion);
						}
						Thread.currentThread().interrupt();
					}catch(IllegalArgumentException e){
						System.out.println("Numéro du port doit etre compris entre 0 et 65535");
					}
				}catch(BindException e){
					System.out.println("Port déja utilisé");
				}


			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}
		else{
			System.out.println("Vous devez passer  le port  en arguments");
		}

	}


	public static TreeSet<String> getStopWord(){
		File fileTemp = new File(pathStop);
		if (!fileTemp.exists()) {
			System.out.println("Fichier n'existe pas ");
		}

		FileInputStream f_in;
		TreeSet<String> stopwords=new TreeSet<String>();
		try {
			f_in = new FileInputStream(pathStop);

			BufferedReader reader = new BufferedReader(new InputStreamReader(f_in));
			System.out.println("Loading stopwords..");

			try {
				String line=reader.readLine();

				do{
					stopwords.add(line.trim());
					line=reader.readLine();
				}while(line!=null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Loading finished");
		return stopwords;


	}
}
