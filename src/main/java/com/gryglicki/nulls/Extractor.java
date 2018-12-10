package com.gryglicki.nulls;

import java.util.function.Function;

class Extractor<IN, CURRENT_LEVEL_OUT> {
  private final Function<IN, CURRENT_LEVEL_OUT> extractingFunction;

  private Extractor(Function<IN, CURRENT_LEVEL_OUT> extractingFunction) {
    this.extractingFunction = extractingFunction;
  }

  public static <IN, CURRENT_LEVEL_OUT> Extractor<IN, CURRENT_LEVEL_OUT> firstLevel(Function<IN, CURRENT_LEVEL_OUT> firstLevelExtractingFunction){
    validateNotNull(firstLevelExtractingFunction);
    return new Extractor(firstLevelExtractingFunction);
  }

  public <NEXT_LEVEL> Extractor<IN, NEXT_LEVEL> nextLevel(Function<CURRENT_LEVEL_OUT, NEXT_LEVEL> nextExtractor){
    validateNotNull(nextExtractor);
    return new Extractor (extractingFunction.andThen(nextExtractor));
  }

  Function<IN, CURRENT_LEVEL_OUT> getExtractingFunction() {
    return extractingFunction;
  }

  private static void validateNotNull(Function<?,?> function) {
    if (function == null){
      throw new IllegalArgumentException("Non empty extractors list have to be provided.");
    }
  }
}
