public class Display(){
  void disp(){
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      float[] colors = fluidField.getColor();
      float r = colors[0];
      float g = colors[1];
      float b = colors[2];
      stroke(r, g ,b);
      fill(r, g, b);
      point(i,j);
    }
  }
}