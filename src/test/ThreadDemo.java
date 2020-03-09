package test;

public class ThreadDemo extends Thread {
    private static int threadCount = 0;
    private int countDown = 5;
    private int threadNumber = ++threadCount;

    public ThreadDemo() {
        System.out.println("Making " + threadNumber);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++)
            new ThreadDemo().start();
        System.out.println("All Threads Started");
    }

    public void run() {
        while (true) {
            System.out.println("Thread " +
                    threadNumber + "(" + countDown + ")");
            if (--countDown == 0) return;
        }
    }
}