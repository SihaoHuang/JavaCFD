class Simulation {
  
  int x, y;
  int totalIterations; 
  int rows;
  int cols;
  Element[][] fluidField;
  Solver solution; 
  
  double velocity, viscosity;
  float scale;
  String velocityString, viscosityString, scaleString;
  boolean densityButton;
  boolean start, stop;
  int slider1X;
  int slider2X;
  int slider3X;



  Simulation(int rows, int cols) {
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
    stop = true;
    slider1X= 750-25;
    slider2X= 1000;
    slider3X=1300;
    velocity = 0.001;
    viscosity= 0.005;
    scale = 0.101;
    velocityString = viscosityString = scaleString ="";
    densityButton = true;
  }

  void display() {
    if (start()){
      //solution = new Solver(fluidField, viscosity*100.0, velocity*100.0);
      solution = new Solver(fluidField, viscosity*100.0 , velocity*40.0 , 0.98);
    }
    if (solution!= null && start){
      solution.iterate();
    }
    // renders fluid field by velocity
    if(!densityButton){
      for (int i = 0; i < rows; i+=10) {
        for (int j = 0; j < cols; j+=10) {
          colorMode(HSB, 100);
          if (fluidField[i][j].solid){
            stroke(0);
            fill(0);
            rect(i, j, 10, 10);
            stroke((int)scaleValue(fluidField[i][j].getDensity(), 0.0, 100.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
            fill((int)scaleValue(fluidField[i][j].getDensity(), 0.0, 100.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
            point(i, j);
          } else {
            stroke((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 2.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
            fill((int)scaleValue(fluidField[i][j].getVelocity(), 0.0, 2.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
            rect(i, j, 10, 10);  
            point(i, j);
          }
        }
      }
    }
    // renders fluid field by density
    else{
      for (int i = 0; i < rows; i+=10) {
        for (int j = 0; j < cols; j+=10) {
          colorMode(HSB, 100);
          if (fluidField[i][j].solid){
            stroke(0);
            fill(0);
            rect(i, j, 10, 10);
            stroke((int)scaleValue(fluidField[i][j].sumVelocities(), 0.0, 1.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
            fill((int)scaleValue(fluidField[i][j].sumVelocities(), 0.0, 1.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
            point(i, j);
          } else {
            stroke((int)scaleValue(fluidField[i][j].sumVelocities(), 0.0, 1.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
            fill((int)scaleValue(fluidField[i][j].sumVelocities(), 0.0, 1.0*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
            rect(i, j, 10, 10);  
            point(i, j);
          }
        }
      }
    }
    
    //draws GUI elements
    stroke(0);
    fill(100);
    rect(50, 525, 150, 50);//start
    rect(50, 625, 150, 50);//stop
    rect(250, 625, 250, 50);  //total iterations box
    textSize(50*2/3);
    fill(0);
    text("Start", 50+150/4, 525+50*3/4);//start button
    text("Stop", 52.5+150/4, 625+50*3/4);//stop button
    text(""+totalIterations, 250, 625+50*3/4);
    //density, velocity radio buttons
    fill(100);
    ellipse(550, 550, 50, 50);//density
    ellipse(550, 650, 50, 50);//velocity
    if (densityButton){
      fill(0);
      ellipse(550, 550, 30, 30);//center(550,550)
      fill(100);
    }
    else{
      fill(0);
      ellipse(550, 650, 30, 30);//center(550,650)
      fill(100);
      
    }
    text("Density", 600-10, 550+25/2);
    text("Velocity", 600-10, 650+25/2);
    //rect();
    text("Total Iterations", 250, 525+50*3/4);
    fill(0);
    fill(100);
    updateVelocity();
    updateViscosity();
    updateScale();
    rect(875,525,100, 50);
    rect(1165,525,100,50);
    rect(1400,525,100,50);
    text("Velocity: ", 750-25, 550+25/2);
    text("Viscosity: " , 1000, 550+25/2);
    text("Scale: ", 1300,550+25/2);
    fill(0);
    text(velocityString, 875+2.5, 550+25/2);
    text(viscosityString,1165+2.5,550+25/2);
    text(scaleString, 1400+2.5,550+25/2);
    fill(100);

    rect(750-25, 650-25/2, 200, 20);
    rect(1000, 650-25/2, 200, 20);  
     rect(1300,650-25/2,200,20);
    fill(0);
    rect(slider1X, 650-25/2, 10, 20);
    rect(slider2X, 650-25/2, 10, 20);
    rect(slider3X,650-25/2,10,20);
    if (velslider()) {
      fill(100);
      rect(750-25, 650-25/2, 200, 20);
      fill(0);
    }
    if (viscslider()) {
      fill(100);
      rect(1000, 650-25/2, 200, 20); 
      fill(0);
    }
    if (scaleslider()){
      fill(100);
      rect(1300,650-25/2,200,20);
      fill(0);
    }
    //flow velocity, viscosity slider 
    if (start) {
      totalIterations++;
    }
  }

  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) { //for colored display purposes
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  
  void mousePressed() {
    barrier();
    //if (start()) {
    //  if (!start) {
    //    start = true;
    //  }
    //  //start the simulation
    //}
    if (start()) {
      start = true;
      stop = false;
    }
    if (stop()) {
      start = false;
      stop = true;
    }
    if (density()) {     
       if (!densityButton){
         densityButton = true;
       }
    }
    if (velocity()) {
      if (densityButton){
        densityButton = false;
      }
    }
    if (velslider()) {

       velocity = (0.120*(mouseX-725)/(200));

      slider1X= mouseX;
    }
    if (viscslider()) {
          viscosity = 0.005 + ((0.2-0.005)*(mouseX-1000)/200);
        slider2X= mouseX;
    }
    if (scaleslider()){
      scale = 0.100+((10.0-0.100)*(mouseX-1300)/200);
      slider3X=mouseX;
    }
    barrier();
  }
  
  void updateVelocity() {
    String a = ""+velocity;
    velocityString = "" + a.substring(0,5);
  }
  
  void updateViscosity() {
    String a = ""+viscosity;
    viscosityString = a.substring(0, 5);
  }
  void updateScale(){
    String a = "" + scale;
    scaleString = a.substring(0,5);
  }
  boolean start() {
    return (mouseX >= 50 && mouseX <= 50+150 && mouseY <= 525+50  && mouseY >= 525);
  }
  
  boolean stop() {
    return (mouseX >= 50 && mouseX <= 50+150 && mouseY <= 625+50  && mouseY >= 625);
  }
  
  boolean density() {
    //center (550,550)
    return (625 >= (mouseX - 550)*(mouseX-550) + (mouseY-550)*(mouseY-550));
  }
  
  boolean velocity() {
    //center (550,650)
    return (625 >= (mouseX-550)*(mouseX-550)+(mouseY-650)*(mouseY-650));
  }
  
  void barrier() {
    //1300,700
    if (mouseX <1300 && mouseY <500) {      
      fluidField[mouseX-mouseX%10][mouseY-mouseY%10].solid = true;
    }
  }
  
  /*rect(750, 650-25/2, 200, 20);
   rect(1000,650-25/2,200,20);  */

  boolean velslider() {
    return (mouseX >= 750-25 && mouseX <=950-10-25 && mouseY >=650-25/2 && mouseY<=650+25/2 && mousePressed);
  }
  
  boolean viscslider() {
    return (mouseX>= 1000 && mouseX<=1200-10 && mouseY >= 650-25/2&& mouseY<=650+25/2 && mousePressed);
  }
  boolean scaleslider(){
    return (mouseX>=1300&&mouseX<=1500-10&&mouseY>=650-25/2&&mouseY<=650+25/2&&mousePressed);
  }
}