public class ThreadsPoolTest {
    public static void main(String[] args) {
        MyThreadsPool myThreadsPool = new MyThreadsPool(3);
        myThreadsPool.submit(()-> {
            for (int i = 0; i < 500; i++){
                System.out.println("------");
            }
        });
        myThreadsPool.submit(()-> {
            for (int i = 0; i < 500; i++){
                System.out.println("------ ------");
            }
        });
        myThreadsPool.submit(()-> {
            for (int i = 0; i < 500; i++){
                System.out.println("------ ------ ------");
            }
        });
        myThreadsPool.submit(()-> {
            for (int i = 0; i < 500; i++){
                System.out.println("------ ------ ------ ------");
            }
        });
    }
}
