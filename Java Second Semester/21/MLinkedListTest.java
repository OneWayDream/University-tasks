import org.junit.*;

public class MLinkedListTest {

    private MLinkedList<String> testData;

    @Before
    public void setUpMLinkedList(){
        String[] data = new String[]{"first", "twice", "third"};
        this.testData = new MLinkedList<>(data);
    }

    @After
    public void tearDownMLinkedList(){
        this.testData = null;
    }

    @Test
    public void addLastCheckAvailabilityInList() throws WrongIndexMLinkedListException {
        testData.addLast("test");
        final String actual = testData.get(3);
        Assert.assertEquals("test", actual);
    }

    @Test
    public void addLastCheckChangeOfSize(){
        final int s = testData.size();
        testData.addLast("test");
        final int actual = testData.size();
        Assert.assertEquals(1, actual - s);
    }

    @Test
    public void addLastCheckInTheVoidList() {
        MLinkedList<String> data = new MLinkedList<>();
        data.addLast("test");
        final int actual = data.size();
        Assert.assertEquals(1, actual);
    }

    @Test
    public void addFirstCheckAvailabilityInList() throws WrongIndexMLinkedListException {
        testData.addFirst("test");
        final String actual = testData.get(0);
        Assert.assertEquals("test", actual);
    }

    @Test
    public void addFirstCheckChangeOfSize(){
        final int s = testData.size();
        testData.addFirst("test");
        final int actual = testData.size();
        Assert.assertEquals(1, actual - s);
    }

    @Test
    public void addFirstCheckInTheVoidList(){
        MLinkedList<String> data = new MLinkedList<>();
        data.addFirst("test");
        final int actual = data.size();
        Assert.assertEquals(1, actual);
    }

    @Test
    public void addAfterCheckAvailabilityInList() throws WrongIndexMLinkedListException {
        testData.addAfter(0, "test");
        final String actual = testData.get(1);
        Assert.assertEquals("test", actual);
    }

    @Test
    public void addAfterCheckChangeOfSize() throws WrongIndexMLinkedListException {
        final int s = testData.size();
        testData.addAfter(1, "test");
        final int actual = testData.size();
        Assert.assertEquals(1, actual - s);
    }

    @Test(expected = WrongIndexMLinkedListException.class)
    public void addAfterCheckIncorrectIndex() throws WrongIndexMLinkedListException {
        testData.addAfter(-1, "I wait exception!");
    }

    @Test
    public void getCheckExistingIndex() throws WrongIndexMLinkedListException {
        final String actual = testData.get(1);
        Assert.assertEquals("twice", actual);
    }

    @Test(expected = WrongIndexMLinkedListException.class)
    public void getCheckNonexistentIndex() throws WrongIndexMLinkedListException {
        testData.get(10000000);
    }

    @Test(expected = WrongIndexMLinkedListException.class)
    public void getCheckIncorrectIndex() throws WrongIndexMLinkedListException {
        testData.get(-10000000);
    }

    @Test
    public void removeCheckExistingIndex() throws WrongIndexMLinkedListException {
        testData.remove(1);
        final String actual = testData.get(1);
        Assert.assertEquals("third", actual);
    }

    @Test(expected = WrongIndexMLinkedListException.class)
    public void removeCheckNonexistentIndex() throws WrongIndexMLinkedListException{
        testData.remove(1000000);
    }

    @Test(expected = WrongIndexMLinkedListException.class)
    public void removeCheckIncorrectIndex() throws WrongIndexMLinkedListException{
        testData.remove(-1000000);
    }

    @Test
    public void removeElementCheckExistingElement() throws WrongIndexMLinkedListException {
        testData.removeElement("twice");
        final String actual = testData.get(1);
        Assert.assertEquals("third", actual);
    }

    @Test
    public void removeElementCheckNonexistentElement(){
        final int s = testData.size();
        testData.removeElement("Hi, im'not exist(");
        final int actual = testData.size();
        Assert.assertEquals(0, actual - s);
    }

    @Test
    public void mergeCheckDifferentNotVoidLists() throws WrongIndexMLinkedListException {
        String[] data = new String[]{"fourth", "fifth"};
        MLinkedList<String> t = new MLinkedList<>(data);
        MLinkedList<String> e = testData.merge(t);
        StringBuilder str = new StringBuilder("");
        for (int i = 0;i<5;i++){
            str.append(e.get(i));
        }
        Assert.assertEquals("firsttwicethirdfourthfifth", str.toString());
    }

    @Test
    public void mergeCheckOneListIsVoid() throws WrongIndexMLinkedListException {
        MLinkedList<String> l = new MLinkedList<>();
        MLinkedList<String> result = testData.merge(l);
        StringBuilder actual = new StringBuilder("");
        for (int i = 0; i < 3; i++){
            actual.append(result.get(i));
        }
        Assert.assertEquals("firsttwicethird", actual.toString());
    }

    @Test
    public void isEmptyCheckNotEmpty() {
        final boolean actual = testData.isEmpty();
        Assert.assertFalse(actual);
    }

    @Test
    public void isEmptyCheckEmptyList(){
        MLinkedList<String> test = new MLinkedList<>();
        final boolean actual = test.isEmpty();
        Assert.assertTrue(actual);
    }

    @Test
    public void getLastCheckNotVoidList() throws NoElementsMLinkedListException {
        final String actual = testData.getLast();
        Assert.assertEquals("third", actual);
    }

    @Test(expected = NoElementsMLinkedListException.class)
    public void getLastCheckVoidList() throws NoElementsMLinkedListException {
        MLinkedList<String> voidList = new MLinkedList<>();
        voidList.getLast();
    }

    @Test
    public void sizeCheckNotVoidList() {
        final int actual = testData.size();
        Assert.assertEquals(3, actual);
    }

    @Test
    public void sizeCheckVoidList(){
        MLinkedList<String> voidList = new MLinkedList<>();
        final int actual = voidList.size();
        Assert.assertEquals(0, actual);
    }

    @Test
    public void sizeCheckAfterChange() throws WrongIndexMLinkedListException {
        final int s = testData.size();
        testData.addFirst("1");
        testData.addLast("2");
        testData.remove(3);
        final int actual = testData.size();
        Assert.assertEquals(1, actual - s);
    }
