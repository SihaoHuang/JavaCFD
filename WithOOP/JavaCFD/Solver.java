public class Solver{
  
    int rows, cols;
    int time;
    double viscosity; //fluid viscosity, user input
    double overallVelocity; //wind tunnel velocity, user input
    double[][] n0;
    double[][] nN;
    double[][] nS;
    double[][] nE;
    double[][] nW;
    double[][] nNW;
    double[][] nNE;
    double[][] nSW;
    double[][] nSE;
    boolean[][] solid;
    double[][] density;    
    double[][] speed2;  
    Element[][] fluidField;
    
    double four9ths = 4.0 / 9;
    double one9th = 1.0 / 9;
    double one36th = 1.0 / 36;
    
    public Solver(Element[][] fluidField, double overallVelocity, double viscosity, int rows, int cols){
      
      //initializes the simulation
      //create elements for each block in the display
      this.fluidField = fluidField;
      this.rows = rows;
      this.cols = cols;
      this.viscosity = viscosity;
      this.overallVelocity = overallVelocity;
      
      //initiate fluid with discretized velocity vectors
      //the probabilities derived from Boltzmann distribution; Weber State University paper
      //does not matter if element is solid as the solver disregards velocity data 
      
      double here = 4.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double up = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double down = 1.0/9.0 * (1 - 1.5*overallVelocity*overallVelocity);
      double left = 1.0/9.0 * (1 - 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      double right = 1.0/9.0 * (1 + 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      double northEast = 1.0/36.0 * (1 + 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      double northWest = 1.0/36.0 * (1 - 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      double southEast = 1.0/36.0 * (1 + 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      double southWest = 1.0/36.0 * (1 - 3.0*overallVelocity + 3.0*overallVelocity*overallVelocity);
      
      for(int r = 0; r < rows; r++){
        for(int c = 0; c < cols; c ++){
          if (fluidField[r][c].isSolid()) {
            fluidField[r][c].setHere(0);
            fluidField[r][c].setUp(0);
            fluidField[r][c].setDown(0);
            fluidField[r][c].setLeft(0);
            fluidField[r][c].setRight(0);
            fluidField[r][c].setNorthEast(0);
            fluidField[r][c].setNorthWest(0);
            fluidField[r][c].setSouthEast(0);
            fluidField[r][c].setSouthWest(0);
            fluidField[r][c].setSpeed(0);
          } 
          else {
            fluidField[r][c].setHere(here);
            fluidField[r][c].setUp(up);
            fluidField[r][c].setDown(down);
            fluidField[r][c].setLeft(left);
            fluidField[r][c].setRight(right);
            fluidField[r][c].setNorthEast(northEast);
            fluidField[r][c].setNorthWest(northWest);
            fluidField[r][c].setSouthEast(southEast);
            fluidField[r][c].setSouthWest(southWest);
            fluidField[r][c].setDensity(1.0);
            fluidField[r][c].setSpeed(overallVelocity*overallVelocity);
          }
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
      
    private void collide() {
      double sumVelocities;
      double relaxationTime = 1 / (3*viscosity + 0.5);  // reciprocal of tau, the relaxation time
      for (int x=0; x<rows; x++) {
        for (int y=0; y<cols; y++) {
          if (!fluidField[x][y].isSolid()) {
            sumVelocities = fluidField[x][y].sumVelocities();
            fluidField[x][y].setDensity(sumVelocities);    
            double xVelocity;
            double yVelocity;
            if (sumVelocities > 0) {
              xVelocity = (fluidField[x][y].getRight() + fluidField[x][y].getNorthEast()
                 + fluidField[x][y].getSouthEast() - fluidField[x][y].getLeft() 
                 - fluidField[x][y].getNorthWest() - fluidField[x][y].getSouthWest()) / sumVelocities;
              yVelocity = (fluidField[x][y].getUp() + fluidField[x][y].getNorthEast() 
                 + fluidField[x][y].getNorthWest() - fluidField[x][y].getDown()
                 - fluidField[x][y].getSouthEast() - fluidField[x][y].getSouthWest()) / sumVelocities;
            } 
            else{
              xVelocity = 0;
              yVelocity = 0;
            }
            double threeXVelocity = 3.0 * xVelocity;
            double threeYVelocity = 3.0 * yVelocity;
            double xVelocitySquared = xVelocity * xVelocity;
            double yVelocitySquared = yVelocity * yVelocity;
            double twoXYVelocities = 2 * xVelocity * yVelocity;
            double sumSquares = xVelocitySquared + yVelocitySquared;
            
            fluidField[x][y].setSpeed(sumSquares);
            
            fluidField[x][y].addHere(relaxationTime * (four9ths*sumVelocities * (1 - 1.5*sumSquares) - fluidField[x][y].getHere()));
            fluidField[x][y].addRight(relaxationTime * (one9th*sumVelocities * (1 + threeXVelocity + 4.5*xVelocitySquared - 1.5*sumSquares) - fluidField[x][y].getRight()));
            fluidField[x][y].addLeft(relaxationTime * (one9th*sumVelocities * (1 - threeXVelocity + 4.5*xVelocitySquared - 1.5*sumSquares) - fluidField[x][y].getLeft()));
            fluidField[x][y].addUp(relaxationTime * (one9th*sumVelocities * (1 + threeYVelocity + 4.5*yVelocitySquared - 1.5*sumSquares) - fluidField[x][y].getUp()));
            fluidField[x][y].addDown(relaxationTime * (one9th*sumVelocities * (1 - threeYVelocity + 4.5*yVelocitySquared - 1.5*sumSquares) - fluidField[x][y].getDown()));
            fluidField[x][y].addNorthEast(relaxationTime * (one36th*sumVelocities * (1 + threeXVelocity + threeYVelocity + 4.5*(sumSquares + twoXYVelocities) - 1.5*sumSquares) - fluidField[x][y].getNorthEast()));
            fluidField[x][y].addNorthWest(relaxationTime * (one36th*sumVelocities * (1 - threeXVelocity + threeYVelocity + 4.5*(sumSquares - twoXYVelocities) - 1.5*sumSquares) - fluidField[x][y].getNorthWest()));
            fluidField[x][y].addSouthEast(relaxationTime * (one36th*sumVelocities * (1 + threeXVelocity - threeYVelocity + 4.5*(sumSquares - twoXYVelocities) - 1.5*sumSquares) - fluidField[x][y].getSouthEast()));
            fluidField[x][y].addSouthWest(relaxationTime * (one36th*sumVelocities * (1 - threeXVelocity - threeYVelocity + 4.5*(sumSquares + twoXYVelocities) - 1.5*sumSquares) - fluidField[x][y].getSouthWest()));
            
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
    
      for (int y=0; y<cols-1; y++) {
        fluidField[0][y].setDown(fluidField[0][y + 1].getDown());
      }
      for (int y=cols-1; y>0; y--) {
        fluidField[rows - 1][y].setUp(fluidField[rows - 1][y - 1].getUp());
      }
      
      for(int r = 0; r < rows - 1; r ++){
        for(int c = cols - 1; c > 0; c --){
          fluidField[r][c].setUp(fluidField[r][c - 1].getUp()); //move up
          fluidField[r][c].setNorthWest(fluidField[r + 1][c - 1].getNorthWest()); //move northwest
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
      
      for(int r = 0; r < rows - 1; r ++){
        for(int c = 0; c < cols - 1; c ++){
          fluidField[r][c].setLeft(fluidField[r + 1][c].getLeft()); //move left
          fluidField[r][c].setSouthWest(fluidField[r + 1][c + 1].getSouthWest()); //move southwest
        }
      }
      
      double v = overallVelocity;
      double threeTimesOverallVelocity = 3.0*v;
      double threeTimesOverallVelocitySquared = threeTimesOverallVelocity*v;
      double here = 4.0/9.0 * (1 - 1.5*v*v);
      double up = 1.0/9.0 * (1 - 1.5*v*v);
      double down = 1.0/9.0 * (1 - 1.5*v*v);
      double left = 1.0/9.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double right = 1.0/9.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double northEast = 1.0/36.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double northWest = 1.0/36.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double southEast = 1.0/36.0 * (1 + threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      double southWest = 1.0/36.0 * (1 - threeTimesOverallVelocity + threeTimesOverallVelocitySquared);
      
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
      
      // Try the same thing at the right edge and see if it works:
      for(int c = 0; c < cols; c ++){
        if(!fluidField[rows - 1][c].isSolid()){
          fluidField[rows - 1][c].setLeft(left);
          fluidField[rows - 1][c].setNorthWest(northWest);
          fluidField[rows - 1][c].setSouthWest(southWest);
        }
      }
      // Now handle top and bottom edges:
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