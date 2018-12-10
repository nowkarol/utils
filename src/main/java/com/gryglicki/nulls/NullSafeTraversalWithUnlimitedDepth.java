package com.gryglicki.nulls;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.stream;

/**
 * Allows to extract data from deep structures without worrying of null intermediates.
 * It's a base class used to build type safe null traversals for required depth.
 *
 * @author Michał Gryglicki
 * @since 11/06/2017
 */
class NullSafeTraversalWithUnlimitedDepth {

    /**
     * This generic not type safe method allows to create subsequent null safe traversals that are type safe.
     * @param startingStructure traverse starting structure
     * @param extractors list of extractors applies in sequence to traverse the structure
     * @param <IN> starting structure type
     * @param <OUT> result type
     * @return result of applying given sequence of extractors in order to the starting structure
     */
    @SuppressWarnings("unchecked")
    public static <IN, OUT> Optional<OUT> nullSafeTraverseWithUnlimitedDepth(IN startingStructure, Function... extractors) {
        if (extractors == null || extractors.length == 0 || stream(extractors).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Non empty extractors list have to be provided.");
        }

        Optional intermediate = Optional.ofNullable(startingStructure);
        for (Function extractor : extractors) {
            intermediate = intermediate.map(extractor::apply);
        }
        return (Optional<OUT>) intermediate;
    }

    public static <IN, OUT> Optional<OUT> nullSafeTraverseWithUnlimitedDepth(IN startingStructure, Extractor<IN, OUT> extractor) {
        if (extractor == null){
            throw new IllegalArgumentException("Non empty extractor have to be provided.");
        }
        return Optional.ofNullable(startingStructure)
            .map(extractor.getExtractingFunction());
    }
}
