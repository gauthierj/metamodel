package com.github.gauthierj.metamodel.model;

/**
 * Base class for all generated metamodel classes.
 *
 * <p>Each {@code @Model}-annotated class {@code Foo} gets a generated {@code _Foo} subclass
 * that inherits from this class. Instances carry a root path that accumulates as the metamodel
 * is navigated from parent to nested types.
 *
 * <p>Typical usage is through the generated static factory {@code _Foo.model()}, which returns
 * a root instance with an empty path:
 * <pre>{@code
 * // Simple property -> returns the field name
 * String field = _Person.model().firstName();  // "firstName"
 *
 * // Nested property -> returns the dot-separated path
 * String nested = _Person.model().address().street();  // "address.street"
 *
 * // Map property -> navigate with a dynamic key
 * String mapped = _Person.model()._property("tags");   // "tags"
 * }</pre>
 */
public class _Model {

    private final _Path _rootPath;

    /**
     * Constructs a metamodel instance rooted at the given path.
     *
     * @param _rootPath the root path for this metamodel instance
     */
    public _Model(_Path _rootPath) {
        this._rootPath = new _Path(_rootPath);
    }

    /**
     * Returns the fully-qualified property name by appending the given property segment
     * to the current root path.
     *
     * <p>Useful for navigating map-type properties where the key is only known at runtime:
     * <pre>{@code
     * _Person.model()._property("dynamicField")  // "dynamicField"
     * }</pre>
     *
     * @param property the property name segment to append
     * @return the dot-separated path string including the given property
     */
    public String _property(String property) {
        return _rootPath.with(property).toString();
    }

    /**
     * Returns the path to the given property as a {@link _Path} object.
     *
     * @param property the property name segment to append
     * @return the path to the given property
     */
    public _Path _propertyPath(String property) {
        return _rootPath.with(property);
    }

    /**
     * Returns the root path of this metamodel instance.
     *
     * @return the root path
     */
    public _Path _rootPath() {
        return _rootPath;
    }

    /**
     * Returns the current root path as a dot-separated string.
     *
     * <p>For a root instance created via {@code _Foo.model()}, this returns an empty string.
     * For a nested instance, this returns the path accumulated so far
     * (e.g., {@code "address"} when navigating into an address property).
     *
     * @return the dot-separated path string, or an empty string for a root instance
     */
    public String _path() {
        return _rootPath.toString();
    }
}
