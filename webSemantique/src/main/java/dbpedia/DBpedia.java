package dbpedia;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import parsing.NLPAnalyser;


/**
 * Classe qui gère la connexion et la récupération de données chez DBpedia
 * @author niang
 *
 */
public class DBpedia {
	
	/**
	 * Attribut qui contient l'analyseur des questions
	 */
	protected NLPAnalyser analyser;
	
	/**
	 * Constructeur de la classe
	 * @param analyser
	 */
	public DBpedia(NLPAnalyser analyser) {
		this.analyser = analyser;
	}
	
	/**
	 * Méthode qui constitue la requête SPARQL d'un question et la transforme en url de requête vers DBpedia
	 * @param question la question en langage naturelle
	 * @return un url
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
    private URL getUrl(String question) throws UnsupportedEncodingException, MalformedURLException {
    	String query = analyser.getQuery(question);
    	System.out.println(query);
        String graphEncoded = URLEncoder.encode("http://dbpedia.org", "UTF-8");
        String formatEncoded = URLEncoder.encode("application/sparql-results+json", "UTF-8");
        String queryEncoded = URLEncoder.encode(query, "UTF-8");
        String url = "http://dbpedia.org/sparql?default-graph-uri="+graphEncoded+"&query="+queryEncoded+"&format="+formatEncoded+"&debug=on&timeout=";
        return new URL(url);
    }
    
    /**
     * Méthode qui permet de requêter la base de données de DBpedia
     * @param question en langage naturelle
     * @return la réponse obtenue sous forme de tableau de chaînes de caractères
     * @throws MalformedURLException
     * @throws IOException
     */
    public ArrayList<String> request(String question) throws MalformedURLException, IOException {
    	URL url = getUrl(question);
    	HttpURLConnection httpcon = (HttpURLConnection) ((url.openConnection()));
    	httpcon.setDoOutput(true);
    	httpcon.setRequestMethod("POST");
    	httpcon.connect();
    	int status = httpcon.getResponseCode();
    	ArrayList<String> response = new ArrayList<>();
    	switch (status) {
        	case 200:
        	case 201:
        		BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                //System.out.println(sb.toString());
                response = getResponseValues(sb);
        		break;
        	default :
        		response.add(Integer.toString(status));
    	}
   	
    	return response;
    }
    
    /**
     * Méthode qui permet de transformer la réponse d'une requête qui est sous forme de 
     * StringBuilder en un tableau des différentes valeurs de la réponse.
     * @param sb constructeur dela chaîne de caractère.
     * @return la réponse sous forma de chaîne de caractère
     */
    private ArrayList<String> getResponseValues(StringBuilder sb) {
    	ArrayList<String> reponseValue = new ArrayList<>();
    	JSONObject json = new JSONObject(sb.toString());
        JSONObject results = (JSONObject) json.get("results");
        JSONArray bindings = (JSONArray) results.get("bindings");
        for (Object response : bindings) {
			JSONObject reponseObjet = new JSONObject(response.toString());
			JSONObject reponse = reponseObjet.getJSONObject("reponse");
			reponseValue.add((String) reponse.get("value"));
		}
        return reponseValue;
    }
    
}