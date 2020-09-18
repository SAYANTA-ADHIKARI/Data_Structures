public class UnionFind {

    //Number of elements
    private int size;
    //Sizes of each component
    private int[] componentSize;
    //Array of parents of the indexes; id[i] = j means j is the parent of i
    //id[i] = i means i is a root node
    private int[] parentList;
    //Number of components in union find
    private int numComponents;

    //Constructor
    public UnionFind(int size){
        if(size <= 0)
            throw new IllegalArgumentException("size <=0 is not allowed");
        this.size = numComponents = size;
        componentSize = new int[size];
        parentList = new int[size];

        for(int i = 0; i < size; i++){
            componentSize[i] = 1;
            parentList[i] = i;
        }
    }

    //Finds and returns the parent and root element of the provided index
    public int find(int x){
        int root = x;
        while (parentList[root] != root)
            root = parentList[root];

        //Applying Path Compression
        while (x != root){
            int next = parentList[x];
            parentList[x] = root;
            x = next;
        }

        return root;
    }

    //Returns true if both index have same root
    public boolean connected(int i, int j){
        return find(i) == find(j);
    }

    //Returns the size of the root of the element x
    public int componentSize(int x){
        return componentSize[find(x)];
    }

    //Returns the size of the UnionFind
    public int size(){
        return size;
    }

    //Returns number of components present in the UnionFind
    public int getNumComponents(){
        return numComponents;
    }

    //Unifies the sets of the provided elements
    public void union(int x, int y){
        int root1 = find(x);
        int root2 = find(y);

        if(root1 == root2)
            return;

        if(componentSize[root1] < componentSize[root2]){
            componentSize[root2] += componentSize[root1];
            parentList[root1] = root2;
        }
        else{
            componentSize[root1] += componentSize[root2];
            parentList[root2] = root1;
        }
        numComponents--;
    }

}
