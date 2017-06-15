package com.willowtreeapps.hyperion.core.internal;

abstract class Try<T> {

    /**
     * Factory method for failure.
     *
     * @param e   throwable to create the failed Try with
     * @param <U> Type
     * @return a new Failure
     */

    static <U> Try<U> failure(Throwable e) {
        return new Failure<>(e);
    }

    /**
     * Factory method for success.
     *
     * @param x   value to create the successful Try with
     * @param <U> Type
     * @return a new Success
     */
    static <U> Try<U> successful(U x) {
        return new Success<>(x);
    }

    /**
     * Return a value in the case of a failure. This is similar to recover but does not expose the
     * exception type.
     *
     * @param value return the try's value or else the value specified.
     * @return new composed Try
     */
    public abstract T orElse(T value);

    /**
     * Gets the value on Success or throws the cause of the failure.
     *
     * @return new composed Try
     * @throws Throwable
     */
    public abstract T get() throws Throwable;

    public abstract boolean isSuccess();

    private static class Success<T> extends Try<T> {
        private final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public T orElse(T value) {
            return this.value;
        }

        @Override
        public T get() throws Throwable {
            return value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public String toString() {
            return "Success{" +
                    "value=" + value +
                    '}';
        }
    }

    private static final class Failure<T> extends Try<T> {
        private final Throwable e;

        Failure(Throwable e) {
            this.e = e;
        }

        @Override
        public T orElse(T value) {
            return value;
        }

        @Override
        public T get() throws Throwable {
            throw e;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public String toString() {
            return "Failure{" +
                    "e=" + e +
                    '}';
        }
    }
}