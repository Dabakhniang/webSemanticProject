package parsing;



import java.util.ArrayList;
import java.util.Arrays;

import org.omg.PortableServer.ImplicitActivationPolicyOperations;

public class NLPAnalyser {
	
	/**
	 * Attribut qui va contenir le graphe après l'analyse
	 */
	protected String prefixes;
	
	public NLPAnalyser() {
		prefixes = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			     + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			     + "PREFIX : <http://dbpedia.org/resource/>\n"
			     + "PREFIX dbp: <http://dbpedia.org/property/>\n" 
			     + "PREFIX dbo: <http://dbpedia.org/ontology/>\n" 
			     + "PREFIX dbr: <http://dbpedia.org/resource/>\n";
	}
	
	
    /**
    * * Méthode qui transforme une question en Sparql
    * *
    * @param question
    * @return la requête SPARQL sous forme de chaîne de caractère
    */
    public String getQuery(String question){
        // On enlève le point virgule.
        question = question.replace("?", "");
        question = question.replace("What's", "What is");
        question = question.replace("Who's", "Who is");
        // On tokenise la question.
        String[] terms = question.split(" ",3);
        // On récupère le premier mot de la question
        String qType = terms[0];
        String sparqlQ = null;
        // Si la question est composé que de trois mots.
        switch (qType) {
        // Si le premier mot de la question est Qui
        case "Qui":
        	// S'il n'y a pas de "de" dans la Target
        	if(!terms[2].contains(" de ") && !terms[2].contains(" du ") && !terms[2].contains(" des ")) {
        		sparqlQ = "SELECT ?reponse\n"
        				+ "WHERE {\n"
        				+ "?person rdfs:label \""+terms[2]+"\"@fr.\n"
        				+ "?person a dbo:Person.\n"
        				+ "?person rdfs:comment ?reponse.\n"
        				+ "FILTER(LANG(?reponse) = \"fr\")\n"
        				+ "}";
        	}else {	
        		// S'il y a de "de" dans la Target
        		String word = explodeString(terms[2])[0];
        		String complement = explodeString(terms[2])[1];
                String property = getDBpediaProperty(word)[0];
                String targetType = getDBpediaProperty(word)[1];
	        	sparqlQ = "SELECT ?reponse\n"
	        			+ "WHERE {\n"
	        			+ "?complement rdfs:label \""+complement+"\"@fr.\n"
	        			+ "?complement "+property+" ?person.\n"
	        			+ "?person rdf:type dbo:Person.\n"
	        			+ "?person rdfs:label ?reponse.\n"
	        			+ "FILTER(LANG(?reponse) = \"fr\")\n"
	        			+ "}";                               
        	}
        	break;
        // Si le premier mot de la question est Qu'est-ce
        case "Qu'est-ce":
            String target = takeDownArticles(terms[2]);
            sparqlQ =  "SELECT ?reponse\n"
                     + "WHERE {\n"
                     + "?thing rdfs:label \""+target+"\"@fr.\n"
                     + "?thing rdfs:comment ?reponse.\n"
                     + "FILTER(LANG(?reponse) = \"fr\")\n"
                     + "}";
            break;
        // Si le premier mot de la question est Où
        case "Ou":
        case "Où":
        	String word = terms[2];
    		word  = takeDownArticles(word);
        	if( !terms[1].equals("se")) {
        		sparqlQ = "SELECT ?reponse\n"
                        +"WHERE { \n"
                        + "?x rdfs:label \""+word+"\"@fr.\n"
                        + "{ ?x dbo:country ?location } "
                        + "UNION "
            			+ "{ ?x dbo:birthPlace/dbo:country ?location } "
            			+ "UNION "
            			+ "{ ?x dbo:location ?location }.\n"
                        + "?location rdfs:label ?reponse.\n"
                        + "FILTER(LANG(?reponse) = \"fr\")\n"
                        + "}";
        	}else {
        		word = terms[2].replace("trouve ", "");
        		word  = takeDownArticles(word);
        		sparqlQ = "SELECT ?reponse\n "
        				+ "WHERE { \n"
        				+ "?x rdfs:label \""+word+"\"@fr.\n"
                        + "{ ?x dbo:country ?location } "
                        + "UNION "
            			+ "{ ?x dbo:birthPlace/dbo:country ?location } "
            			+ "UNION "
            			+ "{ ?x dbo:location ?location }.\n"
        				+ "?location rdfs:label ?reponse.\n"
        				+ "FILTER(LANG(?reponse) = \"fr\")\n"
        				+ "}";
        	}
        	break;
        // Si le premier mot de la question est Quelle ou Quel
        case "Quelle":     	
        case "Quel":
        	String word1 = explodeString(terms[2])[0];
    		String genitive = explodeString(terms[2])[1];
            String property = getDBpediaProperty(word1)[0];
            String targetType = getDBpediaProperty(word1)[1];
            sparqlQ = "SELECT ?reponse\n"
                	+ "WHERE {\n"
                    + "?complement a "+targetType+".\n"
                    + "?complement rdfs:label \""+genitive+"\"@fr.\n"
                    + "?complement "+property+" ?thing.\n"
                    + "?thing rdfs:label ?reponse.\n"
                    + "FILTER(LANG(?reponse) = \"fr\")\n"
                    + "}";	
        	break;
       	// Si le premier mot de la question est Where
        case "Where":
        	String targetW = terms[2];
        	targetW  = takeDownArticles(targetW);
        	sparqlQ = "SELECT ?reponse\n"
        			+ "WHERE { ?x rdfs:label \""+targetW+"\"@en.\n "
        			+ "{ ?x dbo:country ?location } "
        			+ "UNION "
        			+ "{ ?x dbo:birthPlace/dbo:country ?location } "
        			+ "UNION "
        			+ "{ ?x dbo:location ?location }.\n"
        			+ "?location rdfs:label ?reponse.\n"
                    + "FILTER(LANG(?reponse) = \"en\")\n"
        			+ "}";
        	break;
        // Si le premier mot de la question est Who
        case "Who":
        	if(!terms[2].contains("of") && !terms[2].contains("'s")) {
        		sparqlQ = "SELECT ?reponse\n"
        				+ "WHERE {\n"
        				+ "?person rdfs:label \""+terms[2]+"\"@en.\n"
        				+ "?person a dbo:Person.\n"
        				+ "?person rdfs:comment ?reponse.\n"
        				+ "FILTER(LANG(?reponse) = \"en\")\n"
        				+ " }";
        	}else{
        		String word2 = explodeString(terms[2])[0];
        		String genetive = explodeString(terms[2])[1];
                        String property1 = getDBpediaProperty(word2)[0];
        		// S'il y a de "de" dans la Target
	        	sparqlQ = "SELECT ?reponse\n"
	        			+ "WHERE { \n"
	        			+ "?complement rdfs:label \""+genetive+"\"@en. \n"
	        			+ "?complement "+property1+" ?person.\n"
                        + "?person a dbo:Person.\n"
                        + "?person rdfs:label ?reponse.\n"
                        + "FILTER(LANG(?reponse) = \"en\")\n"
                        + "}";
	        			
        	}
        	break;
        // Si le premier mot de la question est What
        case "What":
        	if(!terms[2].contains("of") && !terms[2].contains("'s")) {
                    String target1 = takeDownArticles(terms[2]);
                    sparqlQ = "SELECT ?reponse\n"
                    		+ "WHERE {\n"
                    		+ "?thing rdfs:label \""+target1+"\"@en.\n"
                    		+ "?thing rdfs:comment ?reponse.\n"
                    		+ "FILTER(LANG(?reponse) = \"en\")\n"
                    		+ "}";
        	}else{
                    String word3 = explodeString(terms[2])[0];
                    String genitive1 = explodeString(terms[2])[1];
                    String property2 = getDBpediaProperty(word3)[0];
                    String targetType1 = getDBpediaProperty(word3)[1];
                    sparqlQ = "SELECT ?reponse\n"
                    		+ "WHERE {\n"
                    		+ "?complement a "+targetType1+".\n"
                    		+ "?complement rdfs:label \""+genitive1+"\"@en.\n"
                    		+ "?complement "+property2+" ?entity.\n"
                    	    + "?entity rdfs:label ?reponse.\n"
                    	    + "FILTER(LANG(?reponse) = \"en\")\n"
                    		+ "}";
        	}
        	break;
        default: sparqlQ = "";
        
        
        }
        
        return prefixes+sparqlQ;
    }
    
    
    private String[] explodeString(String chaine) {
    	String[] tableau = new String[2];
    	chaine = takeDownArticles(chaine);
    	chaine = chaine.replace("l'", "");
    	if( !chaine.contains("'s")) {
	    	tableau = chaine.split(" ");
    	}else {
    		chaine = chaine.replace("'s", "");
	    	tableau[0] = chaine.split(" ")[1];
	    	tableau[1] = chaine.split(" ")[0];
		}
    	return tableau;
    }
    
    private String takeDownArticles(String str) {
    	String[] tokens = str.split(" ");
    	ArrayList<String> result = new ArrayList<>();
    	ArrayList<String> articles = new ArrayList<>(Arrays.asList("le","la","de","du","des","les","un","une","the","of","a"));
    	for(String word:tokens) {
    		if(! articles.contains(word)) {
    			result.add(word);
    		}
    	}
    	return String.join(" ", result);
    }

    /**
     * Méthode qui permet determiner la proprité DBpedia qui correspond à un mot donné.
     * @param str
     * @return
     */
    private String[] getDBpediaProperty(String str){
        String property = "?inconnuPred";
        String targetType = null;
        switch (str){
            case "population":
                property = "dbp:populationCensus";
                targetType = "dbo:Country";
                break;
            case "capitale":
            case "capital":
                property = "dbo:capital";
                targetType = "dbo:Country";
                break;
            case "superficie":
            case "surface":
                property = "dbo:areaTotal";
                targetType = "dbo:Country";
                break;
            case "président":
            case "présidente":
            case "president":
                property = "dbo:leader";
                targetType = "dbo:Person";
                break;
            
        }
        return new String[]{property,targetType};
    }
}