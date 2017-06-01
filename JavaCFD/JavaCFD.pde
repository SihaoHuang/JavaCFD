//while (!newSim){
//  size(1000,1000);
//  int cols = width;
//  int rows = height;
  
//  while (startNotPressed) {
//    drawGeometry(readMouseLocation);
//  }
//  instantiateSimulationConditions();
  
//  int time = 0;
//  while (){
//    if(time % updateRate == 0) Display.disp();
//    Solver.solve(fluidField);
//    time ++;
//  }
//}

void setup(){
  Simulation sim;
  size(1000, 1000);
  int rows = height;
  int cols = width;
  
  sim = new Simulation(rows, cols);
}