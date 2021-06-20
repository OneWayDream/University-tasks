public class MLinkedList<T> {

    protected Node head;
    protected int counter;

    private class Node{

        private T data;
        private Node next;

        public Node(){
            this.data = null;
            this.next = null;
        }

        public Node(T data, Node next){
            this.data = data;
            this.next = next;
        }

        public Node(T data){
            this.data = data;
            this.next = null;
        }

        public T getData(){
            return data;
        }

        public Node getNext(){
            return next;
        }

        public void setNext(Node next){
            this.next = next;
        }

        public void setData(T data){
            this.data = data;
        }
    }

    public MLinkedList(){
        this.head = null;
        counter = 0;
    }

    public MLinkedList(T data){
        this.head = new Node(data,(null));
        this.counter = 1;
    }

    public MLinkedList(T[] array){
        this.head = new Node(array[0]);
        Node nd = head;
        Node item;
        for (int i = 1;i<array.length;i++){
            item = new Node(array[i]);
            nd.setNext(item);
            nd = item;
        }
        this.counter = array.length;
    }

    public MLinkedList(MLinkedList<T> mll){
         if (mll.getHead()==null){
            this.head=null;
            this.counter=0;
            return;
        }
         this.head = new Node(mll.getHead().getData(), null);
         this.counter = mll.size();
         Node e1 = mll.getHead();
         Node e2 = this.head;
         while (e1.getNext()!=null){
             e1 = e1.getNext();
             e2.setNext(new Node(e1.getData()));
             e2 = e2.getNext();
         }
    }

    public boolean addLast(T item){
        if (head==null){
            head = new Node(item,(null));
            counter++;
            return true;
        }
        Node itemm = new Node(item,(null));
        Node nd = head;
        while (nd.getNext() != null){
            nd = nd.getNext();
        }
        nd.setNext(itemm);
        counter++;
        return true;
    }

    public boolean addFirst(T item){
        if (head==null){
            head = new Node(item,(null));
            counter++;
            return true;
        }
        Node nd = head;
        this.head = new Node(item,nd);
        counter++;
        return true;
    }

    public boolean addAfter(int index, T item) throws WrongIndexMLinkedListException{
        if ((index>=0)&&(index<counter)){
            Node itemm = new Node(item,(null));
            Node nd = head;
            int k = 0;
            while (k<index){
                nd = nd.getNext();
                k++;
            }
            Node node = nd.getNext();
            nd.setNext(itemm);
            itemm.setNext(node);
            counter++;
            return true;
        } else {
            throw new WrongIndexMLinkedListException("An item with this index does not exist.");
        }
    }

    public T get(int index) throws WrongIndexMLinkedListException{
        int k = 0;
        Node nd = head;
        if ((index>=0)&&(index<counter)){
            while (k<index){
                nd = nd.getNext();
                k++;
            }
            return nd.getData();
        } else {
            throw new WrongIndexMLinkedListException("An item with this index does not exist.");
        }
    }

    public boolean remove(int index) throws WrongIndexMLinkedListException{
        if ((index>=0)&&(index<counter)){
            int k = 0;
            Node nd = head;
            if (index == 0){
                if (counter==1){
                    this.head = null;
                    counter--;
                    return true;
                } else {
                    this.head = new Node(this.head.getData());
                    counter--;
                    return true;
                }
            } else {
                while (k<index-1){
                    nd = nd.getNext();
                    k++;
                }
                Node rnode = nd.getNext();
                Node afretRNode = rnode.getNext();
                nd.setNext(afretRNode);
                counter--;
                return true;
            }
        } else {
            throw new WrongIndexMLinkedListException("An item with this index does not exist anyway.");
        }
    }

    public boolean removeElement(T item){
        Node nd = head;
        Node ld;
        if (head.getData().equals(item)){
            head = head.getNext();
            counter--;
            return true;
        }
        ld = head;
        while (nd!=null){
            if (nd.getData().equals(item)){
                ld.setNext(nd.getNext());
                counter--;
                return true;
            }
            ld = nd;
            nd = nd.getNext();
        }
        return false;
    }

    public MLinkedList merge(MLinkedList<T> twiceList){
        MLinkedList<T> t1 = new MLinkedList<T>(this);
        MLinkedList<T> t2 = new MLinkedList<T>(twiceList);
        if (t1.head == null){
            return t2;
        } else if (t2.head==null){
            return t1;
        }
        Node nd = t1.getHead();
        while (nd.getNext()!=null){
            nd = nd.getNext();
        }
        nd.setNext(t2.getHead());
        t1.counter = this.counter + twiceList.size();
        return t1;
    }

    public boolean isEmpty(){
        if (counter==0){
            return true;
        } else {
            return false;
        }
    }

    public T getLast() throws NoElementsMLinkedListException {
        if (counter!=0){
            Node nd = this.head;
            while (nd.getNext()!=null){
                nd = nd.getNext();
            }
            return nd.getData();
        } else {
            throw new NoElementsMLinkedListException("This list is empty.");
        }
    }

    public int size(){
        return counter;
    }

    private Node getHead(){
        return head;
    }
}
