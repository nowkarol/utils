package com.gryglicki.nulls;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Helper to initialize deep structures in a null-safe way.
 *
 * @param <T> generic type
 *
 * @author Michał Gryglicki
 * @since 08/08/2017
 */

public class NullSafeInitializer<T> {
    private T object;

    private NullSafeInitializer(T object)
    {
        if (object == null) {
            throw new NullPointerException("Starting object have to be provided");
        }
        this.object = object;
    }

    /**
     * Returns initializer with a given not null starting object.
     * Monadic type constructor.
     * @param object starting object
     * @param <T> generic type
     * @return NullSafeInitializer
     */
    public static <T> NullSafeInitializer<T> of(T object)
    {
        return new NullSafeInitializer<>(object);
    }

    /**
     * Initializes field in a current object using given accessors and constructor.
     * Monadic bind operator.
     * @param getter getter for a field to be initialized
     * @param setter setter for a field to be initialized
     * @param supplier supplier for a value to be initialized (eg. no-arg, value supplier)
     * @param <R> generic type of the field to be initialized
     * @return current NullSafeInitializer with an initialized field
     */
    public <R> NullSafeInitializer<T> initIfNull(
            Function<T, R> getter, BiConsumer<T, R> setter, Supplier<R> supplier)
    {
        if (getter.apply(object) == null)
        {
            setter.accept(object, supplier.get());
        }
        return this;
    }

    /**
     * Initializes field in a current object using given accessors and constructor.
     * Monadic bind operator.
     * @param getter getter for a field to be initialized
     * @param setter setter for a field to be initialized
     * @param supplier supplier for a value to be initialized (eg. no-arg, value supplier)
     * @param <R> generic type of the field to be initialized
     * @return NullSafeInitializer of an initialized field
     */
    public <R> NullSafeInitializer<R> initIfNullAndGet(
            Function<T, R> getter, BiConsumer<T, R> setter, Supplier<R> supplier)
    {
        if (getter.apply(object) == null)
        {
            setter.accept(object, supplier.get());
        }
        return of(getter.apply(object));
    }

    /**
     * Returns last initialized not null object.
     * Monadic return function.
     * @return initialized not null object
     */
    public T get()
    {
        return object;
    }


}
