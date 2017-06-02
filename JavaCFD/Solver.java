public class Solver{
    int rows, cols;
    Element[][] fluidField;
    int time;
    public Solver(Element[][] fluidField, int vel){
	//initializes the simulation
	//create elements for each block in the display
	time = 0;
	fluidField = this.fluidField;
	rows = fluidField.length;
	cols= fluidField[0].length;
	//initiate fluid with specific density and specified speed in x dir
	for (int r = 0; r < rows; r++){
	    for (int c = 0; c< cols; c++){
		//for non solids, set the initial values for each instance variable for each element based on the algorithm
	    }
	}
	//set initial time
	time = 0;	
    }
    public void run(){
	//actual algorithm  here
	//performs the algorithm for one unit of time
	//lattice boltzmann each element collides then streams
	//need an input for time depending on the program
	collide();
	stream();
    }
    public void collide(){
	//f(x,t+deltat)=f(x,t)+ (feq - finitial)/(tau)
	//set the value for tau
	//for each element do something
	for (int r = 0; r < rows; r++){
	    for (int c = 0; c < cols; c++){
	    }
	}
    }
    public void stream(){
	//f(x+e*deltat, t+deltat)=f(x,t+deltat)	
    }
    
}
