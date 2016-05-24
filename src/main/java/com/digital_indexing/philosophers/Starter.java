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
package com.digital_indexing.philosophers;

import javax.swing.SwingUtilities;

import com.digital_indexing.philosophers.system.PhilosopherFrame;

public class Starter {

	/**
	 * Start Application by opening central building class: PhilosopherFrame.
	 * @param args none
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PhilosopherFrame x = new PhilosopherFrame();
				x.setVisible(true);	
			}
		});
	}

}
