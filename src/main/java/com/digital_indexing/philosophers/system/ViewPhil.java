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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.digital_indexing.philosophers.enums.ModePhil;
import com.digital_indexing.philosophers.enums.StatePhil;
import com.digital_indexing.philosophers.listeners.ModeListener;
import com.digital_indexing.philosophers.listeners.ModeSetEvent;
import com.digital_indexing.philosophers.listeners.ModelListener;
import com.digital_indexing.philosophers.listeners.PhilChangedEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetListener;
import com.digital_indexing.philosophers.listeners.SticksChangedEvent;

/**
 * Class for painting the main panel.<br>
 * <p>
 * Overiden paintComponent method:<br>
 * - paints whole table in rested position if method is called and no flags are set<br>
 * - only overpaint circles or sticks if toUpdate flag is set. this is the case if the ModelListener methods are invoked.<br>
 * <br>
 * Calculation of the circle and stick position in dependency to the nr of philosophers by sinus/cosinus calculation. Coordinates are stored in arrays scs and ccs.<br>
 * Letters are drawn so that the philosophers can be identified int the different simulation modes.<br>
 * 
 * @author mabo
 *
 */
public class ViewPhil extends JPanel implements PhilNumberSetListener, ModelListener, ModeListener {
	
//	-------------------------------- ATTRUBUTES ----------------------------------
	
	private static final Color TABLE_COLOR = new Color(175, 171, 171);
	
	private static final int W_HEIGHT = 630;
	private static final int W_WIDTH = 600;
	private static final int TRANSLATE_COORDINATES_X = 300;
	private static final int TRANSLATE_COORDINATES_Y = 300;
	
	private static final int CENTER_CIRCLE_DIAMETER = 300; 
	private static final int CIRCLE_DIAMETER = 50; 
	
	private static final Map<StatePhil, Color> COLOR_MAP = new HashMap<>();
	{
		COLOR_MAP.put(StatePhil.WAITING, new Color(255, 0, 0));
		COLOR_MAP.put(StatePhil.EATING, new Color(160, 82, 15));
		COLOR_MAP.put(StatePhil.PHILOSOPHIZING, new Color(0, 255, 0));
	}
	
	private BufferedImage bImg;
	
	private int nrOfPhilosophers;
	private ModePhil mode;
	
	private CircleCoordinates[] ccs = {};
	private StickCoordinates[] scs = {};
	private LetterCoordinates[] lcs = {};
	private StatePhil[] ccState;
	private boolean[] scSet;
	
	private boolean toUpdate;
	private boolean init;
	private PhilChangedEvent philEvt;
	private SticksChangedEvent sticksEvt;
	
	
//	---------------------- CONSTRUCTOR & INSTANCE -----------------------------
	
	private static ViewPhil instance;
	
	private ViewPhil() {
		makeBImg();
	}

	private void makeBImg() {
		//instantiate buffered image as background
		bImg = new BufferedImage(W_WIDTH, W_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bImg.createGraphics();
		//paint background
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, W_WIDTH, W_HEIGHT);
		//set center
		g2.translate(TRANSLATE_COORDINATES_X, TRANSLATE_COORDINATES_Y);
		//paint main circle
		g2.setColor(TABLE_COLOR);
		g2.fillOval(-150, -150, CENTER_CIRCLE_DIAMETER, CENTER_CIRCLE_DIAMETER);
		g2.dispose();
	}
	
	public static ViewPhil getSingleInstance() {
		if (instance == null)
			instance = new ViewPhil();
		return instance;
	}
	
	
//	-------------------------------- GETTER ----------------------------------
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(W_WIDTH, W_HEIGHT);
	};
	
	
//	------------------------ PAINT COMPONENT ----------------------------------
		
	@Override
	protected void paintComponent( Graphics g ) {
		Graphics2D g2 = ( Graphics2D ) g;
		
		//paint whole window
		if (!toUpdate) {
			paintPanelEntire(g2);	
		}
		//paint update
		else {
			overpaintPanelPart(g2);
		}
		
		//reset flags
		philEvt = null;
		sticksEvt = null;
		toUpdate = false;
		init = false;
	}

	private void paintPanelEntire(Graphics2D g2) {
		//paint background
		super.paintComponent(g2);
		if (bImg != null) {
			g2.drawImage(bImg, 0, 0, this);
		}
		//set center
		g2.translate(TRANSLATE_COORDINATES_X, TRANSLATE_COORDINATES_Y);
		
		//case 1: initialization
		if (init) {
			//paint philosophers
			g2.setColor(COLOR_MAP.get(StatePhil.WAITING));
			for (CircleCoordinates ccs : ccs) {
				g2.fillOval(ccs.x, ccs.y, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
			}
			//paint sticks
			g2.setColor(Color.WHITE);
			StickCoordinates sc;
			for (int i = 1; i < scs.length; i += 3) {
				sc = scs[i];
				g2.setStroke(new BasicStroke(3.0f));
				g2.drawLine(sc.x1, sc.y1, sc.x2, sc.y2);
			}
		}
		//case 1: general repainting
		else {
			//paint philosophers
			CircleCoordinates cc;
			for (int i = 0; i < ccs.length; i++) {
				cc = ccs[i];
				g2.setColor(COLOR_MAP.get(ccState[i]));
				g2.fillOval(cc.x, cc.y, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
			}
			//paint sticks
			StickCoordinates sc;
			for (int i = 0; i < scSet.length; i++) {
				sc = scs[i];
				g2.setStroke(new BasicStroke(3.0f));
				if (scSet[i])
					g2.setColor(Color.WHITE);
				else
					g2.setColor(Color.BLACK);
				g2.drawLine(sc.x1, sc.y1, sc.x2, sc.y2);
			}
		}
		
		//draw letters
		g2.setColor(Color.BLACK);
		for (LetterCoordinates lc : lcs) {
			g2.drawString(lc.letter, lc.x, lc.y);
		}
		//write bottom text
		g2.setColor(Color.WHITE);
		Font f = new Font ("Arial", Font.PLAIN, 16);
		g2.setFont(f); 
		g2.drawString("Waiting", -240, 310);
		g2.drawString("Eating", -130, 310);
		g2.drawString("Philosophizing", -30, 310);
		g2.setColor(COLOR_MAP.get(StatePhil.WAITING));
		g2.fillRect(-270, 297, 16, 16);
		g2.setColor(COLOR_MAP.get(StatePhil.EATING));
		g2.fillRect(-160, 297, 16, 16);
		g2.setColor(COLOR_MAP.get(StatePhil.PHILOSOPHIZING));
		g2.fillRect(-60, 297, 16, 16);
	}
	
	
	private void overpaintPanelPart(Graphics2D g2) {
		//set center
		g2.translate(TRANSLATE_COORDINATES_X, TRANSLATE_COORDINATES_Y);
		
		//case 1: phil state changed -> repaint circle (first black, the with new color)
		if (philEvt != null) {
			int philNr = philEvt.philNr;
			CircleCoordinates cc = ccs[philNr];
			Color col = COLOR_MAP.get(philEvt.state);
			//paint circle
			g2.setColor(Color.BLACK);
			g2.fillOval(cc.x, cc.y, CIRCLE_DIAMETER + 2, CIRCLE_DIAMETER + 2);
			g2.setColor(col);
			g2.fillOval(cc.x, cc.y, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
			//draw letter
			LetterCoordinates lc = lcs[philNr];
			g2.setColor(Color.BLACK);
			g2.drawString(lc.letter, lc.x, lc.y);
			//set state
			ccState[philNr] = philEvt.state;
		}
		
		//case 2: stick position changed -> paint new stick position white, old stick position black
		else if (sticksEvt != null) {
			int firstNr = sticksEvt.firstNr;
			int secondNr = sticksEvt.secondNr;
			StickCoordinates cc1 = scs[firstNr];
			StickCoordinates cc2 = scs[secondNr];
			//paint
			g2.setStroke(new BasicStroke(3.0f));
			g2.setColor(Color.WHITE);
			g2.drawLine(cc1.x1, cc1.y1, cc1.x2, cc1.y2);
			g2.setColor(Color.BLACK);
			g2.drawLine(cc2.x1, cc2.y1, cc2.x2, cc2.y2);
			scSet[firstNr] = true;
			scSet[secondNr] = false;
		}
	}
	
	
//	------------------------ PAINT INVOKING METHODS ------------------------
	
	public void paintInitPanel() {
		init = true;
		repaint();
	}
	
	
	@Override
	public void nrOfPhilosophersSet( PhilNumberSetEvent e ) {
		nrOfPhilosophers = e.nrOfPhilosophers;
		calculateCoordinates();
		setLetters();
		paintInitPanel();
	}
	
	
	@Override
	public void modeSet(ModeSetEvent e) {
		mode = e.mode;
		setLetters();
		paintInitPanel();
	}
	
	
	@Override
	public void philStateChanged(PhilChangedEvent e) {
		toUpdate = true;
		if (SwingUtilities.isEventDispatchThread()) {
			philEvt = e;
			paintImmediately(0, 0, W_WIDTH, W_HEIGHT);
		}
	}

	@Override
	public void sticksChanged(SticksChangedEvent e) {
		toUpdate = true;
		if (SwingUtilities.isEventDispatchThread()) {
			sticksEvt = e;
			paintImmediately(0, 0, W_WIDTH, W_HEIGHT);
		}
	}

	
//	--------------------------- CALCULATIONS ------------------------------- 

	private void calculateCoordinates() 
	{
		//calculate circle positions and letter coordinates
		ccs = new CircleCoordinates[ nrOfPhilosophers ];
		lcs = new LetterCoordinates[ nrOfPhilosophers ];
		ccState = new StatePhil[ nrOfPhilosophers ];
		final double angle_const = 360.0 / (double) nrOfPhilosophers;
		double angle = 90.0;
		CircleCoordinates cc;
		LetterCoordinates lc;
		for (int i = 0; i < nrOfPhilosophers; i++) {
			cc = new CircleCoordinates();
			lc = new LetterCoordinates();
			cc.x = ((int)(Math.cos(Math.toRadians( angle )) * -250.0) - CIRCLE_DIAMETER / 2);
			cc.y = ((int)(Math.sin(Math.toRadians( angle )) * -250.0) - CIRCLE_DIAMETER / 2);
			lc.x = cc.x + (CIRCLE_DIAMETER / 2) - 5;
			lc.y = cc.y + (CIRCLE_DIAMETER / 2) + 5;
			ccs[i] = cc;
			lcs[i] = lc;
			ccState[i] = StatePhil.WAITING;
			angle += angle_const;
		}
		
		//calculate stick positions
		scs = new StickCoordinates[ nrOfPhilosophers * 3 ];
		scSet = new boolean[ nrOfPhilosophers * 3 ];
		double angle2_const = 360.0 / (double) ( 4 * nrOfPhilosophers );
		double angle2 = 90.0;
		StickCoordinates sc;
		int arix = 0;
		for (int i = 1; i <= nrOfPhilosophers * 4; i++) {
			angle2 += angle2_const;
			if (!(i % 4 == 0)) {
				sc = new StickCoordinates();
				sc.x1 = Math.negateExact((int)(Math.cos(Math.toRadians( angle2 )) * 270.0));
				sc.y1 = Math.negateExact((int)(Math.sin(Math.toRadians( angle2 )) * 270.0));
				sc.x2 = Math.negateExact((int)(Math.cos(Math.toRadians( angle2 )) * 230.0));
				sc.y2 = Math.negateExact((int)(Math.sin(Math.toRadians( angle2 )) * 230.0));
				scs[arix] = sc;
				if (i % 2 == 0)
					scSet[arix] = true;
				else
					scSet[arix] = false;
				arix++;
			}
		}
	}
	
	
	private void setLetters() {
		if (mode == null)
			return;
		int startCodePoint = ModePhil.NORMALO == mode ? 65 : 64;
		//first letter
		switch (mode) {
		case NORMALO:
			lcs[0].letter = "A";
			break;
		case SOCIALIST:
			lcs[0].letter = "M";
			break;
		case NIHILIST:
			lcs[0].letter = "N";
			break;
		case OFF:
			lcs[0].letter = "";
			break;
		}
		//following letters
		for (int i = 1; i < lcs.length; i++) {
			lcs[i].letter = mode == ModePhil.OFF ? "" : String.valueOf((char)(startCodePoint + i));
		}
	}

}


// -------------------------------- DATA CLASSES -----------------------------

class CircleCoordinates {
	int x;
	int y;
}


class StickCoordinates {
	int x1;
	int y1;
	int x2;
	int y2;
}

class LetterCoordinates {
	String letter;
	int x;
	int y;
}
