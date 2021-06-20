public class MLinkedListProgram {
    public static void main(String[] args) {
        MLinkedList<String> sl = new MLinkedList<>(new String[]{"1", "2","4"});
        System.out.println(sl.addLast("5"));
        System.out.println(sl.addFirst("0"));

        try{
            System.out.println(sl.addAfter(2,("3")));
            System.out.println("");
            MLinkedList sl1 = new MLinkedList<>(new String[]{"6","7","8","9","10"});
            MLinkedList slm = sl.merge(sl1);
            for (int i = 0;i<11;i++){
                System.out.println(slm.get(i));
            }
            System.out.println("");
            System.out.println(sl.remove(1));
            System.out.println(sl.addAfter(-1,"Sos"));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }

        try{
            System.out.println(sl.get(0));
            System.out.println(sl.get(1));
            System.out.println(sl.get(2));
            System.out.println(sl.get(3));
            System.out.println(sl.get(4));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(sl.isEmpty());
        System.out.println(sl.size());

        System.out.println("");

        MLinkedList<String> a= new MLinkedList<>();
        MLinkedList<String> b= new MLinkedList<>();
        try{
            System.out.println(a.get(0));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println(a.addLast("1"));
        try{
            System.out.println(a.get(0));
            System.out.println(a.remove(0));
            System.out.println(a.get(0));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("");
        a = new MLinkedList<>();
        try{
            System.out.println(a.addFirst("5"));
            System.out.println(a.get(0));
            System.out.println(a.remove(0));
            System.out.println(a.addAfter(0,"5"));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("");
        System.out.println("");
        MLinkedList<String> c = a.merge(b);
        MLinkedList<String> d = a.merge(sl);
        MLinkedList<String> e = sl.merge(a);
        System.out.println(c.size());
        System.out.println(d.size());
        System.out.println(e.size());
        System.out.println("");

        MLinkedList<String> mll= new MLinkedList<>(new String[]{"c","r","a","c","k","e","r"});
        System.out.println(mll.removeElement("c"));
        for (int i = 0; i<mll.size();i++){
            try{
                System.out.println(mll.get(i));
            }catch (WrongIndexMLinkedListException ex){
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("");

        System.out.println(mll.removeElement("c"));
        for (int i = 0; i<mll.size();i++){
            try{
                System.out.println(mll.get(i));
            }catch (WrongIndexMLinkedListException ex){
                System.out.println(ex.getMessage());
            }
        }

        MLinkedList<String> mol = new MLinkedList<>(mll);
        mll.removeElement("k");
        try{
            System.out.println(mol.get(0));
            System.out.println(mol.get(1));
            System.out.println(mol.get(2));
            System.out.println(mol.get(3));
            System.out.println(mol.get(4));
            System.out.println(mol.get(5));
        } catch (WrongIndexMLinkedListException ex){
            System.out.println(ex.getMessage());
        }
    }
}
