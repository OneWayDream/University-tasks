import java.util.Arrays;

public class Multiplication {
    public static void main(String[] args) {
        final int size1 = 3;
        final int size2 = 3;
        int k;
        boolean[] a1 = new boolean[size1];
        boolean[] a2 = new boolean[size2];
        for (int i = 0;i<size1;i++) {
            k = (int) (Math.random() * 2);
            a1[i] = (k == 1);
        }
        for (int i = 0;i<size2;i++){
            k = (int) (Math.random()*2);
            a2[i] = (k==1);
        }
        //a1[0] = false;
        a1[0] = true;
        a1[1] = true;
        a1[2] = true;
        //a2[0] = false;
        a2[0] = false;
        a2[1] = false;
        a2[2] = true;
        System.out.print("First number : ");
        for (int i = 0;i<size1;i++){
            System.out.print((a1[i] ? 1 : 0));
        }
        System.out.println("");
        System.out.print("Twice number : ");
        for (int i = 0;i<size2;i++){
            System.out.print((a2[i] ? 1 : 0));
        }
        System.out.println("");
        boolean[] mult = multiply(a1,a2);
        System.out.print("Result if multiplication : ");
        boolean b = true;
        for (int i = 0;i<mult.length;i++){
            if (!((b)&&(!mult[i]))){
                System.out.print((mult[i] ? 1 : 0));
                b = false;
            }
        }
        if (b){
            System.out.print(0);
        }
    }

    public static boolean[] multiply(boolean[] a1, boolean[] a2){
        if (a1.length!=a2.length){
            if (a1.length>a2.length){
                boolean[] b = new boolean[a1.length];
                int k = b.length-1;
                for (int i =a2.length-1;i>=0;i--){
                    b[k] = a2[i];
                    k--;
                }
                a2 = b;
            } else {
                boolean[] b = new boolean[a2.length];
                int k = b.length-1;
                for (int i =a1.length-1;i>=0;i--){
                    b[k] = a1[i];
                    k--;
                }
                a1 = b;
            }
        }
        if (a1.length!=1){
            boolean[] x1 = Arrays.copyOfRange(a1,0,a1.length/2);
            boolean[] x2 = Arrays.copyOfRange(a1, a1.length/2,a1.length);
            boolean[] y1 = Arrays.copyOfRange(a2,0,a2.length/2);
            boolean[] y2 = Arrays.copyOfRange(a2,a2.length/2,a2.length);
            boolean[] x1y1 = multiply(x1,y1); // Число x1y2;
            boolean[] x2y2 = multiply(x2,y2); // Число x2y2;
            boolean[] p = multiply(sum(x1,x2),sum(y1,y2)); // Число (x1+x2)*(y1+y2);
            boolean[] answer = new boolean[x1y1.length + a1.length]; // Число x1y1*2^n
            for (int i = 0;i<x1y1.length;i++){
                answer[i] = x1y1[i];
            }
            boolean[] s = sum(x1y1,x2y2); // Число x1y2 + x2y2;
            boolean[] d = sub(p,s); // Число (x1+x2)*(y1+y2) - x1y1 - x2y2;
            boolean[] r = new boolean[d.length + a1.length/2]; // Число 2^(n/2)*((x1+x2)*(y1+y2) - x1y1 - x2y2);
            for (int i = 0;i<d.length;i++){
                r[i] = d[i];
            }
            answer = sum (answer,r); // Число  x1y1*2^n + 2^(n/2)*((x1+x2)*(y1+y2) - x1y1 - x2y2);
            answer = sum (answer,x2y2);
            return answer;
        } else {
            if ((a1[0])&&(a2[0])){
                return a1;
            } else {
                return new boolean[]{false};
            }
        }
    }

    public static boolean[] sum (boolean[] a1, boolean[] a2){
        boolean[] answ = new boolean[Math.max(a1.length,a2.length)+1];
        int k = 0;
        int s ;
        int a = a1.length -1 ,b = a2.length - 1;
        for (int i = answ.length-1;i>=0;i--){
            s = 0;
            if ((a>=0)&&(a1[a])){
                s+=1;
            }
            if ((b>=0)&&(a2[b])){
                s+=1;
            }
            s+=k;
            switch (s){
                case 3 :
                    k = 1;
                    answ[i] = true;
                    break;
                case 2 :
                    k = 1;
                    answ[i] = false;
                    break;
                case 1:
                    k = 0;
                    answ[i] = true;
                    break;
                case 0:
                    k = 0;
                    answ[i] = false;
                    break;
            }
            a--;
            b--;
        }
        if (!answ[0]){
            return Arrays.copyOfRange(answ,1,answ.length);
        } else {
            return answ;
        }
    }

    public static boolean[] sub(boolean[] a, boolean[] b){
        boolean[] answer = new boolean[a.length];
        int k = answer.length - 1;
        for (int i = b.length-1;i>=0;i--){
            if (((!a[k])&&(!b[i]))||((a[k])&&(b[i]))){
                answer[i] = false;
            } else if ((!a[k])&&(b[i])){
                for (int j = k;j>=0;j--){
                    if (a[j]){
                        answer[j]=false;
                        break;
                    } else {
                        answer[j] = true;
                    }
                }
            } else if ((a[i])&&(!b[i])){
                answer[i] = true;
            }
            k--;
        }
        return answer;
    }
}
