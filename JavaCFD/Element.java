public class Element{
  
    double up, down, left, right, northEast, southEast, northWest, southWest, here; // velocity vectors in element
    boolean solid;
    boolean inlet;
    boolean outlet;
    double density, xv, yv, spd;
    public Element(){
      up = down = left = right = northEast = southEast = northWest = southWest = here =  density = xv = yv = spd = 0.0;
      solid = inlet = outlet = false;
    }
    public Element(String config, double up, double down, double left, double right, double northEast, double southEast, double northWest, double southWest, double here, double density, double xv, double yv, double spd){
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
      up = this.up;
      down = this.down;
      left = this.left;
      right = this.right;
      northEast = this.northEast;
      southEast = this.southEast;
      northWest = this.northWest;
      southWest = this.southWest;
      here = this.here;
      density = this.density;
      xv=this.xv;
      yv=this.yv;
      spd=this.spd;
	 
    }
    
    public double getVelocity(){
      return here;
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
