import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingBTree() {
		super();
	}

	public BacktrackingBTree(int order) {
		super(order);
	}

	//You are to implement the function Backtrack.
	public void Backtrack() {
	    
	    if (!list4back.isEmpty()) {
	    	LinkedList<Object> LastInsertList = (LinkedList<Object>) list4back.removeFirst();
	    	Node<T> currentNode = null;
	    	Node<T> medianNode = null;
	    	while (!LastInsertList.isEmpty()) {
	    		Object lastAction = LastInsertList.removeFirst(); 
	    		if (lastAction.equals(true)) { //last insertion was accompanied with split action on one of the nodes in the BTree structure
	    			T valueInserted = (T) LastInsertList.removeFirst();
	    			
	    			if (!LastInsertList.isEmpty() && !LastInsertList.peekFirst().equals(true)) {
	    				T medianVal = (T) LastInsertList.removeFirst();
	    				
	    				if (currentNode == null) 
	    					currentNode = this.getNode(valueInserted);
	    				
	    				currentNode.removeKey(valueInserted);
	    				medianNode = currentNode.parent;
	    				while (medianNode.indexOf(medianVal) == -1)
	    					medianNode = medianNode.parent;
	    				
	    				merge(medianVal, medianNode); //merge the splitted nodes
	    			}
	    			else { //one of the internal nodes in BTree was split during the insertion
	    				T medianVal = valueInserted;
	    				medianNode = currentNode.parent;
						while (medianNode.indexOf(medianVal) == -1)
							medianNode = medianNode.parent;

						merge(medianVal, medianNode); //merge the splitted nodes					
	    			}
	    			
	    			
	    		}
	    		else { //no split actions occurred in last insertion
	    			T valueInserted = (T) lastAction;
	    			currentNode = this.getNode(valueInserted);
	    			currentNode.removeKey(valueInserted); //leaf is now in former state before last insertion
	    		}
	    	}
	    }
	    if (this.root != null && this.root.numOfKeys == 0) { //in case we removed the last key in last node in the BTree
			this.root = null;
			this.size = 0;
	    }
    }

	private void merge(T medianVal, Node<T> midNode) {
		int currentIdx = midNode.indexOf(medianVal);//the index of the median value who was split
		midNode.removeKey(medianVal); //remove the middle value 
		Node<T> leftChild = midNode.getChild(currentIdx);
		Node<T> rightChild = midNode.getChild(currentIdx + 1);
		leftChild.addKey(medianVal); //adding to the left child the middle value 

		for(int i = 0; i<rightChild.getNumberOfKeys(); i ++) {//adding all the keys of the right child to the left one
			leftChild.addKey(rightChild.getKey(i));
		}
		if (rightChild.getNumberOfChildren() > 0) {
			for (int j = 0; j < rightChild.getNumberOfChildren(); j++) {
				Node<T> child = rightChild.getChild(j);
				leftChild.addChild(child);
			}
		}
		midNode.removeChild(rightChild);//delete the right child
		if(leftChild.parent.numOfKeys==0) {//if the root was split
			this.root = leftChild;
		}

	}
	
	//Change the list returned to a list of integers answering the requirements
	public static List<Integer> BTreeBacktrackingCounterExample(){

	    LinkedList<Integer> listOfInputInserts = new LinkedList<Integer>();
	    listOfInputInserts.add(1);
	    listOfInputInserts.add(5); 
	    listOfInputInserts.add(9);
	    listOfInputInserts.add(13);
	    listOfInputInserts.add(19);
	    listOfInputInserts.add(10); //last crucial insert
	    //BTree toString() output:
	    // ~~~ 9
	    //     |-- 1, 5
	    //     ~~~ 10,13,19
	    //if we delete(BTree,10) we'll get:
	    // ~~~ 9
	    //     |-- 1, 5
	    //     ~~~ 13,19
	    //but if we Backtrack(); we'll get:
	    // ~~~ 1, 5, 9, 13, 19
	    return listOfInputInserts;
	}
}
