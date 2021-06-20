import org.junit.*;

import java.util.ArrayList;
import java.util.Iterator;

public class MCollectionTest {

    private static MCollection<String> testData;
    private static ArrayList<String> arrayList = new ArrayList<>();

    @Before
    public void setUpMCollection(){
        arrayList.add("one");
        arrayList.add("two");
        arrayList.add("three");
        testData = new MCollection<String>(arrayList);
    }

    @After
    public void tearDownMCollection(){
        arrayList.clear();
    }

    @Test
    public void iteratorCheck3ElementsInCollection() {
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwothree", str.toString());
    }

    @Test
    public void iteratorCheckEmptyCollection(){
        testData.remove("one");
        testData.remove("two");
        testData.remove("three");
        Iterator<String> iter = testData.iterator();
        Assert.assertFalse(iter.hasNext());
    }

    @Test
    public void iteratorCheckAfterAdding(){
        testData.add("fourth");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwothreefourth", str.toString());
    }

    @Test
    public void iteratorCheckAfterRemoving(){
        testData.remove("one");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("twothree", str.toString());
    }

    @Test
    public void iteratorCheck10ElementsInCollection(){
        testData.add("fourth");
        testData.add("fifth");
        testData.add("sixth");
        testData.add("seventh");
        testData.add("eighth");
        testData.add("ninth");
        testData.add("tenth");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwothreefourthfifthsixthseventheighthninthtenth", str.toString());
    }

    @Test
    public void sizeCheck3ElementsInCollection() {
        final int actual = testData.size();
        Assert.assertEquals(3, actual);
    }

    @Test
    public void sizeCheckAfterRemoveFirstElement(){
        testData.remove("one");
        final int actual = testData.size();
        Assert.assertEquals(2, actual);
    }

    @Test
    public void sizeCheckAfterRemoveLastElement(){
        testData.remove("three");
        final int actual = testData.size();
        Assert.assertEquals(2, actual);
    }

    @Test
    public void sizeCheckEmptyCollection(){
        testData.remove("one");
        testData.remove("two");
        testData.remove("three");
        final int actual = testData.size();
        Assert.assertEquals(0, actual);
    }

    @Test
    public void sizeCheck100ElementsInCollection(){
        for (int i = 0; i<97;i++){
            testData.add("" + i);
        }
        final int actual = testData.size();
        Assert.assertEquals(100, actual);
    }

    @Test
    public void addCheckAvailabilityInCollection() {
        testData.add("test");
        Iterator<String> iter = testData.iterator();
        String actual = null;
        while (iter.hasNext()){
            actual = iter.next();
        }
        Assert.assertEquals("test", actual);
    }

    @Test
    public void addCheckChangeOfSize(){
        testData.add("test");
        final int actual = testData.size();
        Assert.assertEquals(4, actual);
    }

    @Test
    public void addCheckAddAndRemoveTheSameElement(){
        testData.add("test");
        testData.remove("test");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwothree", str.toString());
    }

    @Test
    public void addCheckAddAndRemoveDifferentElements(){
        testData.add("test");
        testData.remove("three");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwotest", str.toString());
    }

    @Test
    public void addCheckAdd97ElementsAndCheckChangeOfSize(){
        final int s = testData.size();
        for (int i = 0; i<97;i++){
            testData.add("" + i);
        }
        final int actual = testData.size();
        Assert.assertEquals(97, actual - s );
    }

    @Test
    public void removeCheckCentralElement() {
        testData.remove("two");
        final boolean actual = testData.contains("two");
        Assert.assertFalse(actual);
    }

    @Test
    public void removeCheckFirstElement(){
        testData.remove("one");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("twothree", str.toString());
    }

    @Test
    public void removeCheckLastElement(){
        testData.remove("three");
        StringBuilder str = new StringBuilder("");
        Iterator<String> iter = testData.iterator();
        while (iter.hasNext()){
            str.append(iter.next());
        }
        Assert.assertEquals("onetwo", str.toString());
    }

    @Test
    public void equalsCheckEmptyCollections(){
        MCollection<String> col1 = new MCollection<>();
        MCollection<String> col2 = new MCollection<>();
        final boolean actual = col1.equals(col2);
        Assert.assertTrue(actual);
    }

    @Test
    public void equalsCheckCollectionAndInstanceOfAnotherClass(){
        ArrayList<MCollection<String>> data = new ArrayList<>();
        final boolean actual = testData.equals(data);
        Assert.assertFalse(actual);
    }

    @Test
    public void equalsCheckCollectionsWithDifferentContents(){
        MCollection<String> data = new MCollection<>();
        data.add("one");
        data.add("three");
        final boolean actual = testData.equals(data);
        Assert.assertFalse(actual);
    }

    @Test
    public void equalsCheckCollectionsWithSameContentsAndSameOrderOfAddition(){
        MCollection<String> example = new MCollection<>();
        example.add("one");
        example.add("two");
        example.add("three");
        final boolean actual = testData.equals(example);
        Assert.assertTrue(actual);
    }

    @Test
    public void equalsCheckCollectionsWithSameContentsAndDifferentOrderOfAddition() {
        MCollection<String> example = new MCollection<>();
        example.add("three");
        example.add("one");
        example.add("two");
        final boolean actual = testData.equals(example);
        Assert.assertTrue(actual);
    }
}
