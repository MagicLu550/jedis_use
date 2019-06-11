package net.noyark.redis_demo;

public class Test {

    public static void main(String[]args){
        TestSync();
    }


    public static void TestSync(){
        User user = new User();

        Runnable r1 = ()->{
            user.hello();
        };

        Thread t1 = new Thread(r1);
        t1.start();


        Runnable r2 = ()->{
            user.nb();
        };

        Thread t2 = new Thread(r2);
        t2.start();
        try{
            t2.join();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
class User{

    Integer i =0;

    public void hello(){
        synchronized (this){
            System.out.println("有锁");
            try{
                Thread.sleep(5);
            }catch (Exception e){
                e.printStackTrace();
            }

            synchronized (i){
                i++;
                System.out.println("now");
            }
        }
        System.out.println("锁没了");
    }

    public void nb(){
        synchronized (this){
            i++;
            System.out.println(11);
        }

    }

}
