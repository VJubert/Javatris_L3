package javatris.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Locale;

import javatris.model.Game;
import javatris.model.Grid;
import javatris.model.Tetromino;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	// taille de bae de la fenetre
	public static final int WIDTH = 510;
	public static final int HEIGHT = 720;
	// jeu en cours
	private Game g;
	// panel de la grille de jeu
	private PanelGrid panelGrid;
	// panel de la grille suivante
	private PanelGrid suivantGrid;
	// barre de menu
	private JMenuBar menuBar = new JMenuBar();
	// bouton reprendre (sur le côté)
	private Button replay;
	// bouton pause (sur le côté)
	private Button bpause;
	// bouton nouvelle partie (sur le côté)
	private Button restart;
	// container jeu de la barre de menu
	private JMenu jeu = new JMenu("Jeu");
	// bouton pause (de la barre de menu)
	private JMenuItem pause = new JMenuItem("Pause");
	// bouton reprendre (de la barre de menu)
	private JMenuItem reprendre = new JMenuItem("Reprendre");
	// bouton reset (de la barre de menu)
	private JMenuItem resetPartie = new JMenuItem("Nouvelle Partie");
	// bouton fermer (de la barre de menu)
	private JMenuItem fermer = new JMenuItem("Fermer");
	// container score dans la barre de menu
	private JMenu menuScore = new JMenu("Scores");
	// bouton voir highscore de la barre de menu
	private JMenuItem voirHighScore = new JMenuItem("Voir les meilleurs scores");
	// bouton reset highscore de la barre de menu
	private JMenuItem resetHighScore = new JMenuItem(
			"Reset les meilleurs scores");
	// container parametres (de la barre de menu)
	private JMenu parametres = new JMenu("Paramètres");
	// bouton paramètres de barre de menu
	private JMenuItem ret_param = new JMenuItem("Paramètres");
	// checkbox son de la barre de menu
	private JCheckBoxMenuItem checkSon = new JCheckBoxMenuItem("Son");
	// checkbox affichage piece suivante de la barre de menu
	private JCheckBoxMenuItem pieceSuivante = new JCheckBoxMenuItem(
			"Pièce suivante");

	// container aide de la barre de menu
	private JMenu helpmenu = new JMenu("Aide");
	// bouton controle de la barre de menu
	private JMenuItem help_controle = new JMenuItem("Contrôle");
	// bouton pour retourner l'accueil (dans la barre de menu)
	private JMenuItem ret_acc = new JMenuItem("Accueil");
	// label score
	private Label score;
	// label piece suivante
	private Label label_suiv = new Label("Pièce suivante : ", 100, 25, 370, 70);
	// niveau de départ
	private int lvl_dep;
	// densité de pré-remplissage
	private int dens_dep;
	// nombre de ligne à pré-remplir
	private int dens_ligne_dep;
	// savoir si on doit afficher le suivant
	private boolean aff_suivant;
	// savoir si on doit jouer les sons
	private boolean son;

	/**
	 * 
	 * @param grid
	 *            grille de depart
	 * @param scoreDep
	 *            score de depart
	 * @param game
	 * @param lvlDep
	 */
	public Frame() {
		// initialisation des variables
		aff_suivant = true;
		son = true;
		lvl_dep = 0;
		dens_dep = 0;
		dens_ligne_dep = 0;
		// met l'icone
		this.setIconImage(new ImageIcon("res"
				+ System.getProperty("file.separator") + "icon.png").getImage());
		this.setTitle("Javatris");
		// mettre la fenetre au second plan (ne cache plus la barre des tâches
		// si on bouge la fenêtre)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ajoute l'action du checkbox piecesuivante
		pieceSuivante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aff_suivant = pieceSuivante.isSelected();
				if (pieceSuivante.isSelected()) {
					// si on l'active on active l'affichage
					label_suiv.setVisible(true);
					suivantGrid.setVisible(true);
				} else {
					// si on l'a désaction on désactive l'affichage
					label_suiv.setVisible(false);
					suivantGrid.setVisible(false);
				}
			}
		});
		// ajout l'action de la checkbox son
		checkSon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkSon.isSelected()) {
					// si on le coche on active le son
					son = true;
				} else {
					// si on le décoche on désactive le son
					son = false;
				}
			}
		});
		// ajoute l'action du bouton reset_partie (nouvelle partie sur dans les
		// menus)
		resetPartie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on désative les boutons reprendre et on réactive les boutons
				// pause
				reprendre.setEnabled(false);
				pause.setEnabled(true);
				replay.setEnabled(false);
				bpause.setEnabled(true);
				if (lvl_dep != 0 || dens_dep != 0 || dens_ligne_dep != 0) {
					// si on a modifié un des paramètres on vérifie que le
					// joueur veut les garder
					int j = JOptionPane.showConfirmDialog(null,
							"Voulez vous garder les même paramètres ?",
							"Paramètres", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (j == JOptionPane.NO_OPTION) {
						// s'il veut pas on reset les paramètres
						reset_param();
					} else {
						if (j == JOptionPane.CANCEL_OPTION) {
							// s'il annule on fait rien
							return;
						}
					}
				}
				// on relance le jeu
				g.reset_jeu(lvl_dep, dens_dep, dens_ligne_dep);
			}
		});
		// ajoute l'action du bouton pause du menu
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on désactive les boutons pause et on active les boutons
				// reprendre
				pause.setEnabled(false);
				bpause.setEnabled(false);
				replay.setEnabled(true);
				reprendre.setEnabled(true);
				// on lance la pause
				g.pause();
			}
		});
		// ajoute l'action du bouton reprendre du menu
		reprendre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on désactive les boutons reprendre, on active les boutons
				// pause
				reprendre.setEnabled(false);
				pause.setEnabled(true);
				replay.setEnabled(false);
				bpause.setEnabled(true);
				// on reprend le jeu
				g.reprendre();
			}
		});
		// ajoute l'action du bouton fermer du menu
		fermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on quitte avec 0 (tout va bien)
				System.exit(0);
			}
		});
		// ajoute l'action du bouton voir highscore
		voirHighScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on met en pause
				g.pause();
				// on va voir les highscores
				g.consulter_highscore();
				// on reprend le jeu
				g.reprendre();
			}
		});
		// on ajoute l'action de reset des highscores
		resetHighScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				g.vider_highscore();
			}
		});
		// ajoute l'action pour le bouton d'aide
		help_controle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// lance la pause,affiche l'aide et reprend le jeu
				g.pause();
				affichage_aide();
				g.reprendre();
			}
		});
		// ajoute l'action pour le bouton paramètres
		// apres les paramètres on retourne à l'accueil, puis si on veut jouer
		// on clique sur jouer et on lance un nouveau jeu
		ret_param.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// on demande si on peut couper le jeu
				int i = JOptionPane
						.showConfirmDialog(
								null,
								"Cela interrompra  la partie en cours"
										+ System.getProperty("line.separator")
										+ System.getProperty("line.separator")
										+ "Voulez vous continuez ?",
								"Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					// si oui on lance la pause pour arrêter le jeu
					g.couper();
					// on retourne au parametre
					param();
				} else {
					// sinon on fait rien
					return;
				}
			}
		});
		// bouton de retour à l'accueil
		ret_acc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// on prévient que ça coupera le jeu
				int i = JOptionPane
						.showConfirmDialog(
								null,
								"Cela interrompra  la partie en cours"
										+ System.getProperty("line.separator")
										+ System.getProperty("line.separator")
										+ "Voulez vous continuez ?",
								"Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					// on lance la pause pour arrêter le jeu
					g.couper();
					// on retourne à l'accueil
					aff_accueil();
					;
				} else {
					// on fait rien si on clique sur non
					return;
				}

			}
		});

		// raccourcis clavier pour les différentes options de manipulation du
		// jeu
		pieceSuivante.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.ALT_MASK));
		checkSon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				KeyEvent.ALT_MASK));
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK));
		reprendre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK));
		resetPartie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				KeyEvent.CTRL_MASK));
		fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_MASK));
		voirHighScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK));
		resetHighScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK));
		help_controle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				KeyEvent.CTRL_MASK));
		ret_acc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				KeyEvent.CTRL_MASK));
		ret_param.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.ALT_MASK));

		// debut d'ajout des sous menus aux menus
		jeu.add(pieceSuivante);
		jeu.add(pause);
		jeu.add(reprendre);
		jeu.add(resetPartie);
		jeu.addSeparator();
		jeu.add(ret_acc);
		jeu.add(fermer);
		menuBar.add(jeu);

		menuScore.add(voirHighScore);
		menuScore.add(resetHighScore);
		menuBar.add(menuScore);

		parametres.add(ret_param);
		parametres.add(checkSon);
		parametres.add(pieceSuivante);
		menuBar.add(parametres);

		helpmenu.add(help_controle);
		menuBar.add(helpmenu);
		// fin
		// on affiche l'accueil
		aff_accueil();
		setVisible(true);
	}

	private void aff_accueil() {
		// on enleve la barre de menu
		setJMenuBar(null);
		// on remet la fenetre au centre
		setLocationRelativeTo(null);
		JPanel pan = new JPanel();
		// bouton "jouer" qui lance le jeu
		JButton bouton = new JButton("Jouer");
		bouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lanc_jeu();
			}
		});
		// bouton "aide" qui affiche l'aide
		JButton bouton2 = new JButton("Aide");
		bouton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				affichage_aide();
			}
		});
		// bouton "paramètres" qui affiche les paramètres
		JButton bouton3 = new JButton("Paramètres");
		bouton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param();
			}
		});
		// on ajoute les boutons au panel
		pan.add(bouton);
		pan.add(bouton3);
		pan.add(bouton2);
		// on définit notre contentpane (en swing le container principal)
		setContentPane(pan);
		// on redimensionne à la taille de nos composants
		pack();
		validate();
		repaint();
		// on repeint la fenetre

	}

	/**
	 * on remet à 0 les paramètres numériques
	 */
	private void reset_param() {
		lvl_dep = 0;
		dens_dep = 0;
		dens_ligne_dep = 0;
	}

	/**
	 * on met à jour le label score
	 * 
	 * @param score
	 *            le score à mettre
	 * @param lvl
	 *            le niveau à afficher
	 */
	public void update_score(int score, int lvl) {
		this.score.setText(lvl + "   -   " + score);

	}

	/**
	 * on lance le jeu en créant une instance de jeu
	 * 
	 */
	public void lanc_jeu() {
		g = new Game(this, lvl_dep, dens_dep, dens_ligne_dep);
	}

	/**
	 * on initialise la fenetre de jeu
	 * 
	 * @param grid
	 *            la grille à mettre
	 * @param scoreDep
	 *            le score de départ à mettre
	 * @param suiv
	 *            la piece suivante à afficher
	 * @param lvlDep
	 *            le niveau de départ à afficher
	 */
	public void init_grid(Grid grid, int scoreDep, Tetromino suiv, int lvlDep) {
		// on met les checkbox suivant les paramètres de base
		pieceSuivante.setSelected(aff_suivant);
		checkSon.setSelected(son);
		// on définit la taille suivant la taille de base et on remet la fenetre
		// au milieu
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setLocationRelativeTo(null);
		// on créée les panel pour la grille de jeu et la piece suivante
		this.panelGrid = new PanelGrid(grid, 0, 0);
		this.suivantGrid = new PanelGrid(new Grid(6, 6), 370, 90, 20);
		// on ajoute la barre de menu
		this.setJMenuBar(menuBar);
		// container principal (sans layout)
		JPanel container = new JPanel(null);
		// on met sa dimension à la taille de la fenetre
		container.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		// on crée les boutons du côté
		replay = new Button("Reprendre", 100, 50, 370, 280);
		bpause = new Button("Pause", 100, 50, 370, 220);
		// besoin du html et br pour faire un saut de ligne dans un label
		restart = new Button("<html>Nouvelle<br/>Partie</html>", 100, 50, 370,
				340);
		// idem que pour le bouton de la barre de menu (nouvelle partie)
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reprendre.setEnabled(false);
				pause.setEnabled(true);
				replay.setEnabled(false);
				bpause.setEnabled(true);
				if (lvl_dep != 0 || dens_dep != 0 || dens_ligne_dep != 0) {
					int j = JOptionPane.showConfirmDialog(null,
							"Voulez vous garder les même paramètres ?",
							"Paramètres", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (j == JOptionPane.NO_OPTION) {
						reset_param();
					} else {
						if (j == JOptionPane.CANCEL_OPTION) {
							return;
						}
					}
				}
				g.reset_jeu(lvl_dep, dens_dep, dens_ligne_dep);

			}
		});
		// idem (pause)
		bpause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause.setEnabled(false);
				bpause.setEnabled(false);
				replay.setEnabled(true);
				reprendre.setEnabled(true);
				g.pause();

			}
		});
		// idem (reprendre)
		replay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reprendre.setEnabled(false);
				pause.setEnabled(true);
				replay.setEnabled(false);
				bpause.setEnabled(true);
				g.reprendre();

			}
		});
		// on ajoute un label pour indiquer le niveau et le score
		Label scoreLabel = new Label("Niveau  -  Score : ", 100, 25, 370, 15);
		// on met le score de départ
		score = new Label(lvlDep + "    -    " + scoreDep, 100, 25, 370, 35);
		// met l'opacité
		score.setOpaque(true);
		score.setBackground(Color.BLACK);
		score.setForeground(Color.RED);
		// bouton retour qui ramène à l'accueil
		Button retour = new Button("Retour", 100, 50, 370, 400);
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.couper();
				aff_accueil();
			}
		});
		// on ajoute les élèments dans le container
		container.add(panelGrid);
		container.add(replay);
		container.add(bpause);
		container.add(restart);
		container.add(scoreLabel);
		container.add(score);
		container.add(retour);
		container.add(suivantGrid);
		container.add(label_suiv);
		// on empeche le focus des boutons (on garde toujours le focus sur la
		// grille sinon on ne peut plus jouer au clavier)
		restart.setFocusable(false);
		replay.setFocusable(false);
		bpause.setFocusable(false);
		retour.setFocusable(false);
		if (!aff_suivant) {
			// si on ne veut pas afficher les suivants on désactive l'affichage
			// de la piece suivante
			suivantGrid.setVisible(false);
			label_suiv.setVisible(false);
		}
		replay.setEnabled(false);
		// on attribue le contentpane au container principal
		this.setContentPane(container);
		this.validate();
		this.repaint();
		// on repeint

	}

	private void affichage_aide() {
		// on crée une fenetre de dialog d'aide
		new DialogHelp(this);
	}

	private void param() {
		// on enleve la barre de menu
		setJMenuBar(null);
		// on recentre la fenetre
		setLocationRelativeTo(null);
		// on met la dimension
		setSize(new Dimension(460, 200));
		// on empeche le redimensionnement
		setResizable(false);
		JPanel p = new JPanel();
		// panel pour le pré-remplissage
		JPanel pan_preremplissage = new JPanel();
		// layout pour placer les objets
		pan_preremplissage.setLayout(new GridLayout(2, 2));
		pan_preremplissage.setPreferredSize(new Dimension(200, 60));
		// bordure du panel
		pan_preremplissage.setBorder(BorderFactory
				.createTitledBorder("Pré-remplissage"));
		String[] dens_pourcent = { "0%", "10%", "20%", "30%", "40%", "50%",
				"60%", "70%", "80%", "90%" };
		JLabel densite = new JLabel("Densité : ");
		// combobox pour la densité
		final JComboBox<String> densite_choix = new JComboBox<String>(dens_pourcent);
		Integer[] nb_ligne = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 15, 16, 17 };
		JLabel s_nb_ligne = new JLabel("Ligne :");
		// combobox pour le choix de ligne
		final JComboBox<Integer> nb_pre_remp = new JComboBox<Integer>(nb_ligne);
		// définition des choix de départ
		densite_choix.setSelectedIndex(dens_dep / 10);
		nb_pre_remp.setSelectedIndex(dens_ligne_dep);
		// on ajoute les élèments au panel
		pan_preremplissage.add(densite);
		pan_preremplissage.add(densite_choix);
		pan_preremplissage.add(s_nb_ligne);
		pan_preremplissage.add(nb_pre_remp);

		JPanel pan_suiv = new JPanel();
		pan_suiv.setLayout(new GridLayout(1, 2));
		pan_suiv.setPreferredSize(new Dimension(200, 50));
		pan_suiv.setBorder(BorderFactory.createTitledBorder("Piece suivante"));
		final JCheckBox p_suiv = new JCheckBox();
		// on coche la checkbox suivant l'actuel affichage
		p_suiv.setSelected(aff_suivant);
		JLabel s_p_suiv = new JLabel("Pièce suivante :");
		pan_suiv.add(s_p_suiv);
		pan_suiv.add(p_suiv);

		JPanel pan_son = new JPanel();
		pan_son.setLayout(new GridLayout(1, 2));
		pan_son.setPreferredSize(new Dimension(200, 50));
		pan_son.setBorder(BorderFactory.createTitledBorder("Son"));
		final JCheckBox p_son = new JCheckBox();
		p_son.setSelected(son);
		JLabel s_p_son = new JLabel("Son :");
		pan_son.add(s_p_son);
		pan_son.add(p_son);

		JPanel pan_niv = new JPanel();
		pan_niv.setLayout(new GridLayout(1, 2));
		pan_niv.setPreferredSize(new Dimension(200, 50));
		pan_niv.setBorder(BorderFactory.createTitledBorder("Niveau"));
		// on crée un champ de texte pour les nombres (on peut pas mettre de
		// caractères)
		final JFormattedTextField lvl = new JFormattedTextField(
				NumberFormat.getIntegerInstance(Locale.FRENCH));
		lvl.setText(lvl_dep + "");
		JLabel s_lvl = new JLabel("Départ :");
		pan_niv.add(s_lvl);
		pan_niv.add(lvl);
		// on ajoute des bulles d'aides au label
		s_lvl.setToolTipText("Niveau de départ (max 40)");
		s_nb_ligne.setToolTipText("Nombre de ligne pré-rempli");
		densite.setToolTipText("Nombre de case rempli dans une ligne");
		s_p_suiv.setToolTipText("Afficher la pièce suivante");
		s_p_son.setToolTipText("Jouer le son");
		// on ajoutes les pannels secondaires au pannel de base
		p.add(pan_preremplissage, BorderLayout.CENTER);
		p.add(pan_suiv, BorderLayout.CENTER);
		p.add(pan_niv, BorderLayout.CENTER);
		p.add(pan_son, BorderLayout.CENTER);
		// bouton annuler (on retourne à l'accueil sans rien faire)
		JButton retour = new JButton("Annuler");
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aff_accueil();
			}
		});
		// bouton ok (on retourne à l'accueil en sauvegardant les paramètres)
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// on recupère le niveau entré
				// le champ formaté nous donne un espace insécable qu'il faut
				// supprimer
				String s = lvl.getText().replace("\u00A0", "");
				int i = Integer.parseInt(s);
				if (i > 40) {
					// si le niveau est supérieur à 40 on demande si on le
					// ramène à 40 ou si on annule
					int j = JOptionPane.showConfirmDialog(null,
							"Le lvl entré est supérieur à 40, il sera rabaissé à 40"
									+ System.getProperty("line.separator")
									+ System.getProperty("line.separator")
									+ "Voulez-vous continuez ?",
							"Level Incorrect", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (j == JOptionPane.YES_OPTION) {
						i = 40;
					} else {
						return;
					}
				}
				// on enleve les pourcentages et on converti la densité en
				// nombres
				dens_dep = Integer.parseInt(((String) densite_choix
						.getSelectedItem()).replace("%", ""));
				// on recupere le choix du nombre de ligne
				dens_ligne_dep = (int) nb_pre_remp.getSelectedItem();
				lvl_dep = i;
				aff_suivant = p_suiv.isSelected();
				son = p_son.isSelected();
				// on retourne à l'accueil
				aff_accueil();

			}
		});

		JPanel p2 = new JPanel();
		p2.add(ok);
		p2.add(retour);
		p.add(p2, BorderLayout.SOUTH);
		// on ajoute le panel principal en tant que contentpane
		setContentPane(p);
		validate();
		repaint();
		// on redessine la fenetre
	}

	public PanelGrid getPanelGrid() {
		return panelGrid;
	}

	public PanelGrid getSuivantGrid() {
		return suivantGrid;
	}

	public boolean isSon() {
		return son;
	}
}