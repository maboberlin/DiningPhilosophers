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
package com.digital_indexing.philosophers.auxiliary;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Logger4Philosophers {
	
// ----------------------------- LOGGER INITIALIZATION ------------------------

	public static Logger logger4Philosophers = Logger.getLogger("PhilosopherLogger");
	static {
		Path log4jFile = Paths.get(".", "config", "log4j.properties").toAbsolutePath().normalize(); 
		PropertyConfigurator.configure(log4jFile.toString());
		logger4Philosophers.setLevel(Level.WARN);
	}

}
