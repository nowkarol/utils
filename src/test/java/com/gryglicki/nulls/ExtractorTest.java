package com.gryglicki.nulls;


import com.gryglicki.nulls.testClasses.Last;
import com.gryglicki.nulls.testClasses.Second;
import com.gryglicki.nulls.testClasses.Structure;
import com.gryglicki.nulls.testClasses.Third;
import com.gryglicki.nulls.testClasses.ThirdSubclass;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
  public void shouldExtractAsOptional() {
    //given
    Structure structure = new Structure(new Second(null));

    // when
    Optional<Last> executor = Extractor.create(structure, Structure::getSecond)
        .nextLevel(Second::getThird)
        .nextLevel(Third::getLast)
        .extract();
    Optional<Last> optional = Optional.ofNullable(structure)
        .map(Structure::getSecond)
        .map(Second::getThird)
        .map(Third::getLast);

    //then
    assertSame(executor, optional);
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
  public void shouldAllowLiskovSubclassSubstitutionLikeOptional() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last("Ignored Path")), new ThirdSubclass(new Last("Works fine"))));

    //when
    Optional<Last> extractor = Extractor.create(structure, Structure::getSecond)
        .nextLevel(Second::getThirdSubclass)
        .nextLevel(Third::getLast)
        .extract();

    Optional<Last> optional = Optional.ofNullable(structure)
        .map(Structure::getSecond)
        .map(Second::getThirdSubclass)
        .map(Third::getLast);


    //then
    assertSame(extractor.get(), optional.get());
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

  @Test
  public void shouldReturnEmptyForNullStartingObjectLikeOptional() {
    //Given - null starting structure
    //When
    Optional<String> extractor = Extractor.create(null, Object::toString)
        .extract();
    Optional<String> optional = Optional.ofNullable(null).map(Object::toString);
    //Then
    assertEquals(extractor, optional);
  }


  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForNullMapper() {
    //Given - null mapper
    //When
    Extractor.create(new Object(),null);
    //Then - Exception
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowExceptionForNullMapperAsOptional() {
    //Given - null mapper
    //When
    Optional.ofNullable(new Object()).map(null);
    //Then - Exception
  }


  @Test
  public void shouldReturnEmptyForNullTraversedReference() {
    //Given
    Structure startingObject = new Structure(null);
    //When
    Optional<Second> result = Extractor.create(startingObject, Structure::getSecond).extract();
    //Then
    assertFalse(result.isPresent());
  }

  @Test
  public void shouldReturnEmptyForNullTraversedReferenceLikeOptional() {
    //Given
    Structure startingObject = new Structure(null);
    //When
    Optional<Second> result = Optional.ofNullable(startingObject).map(Structure::getSecond);
    //Then
    assertFalse(result.isPresent());
  }

  @Test
  public void shouldReturnValueForDeepTraversedReference() {
    //given
    Structure structure = new Structure(new Second(null));

    // when
    Optional<Last> last = Extractor.create(structure, Structure::getSecond)
        .nextLevel(Second::getThird)
        .nextLevel(Third::getLast)
        .extract();


    //then
    assertSame(last, Optional.empty());
  }

  @Test
  public void shouldReturnValueForDeepTraversedReferenceLikeOptional() {
    //given
    Structure structure = new Structure(new Second(null));

    // when
    Optional<Last> last = Optional.ofNullable(structure)
        .map(Structure::getSecond)
        .map(Second::getThird)
        .map(Third::getLast);


    //then
    assertSame(last, Optional.empty());
  }

}