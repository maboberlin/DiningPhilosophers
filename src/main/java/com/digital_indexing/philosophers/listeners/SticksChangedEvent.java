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

public class SticksChangedEvent extends EventObject {
	
	public int firstNr;
	public int secondNr;
	public boolean firstState;
	public boolean secondState;

	public SticksChangedEvent(Object source, int firstNr, boolean firstState, int secondNr, boolean secondState) {
		super(source);
		this.firstNr = firstNr;
		this.firstState = firstState;
		this.secondNr = secondNr;
		this.secondState = secondState;
	}

}
