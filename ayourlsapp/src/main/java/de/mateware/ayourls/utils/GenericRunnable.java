package de.mateware.ayourls.utils;

/**
 * Created by Mate on 27.10.2015.
 */
public class GenericRunnable<T> implements Runnable {

    private T value;

    public GenericRunnable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void run() {

    }
}
