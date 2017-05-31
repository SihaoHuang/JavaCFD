while (!newSim){
  size(1000,1000);
  int cols = width;
  int rows = height;
  
  Element[][] fluidField = new Element[cols][rows];
  
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      fluidField[i][j] = //instantiate
    }
  }
  
  while (startNotPressed) {
    drawGeometry(readMouseLocation);
  }
  instantiateSimulationConditions();
  
  int time = 0;
  while (){
    if(time % updateRate == 0) Display.disp();
    Solver.solve(fluidField);
    time ++;
  }
}