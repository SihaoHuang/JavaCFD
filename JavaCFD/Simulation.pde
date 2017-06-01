class Simulation{
  
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows;
  int cols;
  Element[][] fluidField;
  
  Simulation(int rows, int cols){
    colorMode(HSB, 100);
    rows = this.rows;
    cols = this.cols;
    fluidField = new Element[rows][cols];
    //for (int i = 0; i < cols; i++) {
    //  for (int j = 0; j < rows; j++) {
    //    fluidField[i][j].set
    //  }
    //}
  }
  
  void disp(){
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        stroke(scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100 ,100); // scale
        fill(scaleValue(fluidField[i][j].getVelocity()), 100, 100);
        point(i,j);
      }
    }
  }
  
  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) {
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  
  
}