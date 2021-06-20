import java.util.Arrays;

public class RMergeSet {
    public static void main(String[] args) {
        final int size = 10;
        int[] array = new int[size];
        for (int i = 0;i<size;i++){
            array[i] = (int) (Math.random()*10);
            System.out.print(array[i] + " ");
        }
        System.out.println("");
        System.out.print("After mergeSort : ");
        array = mergeSort(array);
        Arrays.stream(array).forEach(x-> System.out.print(x + " "));
    }

    public static int[] mergeSort(int[] array){
        if (array.length>1){
            int[] a1 = mergeSort(Arrays.copyOfRange(array,0,array.length/2));
            int[] a2 = mergeSort(Arrays.copyOfRange(array,array.length/2,array.length));
            return merge(a1,a2);
        } else {
            return array;
        }

    }

    public static int[] merge(int[] a1, int[] a2){
        int[] array = new int[a1.length+a2.length];
        int j = 0,k = 0;
        for (int i = 0;i<a1.length+a2.length;i++){
            if (j>=a1.length){
                array[i] = a2[k];
                k++;
            } else if (k>=a2.length){
                array[i] = a1[j];
                j++;
            } else {
                if (a1[j]<a2[k]){
                    array[i] = a1[j];
                    j++;
                } else {
                    array[i] = a2[k];
                    k++;
                }
            }
        }
        return array;
    }
}
