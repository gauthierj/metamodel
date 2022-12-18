package com.github.gauthierj.metamodel.model;

public class _Model {

    private final _Path _rootPath;

    public _Model(_Path _rootPath) {
        this._rootPath = new _Path(_rootPath);
    }

    public String _property(String property) {
        return _rootPath.with(property).toString();
    }

    public _Path _rootPath() {
        return _rootPath;
    }

    public String _path() {
        return _rootPath.toString();
    }
}
