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

Simulation sim;
int rows;
int cols;

void setup(){
  
  size(1000,1000);
  rows = height;
  cols = width;
  
  sim = new Simulation(rows, cols, 100, 100.0);
}
void draw(){
  sim.display();
}