package ITEM65;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Example {
    public static void main(String[] args) {
        Class<ReflectCommon> cl = null;
        Constructor<ReflectCommon> cons = null;
        ReflectCommon reflictCommon = null;

        try {
            // classLoader에 newInstance Deprecated 됨.
            // Constructor를 통해서 newInstance를 사용해야함.
            //cl = ((Class<ReflectCommon>) ClassLoader.getSystemClassLoader().loadClass(args[0])).newInstance();
            cl = (Class<ReflectCommon>) Class.forName(args[0]);                 // class load
            cons = (Constructor<ReflectCommon>) cl.getDeclaredConstructor();    // 생성자 가져옴
            reflictCommon = cons.newInstance();                                 // 인스턴스 생성
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // set 함수 실행
        for (Method method: reflictCommon.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                System.out.println("set Method: " + method.getName());
            }
        }
        // get 함수 실행
        for (Method method: reflictCommon.getClass().getMethods()) {
            if (method.getName().startsWith("get")) {
                System.out.println("get Method: " + method.getName());
            }
        }
    }
}
