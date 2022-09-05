import java.util.LinkedList;
import java.util.List;

public class BacktrackingAVL extends AVLTree {
    // For clarity only, this is the default ctor created implicitly.
    public BacktrackingAVL() {
        super();
    }

	//You are to implement the function Backtrack.
    public void Backtrack() {
        
        while (!list4back.isEmpty() && !list4back.peekFirst().equals(true)) {
        	Object lastAction = list4back.removeFirst();
        	Node toDelete = (Node) list4back.removeFirst();
        	
        	switch ((ImbalanceCases) lastAction) {
        	
        		case RIGHT_RIGHT: //there was R_R rotation during insertion, we need to perform Left rotation in order to "undo" it
        			if (this.root == toDelete.parent) {
        				this.root = rotateLeft(toDelete.parent);
        			} else {
        				Node wasPivot = rotateLeft(toDelete.parent);
        				Node wasPivotParent = toDelete.parent.parent.parent;
        				
        				if (wasPivot.value < wasPivotParent.value)
        					wasPivotParent.left = wasPivot;
        				else
        					wasPivotParent.right = wasPivot;
        			}
        			break;
        			
        		case LEFT_LEFT:
        			if (this.root == toDelete.parent) {
        				this.root = rotateRight(toDelete.parent);
        			} else {
        				Node wasPivot = rotateRight(toDelete.parent);
        				Node wasPivotParent = toDelete.parent.parent.parent;
        				
        				if (wasPivot.value < wasPivotParent.value)
        					wasPivotParent.left = wasPivot;
        				else
        					wasPivotParent.right = wasPivot;
        			}
        			break;
        			
        		case LEFT_RIGHT:
        			if (toDelete == this.root) {
        				this.root = rotateLeft(toDelete);
        				Node tmpPivot = toDelete.parent;
        				tmpPivot.left = rotateRight(toDelete);
        			} else {
        				Node tmpPivot = rotateLeft(toDelete);
        				Node tmpPivotParent = toDelete.parent.parent;
        				//deciding which branch we "glue" the node after rotation
        				if (tmpPivot.value < tmpPivotParent.value)
        					tmpPivotParent.left = tmpPivot;
        				else
        					tmpPivotParent.right = tmpPivot;
        				//reversing the second rotation that was made during insertion
        				tmpPivot = rotateRight(toDelete);
        				tmpPivotParent = toDelete.parent.parent;
        				//deciding which branch we "glue" the node after rotation
        				if (tmpPivot.value < tmpPivotParent.value)
        					tmpPivotParent.left = tmpPivot;
        				else
        					tmpPivotParent.right = tmpPivot;
        			}
        			break;
        			
        		case RIGHT_LEFT:
        			if (toDelete == this.root) {
        				this.root = rotateRight(toDelete);
        				Node tmpPivot = toDelete.parent;
        				tmpPivot.right = rotateLeft(toDelete);
        			} else {
        				Node tmpPivot = rotateRight(toDelete);
        				Node tmpPivotParent = toDelete.parent.parent;
        				//deciding which branch we "glue" the node after rotation
        				if (tmpPivot.value < tmpPivotParent.value)
        					tmpPivotParent.left = tmpPivot;
        				else
        					tmpPivotParent.right = tmpPivot;
        				//reversing the second rotation that was made during insertion
        				tmpPivot = rotateLeft(toDelete);
        				tmpPivotParent = toDelete.parent.parent;
        				//deciding which branch we "glue" the node after rotation
        				if (tmpPivot.value < tmpPivotParent.value)
        					tmpPivotParent.left = tmpPivot;
        				else
        					tmpPivotParent.right = tmpPivot;
        			}
        			
        			
        			break;
        			
        		case NO_IMBALANCE:
        			if (!list4back.isEmpty() && list4back.peekFirst().equals(true))
        				deleteInsertedNode(toDelete);
        			break;
        		
        		//if default is needed - in case there is no match for any case - implement default here in the end
        	}
        	if (!list4back.isEmpty() && list4back.peekFirst().equals(true)) {
        		list4back.removeFirst(); //remove the boolean flag
				break;
			}
        }
        
    }

    private void deleteInsertedNode(Node toDelete) {
		if (toDelete == this.root)
			this.root = null;
		else {
			Node toDeleteOriginalParent = toDelete.parent; //for later use in order to update height & size of nodes
			if (toDelete.parent.value > toDelete.value)
				toDelete.parent.left = null;
			else
				toDelete.parent.right = null;

			//maybe to update field size here
			if(toDeleteOriginalParent.right == null & toDeleteOriginalParent.left == null) { //the deleted node was an only child
				while (toDeleteOriginalParent != null) { //update the height & size as long as the node is unbalanced or until we arrive to the root
					int sizeBefore = toDeleteOriginalParent.size;
					toDeleteOriginalParent.updateSize();
					
					int heightBefore = toDeleteOriginalParent.height;
					toDeleteOriginalParent.updateHeight();
					
					int sizeAfter = toDeleteOriginalParent.size;
					int heightAfter = toDeleteOriginalParent.height;
					
					//if height's are equal it means we don't longer need to go up the tree in order to update height's
					if(heightAfter == heightBefore & sizeBefore == sizeAfter)
						break;
					//else we didn't break out of the loop we continue our way up the tree in order to update the height's
					toDeleteOriginalParent = toDeleteOriginalParent.parent;
				}
			}
		}
		//--treeSize;
	}
    
    //Change the list returned to a list of integers answering the requirements
    public static List<Integer> AVLTreeBacktrackingCounterExample() {
        
        LinkedList<Integer> listOfInsertions = new LinkedList<Integer>();
        listOfInsertions.add(2);
        listOfInsertions.add(3);
        listOfInsertions.add(4);
        //tree now after third insertion and self-balancing rotations looks:
        //    (     3)    
        //    /      \    
        // (     2)(     4)
        
        //after Backtrack() tree will look like:
        //    (     2)    
        //			\    
        //		   (     3)
        //after delete() tree will look like:
        //    (     3)    
        //    /           
        // (     2)    
        return listOfInsertions;
    }
    
    public int Select(int index) {

		if (root != null) //the i'th value is in range of the number of nodes in tree
			return Select(root, index);
		
		return 0; //it is a problem to return null when return type is int?
    }
    
    private int Select(Node node, int i) {
		int currRank = 1;
		if (node.left != null)
			currRank = node.left.size + 1;
		
		if (currRank == i)
			return node.value;
		else if (i < currRank)
			return Select(node.left, i);
		else
			return Select(node.right, i - currRank);
	}
    
    public int Rank(int value) {

        if(root == null | value <= 0) //if tree is empty means we don't have a rank of nodes
        	return 0;
        return Rank(root, value);
    }
    
    private int Rank(Node node, int value) {
    	int rank = 0;
    	while (node != null) {
    		if (value < node.value)
    			node = node.left;
    		else if (value > node.value) {
    			if (node.left != null)
    				rank += 1 + node.left.size;
    			else
    				rank += 1;
    			node = node.right;
    		}
    		else {
    			if (node.left != null)
    				rank += node.left.size;
    			return rank;
    		}
    	}
    	return rank;
    }
    
}
