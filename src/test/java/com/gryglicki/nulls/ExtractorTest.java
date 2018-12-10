package com.gryglicki.nulls;


import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class ExtractorTest {

  @Test
  public void shouldExtractObject() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Works fine"))));

    // when
    Last last = Extractor.create(structure, Structure::getSecond)
        .nextLevel(Second::getThird)
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
    Last last = Extractor.create(structure, Structure::getSecond)
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
    Optional<String> result = Extractor.create(null, Object::toString)
                              .extract();
    //Then
    assertFalse(result.isPresent());
  }


  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMapper() {
    //Given - null mapper
    //When
    Extractor.create(new Object(),null);
    //Then - Exception
  }


  @Test
  public void shouldReturnEmptyForNullTraversedReference() {
    //Given
    TestClass startingObject = new TestClass(null);
    //When
    Optional<TestClass> result = Extractor.create(startingObject, TestClass::getReference).extract();
    //Then
    assertFalse(result.isPresent());
  }


  @Test
  public void shouldReturnValueForTraversedReference() {
    //Given
    TestClass expectedResult = new TestClass(null);
    TestClass startingObject = new TestClass(expectedResult);
    //When
    Optional<TestClass> result = Extractor.create(startingObject, TestClass::getReference).extract();
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
    Optional<TestClass> result = Extractor.create(startingObject, TestClass::getReference)
        .nextLevel(TestClass::getReference)
        .extract();
    //Then
    assertSame(expectedResult, result.get());
  }

}