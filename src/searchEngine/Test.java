package searchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Test {
	static final String pathStop = "stopwords.txt";
	//private static Index i1;
	private static TreeMap<String, Keyword> keywords;
	private static TreeMap<Integer, Document> documents;
	private    Set<String> stop_word;


	public Test(TreeMap<String, Keyword> keywords,TreeMap<Integer,Document> documents,Set<String> stop_word){
		this.keywords=keywords;
		this.documents=documents;
		this.stop_word=stop_word;
		
	}


	public HashMap<Integer, Double> calcul(String saisie){
		return modele_vectoriel( saisie);

	}




	public  HashMap<Integer, Double> modele_vectoriel(String saisie){

		if(saisie.length()==0){
			System.out.println("Saisie vide!");
			return null;
		}

		TreeMap<Integer, TreeMap<String,Double> > frequences_mot =new TreeMap<Integer, TreeMap<String,Double>>() ;
		//Stocker les termes de la requete
		Set<String>  req= new HashSet<String>();
		//Recuperer les id des documents
		Set<Integer> all_id=documents.keySet();

		Iterator<Integer> id_iterator=all_id.iterator();
		saisie=saisie.toLowerCase();
		//Enleve les espaces multiples ainsi que plusieurs caractères
		Collections.addAll(req, saisie.trim().replaceAll("[.,!;:\n\t\r]", " ").replace("["," ").replace("]"," ").replaceAll(" +", " ").split(" "));
		req.remove("");
		req.remove(" ");
		req.remove("-");
		//Suppression des stopWords
		//System.out.println("Stop_words"+stop_word==null);
		
		req.removeAll(this.stop_word);


		// Affichage de la requete 
		System.out.println("requete apres suppression des stop words ainsi que les espaces "+req);
		while(id_iterator.hasNext()){
			Integer id=id_iterator.next();
			TreeMap<String,Double> frequence_mot_doc=new TreeMap<String,Double>();
			Iterator<String>req_iterator=req.iterator();
			while(req_iterator.hasNext()){
				String mot=req_iterator.next();
				// Recuperer la frequence du mot dans un document 
				Double freq=documents.get(id).get1Freq(mot);
				if(freq==null){
					freq=0.0;
				}
				frequence_mot_doc.put(mot, freq);
			}
			frequences_mot.put(id, frequence_mot_doc);
		}

		HashMap<Integer,Double> classement = new HashMap<Integer,Double>();
		id_iterator=all_id.iterator();
		while(id_iterator.hasNext()){
			Integer current_id=id_iterator.next();
			//Iterator<String>ens_mot_iterator=ens_mot.iterator();
			Double produit=0.0;
			Double somme_poids=0.0;
			Iterator<String>req_iterator=req.iterator();
			//Calcul la somme des frequences ainsi que la somme des frequences carré qui vont servir pour le calcul du cos
			while(req_iterator.hasNext()){
				String current_mot=req_iterator.next();
				//id donnée,mot -> FRequence
				Double freq=frequences_mot.get(current_id).get(current_mot);
				if(freq!=0.0){
					somme_poids+=freq;
					produit+=freq*freq;
				}
			}
			//Valeur cos 
			Double valeur_classement;
			if(somme_poids!=0.0){
				valeur_classement=somme_poids/(Math.sqrt((produit))*Math.sqrt(req.size()*1.0));
				classement.put(current_id,valeur_classement);
			}
		}

		System.out.println("Classement fini!");

		return classement;

	}
	//Lecture des stopWord






}
