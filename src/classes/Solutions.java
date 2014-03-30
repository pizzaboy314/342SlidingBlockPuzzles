package classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
					printFromString(N.Snapshot);
					solStrings = printSolutionBackwards(N);
				}
				break;
			}
			else{
				Button [][] newGrid = copyGrid((Button [][] )this.SnapShotMap.get(N.Snapshot));
				setChildrenList(N, newGrid );
			}
		}
		if(this.queue.isEmpty()){
			System.out.println("empty queue!!! No solution");
		}

		return solStrings;
	}

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

	public void printFromString(String SnapShot){
		for(int i=0; i<this.size; i++){
			for(int j=0; j<this.size; j++){
				System.out.print(SnapShot.charAt((i*this.size)+j)+" ");
			}
			System.out.println();
		}
	}

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
						//tempGrid[Pos+listIndex][j].setI(Pos+listIndex);
						tempGrid[Pos][j] = blankButton;
						//tempGrid[Pos][j].setI(Pos);
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
						//tempGrid[i][Pos-listIndex].setJ(Pos-listIndex);
						tempGrid[i][Pos] = blankButton;
						//tempGrid[i][Pos].setJ(Pos);
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
					//System.out.println("at listIndex grid: "+listIndex);
					//printGrid(tempGrid);
					i = tempButton.getI();
					j = tempButton.getJ();
					h = tempButton.getH();
					w = tempButton.getW();
					Button blankButton = tempGrid[i][j+w];		//get blank piece to the right
					for(int Pos=(j+w-1); Pos>(j-1); Pos--){		// start at the right end of the vertical piece, swap with blank piece, work your way left 
						Button swapButton = tempGrid[i][Pos];
						tempGrid[i][Pos+listIndex] = swapButton;
						//tempGrid[i][Pos+listIndex].setJ(Pos+listIndex);
						tempGrid[i][Pos] = blankButton;
						//tempGrid[i][Pos].setJ(Pos);
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

	private boolean gameWonSnap(String SnapShot){
		int stringLength = SnapShot.length();
		for(int i=0; i<stringLength; i++){
			if(i % this.size == (this.size -1) && SnapShot.charAt(i) == 'z'){
				return true;
			}
		}
		return false;
	}

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


	public String getSnapshot(Button [][] Grid){
		String temp;
		StringBuilder stringBuilder = new StringBuilder();
		// System.out.println("-----------------");
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (Grid[i][j].getTag() == "blank") {
					stringBuilder.append("B");
				} else
					stringBuilder.append(Grid[i][j].getTag());
			}
		}
		temp = stringBuilder.toString();
		// System.out.println(temp);

		return temp;
	}
	public String getSnapshot2(Button [][] Grid){
		String temp;
		StringBuilder stringBuilder = new StringBuilder();
		
		temp = stringBuilder.toString();
		
		return temp;
	}

	public void setAllMovesLists(Button [][] Grid, HashMap BMap){
		int [] Umoves = new int [this.size-1];
		int[] Dmoves = new int[this.size - 1];
		int[] Lmoves = new int[this.size - 1];
		int[] Rmoves = new int[this.size - 1];
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

					//System.out.println("tag: "+tempButton.getTag()+ "  ("+ tempButton.getI() +", "+ tempButton.getJ() +")"+" index: "+ index + "  Ly = " + Ly + "  Ry = "+ Ry);
					if(Ly >= 0){ //check if moves to the left are in bounds
						if(Grid[x][Ly].getTag() == "blank" ){
							Lmoves[index] = index;
						}
						else{
							//if(index == 0)
							// Lmoves[index] = 0;
							//else
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
							//if(index == 0)
							// Rmoves[index]=0;
							//else
							Rmoves[index] = -1;
						}
					}
					else
						Rmoves[index] = -1;

				}// end for

				//System.out.println("For "+ tempButton.getTag()+": ");
				//for(int j =0; j<this.size-1; j++){
				// Umoves[j]=-1;
				// Dmoves[j]=-1;
				// System.out.println("L "+j+" : "+ Lmoves[j]+"  R "+ j+ " : "+
				// Rmoves[j]);
				//}
				//tempButton.setAllMoves(Umoves, Dmoves, Lmoves, Rmoves, this.size);
				tempButton.setLMoves(Lmoves, this.size);
				tempButton.setRMoves(Rmoves, this.size);
				//tempButton.printMoves();

			}
			if(tempButton.getW() < tempButton.getH()){//Vertical piece. Top part of Vertical piece stored in (x, y) ie. (I,J)
				x = tempButton.getI();
				y = tempButton.getJ();
				Ux =0;
				Dx = 0;
				for (int index = 0; index < this.size - 1; index++) {
					Ux = x - index;
					Dx = x + index + tempButton.getH() - 1;

					if (Ux >= 0) { // check if moves Up are in bounds
						if(Grid[Ux][y].getTag() == "blank" ){
							Umoves[index] = index;
						}
						else{
							//if(index == 0)
							// Umoves[index] = 0;
							//else
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
							//if(index == 0)
							// Dmoves[index]=0;
							//else
							Dmoves[index] = -1;
						}
					}
					else
						Dmoves[index] = -1;
				}
				// System.out.println("For "+ tempButton.getTag()+": ");
				//for(int j =0; j<this.size-1; j++){
				// Lmoves[j]=-1;
				// Rmoves[j]=-1;
				// System.out.println("U "+j+" : "+ Umoves[j]+"  D "+ j+ " : "+
				// Dmoves[j]);
				//}
				//tempButton.setAllMoves(Umoves, Dmoves, Lmoves, Rmoves, this.size);
				tempButton.setUMoves(Umoves, this.size);
				tempButton.setDMoves(Dmoves, this.size);
				//tempButton.printMoves();
			}
		}//end while
	}//end setAll..

	public HashMap setGridHashMap(String CurrentSnapshot, Button [][] Grid){
		//System.out.println("****"+CurrentSnapshot+"****");
		HashMap tempMap = new HashMap();
		int Limit = this.size * this.size;
		for(int StringIndex = 0; StringIndex < Limit; StringIndex++){
			if (CurrentSnapshot.charAt(StringIndex) != 'B') {
				if (!tempMap.containsKey(CurrentSnapshot.charAt(StringIndex))) {
					int i;
					int j;
					i = StringIndex / this.size;
					j = StringIndex % this.size;
					tempMap.put(CurrentSnapshot.charAt(StringIndex), Grid[i][j]);
				}
			}
		}
		return tempMap;
	}	

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

	public void printMap(HashMap HMap){
		System.out.println("+-----------+");
		Set set = HMap.entrySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.println(me.getKey() + " at " + ((Button) me.getValue()).getI() + ", " + ((Button) me.getValue()).getJ());
		}
	}

}