package com.github.bonifaido.inject.unused;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Stage;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BindingsTest {
    @Test
    public void testFindUnusedBindings() throws Exception {
        Injector injector = Guice.createInjector(Stage.TOOL, new TestModule());
        Set<Key<?>> rootKeys = ImmutableSet.<Key<?>>of(Key.get(A.class));

        Set<Binding<?>> unusedBindings = Bindings.findUnusedBindings(injector, rootKeys);

        assertTrue(unusedBindings.size() == 1);
        Binding<?> unusedBinding = unusedBindings.iterator().next();
        assertEquals(Key.get(UnusedClass.class), unusedBinding.getKey());
    }
}

class UnusedClass {
}

class A {
    @Inject
    A(int i) {
    }
}

class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(int.class).toInstance(2);
    }

    @Provides
    UnusedClass provideUnusedClass() {
        return new UnusedClass();
    }
}
