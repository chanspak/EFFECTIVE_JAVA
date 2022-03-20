package ITEM78;

import java.util.concurrent.TimeUnit;

public class Example {
    private static boolean stopRequested;

    private static synchronized void requestStop(){
        stopRequested = true;
    }

    private static synchronized boolean stopRequested(){
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        //doExample1();
        doExample2();
    }

    public static void doExample1() throws InterruptedException {
        Thread backgroundThread = new Thread(()->{
            int i = 0;
            while(!stopRequested){
                i++;
                //System.out.println(i++);
                //System.out.println("backgroundThread Thraad: " + stopRequested);
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
        //System.out.println("main Thraad: " + stopRequested);
    }

    public static void doExample2() throws InterruptedException {
        Thread backgroundThread = new Thread(()->{
            int i = 0;
            while(!stopRequested()){
                i++;
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}