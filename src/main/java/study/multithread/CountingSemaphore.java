package study.multithread;

public class CountingSemaphore {
    int usedPermits = 0;
    int maxCount;

    public CountingSemaphore (int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void acquire() throws InterruptedException {
        while (this.usedPermits == this.maxCount){
            wait();
        }

        usedPermits++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while (this.usedPermits == 0) {
            wait();
        }

        usedPermits--;
        notify();
    }
}
