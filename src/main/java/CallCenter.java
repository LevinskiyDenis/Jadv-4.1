import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.*;

public class CallCenter {

    ReentrantLock lock = new ReentrantLock(true);
    Condition condition = lock.newCondition();

    // выбрал LinkedBlockingQueue, т.к. она производительнее, чем ArrayBlockingQueue, за счет двух локов (чтение и запись)
    // плюс возможность указывать буфер или работать без буфера. Исходя из ТЗ, это теоретически может быть удобно при реализации функционала колл-центра

    private LinkedBlockingQueue<Call> lbq = new LinkedBlockingQueue<>();
    volatile int NumWaitingOnLine;

    final int NUMOFCALLSPERMIN = 30;
    final int TIMETOANSWERCALL = 5000;
    final int CALLTIMEOUT = 1000;

    public void atsWork() {

        for (int i = 0; i < NUMOFCALLSPERMIN; i++) {
            Call call = new Call();
            lbq.add(call);
            System.out.println(Thread.currentThread().getName() + " поставила в очередь звонок от " + call.getName());
            NumWaitingOnLine += 1;

//            synchronized (this) {
//                notifyAll();
//            }

            lock.lock();
            try {
                condition.signalAll();
                Thread.sleep(CALLTIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void operatorWork() {

//        synchronized (this) {
//            while (NumWaitingOnLine == 0) {
//                try {
//                    System.out.println(Thread.currentThread().getName() + " ожидает поступление звонков");
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        lock.lock();
        try {
            while (NumWaitingOnLine == 0) {
                System.out.println(Thread.currentThread().getName() + " ожидает поступление звонков");
                condition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


        while (lbq.peek() != null) {
            Call call = lbq.poll();
            NumWaitingOnLine -= 1;
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
