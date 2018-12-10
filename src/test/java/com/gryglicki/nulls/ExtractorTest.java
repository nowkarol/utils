package com.gryglicki.nulls;


import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static com.gryglicki.nulls.NullSafeTraversal.nullSafeFluentTraverse;
import static com.gryglicki.nulls.NullSafeTraversal.nullSafeTraverse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class ExtractorTest {
  @Test
  public void shouldExtractObject() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Works fine"))));

    //when
    Extractor<Structure, Last> extractor = Extractor.create(Structure::getSecond)
        .nextLevel(Second::getThird)
        .nextLevel(Third::getLast);
    Last last = nullSafeTraverse(structure, extractor).get();

    //then
    assertSame(last.toString(), "Works fine");
  }

  @Test
  public void shouldFluentlyExtractObject() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Works fine"))));

    // when
    Last last = nullSafeFluentTraverse(structure, Structure::getSecond).nextLevel(Second::getThird)
        .nextLevel(Third::getLast)
        .extract()
        .get();

    //then
    assertSame(last.toString(), "Works fine");
  }

  @Test
  public void shouldAllowLiskovSubclassSubstitution() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Ignored Path")), new ThirdSubclass(new Last("Works fine"))));

    //when
    Extractor<Structure, Last> extractor = Extractor.create(Structure::getSecond)
        .nextLevel(Second::getThirdSubclass)
        //should not work without Function<CURRENT_LEVEL_OUT, ? extends NEXT_LEVEL> in nextLevel signature why it does?
        .nextLevel(Third::getLast);
    Last last = nullSafeTraverse(structure, extractor).get();

    //then
    assertSame(last.toString(), "Works fine");
  }

  @Test
  public void shouldFluentlyAllowLiskovSubclassSubstitution() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Ignored Path")), new ThirdSubclass(new Last("Works fine"))));

    //when
    Last last = nullSafeFluentTraverse(structure, Structure::getSecond)
        .nextLevel(Second::getThirdSubclass)
        .nextLevel(Third::getLast)
        .extract()
        .get();

    //then
    assertSame(last.toString(), "Works fine");
  }

  @Test
  public void shouldReturnEmptyForNullStartingObject() {
    //Given - null starting structure
    //When
    Optional<String> result = nullSafeTraverse(null, Extractor.create(Object::toString));
    //Then
    assertFalse(result.isPresent());
  }

  @Test
  public void shouldFluentlyReturnEmptyForNullStartingObject() {
    //Given - null starting structure
    //When
    Optional<String> result = nullSafeFluentTraverse(null, Object::toString)
                              .extract();
    //Then
    assertFalse(result.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMapper() {
    //Given - null mapper
    //When
    nullSafeTraverse(new Object(), (Extractor) null);
    //Then - Exception
  }

  @Ignore("refactor")
  @Test(expected = IllegalArgumentException.class)
  public void shouldFluentlyThrowExceptionForNullMapper() {
    //Given - null mapper
    //When
    nullSafeFluentTraverse(new Object(),null);
    //Then - Exception
  }

  @Test
  public void shouldReturnEmptyForNullTraversedReference() {
    //Given
    TestClass startingObject = new TestClass(null);
    //When
    Optional<TestClass> result = nullSafeTraverse(startingObject, Extractor.create(TestClass::getReference));
    //Then
    assertFalse(result.isPresent());
  }

  @Test
  public void shouldFluentlyReturnEmptyForNullTraversedReference() {
    //Given
    TestClass startingObject = new TestClass(null);
    //When
    Optional<TestClass> result = nullSafeFluentTraverse(startingObject, TestClass::getReference).extract();
    //Then
    assertFalse(result.isPresent());
  }

  @Test
  public void shouldReturnValueForTraversedReference() {
    //Given
    TestClass expectedResult = new TestClass(null);
    TestClass startingObject = new TestClass(expectedResult);
    //When
    Optional<TestClass> result = nullSafeTraverse(startingObject, Extractor.create(TestClass::getReference));
    //Then
    assertSame(expectedResult, result.get());
  }

  @Test
  public void shouldFluentlyReturnValueForTraversedReference() {
    //Given
    TestClass expectedResult = new TestClass(null);
    TestClass startingObject = new TestClass(expectedResult);
    //When
    Optional<TestClass> result = nullSafeFluentTraverse(startingObject, TestClass::getReference).extract();
    //Then
    assertSame(expectedResult, result.get());
  }

  @Test
  public void shouldReturnValueForDeepTraversedReference() {
    //Given
    TestClass expectedResult = new TestClass(null);
    TestClass referencedObject = new TestClass(expectedResult);
    TestClass startingObject = new TestClass(referencedObject);
    //When
    Optional<TestClass> result = nullSafeTraverse(startingObject, Extractor.create(TestClass::getReference)
        .nextLevel(TestClass::getReference));
    //Then
    assertSame(expectedResult, result.get());
  }

  @Test
  public void shouldFluentlyReturnValueForDeepTraversedReference() {
    //Given
    TestClass expectedResult = new TestClass(null);
    TestClass referencedObject = new TestClass(expectedResult);
    TestClass startingObject = new TestClass(referencedObject);
    //When
    Optional<TestClass> result = nullSafeFluentTraverse(startingObject, TestClass::getReference)
        .nextLevel(TestClass::getReference)
        .extract();
    //Then
    assertSame(expectedResult, result.get());
  }

}