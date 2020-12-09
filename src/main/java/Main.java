public class Main {

    public static void main(String[] args) throws InterruptedException {

        CallCenter callcenter = new CallCenter();

        Runnable atsTask = callcenter::atsWork;
        Runnable operatorTask = callcenter::operatorWork;

        Thread ats = new Thread(null, atsTask, "АТС");
        Thread operator1 = new Thread(null, operatorTask, "Оператор 1");
        Thread operator2 = new Thread(null, operatorTask, "Оператор 2");


        operator1.start();
        operator2.start();
        ats.start();

        ats.join();
        operator1.join();
        operator2.join();

        System.out.println(Thread.currentThread().getName() + " завершает работу");
    }
}
