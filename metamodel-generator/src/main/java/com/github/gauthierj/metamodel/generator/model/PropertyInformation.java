package com.github.gauthierj.metamodel.generator.model;

/**
 * Base interface for metadata describing a single property within a {@link TypeInformation}.
 *
 * <p>Properties are classified into three subtypes:
 * <ul>
 *   <li>{@link SimplePropertyInformation} — a scalar property (primitive, String, etc.)
 *       that maps directly to a string method in the generated metamodel.</li>
 *   <li>{@link StructuredPropertyInformation} — a nested object property whose type also
 *       has a metamodel, allowing path navigation into sub-properties.</li>
 *   <li>{@link UnsupportedPropertyInformation} — a property whose type cannot be mapped
 *       (e.g. a collection or map); it is excluded from generation with a warning.</li>
 * </ul>
 */
public interface PropertyInformation {

    /**
     * Returns the physical property name as it appears in the source or as remapped by an
     * annotation (e.g. {@code @Property(name = "...")}, {@code @Field}).
     *
     * <p>This is the value that will be returned at runtime by the generated metamodel method.
     *
     * @return the physical property name
     */
    String name();

    /**
     * Returns the logical property name as derived from the field or getter method name in
     * the Java source.
     *
     * <p>This is used as the name of the generated method in the metamodel class.
     * It may differ from {@link #name()} when a name remapping annotation is present.
     *
     * @return the logical property name
     */
    String logicalName();
}
