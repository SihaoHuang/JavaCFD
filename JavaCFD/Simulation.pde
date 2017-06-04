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
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        fluidField[i][j] = new Element();
        fluidField[i][j].setHere(random(100));
      }
    }
  }
 
  void display(){
     for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        colorMode(HSB, 100);
        stroke((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0 ,100.0); // fix scaling values
        fill((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
        point(i, j);
      }
    }
    //colors are hard
    fill(100);
    rect(50,525,150,50);//start
    rect(50,625,150,50);//stop
    textSize(50*2/3);
    fill(0);
    text("Start",50+150/4,525+50*3/4);//start button
    text("Stop",52.5+150/4,625+50*3/4);//stop button
    //flow velociy slider
    //total iterations box
    //density/velocity display radio buttons
  }
  
  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) {
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  
  
}