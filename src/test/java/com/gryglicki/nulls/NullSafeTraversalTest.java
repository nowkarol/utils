package com.gryglicki.nulls;

import org.junit.Test;

import java.util.Optional;

import static com.gryglicki.nulls.NullSafeTraversal.nullSafeTraverse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link NullSafeTraversal}
 *
 * @author Michał Gryglicki
 * @since 11/06/2017
 */
public class NullSafeTraversalTest {
    @Test
    public void shouldReturnEmptyForNullStartingObject()
    {
        //Given - null starting structure
        //When
        Optional<String> result = nullSafeTraverse(null, Object::toString);
        //Then
        assertFalse(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullMapper()
    {
        //Given - null mapper
        //When
        nullSafeTraverse(new Object(), null);
        //Then - Exception
    }

    @Test
    public void shouldReturnEmptyForNullTraversedReference()
    {
        //Given
        TestClass startingObject = new TestClass(null);
        //When
        Optional<TestClass> result = nullSafeTraverse(startingObject, TestClass::getReference);
        //Then
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnValueForTraversedReference()
    {
        //Given
        TestClass expectedResult = new TestClass(null);
        TestClass startingObject = new TestClass(expectedResult);
        //When
        Optional<TestClass> result = nullSafeTraverse(startingObject, TestClass::getReference);
        //Then
        assertSame(expectedResult, result.get());
    }

    @Test
    public void shouldReturnValueForDeepTraversedReference()
    {
        //Given
        TestClass expectedResult = new TestClass(null);
        TestClass referencedObject = new TestClass(expectedResult);
        TestClass startingObject = new TestClass(referencedObject);
        //When
        Optional<TestClass> result =
                nullSafeTraverse(startingObject, TestClass::getReference, TestClass::getReference);
        //Then
        assertSame(expectedResult, result.get());
    }
}

class TestClass
{
    public TestClass reference;

    public TestClass(TestClass reference)
    {
        this.reference = reference;
    }

    public TestClass getReference()
    {
        return reference;
    }
}
