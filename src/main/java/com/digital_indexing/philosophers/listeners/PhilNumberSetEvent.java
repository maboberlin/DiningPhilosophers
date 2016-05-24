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
package com.digital_indexing.philosophers.listeners;

import java.util.EventObject;

public class PhilNumberSetEvent extends EventObject {

	public int nrOfPhilosophers;
	
	public PhilNumberSetEvent(Object source, int nr) {
		super(source);
		nrOfPhilosophers = nr;
	}

}
