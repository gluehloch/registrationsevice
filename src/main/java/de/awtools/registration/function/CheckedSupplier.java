package de.awtools.registration.function;

@FunctionalInterface
public interface CheckedSupplier<V, E extends Throwable> {
    V get() throws E;

    public static <V, E extends Throwable> Result<V, E> attempt(CheckedSupplier<? extends V, ? extends E> p) {
        try {
            return Result.success(p.get());
        } catch (Throwable e) {
            @SuppressWarnings("unchecked")
            E err = (E) e;
            return Result.failure(err);
        }
    }
}
