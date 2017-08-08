package com.gryglicki.nulls;

import java.util.Optional;
import java.util.function.Function;

import static com.gryglicki.nulls.NullSafeTraversalWithUnlimitedDepth.nullSafeTraverseWithUnlimitedDepth;

/**
 * Allows to extract data from deep structures without worrying of null intermediates.
 * It's type safe for the depth of 2 object but can be easily extended to whatever depth you need.
 *
 * @author Michał Gryglicki
 * @since 11/06/2017
 */
public class NullSafeTraversal {

    /**
     * Performs null safe and type safe traverse of structure properties
     * @param startingStructure starting structure
     * @param extractor1 property extractor (eg. method reference)
     * @return result of applying all extractors in sequence to the starting structure
     */
    public static <IN, OUT> Optional<OUT> nullSafeTraverse(IN startingStructure, Function<IN, OUT> extractor1) {
        return nullSafeTraverseWithUnlimitedDepth(startingStructure, extractor1);
    }

    /**
     * Performs null safe and type safe traverse of structure properties
     * @param startingStructure starting structure
     * @param extractor1 property extractor (eg. method reference)
     * @param extractor2 second property extractor (eg. method reference)
     * @return result of applying all extractors in sequence to the starting structure
     */
    public static <IN, INTERMEDIATE1, OUT> Optional<OUT> nullSafeTraverse(IN startingStructure,
                              Function<IN, INTERMEDIATE1> extractor1, Function<INTERMEDIATE1, OUT> extractor2) {
        return nullSafeTraverseWithUnlimitedDepth(startingStructure, extractor1, extractor2);
    }


}
