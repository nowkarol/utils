package com.gryglicki.nulls;


import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ExtractorTest {

  @Test
  public void shouldExtractObject() {
    //given
    Structure structure = new Structure(new Second(new Third(new Last())));

    //when
    Extractor<Structure, Last> extractor = Extractor.firstLevel(Structure::getSecond)
        .nextLevel(Second::getThird)
        .nextLevel(Third::getLast);
    Last last = NullSafeTraversalWithUnlimitedDepth.nullSafeTraverseWithUnlimitedDepth(structure, extractor).get();

    //then
    assertSame(last.toString(), "Works fine");

  }

}