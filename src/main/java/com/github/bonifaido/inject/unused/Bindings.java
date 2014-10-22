package com.github.bonifaido.inject.unused;

import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.grapher.TransitiveDependencyVisitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class Bindings {

    private static final Key<Logger> loggerKey = Key.get(Logger.class);

    public static Set<Binding<?>> findUnusedBindings(Injector injector, Set<Key<?>> rootKeys) {
        Set<Binding<?>> allBindings = getAllBindings(injector);
        Set<Binding<?>> usedBindings = getUsedBindings(injector, rootKeys);

        return Sets.difference(allBindings, usedBindings);
    }

    static Set<Binding<?>> getAllBindings(Injector injector) {
        Set<Binding<?>> bindings = new HashSet<Binding<?>>(injector.getBindings().values());
        Iterator<Binding<?>> iterator = bindings.iterator();
        while (iterator.hasNext()) {
            Key<?> key = iterator.next().getKey();
            if (key.getTypeLiteral().getRawType().getPackage() == Guice.class.getPackage()
                    || loggerKey.equals(key)) {
                iterator.remove();
            }
        }
        return bindings;
    }

    static Set<Binding<?>> getUsedBindings(Injector injector, Set<Key<?>> root) {
        Set<Key<?>> keys = Sets.newHashSet(root);
        Set<Key<?>> visitedKeys = Sets.newHashSet();
        Set<Binding<?>> bindings = Sets.newHashSet();
        TransitiveDependencyVisitor keyVisitor = new TransitiveDependencyVisitor();

        while (!keys.isEmpty()) {
            Iterator<Key<?>> iterator = keys.iterator();
            Key<?> key = iterator.next();
            iterator.remove();

            if (!visitedKeys.contains(key)) {
                Binding<?> binding = injector.getBinding(key);
                bindings.add(binding);
                visitedKeys.add(key);
                keys.addAll(binding.acceptTargetVisitor(keyVisitor));
            }
        }
        return bindings;
    }
}
