class Simulation{
  
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows;
  int cols;
  Element[][] fluidField;
  Double overallVelocity;
  
  Simulation(int rows, int cols, int totalIterations, double overallVelocity){
    this.rows = rows;
    this.cols = cols;
    fluidField = new Element[rows][cols];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        fluidField[i][j] = new Element();
        fluidField[i][j].setHere(random(20));
      }
    }
  }
  
  void run(){
    long iterationCount = 0;
    while(running){ //IMPORTANT implement button latter
      iterate();
      iterationCount += 1;
      if(iterationCount % 50 == 0) display();
    }
  }
 
  void display(){
    background(0);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        colorMode(HSB, 100);
        stroke((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0 ,100.0); // fix scaling values
        fill((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
        point(i, j);
      }
    }
  }
  
  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) {
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  
  
}