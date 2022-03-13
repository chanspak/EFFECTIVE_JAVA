package ITEM70;

public class Example {
    public static void main(String[] args) {
    }

    public class ExampleThrowable extends Exception {
        ExampleThrowable (String message, Throwable cause) {
            super(message, cause);
        }
    }
}