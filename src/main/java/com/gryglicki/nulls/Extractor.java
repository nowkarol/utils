package com.gryglicki.nulls;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

class Extractor<IN, CURRENT_LEVEL_OUT> {
  private IN startingStructure;

  private Function<IN, CURRENT_LEVEL_OUT> extractingFunction;
  private Extractor(Function<IN, CURRENT_LEVEL_OUT> extractingFunction) {
    this.extractingFunction = extractingFunction;
  }

  private Extractor(IN startingStructure, Function<IN, CURRENT_LEVEL_OUT> extractingFunction) {
    this.startingStructure = startingStructure;
    this.extractingFunction = extractingFunction;
  }

  public static <IN, CURRENT_LEVEL_OUT> Extractor<IN, CURRENT_LEVEL_OUT> create(Function<IN, CURRENT_LEVEL_OUT> firstLevelExtractingFunction){
    validateNotNull(firstLevelExtractingFunction);
    return new Extractor(firstLevelExtractingFunction);
  }

  static <IN, OUT> Extractor<IN, OUT> create(IN startingStructure, Function<IN, OUT> firstExtractor) {
    return new Extractor(startingStructure, firstExtractor);
  }

  public <NEXT_LEVEL> Extractor<IN, NEXT_LEVEL> nextLevel(Function<CURRENT_LEVEL_OUT, NEXT_LEVEL> nextExtractor){
    validateNotNull(nextExtractor);
    return new Extractor(startingStructure, nullSafeAndThen(nextExtractor));
  }

  Function<IN, CURRENT_LEVEL_OUT> getExtractingFunction() {
    return extractingFunction;
  }

  private static void validateNotNull(Function<?,?> function) {
    if (function == null){
      throw new IllegalArgumentException("Non empty extractor function have to be provided.");
    }
  }

  private <NEXT_LEVEL> Function<IN, NEXT_LEVEL> nullSafeAndThen(Function<CURRENT_LEVEL_OUT, NEXT_LEVEL> after) {
    Objects.requireNonNull(after);
    return (IN in) -> {
      CURRENT_LEVEL_OUT apply = extractingFunction.apply(in);
      if (apply == null){
        return null;
      }
      return after.apply(apply);
    };
  }

  Optional<CURRENT_LEVEL_OUT> extract() {
    return Optional.ofNullable(startingStructure)
        .map(extractingFunction);
  }
}
