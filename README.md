# JavaCFD
Computational fluid dynamics software written in Java using the Lattice-Boltzmann method. Allows custom-defined, arbitrary geometries in 2D incompressible flow field.

<hr>
TO RUN: Execute JavaCFD.pde in the NonOOP folder of the main branch. The object-oriented version optimizes the Lattice-Boltzmann code for Java but is still a work in progress. Check the demo folder for newest updates on the OOP version.
1) set sliders for velocity and viscosity
2) set display to velocity
3) click start, and draw geometry on the fluid field by clicking and dragging
- set the scaling slider to optimize display
- clicking start again resets the simulation

Note that the simulation is highly CPU intensive and only small fluid fields should be attempted on most machines. 

<hr>

Computational fluid dynamics is used extensively in engineering to accurately model fluid flow and its associated phenomena. Turbulent flow/hypersonic flow/vortex shredding etc can be simulated using the Navier-Stokes equations, which describe fluid flow using the conservation of momenta and mass flow. However, as they are nonlinear partial differential equations, a solver is very difficult to code. 

The Lattice-Boltzmann method works in a way comparable to cellular automata. By discretizing thermal velocities for each element and then reassigning it based on the Boltzmann distribution, it is able to very accurately reconstruct the behavior of the Navier-Stokes equations in subsonic velocities (higher velocities require thermal coupling). In fact it is even more accurate in modeling turbulent flow and is optimized for massively parallel architectures. We have created our own implementation of the Lattice-Boltzmann algorithm with the D2Q9 model (two dimensions, 9 discretized velocities).

Written for Mr. Konstantinovich's AP Computer Science Class, Stuyvesant High School.<br>

References:
The bulk of the math comes from Alex Wagner's paper, A Practical Introduction to the Lattice-Boltzmann Method. https://www.ndsu.edu/fileadmin/physics.ndsu.edu/Wagner/LBbook.pdf <br>
Lindsay Crowl's GSAC Talk, http://www.math.utah.edu/~crowl/pres.pdf <br>
Weber State University's project on Lattice-Boltzmann, http://physics.weber.edu/schroeder/javacourse/LatticeBoltzmann.pdf

*WORK IN PROGRESS*
