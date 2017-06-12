class Simulation {
  
  int x, y;
  int totalIterations;  //counts the number of iterations past
  int rows;
  int cols;
  Solver solution; //stores everything needed for the simulation 
  
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
    solution = new Solver(velocity, viscosity, rows/5, cols/5);
  }

  void display() {
    
    if (start()){
      solution = new Solver(velocity, viscosity, rows/5, cols/5);
    }
    if (solution!= null && start){
      solution.iterate();
    }
    
    if(totalIterations % 2 == 0){ //redraw every two iterations
    // renders fluid field by velocity
    if(!densityButton){
        for (int i = 0; i < rows/5; i++) {
          for (int j = 0; j < cols/5; j++) {
            colorMode(HSB, 100);
            if (solution.isSolid(i, j)){
              stroke(0);
              fill(0);
              rect(i*5, j*5, 10, 10);
              stroke((int)scaleValue(solution.getDensity(i ,j), 0.0, 0.02*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
              fill((int)scaleValue(solution.getDensity(i, j), 0.0, 0.02*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
              point(i*5, j*5);
            } else {
              stroke((int)scaleValue(solution.getVelocity(i, j), 0.0, 0.02*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
              fill((int)scaleValue(solution.getVelocity(i, j), 0.0, 0.02*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
              rect(i*5, j*5, 10, 10);  
              point(i*5, j*5);
            }
          }
        }
      }
      
      // renders fluid field by density
      else{
        for (int i = 0; i < rows/5; i++) {
          for (int j = 0; j < cols/5; j++) {
            colorMode(HSB, 100);
            if (solution.isSolid(i, j)){
              stroke(0);
              fill(0);
              rect(i*5, j*5, 10, 10);
              //println(solution.getDensity(i ,j));
              stroke((int)scaleValue(abs(1.0 - (float)solution.getDensity(i, j))*1000, 0.0, 1.88*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
              fill((int)scaleValue(abs(1.0 - (float)solution.getDensity(i ,j))*1000, 0.0, 1.88*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
              point(i*5, j*5);
            } else {
              stroke((int)scaleValue(abs(1.0 - (float)solution.getDensity(i, j))*1000, 0.0, 1.88*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values
              fill((int)scaleValue(abs(1.0 - (float)solution.getDensity(i, j))*1000, 0.0, 1.88*scale, 0.0, 100.0), 100.0, 100.0); // fix scaling values 
              rect(i*5, j*5, 10, 10);  
              point(i*5, j*5);
            }
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

  double scaleValue(double val, float inMin, float inMax, float outMin, float outMax) { //for scaling the colored display 
    return ((outMax - outMin) * (val - inMin) / (inMax - inMin)) + outMin;
  }
  
  void keyPressed(){
    if (keyPressed){
      start = true;
      one();
      two();
      three();
    }
  }
  
  void one(){ //press one on the keyboard for a straight line
    if (key == '1' ){
      for (int i = 0; i < rows/5; i++) {
        for (int j = 0; j < cols/5; j++) {
        solution.solid[i][j] = false;
        if (i == 200/5 && j <=400/5 && j >= 100/5){
          solution.solid[i][j] = true;
        }
        }
      }

    }
  }
  
  void two(){ //press two on the keyboard for a diagonal
    if (key == '2'){
      for (int i= 0; i < rows/5; i++){
        for (int j = 0; j < cols/5;j++){
          solution.solid[i][j] = false;
          if (i>=300/5 && i <=400/5 && 600-5*i== j*5){
            solution.solid[i][j] = true;
          }
        }
      }
    }
  }
  
 void three(){ //press three on the keyboard for a part of a circle
      if (key == '3'){
        for (int i = 0; i < rows/5; i++){
          for (int j = 0; j < cols/5; j++){
            solution.solid[i][j] = false;
            int r = 125*125;
            int x = (5*i-500)*(5*i-500);
            int y = (5*j-250)*(5*j-250);
            if (x+y-r<=5 && x+y-r>=-5){
              solution.solid[i][j] = true;
            }
          }
        }
      }
 }
          
  void mousePressed() {
    barrier();
    //if (start) {
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
        velocity = (0.30*(mouseX-725)/(200)); //adjust this to scale slider values
        slider1X= mouseX;
    }
    if (viscslider()) {
        viscosity = 0.005 + ((0.2-0.005)*(mouseX-1000)/200); //adjust this to scale slider values
        slider2X= mouseX;
    }
    if (scaleslider()){
      scale = 0.100+((5.0-0.100)*(mouseX-1300)/200); //adjust this to scale slider values
      slider3X=mouseX;
    }
    barrier();
  }
  
  void updateVelocity() {
    String a = ""+velocity;
    velocityString = "" + a.substring(0,4);
  }
  
  void updateViscosity() {
    String a = ""+viscosity;
    viscosityString = a.substring(0, 4);
  }
  
  void updateScale(){
    String a = "" + scale;
    scaleString = a.substring(0,4);
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
      solution.setSolid(mouseX/5, mouseY/5);
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