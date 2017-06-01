public class Simulation{
  // configuration
  int totalIterations; 
  Double reynolds; // reynold's number
  int rows; // grid dimensions
  int cols; // grid dimensions
  Element[][] fluidField;
  
  public Simulation(){
    fluidField = new Element[cols][rows];
    //for (int i = 0; i < cols; i++) {
    //  for (int j = 0; j < rows; j++) {
    //    fluidField[i][j].set
    //  }
    //}
  }
}