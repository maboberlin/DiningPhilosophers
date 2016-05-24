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
package com.digital_indexing.philosophers.enums;

public enum ModePhil {
	
	OFF, SOCIALIST, NIHILIST, NORMALO;
	
	public static ModePhil getMode(String mode) {
		if (mode.equals(OFF.toString()))
			return OFF;
		else if (mode.equals(SOCIALIST.toString()))
			return SOCIALIST;
		else if (mode.equals(NIHILIST.toString()))
			return NIHILIST;
		else if (mode.equals(NORMALO.toString()))
			return NORMALO;
		else
			return null;
	}
	
	public String getHead() {
		switch (this) {
		case SOCIALIST:
			return "Karl Marx";
		case NIHILIST:
			return "Friedrich Nietzsche";
		case NORMALO:
			return "Paradigm A";
		default:
			return null;
		}
	}
	
	public String getOther() {
		switch (this) {
		case SOCIALIST:
			return "Reactionist";
		case NIHILIST:
			return "Moralist";
		case NORMALO:
			return "Paradigm";
		default:
			return null;
		}
	}

}
