package verifica;

import gui.JMainFrame;
import gui.JResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import parser.ParseException;
import parser.Parser;
import bean.Node;

/**
 * Classe principale che esegue l'applicazione.
 * 
 * @author Federico De Meo
 *
 */
public class Main {

  /**
   * Identifica il grafo
   * */
  private static HashMap<String, Node> Graph;
  /**
   * Lista di letterali uguali
   * */
  private static ArrayList<String> equals;
  /**
   * Lista di letterali non uguali
   * */
  private static ArrayList<String> noEquals;
  /**
   * Lista dei cons da mergiare
   * */
  private static ArrayList<String> consList;
  /**
   * Lista degli atoms da controllare
   * */
  private static ArrayList<String> atoms;
  /**
   * Lista degli atoms da controllare
   * */
  private static ArrayList<String> consfn;
  /**
   * 
   */
  private static Integer edges = 0;
  /**
   * Metodo principale che avvia tutto il programma sia da GUI che da
   * command-line.
   * 
   * @param args
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws UnsupportedLookAndFeelException
   */
  public static void main(String args[]) throws ClassNotFoundException,
	  InstantiationException, IllegalAccessException,
	  UnsupportedLookAndFeelException {

	if (args.length < 1) {
	  System.out
		  .println("Usage:\t cc.jar -h <filename or formula> \t(to execute from command line)\n\t cc.jar -gui \t\t(to execute from gui)");
	  System.exit(-1);
	}

	/* devo usare le euristiche in command line */
	if (args[0].equals("-h")) {
	  Config.algorithm = 0;
	  commandLine(args[1]);
	} else if (args[0].equals("-gui")) {
	  /* devo eseguire la GUI */
	  execGui();
	} else {
	  /* devo eseguire l'algoritmo senza euristiche */
	  Config.algorithm = 1;
	  commandLine(args[0]);
	}

  }

  private static void commandLine(String file) {
	StringBuilder input = new StringBuilder();
	try {
	  FileReader reader = new FileReader(file);
	  Scanner in = new Scanner(reader);
	  while (in.hasNextLine())
		input.append(in.nextLine());

	  reader.close();
	  in.close();
	} catch (FileNotFoundException e) {
	  /* se non è un file forse è una formula */
	  input.append(file);
	} catch (IOException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	  System.exit(-1);
	}

	long tempo = 0;
	int nTest = Config.nTest;
	boolean result = false;
	long inizio = System.currentTimeMillis();
	try {
	  result = execCC(input.toString(), null);
	} catch (ParseException e) {
	  System.out.print("Syntax Error");
	  e.printStackTrace();
	  System.exit(-1);
	} catch (Throwable e) {
	  result = false;
	}
	long fine = System.currentTimeMillis();
	tempo += (fine - inizio);
	int tCorrS = (int) tempo / nTest;
	
	System.out.print(Graph.size());
	int edges = 0;
	for(Node n: Graph.values()){
	  edges += n.getArgs().size();
	}
	System.out.print(";"+edges);
	if (result)
	  System.out.print(";Soddisfacibile;");
	else
	  System.out.print(";Non Soddisfacibile;");
	System.out.println(tCorrS);

	/* esporta le classi di congruenza */
//	try {
//	  PrintWriter writer = new PrintWriter("/Users/demeof/Desktop/Verifica/congruence_classes.txt");
//	  writer.print(exportCC());
//	  writer.close();
//	} catch (FileNotFoundException e) {
//	  // TODO Auto-generated catch block
//	  e.printStackTrace();
//	}
//	System.out.print("stampato");
  }

  private static void execGui() {
	/* ESECUZIONE TRAMITE GUI */
	JFrame.setDefaultLookAndFeelDecorated(true);
	JDialog.setDefaultLookAndFeelDecorated(true);
	try {
	  UIManager
		  .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (ClassNotFoundException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (InstantiationException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (IllegalAccessException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}

	// avvia l'interfaccia

	SwingUtilities.invokeLater(new Runnable() {

	  @Override
	  public void run() {
		new JMainFrame().setVisible(true);
	  }
	});
  }


  /**
   * Metodo che esegue l'algoritmo di chiusura di congruenza.
   * 
   * @param formula di cui verificare la validità
   * @return <pre>true</pre> se la formula è valida, <pre>false</pre> altrimenti
   * @throws ParseException in caso di errori di sintassi nella formula
   */
  public static boolean execCC(String formula, JResult window)
	  throws ParseException {
	Graph = new HashMap<String, Node>();
	equals = new ArrayList<String>();
	noEquals = new ArrayList<String>();
	consList = new ArrayList<String>();
	atoms = new ArrayList<String>();
	consfn = new ArrayList<String>();

	new Parser(formula, Graph, equals, noEquals, consList, atoms, consfn);
	
	// Dopo il parser, setto il risultato nella finestra jResult se ho la GUI
	if( window != null ){
	  window.setParsingResult(Graph.size());
	}
	switch (Config.algorithm) {
	case 0:
	  try {
		return NelsonOppenSpeedUp(formula);
	  } catch (Exception e) {
		// Se raccolgo l'eccezione significa che la formula è insoddisfacibile
		return false;
	  }
	case 1:
	  return NelsonOppen(formula);
	}

	return true;
  }

  /**
   * Exports a representation of this dag in the form of the
   * congruence classes formed by merges.
   * Every class contains its terms in lexicographic order,
   * and is separated from the next by an empty line.
   * The classes appear ordered by their first term.
   *
   * @return a string containing the congruence classes
   */
  public static String exportCC() {
      Map<String,ArrayList<String>> cc = new HashMap<String, ArrayList<String>>();

      /* separate nodes by congruence class */
      for( Node n : Graph.values() ) {
          String representative = findH(n.getId());
          ArrayList<String> cclass = cc.get(representative);
          if( cclass == null ) {
              cclass = new ArrayList<String>();
              cc.put(representative, cclass);
          }
          cclass.add(n.getId());
      }

      /* sort every class */
      for( ArrayList<String> cclass : cc.values() ) {
          Collections.sort(cclass);
      }

      /* copy classes in an ArrayList... */
      ArrayList<ArrayList<String>> cclasses = new ArrayList<ArrayList<String>>(cc.values());
      /* ... and sort them comparing their first term */
      Collections.sort(cclasses, new Comparator<ArrayList<String>>() {
          public int compare(ArrayList<String> c1, ArrayList<String> c2) {
              return c1.get(0).compareTo(c2.get(0));
          }
      });

      /* build the final string */
      StringBuilder s = new StringBuilder();

      for( ArrayList<String> cclass : cclasses ) {
          for (String term : cclass) {
              s.append(term);
              s.append("\n");
          }
          s.append("\n");
      }
      
      return s.toString();
  }

  
  /**
   * Metodo che esegue l'algoritmo del libro e applica le seguenti euristiche:
   * compressione dei cammini, unione per rango, decisione di insoddisfacibilità
   * quando si sta unendo due nodi che non possono stare nella stessa classe.
   * 
   * @param formula di cui verificare la validità
   * @return <pre>true</pre> se la formula è valida, <pre>false</pre> altrimenti
   * @throws ParseException in caso di errori di sintassi nella formula
   * @throws Exception per soddisfare l'ultima euristica in lista
   */
  private static boolean NelsonOppenSpeedUp(String formula) throws Exception {
	boolean result;
	for (int i = 0; i < consList.size(); i = i + 2) {
	  mergeH(consList.get(i), consList.get(i + 1));
	}
	for (int i = 0; i < equals.size(); i = i + 2) {
	  mergeH(equals.get(i), equals.get(i + 1));
	}
	result = checkAtomH() && checkNoEqualsH();
	return result;
  }

  /**
   * Meto che esegue l'algoritmo del libro senza euristiche ulteriori.
   * 
   * @param formula di cui verificare la validità
   * @return <pre>true</pre> se la formula è valida,<pre>false</pre> altrimenti
   * @throws ParseException in caso di errori di sintassi nella formula
   */
  private static boolean NelsonOppen(String formula) throws ParseException {
	boolean result;
	for (int i = 0; i < consList.size(); i = i + 2) {
	  _merge(consList.get(i), consList.get(i + 1));
	}
	for (int i = 0; i < equals.size(); i = i + 2) {
	  _merge(equals.get(i), equals.get(i + 1));
	}
	result = _checkAtom() && _checkNoEquals();
	return result;
  }

  /**
   * Metodo per prelevare un nodo dal grafo, uguale per entrambi gli algoritmi.
   * 
   * @param id
   *          del nodo da prelevare
   * @return Node che identifica il nodo del grafo
   */
  private static Node node(String id) {
	return (Node) Graph.get(id);
  }

  /* METODI DI SUPPORTO CON EURISTICHE */

  /**
   * Metodo che restituisce il rappresentante della classe a cui appartiene il
   * nodo id, implementa la compressione dei cammini.
   * 
   * @param id del nodo di cui restituire la classe
   */
  private static String findH(String id) {
	Node n = node(id);
	if (!id.equals(n.getFind())) {
	  n.setFind(findH(n.getFind()));
	}
	return n.getFind();
  }

  /**
   * Metodo che imprementa l'unione di classi di congruenza con l'euristica
   * dell'unione per rango e controlla se due classi possono essere unite
   * controllando la lista dei forbidden del nodo.
   * 
   * @param id1 primo nodo da unire
   * @param id2 secondo nodo da unire
   * @throws Exception nel caso in cui due nodi non possano essere uniti.
   */
  private static void unionH(String id1, String id2) throws Exception {
	// System.out.println("Inizio node");
	Node n1 = node(findH(id1));
	Node n2 = node(findH(id2));
	if (n1.getRank() > n2.getRank())
	  link(n1, n2);
	else {
	  link(n2, n1);
	  if (n1.getRank() == n2.getRank())
		n2.setRank(n2.getRank() + 1);
	}
	// System.out.println("Fine link");
  }

  private static void link(Node n1, Node n2) {
	/* n1 diventa rappresentante */
	n2.setFind(n1.getFind());
	// System.out.println("Faccio forbidden");
	n1.getCcparent().addAll(n2.getCcparent());
	n2.setCcparent(new HashSet<String>());

	n1.getForbidden().addAll(n2.getForbidden());
	n2.setForbidden(new HashSet<String>());
	// System.out.println("Faccio merge");

  }

  /**
   * Metodo che restituisce la lista dei padri di un nodo.
   * 
   * @param id
   *          nodo di cui sapere i padri
   * @return HashSet con l'insieme dei nodi
   */
  private static HashSet<String> ccparentH(String id) {
	return (node(findH(id))).getCcparent();
  }

  /**
   * Metodo che verifica se due nodi sono congruenti per essere uniti.
   * 
   * @param id1 nodo da controllare
   * @param id2 nodo da controllare
   * @return <pre>true</pre> se i due nodi sono congruenti,<pre>false</pre> altrimenti
   */
  private static boolean congruentH(String id1, String id2) {

	Node n1 = node(id1);
	Node n2 = node(id2);

	if (n1.getFun().equals(n2.getFun())) {
	  ArrayList<String> arg1 = n1.getArgs();
	  ArrayList<String> arg2 = n2.getArgs();
	  if (arg1.size() == arg2.size()) {
		for (int i = 0; i < arg1.size(); i++)
		  if (!findH(arg1.get(i)).equals(findH(arg2.get(i))))
			return false;
	  }
	  return true;

	}
	return false;
  }

  /**
   * Metodo che esegue il merde di due nodi
   * 
   * @param id1 nodo da "mergiare"
   * @param id2 nodo da "mergiare"
   * @throws Exception nel caso in cui due nodi non possano essere uniti.
   */
  private static void mergeH(String id1, String id2) throws Exception {
	String find1 = findH(id1);
	String find2 = findH(id2);
	
	if (!findH(id1).equals(findH(id2))) {

	  if (node(findH(id1)).getForbidden().contains(id2))
		throw new Exception("Insod");
	  
	  /* Euristica dei forbidden */
	  for (String forb : node(find1).getForbidden()) {
		if (findH(forb).equals(find2)) {
		  System.out.println("euristica 1: ["+find1+"]"+forb+" - "+find2);
		  throw new Exception("Non soddisfacibile");
		}
	  }
	  
	  for (String forb : node(find2).getForbidden()) {
		if (findH(forb).equals(find1)) {
		  System.out.println("euristica 2: ["+find2+"]"+forb+" - "+find1);
		  throw new Exception("Non soddisfacibile");
		}
	  }

	  /* Salvo i ccparent per propagare l'assioma di congruenza */
	  Object[] AP1 = ccparentH(id1).toArray();
	  Object[] AP2 = ccparentH(id2).toArray();

	  unionH(id1, id2); /* Faccio la UNION */

	  for (int i = 0; i < AP1.length; i++) {
		String p1 = (String) AP1[i];
		for (int j = 0; j < AP2.length; j++) {
		  String p2 = (String) AP2[j];
		  if (!findH(p1).equals(findH(p2)) && congruentH(p1, p2))
			mergeH(p1, p2);
		}
	  }

	}
  }

  /**
   * Metodo che controlla la validità nella lista delle clausole con
   * disuguaglianza.
   * 
   * @return <pre>true</pre> se non ci sono contraddizioni,<pre>false</pre> altrimenti
   */
  private static boolean checkNoEqualsH() {
	for (int i = 0; i < noEquals.size(); i = i + 2) {
	  if (findH(noEquals.get(i)).equals(findH(noEquals.get(i + 1))))
		return false;
	}
	return true;
  }

  /**
   * Metodo che controlla la validità nella lista degli atomi.
   * 
   * @return <pre>true</pre> se non ci sono contraddizioni,<pre>false</pre> altrimenti
   */
  private static boolean checkAtomH() {
	for (int i = 0; i < consfn.size(); i++) {
	  for (int j = 0; j < atoms.size(); j++) {
		if (findH(consfn.get(i)).equals(findH(atoms.get(j)))) {
		  return false;
		}
	  }
	}
	return true;
  }

  /* METODI SENZA EURISTICHE */
  /**
   * Metodo che restituisce il rappresentante della classe a cui appartiene il
   * nodo id.
   * 
   * @param id del nodo di cui restituire la classe
   */
  private static String _find(String id) {
	Node n = node(id);
	return (n.getFind().equals(id)) ? id : _find(n.getFind());
  }

  /**
   * Metodo che imprementa l'unione di classi di congruenza.
   * 
   * @param id1 primo nodo da unire
   * @param id2 secondo nodo da unire
   */
  private static void _union(String id1, String id2) {

	Node n1 = node(_find(id1));
	Node n2 = node(_find(id2));
	// System.out.println("UNION: \t\t" + n1 + " -- " + n2);
	n1.setFind(n2.getFind());
	n2.getCcparent().addAll(n1.getCcparent());
	n1.setCcparent(new HashSet<String>());
  }

  /**
   * @see Main#ccparent
   */
  private static HashSet<String> _ccparent(String id) {
	return (node(_find(id))).getCcparent();
  }

  /**
   * @see Main#congruent
   */
  private static boolean _congruent(String id1, String id2) {
	Node n1 = node(id1);
	Node n2 = node(id2);

	if (n1.getFun().equals(n2.getFun())) {
	  ArrayList<String> arg1 = n1.getArgs();
	  ArrayList<String> arg2 = n2.getArgs();
	  if (arg1.size() == arg2.size()) {
		for (int i = 0; i < arg1.size(); i++)
		  if (!_find(arg1.get(i)).equals(_find(arg2.get(i))))
			return false;
	  }
	  return true;

	}
	return false;
  }

  /**
   * Metodo che esegue il merde di due nodi
   * 
   * @param id1 nodo da "mergiare"
   * @param id2 nodo da "mergiare"
   */
  private static void _merge(String id1, String id2) {

	if (!_find(id1).equals(_find(id2))) {
	  HashSet<String> P1 = _ccparent(id1);
	  HashSet<String> P2 = _ccparent(id2);

	  Object[] AP1 = P1.toArray();
	  Object[] AP2 = P2.toArray();

	  _union(id1, id2); /* Faccio la UNION */

	  for (int i = 0; i < AP1.length; i++) {
		String p1 = (String) AP1[i];
		for (int j = 0; j < AP2.length; j++) {
		  String p2 = (String) AP2[j];
		  if (!(_find(p1).equals(_find(p2))) && _congruent(p1, p2))
			_merge(p1, p2);
		}
	  }
	}
  }

  /**
   * @see Main#checkNoEquals
   */
  private static boolean _checkNoEquals() {
	for (int i = 0; i < noEquals.size(); i = i + 2) {
	  if (_find(noEquals.get(i)).equals(_find(noEquals.get(i + 1))))
		return false;
	}
	return true;
  }

  /**
   * @see Main#checkAtom
   */
  private static boolean _checkAtom() {
	for (int i = 0; i < consfn.size(); i++) {
	  for (int j = 0; j < atoms.size(); j++) {
		if (_find(consfn.get(i)).equals(_find(atoms.get(j)))) {
		  return false;
		}
	  }
	}
	return true;
  }
}
