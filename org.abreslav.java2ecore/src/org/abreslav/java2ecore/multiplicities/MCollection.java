package org.abreslav.java2ecore.multiplicities;

/**
 * Denotes a collection (non-ordered and non-unique multiple feature) with
 * specified bounds.
 *
 * @param <E> element type.
 * @param <L> lower bound. Type's name must be of the form _NUM, where NUM stands for 
 * a non-negative integer in decimal notation. Example: <code>_152</code>. 
 * The value must not be greater than the upper bound.
 * @param <U> upper bound. Type's name may be of the same form as for <code>L</code> 
 * or one can specify {@link Infinity} or {@link Unspecified} as an upper bound. 
 */
public interface MCollection<E, L extends ILowerBound, U extends IUpperBound> {
}
