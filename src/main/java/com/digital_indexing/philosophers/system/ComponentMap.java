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

import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.digital_indexing.philosophers.enums.ModePhil;

public class ComponentMap extends HashMap<String, JComponent> {
	
//	---------------------- COMPONENT NAMES ---------------------------
	
	public static final String RANKING = "ranking";
	
	public static final String NR_OF_PHILS_PANEL = "nrofphilspanel";
	public static final String MODE_PANEL = "modepanel";
	public static final String SPEED_PANEL = "speedpanel";
	
	public static final String MENU = "menu";
	public static final String START_MENU = "startmenu";
	public static final String STOP_MENU = "stopmenu";
	public static final String EXIT_MENU = "exitmenu";
	public static final String NR_OF_PHILS_MENU = "nrofphilsmenu";
	public static final String MODE_MENU = "modemenu";
	public static final String SPEED_MENU = "speedmenu";
	
	public static final String NR_OF_PHILS_SPINNER = "nrofphilsspinner";
	public static final String SPEED_SPINNER = "speedspinner";
	
	
//	------------------------ BUTTON GROUP -----------------------------
	
	private ButtonGroup modeSelection;
	
	
//	------------------------ CONSTRUCTOR ------------------------------
	
	public ComponentMap() 
	{
		// build menu
		final JMenuItem start = new JMenuItem("(Re)Start");
		final JMenuItem stop = new JMenuItem("Stop");
		final JMenuItem exit = new JMenuItem("Exit");
		final JMenuItem nrOfPhils = new JMenuItem("Number of Philosophers");
		final JMenuItem modeM = new JMenuItem("Mode");
		final JMenuItem speed = new JMenuItem("Speed");
		final JMenuBar menubar = buildMenuBar(start, stop, exit, nrOfPhils, modeM, speed);
		put(START_MENU, start);
		put(STOP_MENU, stop);
		put(EXIT_MENU, exit);
		put(NR_OF_PHILS_MENU, nrOfPhils);
		put(MODE_MENU, modeM);
		put(SPEED_MENU, speed);
		put(MENU, menubar);
		
		// build nr of phil panel
		final JSpinner nrOfPhilSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 12, 1));
		final JPanel nrOfPhilsPanel = buildNrOfPhilsPanel(nrOfPhilSpinner);
		put(NR_OF_PHILS_SPINNER, nrOfPhilSpinner);
		put(NR_OF_PHILS_PANEL, nrOfPhilsPanel);
		
		//build mode panel
		final JSpinner speedSpinner = new JSpinner(new SpinnerNumberModel(3000, 1, 60000, 100));
		final JPanel speedPanel = buildSpeedPanel(speedSpinner);
		put(SPEED_SPINNER, speedSpinner);
		put(SPEED_PANEL, speedPanel);
		
		//build 
		modeSelection = new ButtonGroup();
		final JPanel modePanel = buildModePanel(modeSelection);
		put(MODE_PANEL, modePanel);
	}
	
	
//	-------------------------- GETTER -------------------------------------

	public ButtonGroup getButtonGroup() {
		return modeSelection;
	}
	
	
//	------------------------ BUILD SECTION -------------------------------

	private JMenuBar buildMenuBar(JMenuItem start, JMenuItem stop,
			JMenuItem exit, JMenuItem nrOfPhils, JMenuItem modeM,
			JMenuItem speed) 
	{
		final JMenu mSim = new JMenu("Simulation");
		final JMenu mOpt = new JMenu("Options");
		mSim.add(start);
		mSim.add(stop);
		mSim.addSeparator();
		mSim.add(exit);
		mOpt.add(nrOfPhils);
		mOpt.add(modeM);
		mOpt.add(speed);
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(mSim);
		menuBar.add(mOpt);
		return menuBar;
	}

	
	private JPanel buildNrOfPhilsPanel(JSpinner nrOfPhilSpinner) 
	{
		final JPanel nrOfPhilPanel = new JPanel();
		final JLabel label = new JLabel("Number of philosophers:");
		nrOfPhilPanel.add(label);
		nrOfPhilPanel.add(nrOfPhilSpinner);
		return nrOfPhilPanel;
	}
	
	
	private JPanel buildSpeedPanel(JSpinner speedSpinner) {
		final JPanel speedPanel = new JPanel();
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(speedSpinner, "#");
		speedSpinner.setEditor(editor);
		final JLabel label = new JLabel("Max. time for philosopher action (1-60000 ms):");
		speedPanel.add(label);
		speedPanel.add(speedSpinner);
		return speedPanel;
	}
	
	
	private JPanel buildModePanel(ButtonGroup modeSelection) 
	{
		final JPanel modePanel = new JPanel(new GridLayout(4, 2));
		final JLabel offL = new JLabel("Off");
		final JLabel scoialistL = new JLabel("Socialist");
		final JLabel nihilistL = new JLabel("Nihilist");
		final JLabel normaloL = new JLabel("Normalo");
		Font oldFont = offL.getFont();
		Font newFont = new Font(oldFont.getFontName(), oldFont.getStyle(), oldFont.getSize() + 1);
		offL.setFont(newFont);
		scoialistL.setFont(newFont);
		nihilistL.setFont(newFont);
		normaloL.setFont(newFont);
		final JRadioButton off = new JRadioButton();
		final JRadioButton socialist = new JRadioButton();
		final JRadioButton nihilist = new JRadioButton();
		final JRadioButton normalo = new JRadioButton();
		off.setActionCommand(ModePhil.OFF.toString());
		socialist.setActionCommand(ModePhil.SOCIALIST.toString());
		nihilist.setActionCommand(ModePhil.NIHILIST.toString());
		normalo.setActionCommand(ModePhil.NORMALO.toString());
		modePanel.add(offL);
		modePanel.add(off);
		modePanel.add(scoialistL);
		modePanel.add(socialist);
		modePanel.add(nihilistL);
		modePanel.add(nihilist);
		modePanel.add(normaloL);
		modePanel.add(normalo);
		modeSelection.add(off);
		modeSelection.add(socialist);
		modeSelection.add(nihilist);
		modeSelection.add(normalo);
		off.setSelected(true);
		return modePanel;
	}

}
