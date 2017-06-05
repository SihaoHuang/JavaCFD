public class Element{
  
    double up, down, left, right, northEast, southEast, northWest, southWest, here; // velocity vectors in element
    boolean solid;
    boolean inlet;
    boolean outlet;
    double density, xVelocity, yVelocity, speed;
    public Element(){
      up = down = left = right = northEast = southEast = northWest = southWest = here =  density = xVelocity = yVelocity = speed = 50.0;
      solid = inlet = outlet = false;
    }
    public Element(String config, double up, double down, double left, double right, double northEast, double southEast, double northWest, double southWest, double here, double density, double xVelocity, double yVelocity, double speed){
      if(config.equals("solid")){ // used string parameter to prevent confusingly long constructor
        solid = true;
        inlet = false;
        outlet = false;
      }
      if(config.equals("inlet")){
        solid = false;
        inlet = true;
        outlet = false;
      }
      if(config.equals("outlet")){
        solid = false;
        inlet = false;
        outlet = true;
      }
      else{ // "fluid" is used as a keyword for consistency
        solid = false;
        inlet = false;
        outlet = false;
      }
      this.up = up;
      this.down = down;
      this.left = left;
      this.right = right;
      this.northEast = northEast;
      this.southEast = southEast;
      this.northWest = northWest;
      this.southWest = southWest;
      this.here = here;
      this.density = density;
      this.xVelocity = xVelocity;
      this.yVelocity = yVelocity;
      this.speed = speed;
	 
    }
    
    public double getVelocity(){
      return here;
    }
    
    public double sumVelocities(){
      return up + down + left + right + northEast + southEast + northWest + southWest + here;
    }
    public void setSolid(){
      solid = true;
      inlet = false;
      outlet = false;
    }
    
    public void setInlet(){
      solid = false;
      inlet = true;
      outlet = false;
    }
    
    public void setOutlet(){
      solid = false;
      inlet = false;
      outlet = true;
    }
    
    public void setLiquid(){
      solid = false;
      inlet = false;
      outlet = false;
    }
    
    /******************* SETS ******************/
    
    public void setHere(double val){
      here = val;
    }
    public void setUp(double up){
	    this.up=up;
    }
    public void setDown(double down){
	    this.down= down;
    }
    public void setLeft(double left){
	    this.left = left;
    }
    public void setRight(double right){
	    this.right =right;
    }
    public void setNorthEast(double northEast){
	    this.northEast =northEast;
    }
    public void setNorthWest(double northWest){
	    this.northWest = northWest;
    }
    public void setSouthEast(double southEast){
	    this.southEast = southEast;
    }
    public void setSouthWest(double southWest){
	    this.southWest = southWest; 
    }
    public void setDensity(double density){
	    this.density = density;
    }
    public void setxVelocity(double xVelocity){
	    this.xVelocity = xVelocity;
    }
    public void setyVelocity(double yVelocity){
	    this.yVelocity = yVelocity;
    }
    public void setSpeed(double speed){
	    this.speed = speed;
    }
    
    /******************* GETS ******************/
    
    public double getUp(){
      return up;
    }
    public double getDown(){
      return down;
    }
    public double getLeft(){
      return left;
    }
    public double getRight(){
      return right;
    }
    public double getNorthEast(){
      return northEast;
    }
    public double getNorthWest(){
      return northWest;
    }
    public double getSouthEast(){
      return southEast;
    }
    public double getSouthWest(){
      return southWest; 
    }
    public double getDensity(){
      return density;
    }
    public double getxVelocity(){
      return xVelocity;
    }
    public double getyVelocity(){
      return yVelocity;
    }
    public double getSpeed(){
      return speed;
    }
    public boolean isSolid(){
      return solid;
    }
    
}