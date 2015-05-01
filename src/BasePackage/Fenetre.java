package BasePackage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class Fenetre extends JFrame{
	JPanel mainPan = new JPanel();
	MenusBar menuBar = new MenusBar();
	ToolBar toolBar = new ToolBar();
	BorderLayout bl = new BorderLayout();
	Thread tPanel,tFen,tGame,tPhysics,tIA;
	IOLevel iolvl = new IOLevel();
	Math math;
	Niveau lvl;
	PanneauGame panGame = new PanneauGame(Color.white);
	Moteur mot = new Moteur(panGame);
	PanneauGameThread panThread = new PanneauGameThread(this, panGame);
	char touche;


	public Fenetre(String title){


		this.setTitle(title);
		this.setSize(1280+16,720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		mainPan.setLayout(bl);
		mainPan.add(panGame, BorderLayout.CENTER);
		mainPan.add(toolBar,BorderLayout.NORTH);
		this.setContentPane(mainPan);
		this.setResizable(true);
		this.setJMenuBar(menuBar);
		
		for(Player player : mot.PlayerList){
			this.addKeyListener(player);
		}
		
		this.setVisible(true);
		

		panGame.cellSizeX=1280/mot.lvl.getArraySizeX();
		panGame.cellSizeY=(panGame.getHeight()+menuBar.getHeight())/mot.lvl.getArraySizeY();

		
		this.requestFocus();

		tPanel = new Thread(new refreshPanel());
		tFen = new Thread(new fenThread());
		tGame = new Thread(new gameThread());
		tPhysics = new Thread(new applyPhysicsThread());
		tIA = new Thread(new IAThread());
		
		
		tFen.start();
		tGame.start();
		tPanel.start();
		tPhysics.start();
		tIA.start();


	}

	private void runFen(){
		while(true){
			//panGame.cellSizeX=this.getWidth()/mot.lvl.getArraySizeX();
			//panGame.cellSizeY=this.getHeight()/mot.lvl.getArraySizeY();
			this.requestFocus();
			mot.setToolSelected(toolBar.getToolSelected());
			mot.setSliderValue(toolBar.getSliderValue());
			mot.setPanneau(panGame);
			
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}
		}
	

	class fenThread implements Runnable{
		public void run() {
			runFen();			
		}
	}
	
	class gameThread implements Runnable{
		public void run() {
			mot.runGame();
		}
	}


	class IAThread implements Runnable{
		public void run() {
			mot.runIA();			
		}
	}

	class refreshPanel implements Runnable{
		public void run() {
			panThread.goPanel();			
		}
	}

	class applyPhysicsThread implements Runnable{
		public void run(){
			mot.motApplyPhysics();
		}
	}
	
	class MenusBar extends JMenuBar {
		IOLevel iolvl = new IOLevel();
		
		JMenu fichier = new JMenu("Fichier");
		JMenuItem save = new JMenuItem("Enregistrer");
		JMenuItem saveUnder = new JMenuItem("Enregistrer sous");
		JMenuItem ouvrir = new JMenuItem("Ouvrir");
		
		JMenu options = new JMenu("Options");
		JMenuItem surprise = new JMenuItem("surprise");
		

		
		public MenusBar(){
			surprise.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a) {
					System.out.println("SURPRISE");
				}
			});
			
			ouvrir.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a) {
					try{
						mot.setNiveau(iolvl.openLvl("Pouet"));
					}catch(NullPointerException e){
						System.out.println("Ouverture annulée");
					}
					}
			});
			
			save.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a) {
					try{
						if(iolvl.doPathExist())
							iolvl.saveLvl(mot.getNiveau());
						else{
							iolvl.saveLvlUnder(mot.getNiveau());
						}
					}catch(NullPointerException e){
						System.out.println("Enregistrement annulé");
					}
					
				}
			});
			
			saveUnder.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a) {
					try{
					iolvl.saveLvlUnder(mot.getNiveau());
				}catch(NullPointerException e){
					System.out.println("Enregistrement annulé");
				}
				}
			});
			
			fichier.add(save);
			fichier.add(saveUnder);
			fichier.add(ouvrir);
			options.add(surprise);
			this.add(fichier);
			this.add(options);

		}

		
	}



}