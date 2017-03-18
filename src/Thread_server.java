import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import searchEngine.Document;
import searchEngine.Keyword;

public class Thread_server extends Thread {
	/*
	 * 
	 * Thread server va s'occuper d'envoyer les données et de récuperer le résultat
	 * 
	 * 
	 */

	private Socket connexion;
	private TreeMap<String, Keyword> keywords;
	private TreeMap<Integer, Document> documents;
	private Set<String> stop_words;





	public Thread_server(Socket connexion, TreeMap<String, Keyword> keywords, TreeMap<Integer, Document> documents, Set<String> stop_words) {
		super();
		this.connexion = connexion;
		this.keywords = keywords;
		this.documents = documents;
		this.stop_words=stop_words;
		this.start();
	}





	@Override 
	public void run(){
		try {
			try{

				System.out.println("Adresse ip du Client connecté "+this.connexion.getInetAddress());
				Scanner sc = new Scanner(System.in);
				String saisie;
				System.out.print("Veuillez saisir votre requete:");
				saisie=sc.nextLine();
				//Data test = new Data("Test", 1, (float) 2.5);
				ObjectOutputStream out = new ObjectOutputStream(connexion.getOutputStream());
				ObjectInputStream FromClient = new ObjectInputStream(connexion.getInputStream());
				//String msg=FromClient.readLine();
				out.writeObject(keywords);
				out.writeObject(documents);
				out.writeObject(stop_words);
				out.writeObject(saisie);
				try {
					Object msg= FromClient.readObject();
					System.out.println(msg.getClass());
					if(msg.getClass().equals(HashMap.class)){
						System.out.println("Type Attendu");
						System.out.println();
						HashMap<Integer, Double> classement=(HashMap<Integer, Double>) msg;
						System.out.println("Classement:\n"+classement);
					}else{
						System.out.println("Problème objet inattendu");
						System.exit(-1);
					}

					String message = (String) FromClient.readObject();

					System.out.println("Message recu"+message);

					out.flush();
				}catch (SocketException e){
					System.out.println("Connexion interrompue"+e);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
