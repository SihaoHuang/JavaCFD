public class Solver{
    int rows, cols;
    Element[][] fluidField;
    int time;
    public Solver(Element[][] fluidField, int vel){
	//create elements for each block in the display
	time = 0;
	fluidField = this.fluidField;
	rows = fluidField.length;
	cols= fluidField[0].length;
	//initiate fluid with specific density and specified speed in x dir
	for (int r = 0; r < rows; r++){
	    for (int c = 0; c< cols; c++){
		//for non solids, set the specific values for element based on the algorithm
	    }
	}
    }    
}