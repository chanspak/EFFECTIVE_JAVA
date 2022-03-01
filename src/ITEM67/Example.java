package ITEM67;

import java.awt.Container;
import java.awt.Dimension;

public class Example {
    public static void main(String[] args) {
        Container container = new Container();
        container.setSize(10, 100);

        Dimension dimension = container.getSize();  //Dimension 강제적으로 생성.

        System.out.println("dimension width: " + dimension.getWidth());
        System.out.println("dimension Height: " + dimension.getHeight());

        System.out.println("container width: " + container.getWidth());
        System.out.println("container Height: " + container.getHeight());       
    }
}