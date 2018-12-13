package dbpediaTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import dbpedia.DBpedia;
import parsing.NLPAnalyser;

/**
 * Classe de test de la classe servant à faire des requête vers DBpedia
 * @author niang
 *
 */
public class DBpediaTest {

	@Test
	public void test() throws MalformedURLException, IOException {
		NLPAnalyser analyser = new NLPAnalyser();
		DBpedia bdpediaTest = new DBpedia(analyser);
		
		ArrayList<String> output_Qui = bdpediaTest.request("Qui est Barack Obama?");
		ArrayList<String> expect_Qui = new ArrayList<>(Arrays.asList("Barack Hussein Obama II, né le 4 août 1961 à Honolulu (Hawaï), est un homme d'État américain. Il est le 44e et actuel président des États-Unis, élu pour un premier mandat le 4 novembre 2008 et réélu pour un second le 6 novembre 2012."));
		assertEquals(expect_Qui,output_Qui);
	
		ArrayList<String> output_Ou = bdpediaTest.request("Où est le Tour Eiffel?");
		System.out.println(output_Ou);
		ArrayList<String> expect_Ou = new ArrayList<>(Arrays.asList("Paris","France","7e arrondissement de Paris"));
		assertEquals(expect_Ou,output_Ou);

		ArrayList<String> output_what = bdpediaTest.request("What is the capital of Russia?");
		ArrayList<String> expect_what = new ArrayList<>(Arrays.asList("Moscow"));
		assertEquals(expect_what,output_what);
		
	}

}
