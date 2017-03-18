import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import searchEngine.Document;
import searchEngine.Keyword;
import searchEngine.Test;


/*
 * 
 * Cette Classe Client qui va permettre la connexion au serveur
 * Son but c'est d'initialiser la connexion et on a pensé à ça pour éviter  
 *  trop de résponsabilité dans la classe Handle  c'est à dire 
 * d'initaliser la connexion  et de faire le calcul
 * 
 * 
 * 
 */

public class Client {
	private Socket connexion;
	private ObjectOutputStream toServer ;
	private ObjectInputStream fromServer;


	public Client(int port,String adressIP){
		try {
			this.connexion=new Socket(adressIP, port);
			try {
				toServer = new ObjectOutputStream(this.connexion.getOutputStream());
				fromServer = new ObjectInputStream(this.connexion.getInputStream());
				System.out.println("OK!");
				Object obj;
				try {
					obj = fromServer.readObject();
					if(obj.getClass().equals(TreeMap.class)){
						TreeMap<String,Keyword> keywords=(TreeMap<String,Keyword>) obj;
						System.out.println("obj");
						Object obj1;
						obj1=fromServer.readObject();
						System.out.println("obj1");
						if(obj1.getClass().equals(TreeMap.class)){
							TreeMap<Integer, Document> documents=(TreeMap<Integer, Document>)obj1	;
							Object obj2=fromServer.readObject();
							System.out.println("obj2"+obj2.getClass());
							if(obj2.getClass().equals(TreeSet.class)){
								Set<String>stop_words=(Set<String>)(obj2);
								Object obj3=fromServer.readObject();
								System.out.println("Object 3:"+obj3.getClass());

								if(obj3.getClass().equals(String.class)){
									String req=(String)(obj3);	
									System.out.println("obj3");
									System.out.println("Requete recu:"+req);
									System.out.println("TEST STOP_WORDS==null\t:"+stop_words.size());
									Test essai=new Test(keywords, documents, stop_words);

									HashMap<Integer, Double> classement=essai.calcul(req);

									toServer.writeObject(classement);
									String msg="FIN";
									toServer.writeObject(msg);
									toServer.flush();

									this.closeConnexion();
								}else{
									System.out.println("Object inattendu ");
								}
							}
						}
					}

				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ConnectException e) {
			System.out.println("Problème lors de la connexion");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated catch block




	}



	public Socket getSocket(){
		return this.connexion;
	}



	public void closeConnexion(){
		try {
			this.connexion.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
