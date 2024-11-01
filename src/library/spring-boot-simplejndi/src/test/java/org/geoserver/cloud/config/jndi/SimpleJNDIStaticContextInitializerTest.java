/*
 * (c) 2022 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.config.jndi;

import static org.assertj.core.api.Assertions.assertThat;

import org.geoserver.cloud.autoconfigure.jndi.SimpleJNDIStaticContextInitializer;
import org.geoserver.cloud.jndi.SimpleNamingContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Hashtable;

import jakarta.naming.Context;
import jakarta.naming.InitialContext;
import jakarta.naming.spi.NamingManager;

/**
 * @since 1.0
 */
class SimpleJNDIStaticContextInitializerTest {

    private ApplicationContextRunner runner =
            new ApplicationContextRunner()
                    .withInitializer(new SimpleJNDIStaticContextInitializer());

    @Test
    void test() {
        runner.run(
                context -> {
                    InitialContext initialContext = new InitialContext();
                    Context ctx = NamingManager.getInitialContext(new Hashtable<>());
                    assertThat(ctx).isInstanceOf(SimpleNamingContext.class);

                    Object value = new Object();
                    initialContext.bind("java:comp/env/test", value);

                    initialContext.close();

                    assertThat(ctx.lookup("java:comp/env/test")).isSameAs(value);

                    initialContext = new InitialContext();
                    ctx = NamingManager.getInitialContext(new Hashtable<>());
                    assertThat(ctx).isInstanceOf(SimpleNamingContext.class);

                    assertThat(initialContext.lookup("java:comp/env/test")).isSameAs(value);
                });
    }
}