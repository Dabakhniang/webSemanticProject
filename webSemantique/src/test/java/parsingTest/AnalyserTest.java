package parsingTest;

import static org.junit.Assert.*;

import org.junit.Test;

import parsing.NLPAnalyser;

/**
 * Classe pour faire des tests sur la classe Analyser
 * @author niang
 *
 */
public class AnalyserTest {

	@Test
	public void test() {
		NLPAnalyser analyserTest  = new NLPAnalyser();
		String output_Qui = analyserTest.getQuery("Qui est Barack Obama?");	
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE {\n" + 
				"?person rdfs:label \"Barack Obama\"@fr.\n" + 
				"?person a dbo:Person.\n" + 
				"?person rdfs:comment ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"fr\")\n" + 
				"}",
				output_Qui
				);
		
		String output_Ou = analyserTest.getQuery("Où est Paris?");
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE { \n" + 
				"?x rdfs:label \"Paris\"@fr.\n" + 
				"{ ?x dbo:country ?location } UNION { ?x dbo:birthPlace/dbo:country ?location } UNION { ?x dbo:location ?location }.\n" + 
				"?location rdfs:label ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"fr\")\n" + 
				"}",
				output_Ou
				);
		
		String output_QuEstCeQue = analyserTest.getQuery("Qu'est-ce qu'un chat?");
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE {\n" + 
				"?thing rdfs:label \"chat\"@fr.\n" + 
				"?thing rdfs:comment ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"fr\")\n" + 
				"}",
				output_QuEstCeQue
				);
		String output_Quelle = analyserTest.getQuery("Quelle est la capitale de la Russie?");		
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE {\n" + 
				"?complement a dbo:Country.\n" + 
				"?complement rdfs:label \"Russie\"@fr.\n" + 
				"?complement dbo:capital ?thing.\n" + 
				"?thing rdfs:label ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"fr\")\n" + 
				"}",
				output_Quelle
				);
		
		String output_Who = analyserTest.getQuery("Who is the president of Italia?");
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE { \n" + 
				"?complement rdfs:label \"Italia\"@en. \n" + 
				"?complement dbo:leader ?person.\n" + 
				"?person a dbo:Person.\n" + 
				"?person rdfs:label ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"en\")\n" + 
				"}",
				output_Who
				);
		String output_Where = analyserTest.getQuery("Where is the Eiffel Tower?");
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE { ?x rdfs:label \"Eiffel Tower\"@en.\n" + 
				" { ?x dbo:country ?location } UNION { ?x dbo:birthPlace/dbo:country ?location } UNION { ?x dbo:location ?location }.\n" + 
				"?location rdfs:label ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"en\")\n" + 
				"}",
				output_Where
				);
		String output_What = analyserTest.getQuery("What is the capital of Russia?");
		assertEquals(
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX : <http://dbpedia.org/resource/>\n" + 
				"PREFIX dbp: <http://dbpedia.org/property/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" + 
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" + 
				"SELECT ?reponse\n" + 
				"WHERE {\n" + 
				"?complement a dbo:Country.\n" + 
				"?complement rdfs:label \"Russia\"@en.\n" + 
				"?complement dbo:capital ?entity.\n" + 
				"?entity rdfs:label ?reponse.\n" + 
				"FILTER(LANG(?reponse) = \"en\")\n" + 
				"}",
				output_What
				);
	}

}
