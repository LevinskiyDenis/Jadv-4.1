import java.util.concurrent.LinkedBlockingQueue;

public class CallCenter {

    // выбрал LinkedBlockingQueue, т.к. она производительнее, чем ArrayBlockingQueue, за счет двух локов (чтение и запись)
    // плюс возможность указывать буфер или работать без буфера. Исходя из ТЗ, это теоретически может быть удобно при реализации функционала колл-центра

    private LinkedBlockingQueue<Call> lbq = new LinkedBlockingQueue<>();

    final int NUMOFCALLSPERMIN = 60;
    final int TIMETOANSWERCALL = 5000;
    final int CALLTIMEOUT = 1500;

    public void atsWork() {
        System.out.println(Thread.currentThread().getName() + " начала работу и принимает звонки");

        for (int i = 0; i < NUMOFCALLSPERMIN; i++) {
            Call call = new Call();
            lbq.add(call);
            System.out.println(Thread.currentThread().getName() + " поставила в очередь звонок от " + call.getName());

            try {
                Thread.sleep(CALLTIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void operatorWork() {
        while (lbq.peek() != null) {
            Call call = lbq.poll();
            System.out.printf("%s принял звонок %s \n", Thread.currentThread().getName(), call.getName());

            try {
                Thread.sleep(TIMETOANSWERCALL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("%s завершил звонок %s \n", Thread.currentThread().getName(), call.getName());
        }
    }

}
