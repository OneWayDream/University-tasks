import org.junit.*;

public class QueueTest {

    private Queue<Integer> testData;

    @Before
    public void setUpQueue(){
        Integer[] data = new Integer[]{1, 2, 3};
        testData = new Queue<>(data);
    }

    @After
    public void tearDownQueue(){
        testData = null;
    }

    @Test
    public void addCheckAvailabilityInList() {
        testData.add(4);
        String actual = "" + testData.pop() + testData.pop() + testData.pop() + testData.pop();
        Assert.assertEquals("1234", actual);
    }

    @Test
    public void addCheckChangeOfSize(){
        final int s = testData.getSize();
        testData.add(4);
        testData.add(5);
        final int actual = testData.getSize();
        Assert.assertEquals(2, actual - s);
    }

    @Test
    public void addCheckInTheVoidQueue(){
        Queue<Integer> data = new Queue<>();
        data.add(1000);
        final int actual = data.getSize();
        Assert.assertEquals(1, actual);
    }

    @Test
    public void popCheckNotVoidQueue()  {
        String actual = "" + testData.pop() + testData.pop() + testData.pop();
        Assert.assertEquals("123", actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void popCheckVoidQueue(){
        Queue<Integer> data = new Queue<>();
        data.pop();
    }

    @Test
    public void popCheckChangeOfQueueSizeAfterPop(){
        final int s = testData.getSize();
        testData.pop();
        final int actual = testData.getSize();
        Assert.assertEquals(1, s - actual);
    }

    @Test
    public void peekCheckChangeOfQueueSize() {
        final int s = testData.getSize();
        testData.peek();
        final int actual = testData.getSize();
        Assert.assertEquals(0, actual - s);
    }

    @Test
    public void peekCheckChangeOfQueue(){
        testData.peek();
        StringBuilder actual = new StringBuilder("");
        while (testData.getSize()!=0){
            actual.append(testData.pop());
        }
        Assert.assertEquals("123", actual.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void peekCheckVoidQueue(){
        Queue<Integer> imVoidQueue = new Queue<>();
        imVoidQueue.peek();
    }

    @Test
    public void getSizeNotVoidQueue() {
        int actual = testData.getSize();
        Assert.assertEquals(3,actual);
    }

    @Test
    public void getSizeVoidQueue(){
        Queue<Integer> voidQueue = new Queue<>();
        final int actual = voidQueue.getSize();
        Assert.assertEquals(0, actual);
    }
