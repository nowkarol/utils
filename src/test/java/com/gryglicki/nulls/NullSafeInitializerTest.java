package com.gryglicki.nulls;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link NullSafeInitializer}
 *
 * @author Michał Gryglicki
 * @since 08/08/2017
 */
public class NullSafeInitializerTest {
    @Test
    public void shouldReturnSameObjectAsCreatedWith()
    {
        //Given
        Object object = new Object();
        //When
        NullSafeInitializer<Object> initializer = NullSafeInitializer.of(object);
        //Then
        assertEquals(object, initializer.get());
    }

    @Test
    public void shouldNotInitialzeNotNullElements()
    {
        //Given
        B b = new B("some value");
        A a = new A(b);
        assertNotNull(a.b());
        //When
        NullSafeInitializer<A> initializer = NullSafeInitializer.of(a);
        B bAfterInitialization = initializer.initIfNullAndGet(A::b, A::b, B::new).get();
        //Then
        assertSame(b, bAfterInitialization);
        assertSame(a.b(), b);
    }

    @Test
    public void shouldInitialzeNullElementsAndReturnThem()
    {
        //Given
        A a = new A(null);
        //When
        B bAfterInitialization = NullSafeInitializer.of(a)
                .initIfNullAndGet(A::b, A::b, B::new)
                .get();
        //Then
        assertSame(bAfterInitialization, a.b());
    }

    @Test
    public void shouldInitialzeNullElementsFluently()
    {
        //Given
        A a = new A(null);
        //When
        String stringAfterInitialization = NullSafeInitializer.of(a)
                .initIfNullAndGet(A::b, A::b, B::new)
                .initIfNullAndGet(B::stringValue, B::stringValue, String::new)
                .get();
        //Then
        assertNotNull(a.b());
        assertSame(stringAfterInitialization, a.b().stringValue());
    }

    @Test
    public void shouldAllowToInitializeMultipleElementsOfASameInitializer()
    {
        //Given
        A a = new A(null, null);
        //When
        A aAfterInitialization = NullSafeInitializer.of(a)
                .initIfNull(A::b, A::b, B::new)
                .initIfNull(A::stringValue, A::stringValue, () -> "some value")
                .get();
        //Then
        assertNotNull(aAfterInitialization.b());
        assertEquals("some value", aAfterInitialization.stringValue());
    }

}

class A
{
    private B b = null;
    private String stringValue = null;

    A(B b)
    {
        this.b = b;
    }

    A(B b, String stringValue)
    {
        this.b = b;
        this.stringValue = stringValue;
    }

    B b()
    {
        return b;
    }

    void b(B b)
    {
        this.b = b;
    }

    String stringValue()
    {
        return stringValue;
    }

    void stringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }
}

class B
{
    private String stringValue;

    B() { }

    B(String stringValue)
    {
        this.stringValue = stringValue;
    }

    String stringValue()
    {
        return stringValue;
    }

    void stringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }
}
