package ITEM85;

import java.io.Serializable;

public class TestSerialize implements Serializable {
    private String name = "name";
    private String test = "test";

    public TestSerialize(String name, String test) {
        this.name = name;
        this.test = test;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTest() {
        return test;
    }
    public void setTest(String test) {
        this.test = test;
    }
    @Override
    public String toString() {
        return "TestSerialize [name=" + name + ", test=" + test + "]";
    }    
}
