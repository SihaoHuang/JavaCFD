public class Solver{
  
    int rows, cols; //post-scaled number of elements
    int time; //universal simulation time
    double viscosity; //fluid viscosity, user input
    double overallVelocity; //wind tunvelocityRightl velocity, user input
    double[][] velocityHere;
    double[][] velocityUp;
    double[][] velocityDown;
    double[][] velocityRight;
    double[][] velocityLeft;
    double[][] velocitynorthWest;
    double[][] velocitynorthEast;
    double[][] velocitySouthWest;
    double[][] velocitySouthEast;
    boolean[][] solid;
    double[][] density;    
    double[][] vSquared;  

    public Solver(double overallVelocity, double viscosity, int x, int y){
      
      //initializes the simulation
      //create elements for each block in the display
      rows = x;
      cols = y;
      this.viscosity = viscosity;
      this.overallVelocity = overallVelocity;
      velocityHere = new double[rows][cols];
      velocityUp = new double[rows][cols];
      velocityDown = new double[rows][cols];
      velocityRight = new double[rows][cols];
      velocityLeft = new double[rows][cols];
      velocitynorthWest = new double[rows][cols];
      velocitynorthEast = new double[rows][cols];
      velocitySouthWest = new double[rows][cols];
      velocitySouthEast = new double[rows][cols];
      solid = new boolean[rows][cols];
      density = new double[rows][cols];    
      vSquared = new double[rows][cols];  
      
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
          if (solid[r][c]) {
            velocityHere[r][c] = 0;
            velocityRight[r][c] = 0;
            velocityLeft[r][c] = 0;
            velocityUp[r][c] = 0;
            velocityDown[r][c] = 0;
            velocitynorthEast[r][c] = 0;
            velocitynorthWest[r][c] = 0;
            velocitySouthEast[r][c] = 0;
            velocitySouthWest[r][c] = 0;
            vSquared[r][c] = 0;
          } 
          else {
            velocityHere[r][c]  = here;
            velocityRight[r][c]  = right;
            velocityLeft[r][c]  = left;
            velocityUp[r][c]  = up;
            velocityDown[r][c]  = down;
            velocitynorthEast[r][c] = northEast;
            velocitySouthEast[r][c] = southEast;
            velocitynorthWest[r][c] = northWest;
            velocitySouthWest[r][c] = southWest;
            density[r][c] = 1;
            vSquared[r][c] = overallVelocity*overallVelocity;
          }
        }
      }
      //set initial time
      time = 0;  
    }
    
    public void iterate(){ 
    	collide();
    	move();
      bounce();
      time += 1;
    }
      
    private void collide() {
      
      double sumVelocities;
      double relaxationTime = 1 / (3*viscosity + 0.5);  
      
      for (int x=0; x<rows; x++) {
        for (int y=0; y<cols; y++) {
          
          if (!solid[x][y]) {
            
            sumVelocities = velocityHere[x][y] + velocityUp[x][y] + velocityDown[x][y] + velocityRight[x][y] + velocityLeft[x][y] + velocitynorthWest[x][y] + velocitynorthEast[x][y] + velocitySouthWest[x][y] + velocitySouthEast[x][y];
            density[x][y] = sumVelocities;    
            double xVelocity;
            double yVelocity;
            
            if (sumVelocities > 0) {
              xVelocity = (velocityRight[x][y] + velocitynorthEast[x][y]
                 + velocitySouthEast[x][y] - velocityLeft[x][y] 
                 - velocitynorthWest[x][y] - velocitySouthWest[x][y]) / sumVelocities;
              yVelocity = (velocityUp[x][y] + velocitynorthEast[x][y] 
                 + velocitynorthWest[x][y] - velocityDown[x][y] 
                 - velocitySouthEast[x][y] - velocitySouthWest[x][y]) / sumVelocities;
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
            
            vSquared[x][y] = sumSquares;  
            
            velocityHere[x][y]  += relaxationTime * ((4.0/9.0)*sumVelocities 
                                   * (1 - 1.5*sumSquares) - velocityHere[x][y]);
            velocityRight[x][y]  += relaxationTime * ((1.0/9.0)*sumVelocities 
                                   * (1 + threeXVelocity + 4.5*xVelocitySquared - 1.5*sumSquares) - velocityRight[x][y]);
            velocityLeft[x][y]  += relaxationTime * ((1.0/9.0)*sumVelocities 
                                   * (1 - threeXVelocity + 4.5*xVelocitySquared - 1.5*sumSquares) - velocityLeft[x][y]);
            velocityUp[x][y]  += relaxationTime * ((1.0/9.0)*sumVelocities 
                                   * (1 + threeYVelocity + 4.5*yVelocitySquared - 1.5*sumSquares) - velocityUp[x][y]);
            velocityDown[x][y]  += relaxationTime * ((1.0/9.0)*sumVelocities 
                                   * (1 - threeYVelocity + 4.5*yVelocitySquared - 1.5*sumSquares) - velocityDown[x][y]);
                                   
            velocitynorthEast[x][y] += relaxationTime * ((1.0/36.0)*sumVelocities 
                                   * (1 + threeXVelocity + threeYVelocity + 4.5*(sumSquares + twoXYVelocities) - 1.5*sumSquares) - velocitynorthEast[x][y]);
            velocitynorthWest[x][y] += relaxationTime * ((1.0/36.0)*sumVelocities 
                                   * (1 - threeXVelocity + threeYVelocity + 4.5*(sumSquares - twoXYVelocities) - 1.5*sumSquares) - velocitynorthWest[x][y]);
            velocitySouthEast[x][y] += relaxationTime * ((1.0/36.0)*sumVelocities 
                                   * (1 + threeXVelocity - threeYVelocity + 4.5*(sumSquares - twoXYVelocities) - 1.5*sumSquares) - velocitySouthEast[x][y]);
            velocitySouthWest[x][y] += relaxationTime * ((1.0/36.0)*sumVelocities 
                                   * (1 - threeXVelocity - threeYVelocity + 4.5*(sumSquares + twoXYVelocities) - 1.5*sumSquares) - velocitySouthWest[x][y]);
          
          }
        }
      }
    }

    private void bounce(){ 
     for (int x=0; x<rows; x++) {
          for (int y=0; y<cols; y++) {
            if (solid[x][y]) {
              if (velocityUp[x][y] > 0) { 
                velocityDown[x][y-1] += velocityUp[x][y]; 
                velocityUp[x][y] = 0; 
              }
              if (velocityDown[x][y] > 0) { 
                velocityUp[x][y+1] += velocityDown[x][y]; 
                velocityDown[x][y] = 0; 
              }
              if (velocityRight[x][y] > 0) { 
                velocityLeft[x-1][y] += velocityRight[x][y]; 
                velocityRight[x][y] = 0; 
              }
              if (velocityLeft[x][y] > 0) { 
                velocityRight[x+1][y] += velocityLeft[x][y]; 
                velocityLeft[x][y] = 0; 
              }
              if (velocitynorthWest[x][y] > 0) { 
                velocitySouthEast[x+1][y-1] += velocitynorthWest[x][y]; 
                velocitynorthWest[x][y] = 0; 
              }
              if (velocitynorthEast[x][y] > 0) { 
                velocitySouthWest[x-1][y-1] += velocitynorthEast[x][y]; 
                velocitynorthEast[x][y] = 0; 
              }
              if (velocitySouthWest[x][y] > 0) { 
                velocitynorthEast[x+1][y+1] += velocitySouthWest[x][y]; 
                velocitySouthWest[x][y] = 0; 
              }
              if (velocitySouthEast[x][y] > 0) { 
                velocitynorthWest[x-1][y+1] += velocitySouthEast[x][y]; 
                velocitySouthEast[x][y] = 0; 
              }
            }
          }
        }
    }
    
    private void move(){ //f(x+e*deltat, t+deltat)=f(x,t+deltat) 
    
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
      
      for (int c = 0; c < cols - 1; c ++) {
        velocityDown[0][c] = velocityDown[0][c+1];
      }
      
      for (int c = cols - 1; c > 0; c --) {
        velocityUp[rows - 1][c] = velocityUp[rows - 1][c - 1];
      }
      
      for (int r=0; r < rows - 1; r ++) {    
        for (int c = cols - 1; c > 0; c --) {
          velocityUp[r][c] = velocityUp[r][c - 1];   
          velocitynorthWest[r][c] = velocitynorthWest[r + 1][c - 1];  
        }
      }
      
      for (int r = rows-1; r >0; r --) {  
        for (int c = cols-1; c > 0; c--) {
          velocityRight[r][c] = velocityRight[r - 1][c];  
          velocitynorthEast[r][c] = velocitynorthEast[r - 1][c-1];  
        }
      }
      
      for (int r = rows-1; r > 0; r --) {   
        for (int c = 0; c < cols - 1; c++) {
          velocityDown[r][c] = velocityDown[r][c + 1];  
          velocitySouthEast[r][c] = velocitySouthEast[r - 1][c + 1];  
        }
      }
      
      for (int r = 0; r < rows-1; r ++) {  
        for (int c = 0; c < cols - 1; c ++) {
          velocityLeft[r][c] = velocityLeft[r + 1][c];
          velocitySouthWest[r][c] = velocitySouthWest[r + 1][c + 1];  
        }
      }
      
      for (int y=0; y<cols; y++) {
        if (!solid[0][y]) {
          velocityRight[0][y] = right;
          velocitynorthEast[0][y] = northEast;
          velocitySouthEast[0][y] = southEast;
        }
      }

      for (int y=0; y<cols; y++) {
        if (!solid[0][y]) {
          velocityLeft[rows-1][y] = left;
          velocitynorthWest[rows-1][y] = northWest;
          velocitySouthWest[rows-1][y] = southWest;
        }
      }

      for (int x=0; x<rows; x++) {
        velocityHere[x][0]  = here;
        velocityRight[x][0]  = right;
        velocityLeft[x][0]  = left;
        velocityUp[x][0]  = up;
        velocityDown[x][0]  = down;
        velocitynorthEast[x][0] = northEast;
        velocitySouthEast[x][0] = southEast;
        velocitynorthWest[x][0] = northWest;
        velocitySouthWest[x][0] = southWest;
        velocityHere[x][cols-1]  = here;
        velocityRight[x][cols-1]  = right;
        velocityLeft[x][cols-1]  = left;
        velocityUp[x][cols-1]  = up;
        velocityDown[x][cols-1]  = down;
        velocitynorthEast[x][cols-1] = northEast;
        velocitySouthEast[x][cols-1] = southEast;
        velocitynorthWest[x][cols-1] = northWest;
        velocitySouthWest[x][cols-1] = southWest;
      }
 
    }
    
    public void setSolid(int r, int c){
      solid[r][c] = true;
    }
    
    public boolean isSolid(int r, int c){
      return solid[r][c];
    }
    
    public double getVelocity(int r, int c){
      return vSquared[r][c];
    }
    
    public double getDensity(int r, int c){
      return density[r][c];
    }
    
}