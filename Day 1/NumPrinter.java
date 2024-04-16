class NumberPrinter {
    private final Object lock = new Object();
    private int number;
    private final int maxNumber;

    public NumberPrinter(int startNumber, int maxNumber) {
        this.number = startNumber;
        this.maxNumber = maxNumber;
    }

    public void printNumbers(boolean isEven) {
        synchronized (lock) {
            while (number <= maxNumber) {
                if ((number % 2 == 0 && isEven) || (number % 2 != 0 && !isEven)) {
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    number++;
                    lock.notify();
                } else {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

class ThreadA extends Thread {
    private final NumberPrinter printer;

    public ThreadA(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printNumbers(false);
    }
}

class ThreadB extends Thread {
    private final NumberPrinter printer;

    public ThreadB(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printNumbers(true);
    }
}

public class NumPrinter {
    public static void main(String[] args) {
        NumberPrinter printer = new NumberPrinter(1, 10);

        ThreadA threadA = new ThreadA(printer);
        ThreadB threadB = new ThreadB(printer);

        threadA.start();
        threadB.start();
    }
}
