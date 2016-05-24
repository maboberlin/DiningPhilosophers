/**
 * "The Dining Club of Philosophers"
 *
 * Copyright (C) 2016 Matthias Boesinger (boesingermatthias@gmail.com).
 *
 * Licensed under GNU General Public License 3.0 or later.
 * Some rights reserved. See COPYING, AUTHORS.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 */
package com.digital_indexing.philosophers.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.digital_indexing.philosophers.auxiliary.Logger4Philosophers;
import com.digital_indexing.philosophers.enums.ModePhil;
import com.digital_indexing.philosophers.listeners.ModeListener;
import com.digital_indexing.philosophers.listeners.ModeSetEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetListener;

/**
 * Main Class for instantiating GUI components, listener system and logic.<br>
 * <p>
 * MVC-Framework realized by the following classes: ModelPhil, ViewPhil, ControllerPhil<br>
 * <p>
 * Explanation of the listener system:<br>
 * - PhilNumberSetListener: If the number of philosophers gets set by option menu the following classes get informed:<br>
 * PhilosopherFrame, ModelPhil, ViewPhil.<br>
 * - ModeListener: If the mode of the simulation gets set by option menu the following classes get informed:<br>
 * ViewPhil<br>
 * - TimeListener: If the philosophizing time of a philosopher ends and is set in the model the following classes get informed by the model ModelPhil:<br>
 * RankingDialog
 * - ModelListener: If the state of a philosopher or the position of a stick changes the following classes get informed by the model ModelPhil:<br>
 * ViewPhil
 * 
 * @author mabo
 *
 */
public class PhilosopherFrame extends JFrame implements PhilNumberSetListener {
	
//	---------------------------- ATTRIBUTES ----------------------------------

	private int nrOfPhilosophers;
	private int actionTime;
	private ModePhil mode;
	
	private ComponentMap componentMap;
	
	private ViewPhil view;
	private ModelPhil model;
	
	private RankingDialog ranking;

	private List<PhilNumberSetListener> philNrSetListener;
	private ModeListener modeListener;
	
	
//	---------------------------- CONSTRUCTOR ----------------------------------
	
	public PhilosopherFrame() 
	{
		super("Dining Club of Philosophers");
		
		//set icon image
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("book_icon.png");
		if (resource != null) {
			ImageIcon imgIco = new ImageIcon(resource);
			setIconImage(imgIco.getImage());
		}
		else {
			Logger4Philosophers.logger4Philosophers.debug(String.format("Could not find icon resource!%n"));
		}
		//set look and feel
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        	Logger4Philosophers.logger4Philosophers.debug(String.format("Could not set native Look&Feel!%n%s", ex.getMessage()));
        }
		
		//set model
		model = ModelPhil.getSingleInstance();
		//set view
		view = ViewPhil.getSingleInstance();
		//build components
		componentMap = new ComponentMap();
		//build logic system
		buildLogic();
		
		//set display
		setJMenuBar((JMenuBar)componentMap.get(ComponentMap.MENU));
		setContentPane(view);

		//set default values
		setNrOfPhilosophers(3);	
		setMode(ModePhil.OFF.toString());
		setActionTime(3000);
		//set frame properties
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Locale.setDefault(Locale.ENGLISH);
		JOptionPane.setDefaultLocale(Locale.ENGLISH);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}

	
//	-------------------- LOGIC BUILD SECTION ---------------------------
	
	private void buildLogic() 
	{
		//vector for listeners
		philNrSetListener = new Vector<PhilNumberSetListener>();
		//connect all listeners
		buildListenerSystem();
		//link different actions with buttons
		setStartAction();
		setStopAction();
		setExitAction();
		setNumberOfPhilosophersDialog();
		setModeDialog();
		setSpeed();
	}


	private void buildListenerSystem() {
		this.philNrSetListener.add(model);	
		this.philNrSetListener.add(view);
		this.philNrSetListener.add(this);
		model.setModelListener(view);
		modeListener = view;
	}
	
	
	private void setStartAction() 
	{
		//get menu components
		final JMenuItem start = (JMenuItem)componentMap.get(ComponentMap.START_MENU);
		final JMenuItem nrOfPhils = (JMenuItem)componentMap.get(ComponentMap.NR_OF_PHILS_MENU);
		final JMenuItem modeM = (JMenuItem)componentMap.get(ComponentMap.MODE_MENU);
		final JMenuItem speed = (JMenuItem)componentMap.get(ComponentMap.SPEED_MENU);
		
		//set start menu action
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nrOfPhils.setEnabled(false);
				modeM.setEnabled(false);
				start.setEnabled(false);
				speed.setEnabled(false);
				model.reset();
				view.paintInitPanel();	
				openTable(mode, nrOfPhilosophers);
				
				//START SIMULATION
				ControllerPhil.runPhilosophers(nrOfPhilosophers, actionTime);
			
			}
		});
	}
	
	
	private void setStopAction()
	{
		//get menu components
		final JMenuItem start = (JMenuItem)componentMap.get(ComponentMap.START_MENU);
		final JMenuItem stop = (JMenuItem)componentMap.get(ComponentMap.STOP_MENU);
		final JMenuItem nrOfPhils = (JMenuItem)componentMap.get(ComponentMap.NR_OF_PHILS_MENU);
		final JMenuItem modeM = (JMenuItem)componentMap.get(ComponentMap.MODE_MENU);
		final JMenuItem speed = (JMenuItem)componentMap.get(ComponentMap.SPEED_MENU);
		
		//set stop menu action
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//STOP SIMULATION
				ControllerPhil.stopPhilosophers();
				
				nrOfPhils.setEnabled(true);
				modeM.setEnabled(true);
				start.setEnabled(true);
				speed.setEnabled(true);
			}
		});
	}
	
	
	private void setExitAction()
	{
		//exit button
		final JMenuItem exit = (JMenuItem)componentMap.get(ComponentMap.EXIT_MENU);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitApplication();
			}
		});
		//x-closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(PhilosopherFrame.this, "Really Exit?", "Quit Application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					exitApplication();
				}
			}
		});
	}
	
	
	private void setNumberOfPhilosophersDialog() 
	{
		//get components
		final JMenuItem nrOfPhils = (JMenuItem)componentMap.get(ComponentMap.NR_OF_PHILS_MENU);
		final JSpinner nrOfPhilSpinner = (JSpinner)componentMap.get(ComponentMap.NR_OF_PHILS_SPINNER);
		final JPanel nrOfPhilPanel = (JPanel)componentMap.get(ComponentMap.NR_OF_PHILS_PANEL);
		
		//set nr of philosopher menu action
		nrOfPhils.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {				
				int n = JOptionPane.showConfirmDialog(PhilosopherFrame.this, nrOfPhilPanel, "Number of Philosophers", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (n == JOptionPane.OK_OPTION) {
					int nr = (Integer) nrOfPhilSpinner.getValue(); 
					setNrOfPhilosophers(nr);	
				}
				else {
					nrOfPhilSpinner.setValue((Object)nrOfPhilosophers);
				}
			}
		});
	}
	
	
	private void setModeDialog()
	{
		//get components
		final JMenuItem mode = (JMenuItem)componentMap.get(ComponentMap.MODE_MENU);
		final ButtonGroup modeSelection = componentMap.getButtonGroup();
		final JPanel modePanel = (JPanel)componentMap.get(ComponentMap.MODE_PANEL);
		
		//set mode menu action
		mode.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(PhilosopherFrame.this, modePanel, "Simulation Mode", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (n == JOptionPane.OK_OPTION) {
					String modeString = modeSelection.getSelection().getActionCommand();
					setMode(modeString);
				}	
			}
		});
	}
	
	
	private void setSpeed() {
		//get components
		final JMenuItem speed = (JMenuItem)componentMap.get(ComponentMap.SPEED_MENU);
		final JSpinner speedSpinner = (JSpinner)componentMap.get(ComponentMap.SPEED_SPINNER);
		final JPanel speedPanel = (JPanel)componentMap.get(ComponentMap.SPEED_PANEL);
		
		//set speed menu action
		speed.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(PhilosopherFrame.this, speedPanel, "Speed Setting", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (n == JOptionPane.OK_OPTION) {
					int actionTime = (Integer) speedSpinner.getValue();
					setActionTime(actionTime);
				}	
				else {
					speedSpinner.setValue((Object)PhilosopherFrame.this.actionTime);
				}
			}
		});
		
	}
	
	
//	------------------------ AUXILIARY METHODS -------------------------------
	
	private void setNrOfPhilosophers(int nr) {
		PhilNumberSetEvent evt = new PhilNumberSetEvent(this, nr);
		for (PhilNumberSetListener listener : philNrSetListener) {
			listener.nrOfPhilosophersSet(evt);
		}
	}
	
	
	@Override
	public void nrOfPhilosophersSet(PhilNumberSetEvent e) {
		this.nrOfPhilosophers = e.nrOfPhilosophers;
		ButtonGroup modeSelection = componentMap.getButtonGroup();
		String modeString = modeSelection.getSelection().getActionCommand();
		ModePhil mode = ModePhil.getMode(modeString);
		openTable(mode, nrOfPhilosophers);
	}
	
	
	private void setMode(String modeString) {
		mode = ModePhil.getMode(modeString);
		openTable(mode, nrOfPhilosophers);
		ModeSetEvent evt = new ModeSetEvent(this, mode);
		modeListener.modeSet(evt);
	}
	
	
	private void setActionTime(int i) {
		actionTime = i;	
	}
	
	
	private void openTable(ModePhil mode, int nrOfPhilosophers) {
		if (ranking != null) {
			ranking.setVisible(false);
			ranking.dispose();
			ranking = null;
		}
		if (mode != ModePhil.OFF) {
			ranking = new RankingDialog(this, mode, nrOfPhilosophers);
			model.setTimeListener(ranking);
		}
	}
	
	
	private void exitApplication() {
		this.setVisible(false);
		this.dispose();
		System.exit(0);			
	}
	
}
