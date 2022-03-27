package ITEM85;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Example {
    private final static String FILE_PATH = "C:\\Users\\박찬\\스터디\\EFFECTIVE_JAVA\\src\\ITEM85\\test";
    private final static ObjectInputFilter OBJ_INPUT_FILTER = ObjectInputFilter.Config.createFilter("ITEM85.TestSerialize;");

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        TestSerialize ts = new TestSerialize("testName", "testString");
        System.out.println("before: ts: " + ts);
        serialized(ts);
        System.out.println("after: ts: " + deserialized());


    }

    public static void serialized(TestSerialize obj) throws IOException {
        FileOutputStream fis = new FileOutputStream(FILE_PATH);
        ObjectOutputStream oos = new ObjectOutputStream(fis);
        oos.writeObject(obj);
        oos.close();
    }

    public static TestSerialize deserialized() throws InterruptedException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(FILE_PATH);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.setObjectInputFilter(OBJ_INPUT_FILTER);
        TestSerialize obj = (TestSerialize) ois.readObject();
        ois.close();

        return obj;
    }    
}