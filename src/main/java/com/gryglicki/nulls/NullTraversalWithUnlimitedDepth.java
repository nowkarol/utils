package com.gryglicki.nulls;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.stream;

/**
 * Allows to extract data from deep structures without worrying of null intermediates.
 * It's type safe for the depth of 2 object but can be easily extended to whatever depth you need.
 *
 * @author Michał Gryglicki
 * @since 11/06/2017
 */
class NullTraversalWithUnlimitedDepth {

    /**
     * This generic not type safe method allows to create subsequent null safe traversals that are type safe.
     * @param startingStructure traverse starting structure
     * @param extractors variable list of extractors applies in sequence to traverse the structure
     * @param <IN> starting structure type
     * @param <OUT> result type
     * @return result of applying given sequence of extractors in order to the starting structure
     */
    @SuppressWarnings("unchecked")
    public static <IN, OUT> OUT nullSafeTraverseWithUnlimitedDepth(IN startingStructure, Function... extractors) {
        if (extractors == null || extractors.length == 0 || stream(extractors).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Non null extractors have to be provided.");
        }

        return (OUT) stream(extractors).reduce(
                Optional.ofNullable(startingStructure),
                Optional::map,
                (in, in2) -> { throw new IllegalStateException("Parallel streams are not supported"); });
    }
}
