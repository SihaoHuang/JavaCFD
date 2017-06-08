class Simulation{
  int x, y;
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows;
  int cols;
  Element[][] fluidField;

  boolean start;
  int slider1X;
  int slider2X;
  

  
  Simulation(int rows, int cols, int totalIterations, double overallVelocity){
    this.rows = rows;
    this.cols = cols;
    fluidField = new Element[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        fluidField[i][j] = new Element();
        fluidField[i][j].setHere(random(20));
      }
    }
    start = false;
    slider1X= 750;
    slider2X= 1000;
  }

  void display(){
     for (int i = 0; i < rows; i+=10) {
      for (int j = 0; j < cols; j+=10) {
        colorMode(HSB, 100);
        if (fluidField[i][j].solid){//fluidField[i][j].isSolid()){
          stroke(0);
          fill(0);
          rect(i,j,10,10);
          stroke((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0 ,100.0); // fix scaling values
        fill((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
        point(i, j);
        }
        else{
        stroke((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0 ,100.0); // fix scaling values
        fill((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 100.0, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
      rect(i,j,10,10);  
        point(i, j);
        }
         
      }
      
      }
    //colors are hard
    stroke(0);
    fill(100);
    rect(50,525,150,50);//start
    rect(50,625,150,50);//stop
        rect(250,625,250,50);  //total iterations box
    textSize(50*2/3);
    fill(0);
    text("Start",50+150/4,525+50*3/4);//start button
    text("Stop",52.5+150/4,625+50*3/4);//stop button
            text(""+totalIterations, 250, 625+50*3/4);
    //density, velocity radio buttons
    fill(100);
    ellipse(550,550, 50, 50);//density
    ellipse(550,650, 50, 50);//velocity
    text("Density",600-10,550+25/2);
    text("Velocity",600-10,650+25/2);
    //rect();
    text("Total Iterations", 250, 525+50*3/4);
    fill(0);
    fill(100);
    text("Velocity : "+ 1,750,550+25/2);
    text("Viscosity : " + 1, 1000, 550+25/2);
    
    rect(750, 650-25/2, 200, 20);
    rect(1000,650-25/2,200,20);  
    fill(0);
    rect(slider1X, 650-25/2, 10, 20);
    rect(slider2X,650-25/2, 10, 20);
    if (velslider()){
      fill(100);
      rect(750, 650-25/2, 200, 20);
      fill(0);
    }
    if (viscslider()){
      fill(100);
    rect(1000,650-25/2,200,20); 
    fill(0);
    }
    //flow velocity, viscosity slider 
    if (start){
      totalIterations++;
    }
    mousePressed();
  }
  
  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) {
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  void mousePressed(){
    barrier();
    if (start()){
      if (!start){
        start = true;
      }
      //start the simulation      
    }
    if (stop()){
      if (start){
        start = false;
      }
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
    if (velslider()){
      fill(0);
      slider1X= mouseX;

      rect(slider1X, 650-25/2, 10, 20);
      fill(100);
    }
    if (viscslider()){
      fill(0);
      slider2X= mouseX;
      rect(slider2X, 650-25/2, 10, 20);
      fill(100);
    }
    barrier();
    
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
  void barrier(){
    //1300,700
    if (mouseX <1300 && mouseY <500){      
    fluidField[mouseX-mouseX%10][mouseY-mouseY%10].solid = true;
    
    }
  }
    /*rect(750, 650-25/2, 200, 20);
    rect(1000,650-25/2,200,20);  */

  boolean velslider(){
    return (mouseX >= 750 && mouseX <=950-10 && mouseY >=650-25/2 && mouseY<=650+25/2);
  }
  boolean viscslider(){
    return (mouseX>= 1000 && mouseX<=1200-10 && mouseY >= 650-25/2&& mouseY<=650+25/2);
  }
 
  
  
}