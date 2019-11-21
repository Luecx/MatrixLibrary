package core.threads;

public class PoolThread extends Thread {

    private Object object = new Object();

    private PoolThreadRange counter;
    private PoolFunction function;
    private PoolLock lock;

    private boolean executing = false;

    public PoolThread() {
        this.start();
    }

    public boolean isExecuting() {
        return executing;
    }

    public void execute(PoolThreadRange counter, PoolFunction function, PoolLock lock) {
        if(this.isExecuting()) throw new RuntimeException("Thread is already executing");
        this.lock = lock;
        this.counter = counter;
        this.function = function;
        synchronized (object) {
            object.notify();
        }
    }

    @Override
    public void run() {
        synchronized (object) {
            while (!this.isInterrupted()) {
                try {
                    object.wait();
                    this.executing = true;
                    for(int i = counter.start; i < counter.end; i++){
                        this.function.execute(i);
                    }
                    this.executing = false;
                    this.lock.unlock();

                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
            this.interrupt();
        }

    }

}