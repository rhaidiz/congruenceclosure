package gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import verifica.FormulaGenerator;



/**
 * Classe che visualizza le opzioni per il generatore automatico di formule.
 * 
 * @author Federico De Meo
 *
 */
public class JFormulaGenerator extends JFrame{


  private static final long serialVersionUID = 1L;
  
  /**
   * Identificatore a me stesso.
   */
  private static JFrame me;
  /**
   * Lista dei JLabel che sono visualizzati.
   */
  private final JLabel label_eq = new JLabel();
  private final JLabel label_neq = new JLabel();
  private final JLabel label_atom = new JLabel();
  private final JLabel label_natom = new JLabel();
  
  /**
   * Lista delle aree di testo che per i parametri di input.
   */
  private final JTextField text_eq = new JTextField();
  private final JTextField text_neq = new JTextField();
  private final JTextField text_atom = new JTextField();
  private final JTextField text_natom = new JTextField();
  
  /**
   * JTextArea della formula che si trova nel MainFrame,
   * nel quale visualizzare la formula random generate.
   */
  private final JTextArea text_formula;
  
  /**
   * Button per generare la formula random.
   */
  private final JButton btn_generate = new JButton();
  
  public JFormulaGenerator(JTextArea formula){
	setTitle("Formula Generator");
	setSize(200,200);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	text_formula = formula;
	Container container = getContentPane();
	container.setLayout(new GroupLayout(container));
	initComponents(container);
	me = this;
  }
  
  private void initComponents(Container c){
	/* Inizializzazione dei label */
	label_eq.setText("# Equals:");
	label_eq.setSize(70,20);
	label_eq.setLocation(10, 20);
	label_neq.setText("# No equals:");
	label_neq.setSize(80,20);
	label_neq.setLocation(10, 45);
	label_atom.setText("# Atom:");
	label_atom.setSize(70,20);
	label_atom.setLocation(10, 70);
	label_natom.setText("# No atom:");
	label_natom.setSize(70,20);
	label_natom.setLocation(10, 95);
	
	/* Inizializzazione delle text area */
	text_eq.setSize(60, 20);
	text_eq.setLocation(120, 20);
	text_eq.setText("10");
	text_neq.setSize(60, 20);
	text_neq.setLocation(120,45);
	text_neq.setText("10");
	text_atom.setSize(60, 20);
	text_atom.setLocation(120,70);
	text_atom.setText("10");
	text_natom.setSize(60, 20);
	text_natom.setLocation(120,95);
	text_natom.setText("10");
	
	/* Inizializzazione del bottone */
	btn_generate.setText("Generate Formula");
	btn_generate.setLocation(20, 140);
	btn_generate.setSize(150, 20);
	btn_generate.addActionListener(new ActionListener() {
	  
	  @Override
	  public void actionPerformed(ActionEvent arg0) {
		// Setta la formula e chiudi questa finestra
		int eq=1, neq=1, atom=1, natom =1;
		try{
		 eq = Integer.parseInt(text_eq.getText());
		 neq = Integer.parseInt(text_neq.getText());
		 atom = Integer.parseInt(text_atom.getText());
		 natom = Integer.parseInt(text_natom.getText());
		}catch(NumberFormatException e){
		  
		}
		String ff = FormulaGenerator.generateFormula(eq,neq,atom,natom);
		//System.out.println(ff);
		text_formula.setText(ff);
		me.setVisible(false);
	  }
	});
	
	/* Aggiunta dei label al JFrame */
	c.add(label_eq);
	c.add(label_neq);
	c.add(label_atom);
	c.add(label_natom);
	/* Aggiunta delle text area al JFrame */
	c.add(text_eq);
	c.add(text_neq);
	c.add(text_atom);
	c.add(text_natom);
	/* Aggiunta del button al JFrame */
	c.add(btn_generate);
  }
  
}
