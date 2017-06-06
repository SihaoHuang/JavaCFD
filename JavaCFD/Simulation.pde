class Simulation{
  int x, y;
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
     for (int i = 0; i < rows; i+=2) {
      for (int j = 0; j < cols; j+=3) {
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
    //density, velocity radio buttons
    fill(100);
    ellipse(550,550, 50, 50);//density
    ellipse(550,650, 50, 50);//velocity
    text("Density",600-10,550+25/2);
    text("Velocity",600-10,650+25/2);
    //rect();
    text("Total Iterations", 250, 525+50*3/4);
    rect(250,625,250,50);  //total iterations box
    mousePressed();
    text("Velocity : "+ 1,750,550+25/2);
    text("Viscosity : " + 1, 1000, 550+25/2);
    rect(750, 650-25/2, 200, 20);
    rect(1000,650-25/2,200,20);  
    //flow velocity, viscosity slider 
}
  
  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) {
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  void mousePressed(){
    if (start()){
      //start the simulation      
    }
    if (stop()){
      //stop the simulation       
    }
    if (density()){
      fill(0);
      ellipse(550,550,30,30);//center(550,550)
      fill(100);
    }
    if (velocity()){
      fill(0);
      ellipse(550,650,30,30);//center(550,650)
      fill(100);
    }
  }
  
  boolean start(){
       return (mouseX >= 50 && mouseX <= 50+150 && mouseY <= 525+50  && mouseY >= 525);
  }
  boolean stop(){
       return (mouseX >= 50 && mouseX <= 50+150 && mouseY <= 625+50  && mouseY >= 625);
  }
  boolean density(){
    //center (550,550)
    return (625 >= (mouseX - 550)*(mouseX-550) + (mouseY-550)*(mouseY-550));
  }
  boolean velocity(){
    //center (550,650)
    return (625 >= (mouseX-550)*(mouseX-550)+(mouseY-650)*(mouseY-650));
  }
  boolean barrier(){
    return true;
  }
  
  
}