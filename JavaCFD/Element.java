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
    
    public void setHere(double val){
      here = val;
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
}