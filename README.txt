"The Dining Club of Philosophers"

Copyright (C) 2016 Matthias Boesinger (boesingermatthias@gmail.com).

Licensed under GNU General Public License 3.0 or later.
Some rights reserved. See COPYING, AUTHORS.

@license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>


------------------------------------------------------------------------

"Dining Club of Philosophers" is an implementation of Dijkstras 'dining philosophers problem' as a gui-application.

see: 
https://en.wikipedia.org/wiki/Dining_philosophers_problem

An executable file (DiningPhilosophers.exe) can be found at /target/DiningPhilosophers.exe.

A description of the application is given by the class-documentations of the classes:
- PhilosopherFrame (central class for building the system)
- ModelPhil (model)
- ViewPhil (view)
- Philosopher (a single thread of a philosopher)

The application offers the following options:
1. The number of philosophers can be set (between 3 and 12)
2. The maximum time for eating respectively philosophizing can be set (between 1 and 60000 miliseconds)
3. A mode of the philosophers problem can be set. Possible modes are: Socialist, Nihilist, Normalo.
If the mode has been set the total philosophizing time of each philosopher is set in a ranking.
In this ranking each philosopher has a distinct designation.

... I hope you can grasp the message of the designations of the different modes even if you didn't study philosophy ;)

