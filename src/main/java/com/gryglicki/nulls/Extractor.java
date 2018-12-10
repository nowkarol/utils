package com.gryglicki.nulls;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

class Extractor<IN, CURRENT_LEVEL_OUT> {
  private final IN startingStructure;
  private final Function<IN, CURRENT_LEVEL_OUT> extractingFunction;


  private Extractor(IN startingStructure, Function<IN, CURRENT_LEVEL_OUT> extractingFunction) {
    validateNotNull(extractingFunction, "Non null extracting function have to be provided.");
    this.startingStructure = startingStructure;
    this.extractingFunction = extractingFunction;
  }

  static <IN, OUT> Extractor<IN, OUT> create(IN startingStructure, Function<IN, OUT> firstExtractor) {
    return new Extractor(startingStructure, firstExtractor);
  }

  public <NEXT_LEVEL> Extractor<IN, NEXT_LEVEL> nextLevel(Function<CURRENT_LEVEL_OUT, NEXT_LEVEL> nextExtractor){
    return new Extractor(startingStructure, nullSafeAndThen(nextExtractor));
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

  private static void validateNotNull(Object object, String message) {
    if (object == null){
      throw new IllegalArgumentException(message);
    }
  }
}
