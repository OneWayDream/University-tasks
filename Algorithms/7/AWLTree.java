import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AWLTree<T> {

    protected Comparator<T> comparator;
    protected Node root;
    private class Node{

        private Node parent;
        private Node rightChild;
        private Node leftChild;
        private T value;
        private int height;

        private Node(T value){
            this.parent = null;
            this.value = value;
            this.rightChild = null;
            this.leftChild = null;
            this.height = 1;
        }

        private Node (Node parent, T value){
            this.parent = parent;
            this.value = value;
            this.rightChild = null;
            this.leftChild = null;
            this.height = 1;
        }

        private Node(T value, Node leftChild, Node rightChild){
            this.parent = null;
            this.value = value;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
            this.height = 1;
        }

        private Node(Node parent, T value, Node leftChild, Node rightChild){
            this.parent = parent;
            this.value = value;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
            this.height = 1;
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

        public int getHeight(){
            return this.height;
        }

        public void setHeight(int height){
            this.height = height;
        }
    }

    public AWLTree(Comparator<T> comparator){
        this.root = null;
        this.comparator = comparator;
    }

    public AWLTree(T rootValue, Comparator<T> comparator){
        this.root = new Node(rootValue);
        this.comparator = comparator;
    }

    public void add(T newValue){
        if (newValue==null){
            System.out.println("No, thanks!");
            return;
        }
        Node parentNode = null;
        Node currentNode = root;
        if (root==null){
            this.root = new Node(newValue);
        } else {
            boolean b = false;
            while (currentNode!=null){
                b = comparator.compare(newValue, currentNode.value)<=0;
                parentNode = currentNode;
                currentNode = b ? currentNode.getLeftChild() : currentNode.getRightChild();
            }
            currentNode = new Node (parentNode, newValue);
            if (b){
                parentNode.setLeftChild(currentNode);
            } else {
                parentNode.setRightChild(currentNode);
            }
            while (currentNode.getParent()!=null){
                currentNode = currentNode.getParent();
                int hLeft;
                if (currentNode.getLeftChild()==null){
                    hLeft = 0;
                } else {
                    hLeft = currentNode.getLeftChild().getHeight();
                }
                int hRight;
                if (currentNode.getRightChild()==null){
                    hRight=0;
                } else {
                    hRight = currentNode.getRightChild().getHeight();
                }
                currentNode.setHeight(Math.max(hLeft, hRight) + 1);
                balanсe(currentNode);
            }
            //balanсe(root);
        }
    }

    public void remove(T element){
        Node currentNode = root;
        while (currentNode!=null){
            if (currentNode.getValue().equals(element)){
                break;
            } else {
                 boolean b = comparator.compare(element, currentNode.value)<=0;
                 currentNode = b ? currentNode.getLeftChild() : currentNode.getRightChild();
            }
        }
        if (currentNode==null){
            System.out.println("No such element");
        } else {
            remove(currentNode);
        }
    }

    private void remove(Node node){
        if ((node.getLeftChild()==null)&&(node.getRightChild()==null)){
            Node currentNode = node.getParent();
            if (currentNode.getLeftChild()==node){
                currentNode.setLeftChild(null);
            } else {
                currentNode.setRightChild(null);
            }
            balanсe(currentNode);
            while (currentNode.getParent()!=null){
                currentNode = currentNode.getParent();
                balanсe(currentNode);
            }
        } else {
            int hLeft;
            if (node.getLeftChild()!=null){
                hLeft = node.getLeftChild().getHeight();
            } else {
                hLeft = 0;
            }

            int hRight;
            if (node.getRightChild()!=null){
                hRight = node.getRightChild().getHeight();
            } else {
                hRight = 0;
            }

            Node currentNode = (hLeft>hRight) ? node.getLeftChild() : node.getRightChild();
            Node nearest = null;
            while (currentNode!=null){
                nearest = currentNode;
                if (node.getValue().equals(currentNode.getValue())){
                    break;
                } else if (comparator.compare(node.getValue(), currentNode.getValue())<=0){
                    currentNode = currentNode.getLeftChild();
                } else {
                    currentNode = currentNode.getRightChild();
                }
            }
            remove(nearest);
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

    public List<List<T>> DFS(){
        if (root==null){
            System.out.println("This tree is empty.");
            return null;
        } else {
            return hDFS(root, new ArrayList<ArrayList<T>>(), 0);
        }
    }

    private List<List<T>> hDFS(Node node, ArrayList<ArrayList<T>> data, int l){
        if (node!=null){
            if (data.size()==l){
                data.add(new ArrayList<T>());
            }
            data.get(l).add(node.getValue());
            if (node.getLeftChild()!=null){
                hDFS(node.getLeftChild(), data, l+1);
            }
            if (node.getRightChild()!=null){
                hDFS(node.getRightChild(), data, l + 1);
            }
        }
        return (List) data;
    }

    private void balanсe(Node node){
        if (node != null) {
            boolean isWork = true;
            while (isWork){
                isWork = false;
                Node timing;

                Node a;
                if (node.getLeftChild()!=null){
                    a = node.getLeftChild();
                } else {
                    a = new Node (node, null, null, null);
                    a.setHeight(0);
                }

                //System.out.println(BFS());

                Node b;
                if (node.getRightChild()!=null){
                    b = node.getRightChild();
                } else {
                    b = new Node (node, null, null, null);
                    b.setHeight(0);
                }

                Node c;
                if (b.getLeftChild()!=null){
                    c = b.getLeftChild();
                } else {
                    c = new Node (b, null, null, null);
                    c.setHeight(0);
                }

                Node d;
                if (b.getRightChild()!=null){
                    d = b.getRightChild();
                } else {
                    d = new Node (b, null, null, null);
                    d.setHeight(0);
                }

                Node e;
                if (c.getLeftChild()!=null){
                    e = c.getLeftChild();
                } else {
                    e = new Node (c, null, null, null);
                    e.setHeight(0);
                }

                Node f = c.getRightChild();
                if (c.getRightChild()!=null){
                    f = c.getRightChild();
                } else {
                    f = new Node (c, null, null, null);
                    f.setHeight(0);
                }

                Node g;
                if (a.getLeftChild()!=null){
                    g = a.getLeftChild();
                } else {
                    g = new Node (a, null, null, null);
                    g.setHeight(0);
                }

                Node h;
                if (a.getRightChild()!=null){
                    h = a.getRightChild();
                } else {
                    h = new Node (a, null, null, null);
                    h.setHeight(0);
                }

                Node i;
                if (h.getLeftChild()!=null){
                    i = h.getLeftChild();
                } else {
                    i = new Node (h, null, null, null);
                    i.setHeight(0);
                }

                Node j;
                if (h.getRightChild()!=null){
                    j = h.getRightChild();
                } else {
                    j = new Node (h, null, null, null);
                    j.setHeight(0);
                }
                //Малое левое вращение
                if (((b.getHeight() - a.getHeight())==2)&&(c.getHeight()<=d.getHeight())){
                    if (node.getParent()==null){
                        b.setParent(null);
                        this.root = b;
                    } else {
                        timing = node.getParent();
                        b.setParent(timing);
                        if (timing.getLeftChild()==node){
                            timing.setLeftChild(b);
                        } else {
                            timing.setRightChild(b);
                        }
                    }
                    b.setLeftChild(node);
                    node.setParent(b);
                    node.setRightChild(c);
                    c.setParent(node);
                    node.setHeight(Math.max(a.getHeight(), c.getHeight()) + 1);
                    b.setHeight(Math.max(node.getHeight(), d.getHeight()) + 1);
                    if (b.getParent()!=null){
                        b.getParent().setHeight(Math.max(b.getParent().getLeftChild().getHeight(), b.getParent().getRightChild().getHeight()));
                    }
                    if (c.getValue()==null){
                        node.setRightChild(null);
                    }
                    if (d.getValue()==null){
                        b.setRightChild(null);
                    }
                    if (a.getValue()==null){
                        node.setLeftChild(null);
                    }
                    isWork = true;
                }
                //Большое левое вращение
                if (((b.getHeight() - a.getHeight())==2)&&(c.getHeight()>d.getHeight())){
                    if (node.getParent()==null){
                        c.setParent(null);
                        this.root = c;
                    } else {
                        timing = node.getParent();
                        c.setParent(timing);
                        if (timing.getLeftChild()==node){
                            timing.setLeftChild(c);
                        } else {
                            timing.setRightChild(c);
                        }
                    }
                    c.setLeftChild(node);
                    node.setParent(c);
                    c.setRightChild(b);
                    b.setParent(c);
                    node.setRightChild(e);
                    e.setParent(node);
                    b.setLeftChild(f);
                    f.setParent(b);
                    node.setHeight(Math.max(a.getHeight(), e.getHeight()) + 1);
                    b.setHeight(Math.max(f.getHeight(), d.getHeight()) + 1);
                    c.setHeight(Math.max(node.getHeight(), b.getHeight()) + 1);
                    if (c.getParent()!=null){
                        c.getParent().setHeight(Math.max(c.getParent().getLeftChild().getHeight(), c.getParent().getRightChild().getHeight()));
                    }
                    isWork = true;
                    if (f.getValue()==null){
                        b.setLeftChild(null);
                    }
                    if (e.getValue()==null){
                        node.setRightChild(null);
                    }
                    if (d.getValue()==null){
                        b.setRightChild(null);
                    }
                    if (a.getValue()==null){
                        node.setLeftChild(null);
                    }
                }
                //Малое правое вращение
                if (((a.getHeight() - b.getHeight())==2)&&(h.getHeight()<=g.getHeight())){
                    if (node.getParent()==null){
                        a.setParent(null);
                        this.root = a;
                    } else {
                        timing = node.getParent();
                        a.setParent(timing);
                        if (timing.getLeftChild()==node){
                            timing.setLeftChild(a);
                        } else {
                            timing.setRightChild(a);
                        }
                    }
                    a.setRightChild(node);
                    node.setParent(a);
                    node.setLeftChild(h);
                    h.setParent(node);
                    node.setHeight(Math.max(h.getHeight(), b.getHeight()) + 1);
                    a.setHeight(Math.max(g.getHeight(), node.getHeight()) + 1);
                    if (a.getParent()!=null){
                        a.getParent().setHeight(Math.max(a.getParent().getLeftChild().getHeight(), a.getParent().getRightChild().getHeight()));
                    }
                    isWork = true;
                    if (h.getValue()==null){
                        node.setLeftChild(null);
                    }
                    if (b.getValue()==null){
                        node.setRightChild(null);
                    }
                    if (g.getValue()==null){
                        a.setLeftChild(null);
                    }
                }
                //Большое правое вращение
                if (((a.getHeight() - b.getHeight())==2)&&(h.getHeight()>g.getHeight())){
                    //System.out.println(a.getValue() + ", " + b.getValue());
                    //System.out.println(a.getHeight() + ", " + b.getHeight());
                    if (node.getParent()==null){
                        h.setParent(null);
                        this.root = h;
                    } else {
                        timing = node.getParent();
                        h.setParent(timing);
                        if (timing.getLeftChild()==node){
                            timing.setLeftChild(h);
                        } else {
                            timing.setRightChild(h);
                        }
                    }
                    h.setLeftChild(a);
                    a.setParent(h);
                    a.setRightChild(i);
                    i.setParent(a);
                    h.setRightChild(node);
                    node.setParent(h);
                    node.setLeftChild(j);
                    j.setParent(node);
                    a.setHeight(Math.max(g.getHeight(), i.getHeight()) + 1);
                    node.setHeight(Math.max(j.getHeight(), b.getHeight()) + 1);
                    h.setHeight(Math.max(a.getHeight(), node.getHeight()) + 1);
                    if (h.getParent()!=null){
                        h.getParent().setHeight(Math.max(h.getParent().getLeftChild().getHeight(), h.getParent().getRightChild().getHeight()));
                    }
                    isWork = true;
                    if (g.getValue()==null){
                        a.setLeftChild(null);
                    }
                    if (i.getValue()==null){
                        a.setRightChild(null);
                    }
                    if (j.getValue()==null){
                        node.setLeftChild(null);
                    }
                    if (b.getValue()==null){
                        node.setRightChild(null);
                    }
                }
            }
            //balanсe(node.getLeftChild());
            //balanсe(node.getRightChild());
        }
    }
}
