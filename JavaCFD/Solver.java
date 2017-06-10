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
  		this.fluidField = fluidField;
  		rows = fluidField.length;
  		cols = fluidField[0].length;
  		//initiate fluid with discretized velocity vectors
      //the probabilities derived from Boltzmann distribution; Weber State University paper
      //does not matter if element is solid as the solver disregards velocity data 
      double here = 4.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double up = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double down = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double left = 1.0/9.0 * (1 - 3*overallVelocity + 3*overallVelocity*overallVelocity);
      double right = 1.0/9.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity);
      double northEast = 1.0/36.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity);
      double northWest = 1.0/36.0 * (1 - 3*overallVelocity + 3*overallVelocity*overallVelocity);
      double southEast = 1.0/36.0 * (1 + 3*overallVelocity + 3*overallVelocity*overallVelocity);
      double southWest = 1.0/36.0 * (1 - 3*overallVelocity + 3*overallVelocity*overallVelocity);
  		for(int r = 0; r < rows; r++){
  			for(int c = 0; c< cols; c++){
          if(fluidField[r][c].isSolid()){ //this section is not necessary
            fluidField[r][c].setHere(0.0);
            fluidField[r][c].setUp(0.0);
            fluidField[r][c].setDown(0.0);
            fluidField[r][c].setLeft(0.0);
            fluidField[r][c].setRight(0.0);
            fluidField[r][c].setNorthEast(0.0); 
            fluidField[r][c].setNorthWest(0.0); 
            fluidField[r][c].setSouthEast(0.0); 
            fluidField[r][c].setSouthWest(0.0); 
          }
          fluidField[r][c].setHere(here);
  			  fluidField[r][c].setUp(up);
          fluidField[r][c].setDown(down);
          fluidField[r][c].setLeft(left);
          fluidField[r][c].setRight(right);
          fluidField[r][c].setNorthEast(northEast); 
          fluidField[r][c].setNorthWest(northWest); 
          fluidField[r][c].setSouthEast(southEast); 
          fluidField[r][c].setSouthWest(southWest); 
  			}
  		}
  		//set initial time
  		time = 0;	
    }
    
    public void iterate(){ //executes one iteration of algorithm
    	collide();
    	move();
      collideBoundary();
      time += 1;
    }
    
    private void collide(){ //f(x,t+deltat)=f(x,t)+ (feq - finitial)/(tau)
	    double relaxationTime = 1 / (3*viscosity + 0.5); //omega in the equation per iteration
    	for (int r = 0; r < rows; r++){
    	    for (int c = 0; c < cols; c++){
            if(!fluidField[r][c].isSolid()){
              double sumVelocities = fluidField[r][c].sumVelocities();
              fluidField[r][c].setDensity(sumVelocities);
              
              //calculate the two basis vectors
              double xVelocity, yVelocity;
              if(sumVelocities > 0){
                xVelocity = (fluidField[r][c].getRight() - fluidField[r][c].getNorthEast() - fluidField[r][c].getSouthEast() 
                            - fluidField[r][c].getLeft() - fluidField[r][c].getNorthWest() - fluidField[r][c].getSouthWest()) / sumVelocities;
                yVelocity = (fluidField[r][c].getUp() - fluidField[r][c].getNorthEast() - fluidField[r][c].getNorthWest()
                            - fluidField[r][c].getDown() - fluidField[r][c].getSouthEast() - fluidField[r][c].getSouthWest()) / sumVelocities;
              }
              else{
                xVelocity = 0;
                yVelocity = 0;
              }

              //temporary variables for the quadratic expressoins
              double xVelocitySquared = xVelocity*xVelocity;
              double yVelocitySquared = yVelocity*yVelocity;
              double twoxyVelocities = xVelocity*yVelocity*2;
              double velocitySquaredSum = xVelocitySquared + yVelocitySquared;
              
              //collision code: add new value according to Lattice-Boltzmann algorithm
              fluidField[r][c].setHere(fluidField[r][c].getHere() + relaxationTime * ((4.0/9.0) * sumVelocities * 
                                      (1 - velocitySquaredSum*1.5) - fluidField[r][c].getHere()));

              fluidField[r][c].setLeft(fluidField[r][c].getLeft() + relaxationTime * ((1.0/9.0) * sumVelocities * 
                                      (1 - 3*xVelocity + 4.5*xVelocitySquared - velocitySquaredSum*1.5) - fluidField[r][c].getLeft()));

              fluidField[r][c].setRight(fluidField[r][c].getRight() + relaxationTime * ((1.0/9.0) * sumVelocities * 
                                       (1 + 3*xVelocity + 4.5*xVelocitySquared - velocitySquaredSum*1.5) - fluidField[r][c].getRight()));

              fluidField[r][c].setUp(fluidField[r][c].getUp() + relaxationTime * ((1.0/9.0) * sumVelocities * 
                                    (1 + 3*yVelocity + 4.5*yVelocitySquared - velocitySquaredSum*1.5) - fluidField[r][c].getUp()));

              fluidField[r][c].setDown(fluidField[r][c].getDown() + relaxationTime * ((1.0/9.0) * sumVelocities * 
                                      (1 - 3*yVelocity + 4.5*yVelocitySquared - velocitySquaredSum*1.5) - fluidField[r][c].getDown()));

              fluidField[r][c].setNorthEast(fluidField[r][c].getNorthEast() + relaxationTime * (1.0/36.0) * sumVelocities * 
                                           (1 + 3*xVelocity + 3*yVelocity + 4.5*(velocitySquaredSum + twoxyVelocities) - 
                                           velocitySquaredSum*1.5) - fluidField[r][c].getNorthEast());

              fluidField[r][c].setNorthWest(fluidField[r][c].getNorthWest() + relaxationTime * (1.0/36.0) * sumVelocities * 
                                           (1 - 3*xVelocity + 3*yVelocity + 4.5*(velocitySquaredSum - twoxyVelocities) - 
                                           velocitySquaredSum*1.5) - fluidField[r][c].getNorthWest());

              fluidField[r][c].setSouthEast(fluidField[r][c].getSouthEast() + relaxationTime * (1.0/36.0) * sumVelocities * 
                                           (1 + 3*xVelocity - 3*yVelocity + 4.5*(velocitySquaredSum - twoxyVelocities) - 
                                           velocitySquaredSum*1.5) - fluidField[r][c].getSouthEast());

              fluidField[r][c].setSouthWest(fluidField[r][c].getSouthWest() + relaxationTime * (1.0/36.0) * sumVelocities * 
                                           (1 - 3*xVelocity - 3*yVelocity + 4.5*(velocitySquaredSum + twoxyVelocities) - 
                                           velocitySquaredSum*1.5) - fluidField[r][c].getSouthWest());

            }
    	    }
    	}
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
    
    private void move(){ //f(x+e*deltat, t+deltat)=f(x,t+deltat)  

      //start with the two edges
      for(int c = 0; c < cols - 1; c ++){
        fluidField[0][c].setDown(fluidField[0][c + 1].getDown());
      }
      for(int c = cols - 1; c > 0; c --){
        fluidField[rows - 1][c].setUp(fluidField[rows -1][c - 1].getUp());
      }

      //move elements using four corner vectors, handling all 8 directions
      for(int r = 0; r < rows - 1; r ++){
        for(int c = cols - 1; c > 0; c --){
          fluidField[r][c].setUp(fluidField[r][c - 1].getUp()); //move up
          fluidField[r][c].setNorthWest(fluidField[r + 1][c - 1].getNorthWest()); //move northwest
        }
      }
      for(int r = 0; r < rows - 1; r ++){
        for(int c = 0; c < cols - 1; c ++){
          fluidField[r][c].setLeft(fluidField[r + 1][c].getLeft()); //move left
          fluidField[r][c].setSouthWest(fluidField[r + 1][c + 1].getSouthWest()); //move southwest
        }
      }
      for(int r = rows - 1; r > 0; r --){
        for(int c = cols - 1; c > 0; c --){
          fluidField[r][c].setRight(fluidField[r - 1][c].getRight()); //move right
          fluidField[r][c].setNorthEast(fluidField[r - 1][c - 1].getNorthEast()); //move northeast
        }
      }
      for(int r = rows - 1; r > 0; r --){
        for(int c = 0; c < cols - 1; c ++){
          fluidField[r][c].setDown(fluidField[r][c + 1].getDown()); //move down
          fluidField[r][c].setSouthEast(fluidField[r - 1][c + 1].getSouthEast()); //move southeast
        }
      }

      //set of constants, most of these are for later. MOVE AS INSTANCE VARIABLES IF NEEDED
      double threeTimesOverallVelocity = 3*overallVelocity;
      double threeTimesOverallVelocitySquared = threeTimesOverallVelocity*overallVelocity;
      double here = 4.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double up = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double down = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double left = 1.0/9.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double right = 1.0/9.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double northEast = 1.0/36.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double northWest = 1.0/36.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double southEast = 1.0/36.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double southWest = 1.0/36.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      
      //stream inlet conditions
      for(int c = 0; c < cols; c ++){
        if(!fluidField[0][c].isSolid()){
          // fluidField[0][c].setRight(20);
          // fluidField[0][c].setNorthEast(30);
          // fluidField[0][c].setSouthEast(10);
          fluidField[0][c].setRight(right);
          fluidField[0][c].setNorthEast(northEast);
          fluidField[0][c].setSouthEast(southEast);
        }
      }

      //stream outlet conditions. MIGHT NOT BE REQUIRED
      for(int c = 0; c < cols; c ++){
        fluidField[rows - 1][c].setLeft(left);
        fluidField[rows - 1][c].setNorthWest(northWest);
        fluidField[rows - 1][c].setSouthWest(southWest);
      }

      //stream top and bottom
      for (int r = 0; r < rows; r ++) {
        
        //reinit top boundary
        fluidField[r][0].setHere(here);
  			fluidField[r][0].setUp(up);
        fluidField[r][0].setDown(down);
        fluidField[r][0].setLeft(left);
        fluidField[r][0].setRight(right);
        fluidField[r][0].setNorthEast(northEast); 
        fluidField[r][0].setNorthWest(northWest); 
        fluidField[r][0].setSouthEast(southEast); 
        fluidField[r][0].setSouthWest(southWest); 
        //reinit bottom boundary
        fluidField[r][cols - 1].setHere(here);
  			fluidField[r][cols - 1].setUp(up);
        fluidField[r][cols - 1].setDown(down);
        fluidField[r][cols - 1].setLeft(left);
        fluidField[r][cols - 1].setRight(right);
        fluidField[r][cols - 1].setNorthEast(northEast); 
        fluidField[r][cols - 1].setNorthWest(northWest); 
        fluidField[r][cols - 1].setSouthEast(southEast); 
        fluidField[r][cols - 1].setSouthWest(southWest); 

      }
 
    }
    
}