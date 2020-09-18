import java.util.*;
import java.util.Queue;
import java.util.Stack;

public class BinarySearchTree <T extends Comparable<T>>{

    //Keeps track of number of node in the BST
    private int nodeCount = 0;
    //Root node of the BST
    private Node root = null;

    //private Node class that keeps track of the data and links
    private class Node{
        T data;
        Node left, right;
        public Node(Node left, Node right, T data){
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    //Checks whether the BST is empty or not
    public boolean isEmpty(){
        return size() == 0;
    }

    //Returns the size of the BST
    public int size(){
        return nodeCount;
    }

    //Adds the provided element to the BST
    public boolean add(T element){
        if(contains(element))
            return false;
        else{
            root = add(root, element);
            nodeCount++;
            return true;
        }
    }

    //Private method that recursively adds element to the BST
    private Node add(Node node, T element){
        if(node == null)
            node = new Node(null, null, element);
        else {
            if(element.compareTo(node.data) < 0)
                node.left = add(node.left, element);
            else
                node.right = add(node.right, element);
        }
        return node;
    }

    //Removes the element form the BST
    public boolean remove(T element){
        if(contains(element)){
            root = remove(root, element);
            nodeCount--;
            return true;
        }
        else
            return false;
    }

    //Private method the removes element from the BST recursively
    private Node remove(Node node, T element){
        if(node == null)
            return null;
        int compare = element.compareTo(node.data);
        if(compare < 0)
            node.left = remove(node.left, element);
        else if(compare > 0)
            node.right = remove(node.right, element);
        else{
            if(node.left == null){
                Node rightChild = node.right;
                node.data = null;
                node = null;
                return rightChild;
            }
            else if(node.right == null){
                Node leftChild = node.left;
                node.data = null;
                node = null;
                return leftChild;
            }
            else {
                //This finds the inorder successor of the node
                Node temp = digLeft(node.right);
                node.data = temp.data;
                node.right = remove(node.right, temp.data);

                //This finds the inorder predecessor of the node
                //Node temp = digRight(node.left);
                //node.data = temp.data;
                //node.left = remove(node.left, temp.data);
            }
        }
        return node;
    }

    //Helper methods of the remove class
    private Node digLeft(Node node){
        Node current = node;
        while (current.left != null){
            current = current.left;
        }
        return current;
    }

    //Helper method of the remove class
    private Node digRight(Node node){
        Node current = node;
        while (current.right != null){
            current = current.right;
        }
        return current;
    }

    //Return true if the element exists in the BST
    public boolean contains(T element){
        return contains(root, element);
    }

    //Checks whether the element is present in the BST or not recursively
    private boolean contains(Node node, T element){
        if(node == null)
            return false;
        int compare = element.compareTo(node.data);

        if(compare < 0)
            return contains(node.left, element);
        else if(compare > 0)
            return contains(node.right, element);
        else
            return true;
    }

    //Returns the height of the BST
    public int height(){
        return height(root);
    }

    //Private function that returns the height of the provided node
    private int height(Node node){
        if(node == null)
            return 0;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    public Iterator<T> traverse(TreeTraversalOrder order){
        switch (order){
            case PRE_ORDER: return preOrderTraversal();
            case POST_ORDER: return postOrderTraversal();
            case IN_ORDER: return inOrderTraversal();
            case LEVEL_ORDER: return levelOrderTraversal();
            default: return null;
        }
    }

    private Iterator<T> preOrderTraversal(){
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(root);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return root != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                Node node = stack.pop();
                if(node.right != null)
                    stack.push(node.right);
                if(node.left != null)
                    stack.push(node.left);
                return node.data;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
    private Iterator<T> postOrderTraversal(){
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack1 = new Stack<>();
        final Stack<Node> stack2 = new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty()){
            Node node = stack1.pop();
            if(node != null){
                stack2.push(node);
                if(node.left != null)
                    stack1.push(node.left);
                if(node.right != null)
                    stack1.push(node.right);
            }
        }

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return root != null && !stack2.isEmpty();
            }

            @Override
            public T next() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return stack2.pop().data;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
    private Iterator<T> inOrderTraversal(){
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(root);

        return new Iterator<T>() {
            Node trav = root;

            @Override
            public boolean hasNext() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return root != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();

                while (trav != null && trav.left != null){
                    stack.push(trav.left);
                    trav = trav.left;
                }

                Node node = stack.pop();

                if (node.right != null) {
                    stack.push(node.right);
                    trav = node.right;
                }

                return node.data;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
    private Iterator<T> levelOrderTraversal(){
        final int expectedNodeCount = nodeCount;
        final Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return root != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                if(expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                Node node = queue.poll();
                if(node.left != null)
                    queue.offer(node.left);
                if(node.right != null)
                    queue.offer(node.right);
                return node.data;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
}
