package study.multithread;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DeferredCallbackExecutor {
    PriorityQueue<Callback> queue = new PriorityQueue<>((o1, o2) -> (int) (o1.executeAt - o2.executeAt));

    ReentrantLock lock = new ReentrantLock();
    Condition newCallBackArrived = lock.newCondition();

    public void start () throws InterruptedException {
        long sleepFor = 0;

        while (true) {
            lock.lock();

            while (queue.size() == 0) {
                newCallBackArrived.await();
            }

            while (queue.size() != 0) {
                sleepFor = findSleepDuration();

                if (sleepFor <= 0) {
                    break;
                }

                newCallBackArrived.await(sleepFor, TimeUnit.MILLISECONDS);
            }

            Callback callback = queue.poll();
            System.out.println("Executed at " + System.currentTimeMillis() / 1000 + " required at "
                    + callback.executeAt / 1000 + ": message:" + callback.message);
            
            lock.unlock();
        }
    }

    private long findSleepDuration() {
        long currentTime = System.currentTimeMillis();
        return queue.peek().executeAt - currentTime;
    }

    public void registerCallback(Callback callbBack) {
        lock.lock();
        queue.add(callbBack);
        newCallBackArrived.signal();
        lock.unlock();
    }

    static class Callback {
        long executeAt;
        String message;

        public Callback (long executeAfter, String message) {
            this.executeAt = System.currentTimeMillis() + executeAfter * 1000;
            this.message = message;
        }
    }
}
