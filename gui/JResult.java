package gui;

import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import parser.ParseException;
import parser.TokenMgrError;
import verifica.Main;

/**
 * Classe che visualizza il risultato della computazione.
 * 
 * @author Federico De Meo
 *
 */
public class JResult extends JFrame implements WindowListener {

  private JResult jr;
  
  private static final long serialVersionUID = 1L;

  /**
   * Lista dei JLabel che contengono i risultati.
   */
  private final JLabel label_sodd = new JLabel();
  private final JLabel label_extime = new JLabel();
  private final JLabel label_parser = new JLabel();
  private final JLabel label_graph_dimension = new JLabel();

  private String f;

  public JResult() {
	setTitle("Running...");
	setSize(240, 150);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	Container container = getContentPane();
	container.setLayout(new GroupLayout(container));
	initComponents(container);
	addWindowListener(this);
	jr = this;
  }

  public void windowOpened(WindowEvent e) {
//	new Thread(new Runnable() {
//	  public void run() {
//		runAlgorithm(f);
//	  }
//	}).start();
  }

  private void initComponents(Container c) {

	/* Inizializzazione dei JLabel statici */
	label_parser.setText("Parsing...");
	label_parser.setSize(120, 20);
	label_parser.setLocation(10, 10);
	label_graph_dimension.setText("Graph Dimension: ");
	label_graph_dimension.setSize(200, 20);
	label_graph_dimension.setLocation(10, 30);
	label_extime.setText("Execution time:");
	label_extime.setSize(220, 20);
	label_extime.setLocation(10, 50);

	label_sodd.setSize(120, 20);
	label_sodd.setLocation(10, 70);

	/* Aggiunta dei JLabel */
	c.add(label_graph_dimension);
	c.add(label_extime);
	c.add(label_parser);
	c.add(label_sodd);
  }

  /**
   * Esegue l'algoritmo e misura il tempo
   */
  private void runAlgorithm(String formula) {
	long tempo = 0;
	long inizio = System.currentTimeMillis();
	try {
	  boolean sod = Main.execCC(formula, jr);
	  if (sod) {
		label_sodd.setText("Soddisfacibile");
	  } else {
		label_sodd.setText("Non Soddisfacibile");
	  }
	} catch (ParseException e) {
	  label_sodd.setText("Syntax error");
	  e.printStackTrace();
	} catch (TokenMgrError e) {
	  label_sodd.setText("Syntax error");
	  e.printStackTrace();
	} catch (Exception e) {
	  // Insoddisfacibile
	  label_sodd.setText("Non Soddisfacibile");
	  e.printStackTrace();
	}
	long fine = System.currentTimeMillis();
	tempo += (fine - inizio);
	setTitle("Finish");
	label_extime.setText("Execution time: " + (tempo / 1000) + "s");

  }

  /**
   * Metodo che viene chiamato dopo che il parser ha generato il grafo.
   * 
   * @param graphDim dimensione del grafo
   */
  public void setParsingResult(int graphDim) {
	label_parser.setText("Parsing...DONE!");
	label_graph_dimension.setText("Graph Dimension: "+graphDim);
  }

  public void setFormula(String formula){
	f = formula;
  }
  
  @Override
  public void windowActivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	setTitle("Running...");
	new Thread(new Runnable() {
	  public void run() {
		runAlgorithm(f);
	  }
	}).start();
	//f = formula;
  }

  @Override
  public void windowClosed(WindowEvent arg0) {
	// TODO Auto-generated method stub

  }

  @Override
  public void windowClosing(WindowEvent arg0) {
	// TODO Auto-generated method stub

  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
	// TODO Auto-generated method stub

  }

  @Override
  public void windowDeiconified(WindowEvent arg0) {
	// TODO Auto-generated method stub

  }

  @Override
  public void windowIconified(WindowEvent arg0) {
	// TODO Auto-generated method stub

  }
}
