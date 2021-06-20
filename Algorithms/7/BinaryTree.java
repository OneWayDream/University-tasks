import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryTree<T> {

    protected Node root;
    private class Node{

        private Node parent;
        private Node rightChild;
        private Node leftChild;
        private T value;

        private Node(T value){
            this.parent = null;
            this.value = value;
            this.rightChild = null;
            this.leftChild = null;
        }

        private Node (Node parent, T value){
            this.parent = parent;
            this.value = value;
            this.rightChild = null;
            this.leftChild = null;
        }

        private Node(T value, Node leftChild, Node rightChild){
            this.parent = null;
            this.value = value;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
        }

        private Node(Node parent, T value, Node leftChild, Node rightChild){
            this.parent = parent;
            this.value = value;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
        }

        private void setParent(Node parent){
            this.parent = parent;
        }

        private void setRightChild(Node rightChild){
            this.rightChild = rightChild;
        }

        private void setLeftChild(Node leftChild){
            this.leftChild = leftChild;
        }

        public Node getParent() {
            return parent;
        }


        public Node getRightChild() {
            return rightChild;
        }


        public Node getLeftChild() {
            return leftChild;
        }

        public T getValue() {
            return value;
        }
    }

    public BinaryTree(){
        this.root = null;
    }

    public BinaryTree(T rootValue){
        this.root = new Node(rootValue);
    }

    public void add(T newValue){
        Node currentNode = this.root;
        Node parentNode = null;
        boolean b = false;
        if (this.root==null){
            this.root = new Node(newValue);
        } else {
            while (currentNode!=null){
                b = ((int) (Math.random()*2)) == 1;
                parentNode = currentNode;
                if (!b){
                    currentNode = currentNode.getLeftChild();
                } else {
                    currentNode = currentNode.getRightChild();
                }
            }
            currentNode = new Node(parentNode, newValue);
            if (b){
                parentNode.setRightChild(currentNode);
            } else {
                parentNode.setLeftChild(currentNode);
            }
        }
    }

    public List<T> BFS(){
        List<Node> list = new ArrayList<>();
        int cursor = 0;
        list.add(root);
        while (cursor<list.size()){
            if (list.get(cursor).getLeftChild()!=null){
                list.add(list.get(cursor).getLeftChild());
            }
            if (list.get(cursor).getRightChild()!=null){
                list.add(list.get(cursor).getRightChild());
            }
            cursor++;
        }
        return list.stream().map(Node::getValue).collect(Collectors.toList());
    }

    public void DFS(){
        if (root==null){
            System.out.println("This tree is empty.");
        } else {
            hDFS(root);
        }
    }

    private void hDFS(Node node){
        if (node!=null){
            System.out.print(node.value + " ");
            if (node.getLeftChild()!=null){
                hDFS(node.getLeftChild());
            }
            if (node.getRightChild()!=null){
                hDFS(node.getRightChild());
            }
        }
    }
}
