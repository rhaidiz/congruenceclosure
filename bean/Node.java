package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Classe che identifica un nodo del grafo
 * 
 * @author Federico De Meo
 *
 */
public class Node {
  /**
   * Identificatore del nodo
   */
  private String _id = "";
  /**
   * Simbolo di funzione
   */
  private String _fn = "";
  /**
   * Lista argomenti
   */
  private ArrayList<String> args = new ArrayList<String>();
  /**
   * Campo Find
   */
  private String find = "";
  /**
   * Lista padri
   */
  private HashSet<String> ccparent = new HashSet<String>();
  /**
   * Rango
   */
  private int rank = 0;
  /**
   * Insieme dei nodi proibiti
   */
  private HashSet<String> forbidden = new HashSet<String>();

  /**
   * Costruttore semplice che costruisce un nodo e gli assegna un
   * identificatore.
   * 
   * @param id
   *          identificatore del nuovo nodo
   */
  public Node(String id) {
	_id = id;
	find = id;
  }

  /**
   * Costruisce un nodo che identifica una funzione.
   * 
   * @param id
   *          identificatore del nodo
   * @param fn
   *          simbolo di funzione
   * @param args
   *          lista di argomenti della funzione
   */
  public Node(String id, String fn, ArrayList<String> args) {
	_id = id;
	find = id;
	_fn = fn;
	this.args = args;
  }

  /**
   * Metodo che ritorna l'id del nodo.
   * 
   * @return String id del nodo
   */
  public String getId() {
	return _id;
  }

  /**
   *  Metodo che ritorna il simbolo di funzione.
   *  
   *  @return String simbolo di funzione
   */
  public String getFun() {
	return _fn;
  }

  /**
   *  Metodo che restituisce il numero di argomenti della funzione.
   *  
   *  @return intero che rappresenta il numero di argomenti
   */
  public int argsSize() {
	return args.size();
  }

  /**
   *  Metodo che ritorna la lista degli argomenti
   *  
   *  @return lista degli argomenti delle funzione
   */
  public ArrayList<String> getArgs() {
	return args;
  }

  /**
   *  Metodo per settare il campo find del nodo.
   *  
   *  @param id nuovo identificatore per il campo find
   */
  public void setFind(String id) {
	this.find = id;
  }

  /**
   *  Metodo per ritornare il campo find del nodo .
   *  
   *  @return String valore di find del nodo
   */
  public String getFind() {
	return find;
  }

  /**
   *  Metodo che modifica l'insieme dei padri del nodo.
   *  
   *  @param insieme dei padri del nodo
   */
  public void setCcparent(HashSet<String> ccparent) {
	this.ccparent = ccparent;
  }

  /**
   *  Metoco che aggiungeun padre all'insieme dei padri, se già presente viene sovrascritto.
   *  
   *  @param parent padre da aggiungere
   */
  public void appendParent(String parent) {
	ccparent.add(parent);
  }

  /**
   *  Metodo che restituisce l'insieme dei padri.
   *  
   *  @return insieme dei padri del nodo
   */
  public HashSet<String> getCcparent() {
	return ccparent;
  }

  /**
   *  Metodo per settare il rango del nodo.
   *  
   *  @param k nuovo rango del nodo
   */
  public void setRank(int k) {
	rank = k;
  }

  /** 
   * Metodo che restituisce il rango.
   * 
   * @return rango del nodo
   */
  public int getRank() {
	return rank;
  }

  /**
   *  Metodo che aggiunge un nodo forbidden all'insieme dei nodi proibiti.
   *  
   *  @param id identificatore del nodo proibito 
   */
  public void addForbidden(String id) {
	forbidden.add(id);
  }

  /**
   *  Metodo che restituisce se un nodo argomento è nell'insieme dei forbidden.
   *  
   *  @return <pre>true</pre> se id è nei forbidden, <pre>false</pre> altrimenti
   */
  public boolean conteainsForbidden(String id) {
	return forbidden.contains(id);
  }

  /**
   * Metodo che restituisce l'insieme forbidden del nodo.
   * 
   * @return nodi foribidden
   */
  public HashSet<String> getForbidden(){
	return forbidden;
  }
  
  /**
   * Metodo per settare l'insieme dei forbidden del nodo.
   * 
   * @param forbs la nuova lista forbidden
   */
  public void setForbidden(HashSet<String> forbs){
	forbidden = forbs;
  }
  /**
   * Metodo che unisce l'attuale l'insieme forbidden con un nuovo insieme preso come parametro.
   * 
   * @param forbs l'insieme dei forbidden da unire con l'attuale
   */
  public void mergeForbidden(HashSet<String> forbs){
	this.forbidden.addAll(forbs);
  }
  /**
   * @see Object#toString
   */
  public String toString() {
	return "id: "+_id+"fun:"+_fn + "\n\tccparent:" + ccparent + "\n\tfind:[" + find
		+ "]\n\targs:" + args + "\n\tforbidden:" + forbidden + "\n";
  }
}
