guice-unused
============

Find unused Guice bindings in a large codebase.

Example
=======
```java
Injector injector = ...;
Set<Key<?>> rootKeys = ImmutableSet.<Key<?>>of(Key.get(MainApp.class));
Set<Binding<?>> unusedBindings = Bindings.findUnusedBindings(injector, rootKeys);
```
