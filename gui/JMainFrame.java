package gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import verifica.Config;

/**
 * Classe che visualizza la finestra principale dell'applicazione.
 * 
 * @author Federico De Meo
 *
 */

public class JMainFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  /**
   * Area di testo che contiene la formula da verificare.
   */
  private final JTextArea text_formula = new JTextArea();
  /**
   * Area di scroll che circonda l'area di testo.
   */
  private JScrollPane scrollPane;
  /**
   * Contiene la formula letta da file.
   */
  private StringBuilder input = new StringBuilder();
  /**
   * La finestra con i risultati della computazione.
   */
  private JResult jresult = null;

  /**
   * Costruttore della finestra principale.
   */
  public JMainFrame() {
	setTitle("Conguence Closure Algorithm [RC1]");
	setSize(450, 370);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	Container container = getContentPane();
	container.setLayout(new GroupLayout(container));
	initComponents(container);

  }

  /**
   * Inizializzazione dei componenti sul Pane
   * 
   * @param c contenitore nel quale inserire i componenti
   */
  private void initComponents(Container c) {

	JLabel label1 = new JLabel("Insert Formula:", JLabel.CENTER);
	label1.setLocation(5, 5);
	label1.setSize(100, 10);
	c.add(label1);

	text_formula.setSize(220, 65);
	text_formula.setLocation(0, 0);
	text_formula.setWrapStyleWord(true);
	// text_formula.setLineWrap(true);

	scrollPane = new JScrollPane(text_formula);
	scrollPane.setSize(420, 265);
	scrollPane.setLocation(10, 20);
	scrollPane
		.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	c.add(scrollPane);

	JButton btn_exec = new JButton("Run");
	btn_exec.setSize(70, 20);
	btn_exec.setLocation(360, 290);
	btn_exec.addActionListener(new ActionListener() {

	  @Override
	  public void actionPerformed(ActionEvent arg0) {
		/* Lancia la finestra di esecuzione dell'algoritmo */
		if (jresult == null)
		  jresult = new JResult();
		jresult.setFormula(text_formula.getText());
		jresult.setVisible(true);

	  }
	});
	c.add(btn_exec);

	initMenuBar();

  }

  private void loadFile() {
	/* Carica file formula esterno */
	JFileChooser file_d = new JFileChooser();
	file_d.showDialog(null, "Open file");
	File f = file_d.getSelectedFile();
	try {
	  FileReader reader = new FileReader("" + f.getAbsolutePath());
	  Scanner in = new Scanner(reader);

	  while (in.hasNextLine())
		input.append(in.nextLine());
	  text_formula.setText(input.toString());
	  // text_formula.append(arg0)
	  reader.close();
	  in.close();
	} catch (FileNotFoundException e) {
	  // TODO Auto-generated catch block
	  System.out.print("ciao");
	  // e.printStackTrace();
	} catch (IOException e) {
	  // TODO Auto-generated catch block
	  // e.printStackTrace();
	} catch (NullPointerException e) {

	}
  }

  private void saveFile() {
	/* Salve file formula esterno */
	JFileChooser file_d = new JFileChooser();
	file_d.showDialog(null, "Save file");
	try {
	  File f = file_d.getSelectedFile();
	  PrintWriter writer = new PrintWriter(f.getAbsoluteFile());
	  System.out.println(f.getAbsoluteFile() + f.getName());
	  writer.println(text_formula.getText());
	  writer.close();
	} catch (FileNotFoundException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (NullPointerException e) {

	}
  }

  /**
   * Inizializzazione della barra del menu.
   * 
   */
  private void initMenuBar() {
	JMenuBar jmenu = new JMenuBar();
	JMenu jj = new JMenu("Options");
	/* Opzione di caricamento formula */
	JMenuItem load = new JMenuItem("Load..");
	load.addActionListener(new ActionListener() {

	  @Override
	  public void actionPerformed(ActionEvent arg0) {
		new Thread(new Runnable() {
		  public void run() {
			loadFile();
		  }
		}).start();
	  }
	});

	/* Opzione di salvataggio formula */
	JMenuItem save = new JMenuItem("Save formula as..");
	save.addActionListener(new ActionListener() {
	  @Override
	  public void actionPerformed(ActionEvent arg0) {
		saveFile();
	  }
	});

	/* Lista degli algoritmi diponibili */
	JMenu sub_menu = new JMenu("Algorithms");
	ButtonGroup group = new ButtonGroup();
	/* Algoritmo proposto dal libro */
	JRadioButtonMenuItem algo1 = new JRadioButtonMenuItem("Nelson-Oppen");
	/* Algoritmo proposto dal libro con euristiche */
	JRadioButtonMenuItem algo2 = new JRadioButtonMenuItem(
		"Nelson-Oppen+speedup");
	algo2.setSelected(true);
	algo1.addActionListener(new ActionListener() {

	  @Override
	  public void actionPerformed(ActionEvent e) {
		/* Algoritmo proposto dal libro */
		Config.algorithm = 1;
	  }
	});
	algo2.addActionListener(new ActionListener() {

	  @Override
	  public void actionPerformed(ActionEvent e) {
		/* Algoritmo proposto dal libro con euristiche */
		Config.algorithm = 0;
	  }
	});

	/* Generatore automatico di formule */
	JMenuItem formula_generator = new JMenuItem("Formula Generator");
	formula_generator.addActionListener(new ActionListener() {

	  @Override
	  public void actionPerformed(ActionEvent e) {
		// ATTENZIONE: NON CONSIGLIATO SUPERARE I 2400 TOTALI
		/*
		 * text_formula.setText(FormulaGenerator.generateFormula(1, 1));
		 * System.out.println(text_formula.getText());
		 */
		JFrame jFG = new JFormulaGenerator(text_formula);
		jFG.setVisible(true);
	  }
	});

	group.add(algo1);
	group.add(algo2);

	sub_menu.add(algo1);
	sub_menu.add(algo2);

	jj.add(load);
	jj.add(save);
	jj.add(sub_menu);
	jj.add(formula_generator);

	jmenu.add(jj);
	this.setJMenuBar(jmenu);
  }
}
