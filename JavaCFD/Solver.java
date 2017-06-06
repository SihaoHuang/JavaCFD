public class Solver{
  
    int rows, cols;
    Element[][] fluidField; //passed down from Simulation with boundary conditions properly configured
    int time;
    double viscosity; //fluid viscosity, user input
    double overallVelocity; //wind tunnel velocity, user input
    
    public Solver(Element[][] fluidField, double overallVelocity, double viscosity){
  		//initializes the simulation
  		//create elements for each block in the display
      this.viscosity = viscosity;
      this.overallVelocity = overallVelocity;
  		time = 0;
  		fluidField = this.fluidField;
  		rows = fluidField.length;
  		cols= fluidField[0].length;
  		//initiate fluid with discretized velocity vectors
      //the probabilities derived from Boltzmann distribution; Weber State University paper
      //does not matter if element is solid as the solver disregards velocity data 
  		for(int r = 0; r < rows; r++){
  			for(int c = 0; c< cols; c++){
          fluidField[r][c].setHere(4.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity));
  			  fluidField[r][c].setUp(1.0/9.0 * * (1 - 1.5*overallVelocity*overallVelocity));
          fluidField[r][c].setDown(1.0/9.0 * * (1 - 1.5*overallVelocity*overallVelocity));
          fluidField[r][c].setLeft(1.0/9.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity));
          fluidField[r][c].setRight(1.0/9.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity));
          fluidField[r][c].setNorthEast(1.0/36.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity)); 
          fluidField[r][c].setNorthWest(1.0/36.0 * (1 - 3*overallVelocity + 3*overallVelocity*overallVelocity)); 
          fluidField[r][c].setSouthEast(1.0/36.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity)); 
          fluidField[r][c].setSouthWest(1.0/36.0 * (1 - 3*overallVelocity + 3*overallVelocity*overallVelocity)); 
  			}
  		}
  		//set initial time
  		time = 0;	
    }
    
    public void iterate(){ //executes one iteration of algorithm
    	collide();
    	move();
      collideBounary();
      time += 1;
    }
    private void collide(){ //f(x,t+deltat)=f(x,t)+ (feq - finitial)/(tau)
	    double relaxationTime = 1 / (3*viscosity + 0.5); //omega in the equation per iteration
    	for (int r = 0; r < rows; r++){
    	    for (int c = 0; c < cols; c++){
            if(!fluidField[r][c].isSolid()){
              sumVelocities = fluidField[r][c].sumVelocities();
              fluidField[r][c].setDensity(sumVelocities);
              int xVelocity, yVelocity;
              if(sumVelocities > 0){
                xVelocity = (fluidField[r][c].getRight() - fluidField[r][c].getNorthEast() - fluidField[r][c].getSouthEast() 
                            - fluidField[r][c].getLeft() - fluidField[r][c].getNorthWest() - fluidField[r][c].getSouthWest()) / sumVelocities);
                yVelocity = (fluidField[r][c].getUp() - fluidField[r][c].getNorthEast() - fluidField[r][c].getNorthWest()
                            - fluidField[r][c].getDown() - fluidField[r][c].getSouthEast() - fluidField[r][c].getSouthWest() / sumVelocities);
              }
              else{
                xVelocity = 0;
                yVelocity = 0;
              }
              fluidField[r][c].setHere(relaxationTime)
              fluidField[r][c].setUp()
              fluidField[r][c].setDown()
              fluidField[r][c].setLeft()
              fluidField[r][c].setRight()
              fluidField[r][c].setNorthEast();
              fluidField[r][c].setNorthWest();
              fluidField[r][c].setSouthEast();
              fluidField[r][c].setSouthWest();

            }
    	    }
    	}
    }
    private void move(){
	//f(x+e*deltat, t+deltat)=f(x,t+deltat)	
    }
    
    private void collideBoundary(){ //performs collisions for boundary conditions
      for(int r = 0; r < rows; r++){
        for(int c = 0; c < cols; c++){
          if(fluidField[r][c].isSolid()){ 
            //for every direction, bounce back in opposite direction by setting the element as v_original + v_bounced
            //then set the local velocity vector as zero
            if (fluidField[r][c].getUp() > 0){
              fluidField[r][c - 1].setDown(fluidField[r][c - 1].getDown() + fluidField[r][c].getUp());
              fluidField[r][c].setUp(0.0);
            }
            if (fluidField[r][c].getDown() > 0){
              fluidField[r][c + 1].setUp(fluidField[r][c + 1].getUp() + fluidField[r][c].getDown());
              fluidField[r][c].setDown(0.0);
            }
            if (fluidField[r][c].getLeft() > 0){
              fluidField[r + 1][c].setRight(fluidField[r + 1][c].getRight() + fluidField[r][c].getLeft());
              fluidField[r][c].setLeft(0.0);
            }
            if (fluidField[r][c].getRight() > 0){
              fluidField[r - 1][c].setLeft(fluidField[r - 1][c].getLeft() + fluidField[r][c].getRight());
              fluidField[r][c].setRight(0.0);
            }
            if (fluidField[r][c].getNorthEast() > 0){
              fluidField[r - 1][c - 1].setSouthWest(fluidField[r - 1][c - 1].getSouthWest() + fluidField[r][c].getNorthEast());
              fluidField[r][c].setNorthEast(0.0);
            }
            if (fluidField[r][c].getNorthWest() > 0){
              fluidField[r + 1][c - 1].setSouthEast(fluidField[r + 1][c - 1].getSouthEast() + fluidField[r][c].getNorthWest());
              fluidField[r][c].setNorthWest(0.0);
            }
            if (fluidField[r][c].getSouthEast() > 0){
              fluidField[r - 1][c + 1].setNorthWest(fluidField[r - 1][c + 1].getNorthWest() + fluidField[r][c].getSouthEast());
              fluidField[r][c].setSouthEast(0.0);
            }
            if (fluidField[r][c].getSouthWest() > 0){
              fluidField[r + 1][c + 1].setNorthEast(fluidField[r + 1][c + 1].getNorthEast() + fluidField[r][c].getSouthWest());
              fluidField[r][c].setSouthWest(0.0);
            }
          }
        }
      }
    }
    
}