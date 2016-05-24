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

import com.digital_indexing.philosophers.enums.ModePhil;

public class ModeSetEvent extends EventObject {
	
	public ModePhil mode;

	public ModeSetEvent(Object source, ModePhil mode) {
		super(source);
		this.mode = mode;
	}

}
