package classes;
import java.util.*;

public class Solutions {
	private String SnapShot;
	private HashMap SnapShotMap;  // Hash map for each unique snapshots to prevent loops when performing depth first search 
	private ArrayList <String> SolutionPath;	//array of strings for path of snapshots to solution
	private Queue<Node> queue;
	private HashMap buttonMap;
	private Button [][] ButtonGrid;
	private int size;
	
	public Solutions(String InComingSnapshot, Button [][] incomingButtonGrid, int GridSize){
		this.SnapShot = InComingSnapshot;
		this.queue = new LinkedList<Node>();
		this.SnapShotMap = new HashMap();
		this.SolutionPath = new ArrayList<String>();
		this.buttonMap = new HashMap();
		this.ButtonGrid = new Button [GridSize][GridSize];
		this.ButtonGrid = incomingButtonGrid;
		this.size = GridSize;
		
	}
	
	/**
	 * Creates the map all the possible unique moves made and Performs the breadth first search in order to get to the solution of a given grid in the shortest amount
	 * of moves. 
	 * @return Arraylist of Strings that each represent a move made to get to the solution.
	 */
	public ArrayList<String> getSolution(){
		ArrayList<String> solStrings = new ArrayList<String>();
		System.out.println("SOLUTIONS......"+this.SnapShot);
		
		Node rootNode = new Node(this.SnapShot);
		this.SnapShotMap.put(this.SnapShot, this.ButtonGrid);
		
		Node iteratorNode = null;
		iteratorNode = rootNode;
		this.queue.add(rootNode);
		while(!this.queue.isEmpty()){
			Node N = this.queue.remove();
			if(gameWonSnap(N.Snapshot)){
				if(gameWonSnap(N.Snapshot)){
					System.out.println("Found a solution!!");
					System.out.println(N.Snapshot);
					return solStrings = printSolutionBackwards(N);
				}
				break;
			}
			else{
				setChildrenList(N, ((Button [][] )this.SnapShotMap.get(N.Snapshot)) );
			}
		}
		if(this.queue.isEmpty()){
			System.out.println("empty queue!!! No solution");
			return null;
		}
		
		return null;
	}
	
	/**
	 * Prints out the snapshots as grids starting from the snapshot of the goal piece in the desired position in the grid.
	 * @param N
	 */
	public ArrayList<String> printSolutionBackwards(Node N){
		ArrayList<String> solStrings = new ArrayList<String>();
		while(N != null){
			System.out.println("*****");
			solStrings.add(N.Snapshot);
			printFromString(N.Snapshot);
			N = N.parentNode;
		}
		return solStrings;
	}
	
	/**
	 * Method used for debugging. Print's out the snapshot called in as a Grid
	 * @param SnapShot
	 */
	public void printFromString(String SnapShot){
		for(int i=0; i<this.size; i++){
			for(int j=0; j<this.size; j++){
				System.out.print(SnapShot.charAt((i*this.size)+j)+" ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Runs through each piece of the grid, creates new grids based on each possible move that can be made from each piece, creates a snapshot of the grid, 
	 * checks hash map that will be storing the Snapshots of there is one already made if not then it stores the snapshot along with the grid to the 
	 * hashmap "SnapShotMap" to prevent creating loops when running through the breadth first search.  
	 * @param parentNode
	 * @param Grid
	 */
	public void setChildrenList(Node parentNode, Button[][] Grid){
		Button [][] tempGrid = copyGrid(Grid);
		String newSnapShot = getSnapshot( tempGrid);
		HashMap newBMap = new HashMap();
		newBMap = setGridHashMap(newSnapShot, tempGrid);
		setAllMovesLists(tempGrid, newBMap);
		int listIndex;
		int i;
		int j;
		int h;
		int w;
		
		Set set = newBMap.entrySet();
		Iterator newBMapi = set.iterator();
		while(newBMapi.hasNext()){
			Map.Entry me = (Map.Entry)newBMapi.next();
			Button tempButton = (Button) me.getValue();
			
			if(tempButton.getH() > tempButton.getW()){	// Vertical piece
				int [] DirMoves = new int [this.size-1];
				DirMoves = tempButton.getUMoves();
				listIndex = 1;
				while(listIndex != this.size-1 && DirMoves[listIndex] != -1){
					i = tempButton.getI();
					j = tempButton.getJ();
					h = tempButton.getH();
					w = tempButton.getW();
					Button blankButton = tempGrid[i-1][j];	//get blank piece above
					for(int Pos=i; Pos<=(i+h-1); Pos++){// start at the top of the vertical piece swap with blank piece, work your way down 							
						Button swapButton = tempGrid[Pos][j];
						tempGrid[Pos-listIndex][j] = swapButton;
						tempGrid[Pos][j] = blankButton;
					}
					String moveSnap = getSnapshot(tempGrid);
					if(!this.SnapShotMap.containsKey(moveSnap)){
						this.SnapShotMap.put(moveSnap, tempGrid);
						Node newNode = new Node(moveSnap);
						newNode.setParentNode(parentNode);
						parentNode.nodeChildren.add(newNode);
						this.queue.add(newNode);
					}
					listIndex++;
					tempGrid = copyGrid(Grid);
				}
				
				DirMoves = tempButton.getDMoves();
				listIndex = 1;
				while(listIndex != this.size-1 && DirMoves[listIndex] != -1){
					i = tempButton.getI();
					j = tempButton.getJ();
					h = tempButton.getH();
					w = tempButton.getW();
					Button blankButton = tempGrid[i+h][j];	//get blank piece below
					for(int Pos=(i+h-1); Pos>=(i); Pos--){// start at the bottom of the vertical piece, swap with blank piece, work your way down 
						Button swapButton = tempGrid[Pos][j];
						tempGrid[Pos+listIndex][j] = swapButton;
						tempGrid[Pos][j] = blankButton;
					}
					String moveSnap = getSnapshot(tempGrid);
					if(!this.SnapShotMap.containsKey(moveSnap)){
						this.SnapShotMap.put(moveSnap, tempGrid);
						Node newNode = new Node(moveSnap);
						newNode.setParentNode(parentNode);
						parentNode.nodeChildren.add(newNode);
						this.queue.add(newNode);
					}
					listIndex++;
					tempGrid = copyGrid(Grid);
				}
			}// end Ver Piece
			
			if(tempButton.getH() < tempButton.getW()){	// Horizontal Piece
				
				int [] DirMoves = new int [this.size-1];
				DirMoves = tempButton.getLMoves();
				listIndex = 1;
				while(listIndex != this.size-1 && DirMoves[listIndex] != -1){
					i = tempButton.getI();
					j = tempButton.getJ();
					h = tempButton.getH();
					w = tempButton.getW();
					Button blankButton = tempGrid[i][j-1];	//get blank piece to the left
					for(int Pos=j; Pos<(j+w); Pos++){		// start at the left end of the vertical piece, swap with blank piece, work your way right 
						
						Button swapButton = tempGrid[i][Pos];
						tempGrid[i][Pos-listIndex] = swapButton;
						tempGrid[i][Pos] = blankButton;
					}
					String moveSnap = getSnapshot(tempGrid);
					if(!this.SnapShotMap.containsKey(moveSnap)){
						this.SnapShotMap.put(moveSnap, tempGrid);
						Node newNode = new Node(moveSnap);
						newNode.setParentNode(parentNode);
						parentNode.nodeChildren.add(newNode);
						this.queue.add(newNode);
					}
					listIndex++;
					tempGrid = copyGrid(Grid);
				}
				
				DirMoves = tempButton.getRMoves();
				listIndex = 1;
				while(listIndex != this.size-1 && DirMoves[listIndex] != -1){ // for each possible move
					i = tempButton.getI();
					j = tempButton.getJ();
					h = tempButton.getH();
					w = tempButton.getW();
					Button blankButton = tempGrid[i][j+w];		//get blank piece to the right
					for(int Pos=(j+w-1); Pos>(j-1); Pos--){		// start at the right end of the vertical piece, swap with blank piece, work your way left 
						Button swapButton = tempGrid[i][Pos];
						tempGrid[i][Pos+listIndex] = swapButton;
						tempGrid[i][Pos] = blankButton;
					}
					String moveSnap = getSnapshot(tempGrid);
					if(!this.SnapShotMap.containsKey(moveSnap)){
						this.SnapShotMap.put(moveSnap, tempGrid);
						Node newNode = new Node(moveSnap);
						newNode.setParentNode(parentNode);
						parentNode.nodeChildren.add(newNode);
						this.queue.add(newNode);
					}
					listIndex++;
					tempGrid = copyGrid(Grid);
				}
			}// end Hor Piece
		}// end while has next
		
	}
	
	/**
	 * Checks if the goal piece (Z piece) is in the right most column of the grid. It is using the String Snapshot to calculate the z's position
	 * @param SnapShot
	 * @return
	 */
	private boolean gameWonSnap(String SnapShot){
		int stringLength = SnapShot.length();
		for(int i=0; i<stringLength; i++){
			if(i % this.size == (this.size -1) && SnapShot.charAt(i) == 'z'){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Creates a new grid (2D Array) of Buttons and copy's the information of each button from the Grid being sent in. Used for the
	 * setChildrenList method. 
	 * @param Grid
	 * @return new Grid with the copied info for each button.
	 */
	public Button[][] copyGrid(Button [][] Grid){
		Button [][] tempGrid = new Button [this.size][this.size];
		for (int i=0; i<this.size; i++){
			for(int j=0; j<this.size; j++){
				tempGrid[i][j] = new Button(i,j,1,1);
				tempGrid[i][j].setI(i);
				tempGrid[i][j].setJ(j);
				tempGrid[i][j].setPosition(Grid[i][j].getPosition());
				tempGrid[i][j].setTag(Grid[i][j].getTag());
				tempGrid[i][j].setH(Grid[i][j].getH());
				tempGrid[i][j].setW(Grid[i][j].getW());
				tempGrid[i][j].setBlank(Grid[i][j].isBlank());
			}
		}
		return tempGrid;
	}
	
	/**
	 * Get a string representation of the grid of buttons being called in.
	 * Each character in the string will a button in the grid. B's will represent "blank" buttons.
	 * @param Grid
	 * @return a String that will represent the grid called in.
	 */
	public String getSnapshot(Button [][] Grid){
		  String temp;
		  StringBuilder stringBuilder = new StringBuilder();
		  for(int i=0; i<this.size; i++){
		   for(int j=0; j<this.size; j++){
			if(Grid[i][j].getTag() == "blank"){
				stringBuilder.append("B");
			}
			else
				stringBuilder.append(Grid[i][j].getTag());
		   }
		  }
		  temp = stringBuilder.toString();
		  
		  return temp;
		 }
	
	/**
	 * Sets all possible moves for each piece in the grid. 
	 * @param Grid 
	 * @param BMap
	 */
	public void setAllMovesLists(Button [][] Grid, HashMap BMap){
		int [] Umoves = new int [this.size-1];
	    int [] Dmoves = new int [this.size-1];
	    int [] Lmoves = new int [this.size-1];
	    int [] Rmoves = new int [this.size-1];
	    int x;
	    int y;
	    int Ux;
	    int Dx;
	    int Ly;
	    int Ry;
	    
		Set set = BMap.entrySet();
		Iterator BMapi = set.iterator();
		while(BMapi.hasNext()){			//done for each element in the button hashmap
			
			Map.Entry me = (Map.Entry)BMapi.next();
			Button tempButton = (Button) me.getValue();
			if(tempButton.getW() > tempButton.getH()){//Horizontal piece
				x = tempButton.getI();
				y = tempButton.getJ();
				Ly=0;
				Ry=0;
				for(int index=0; index < this.size-1; index++){
					Ly = y-index; 						//Moving through the loop move to the left by one each time starting at leftmost part of the piece
					Ry = y+index+tempButton.getW()-1; 	//Moving through the loop move to the right by one each time starting at rightmost part of the piece
					
					if(Ly >= 0){ //check if moves to the left are in bounds
						if(Grid[x][Ly].getTag() == "blank" ){
							Lmoves[index] = index;
						}
						else{
								Lmoves[index] = -1;
						}
					}
					else
						Lmoves[index] = -1;
					if( Ry < this.size){//check if moves to the right are in bounds
						if(Grid[x][Ry].getTag() == "blank" ){
							Rmoves[index] = index;
						}
						else{
							Rmoves[index] = -1;
						}
					}
					else
						Rmoves[index] = -1;
					
				}// end for
				
				tempButton.setLMoves(Lmoves, this.size);
				tempButton.setRMoves(Rmoves, this.size);
			}
			
			if(tempButton.getW() < tempButton.getH()){//Vertical piece. Top part of Vertical piece stored in (x, y) ie. (I,J)
				x = tempButton.getI();
				y = tempButton.getJ();
				Ux =0;
			    Dx =0;
			    for(int index=0; index < this.size-1; index++){
			    	Ux = x-index; 
			    	Dx = x+index+tempButton.getH()-1;
			    	
			    	if(Ux >= 0){ //check if moves Up are in bounds
						if(Grid[Ux][y].getTag() == "blank" ){
							Umoves[index] = index;
						}
						else{
							Umoves[index] = -1;
						}
					}
					else
						Umoves[index] = -1;
					if( Dx < this.size){//check if moves Down are in bounds
						if(Grid[Dx][y].getTag() == "blank" ){
							Dmoves[index] = index;
						}
						else{
							Dmoves[index] = -1;
						}
					}
					else
						Dmoves[index] = -1;
			    }
			    
			    tempButton.setUMoves(Umoves, this.size);
			    tempButton.setDMoves(Dmoves, this.size);
			}
		}//end while
	}//end setAll..
	
	/**
	 * Sets a hash map for a grid using the grid's snapshot to set each piece's Tag in the grid as the key that maps to the given button. 
	 * @param CurrentSnapshot
	 * @param Grid
	 * @return the hash map for the pieces in the grid.
	 */
	public HashMap setGridHashMap(String CurrentSnapshot, Button [][] Grid){
		HashMap tempMap = new HashMap();
		int Limit = this.size * this.size;
		for(int StringIndex = 0; StringIndex < Limit; StringIndex++){
				if(CurrentSnapshot.charAt(StringIndex) != 'B'){
					if(!tempMap.containsKey(CurrentSnapshot.charAt(StringIndex) ) ){// If vehicle tag is not in the hashmap add it 
						int i;
						int j;
						i = StringIndex / this.size;
						j = StringIndex % this.size;
						tempMap.put(CurrentSnapshot.charAt(StringIndex), Grid[i][j] );
					}
			   }
		}
		return tempMap;
	}	
	
	/**
	 * Method used for debugging. Prints out the grid of buttons sent into the method
	 * @param Grid
	 */
	public void printGrid(Button [][] Grid){
		for(int i=0; i< this.size; i++){
			for(int j=0; j<this.size; j++){
				if(Grid[i][j].getTag() == "blank"){
					System.out.printf("B ");
				}
				else
					System.out.printf("%s ", Grid[i][j].getTag());
			}
			System.out.println();
		}
	}
	
	/**
	 * Method used for debugging. Prints out the hashmap that contains the info for each button in a given grid.
	 * @param HMap
	 */
	public void printMap(HashMap HMap){
		System.out.println("+-----------+");
		  Set set = HMap.entrySet();
		  Iterator i = set.iterator();
		  while(i.hasNext()){
		   Map.Entry me = (Map.Entry)i.next();
		   System.out.println(me.getKey() + " at "+((Button)me.getValue()).getI() + ", " + ((Button)me.getValue()).getJ());
		  }
	}
	
}