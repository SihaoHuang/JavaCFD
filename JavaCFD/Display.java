public class Display{
  
  boolean densityDisplay;
  
  public Display(){
    colorMode("HSB", 100);
  }
  
  public void disp(){
    if
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        stroke(scaleValue(fluidField.getVelocity(i, j)), 100 ,100);
        fill(scaleValue(fluidField.getVelocity(i, j)), 100, 100);
        point(i,j);
      }
    }
    else{
      for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
          stroke(scaleValue(fluidField.getDensity(i, j)), 100 ,100);
          fill(scaleValue(fluidField.getDensity(i, j)), 100, 100);
          point(i,j);
        }
      }
    }
  }
  
}