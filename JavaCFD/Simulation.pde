class Simulation{
  
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows;
  int cols;
  Element[][] fluidField;
  
  Simulation(int rows, int cols){
    this.rows = rows;
    this.cols = cols;
    fluidField = new Element[rows][cols];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        fluidField[i][j] = new Element();
        fluidField[i][j].setHere(random(100));
      }
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