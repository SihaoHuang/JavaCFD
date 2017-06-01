class Simulation{
  
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows;
  int cols;
  Element[][] fluidField;
  
  Simulation(int rows, int cols){
    colorMode("HSB", 100);
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
        stroke(scaleValue(fluidField.getVelocity(i, j)), 100 ,100);
        fill(scaleValue(fluidField.getVelocity(i, j)), 100, 100);
        point(i,j);
      }
    }
  }
  
  
}