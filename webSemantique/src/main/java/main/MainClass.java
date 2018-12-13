package main;

import java.io.IOException;
import java.util.ArrayList;

import parsing.NLPAnalyser;
import dbpedia.DBpedia;

public class MainClass {

	/**
	 * Méthode pricipale de l'application
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// Requête vers DBpedia
		NLPAnalyser analyser = new NLPAnalyser();
		String query = "Where is the Eiffel Tower?";
		DBpedia db = new DBpedia(analyser);
		ArrayList<String> results = db.request(query);
		String affichage = (results.isEmpty()) ? "\nPas de réponse à cette requête" : "\nResponses : "+showResponse(results);
		System.out.println(affichage);
		
	}	
	
	/**
	 * Méthode qui permetde visualiser une liste de résultats obtenues suite à une requête
	 * @param responses liste des réponses
	 * @return une Chaîne de caractère
	 */
	private static String showResponse(ArrayList<String> responses) {
		String affichage = "";
		for (String str : responses) {
			affichage += "\n"+ str;
		}
		return affichage;
	}

}
