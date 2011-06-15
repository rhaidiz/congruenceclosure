package verifica;

/**
 * Classe per generare formule random da usare per test.
 * 
 * @author Federico De Meo
 *
 */
public class FormulaGenerator {

  private static String[] terms = { "a", "b", "x", "y", "q", "w", "e", "r",
	  "t", "y", "u", "i", "o", "p", "s" };
  private static String[] fun = { "An", "Bn", "Cn1", "Rn1", "Fun", "Gun", "Gm", "car",
	  "cdr", "cons" };
  private static int ricMax = 10; // massima ricorsione di funzioni
  private static int n = 0;

  private static int[] funArity;

  /**
   * Metodo che mi genera formule random
   * 
   * @param equals
   *          numero di clausole con uguaglianza
   * @param noEquals
   *          numero di clausole con disuguaglianza
   * @param atoms
   *          numero di atomi
   * @param natoms
   *          numero di non atomi
   * @return
   */
  public static String generateFormula(int equals, int noEquals, int atoms,
	  int natoms) {
	String result = "";
	StringBuilder r = new StringBuilder();
	funArity = new int[10];
	for (int i = 0; i < 7; i++) {
	  funArity[i] = random(1, 5);
	}
	funArity[7] = funArity[8] = 1;
	funArity[9] = 2;

	for (int i = 0; i < equals; i++) {
	  r.append(makeClaus(true) + "&");
	  if(i == (equals/2)) r.append('\n'); 
	}
	
	for (int i = 0; i < noEquals; i++) {
	  r.append(makeClaus(false) + "&");
	  if(i == (equals/2)) r.append('\n'); 
	}
	
	for (int i = 0; i < atoms; i++) {
	  r.append(makeAtom(true) + "&");
	  if(i == (equals/2)) r.append('\n'); 
	}
	
	for (int i = 0; i < natoms; i++) {
	  r.append(makeAtom(false) + "&");
	  if(i == (equals/2)) r.append('\n'); 
	}
	if(r.lastIndexOf("&") == r.length()-1)
	  return (r.substring(0, r.length()-1));
	if(r.lastIndexOf("&") == r.length()-2)
	  return (r.substring(0, r.length()-2));
	
	return r.toString();
  }

  private static String makeAtom(boolean t) {
	if (t)
	  return "atom(" + terms[random(0, terms.length - 1)] + ")";
	else
	  return "!atom(" + terms[random(0, terms.length - 1)] + ")";
  }

  private static String makeClaus(boolean type) {
	if (type) {
	  return makeTerm() + "=" + makeTerm();
	} else {
	  return makeTerm() + "!=" + makeTerm();
	}
  }

  private static String makeTerm() {
	String result = "";
	if (random(0, 2) == 1) {
	  // Faccio una funzione
	  if (n < ricMax) {
		n++;
		int pos = random(0, fun.length - 1);
		String ff = fun[pos];
		int arity = funArity[pos];
		result = ff + "(";
		for (int i = 1; i <= arity; i++) {
		  result = result + makeTerm();
		  if (i < arity)
			result = result + ",";
		}
		n--;
		result = result + ")";
	  }
	}
	if (result.equals("")) {
	  int pos = random(0, terms.length - 1);
	  result = terms[pos];
	}
	return result;
  }

  private static int random(int min, int max) {
	int r = (int) (max * Math.random()) + min;
	return r;
  }

}
