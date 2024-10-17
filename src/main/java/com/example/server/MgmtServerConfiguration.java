package com.example.server;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.security.ConstraintAware;
import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.server.Handler;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;

@ManagementContextConfiguration
public class MgmtServerConfiguration {

    @Bean
    JettyServerCustomizer mgmtJettyServerCustomizer() {
        return (server) -> {
            ServletContextHandler servletContextHandler = findServletContextHandler(server);
            disableTraceMethodForHandler(servletContextHandler);
        };
    }

    private static ServletContextHandler findServletContextHandler(Handler.Container container) {
        for (Handler handler : container.getHandlers()) {
            if (handler instanceof ServletContextHandler servletContextHandler) {
                return servletContextHandler;
            }
            if (handler instanceof Handler.Container containerHandler) {
                ServletContextHandler servletContextHandler = findServletContextHandler(containerHandler);
                if (servletContextHandler != null) {
                    return servletContextHandler;
                }
            }
        }
        return null;
    }

    private void disableTraceMethodForHandler(final ServletContextHandler servletContextHandler) {
        ConstraintAware securityHandler = (ConstraintAware) servletContextHandler.getSecurityHandler();
        securityHandler.addConstraintMapping(newMethodConstraintMapping("TRACE"));
    }

    private ConstraintMapping newMethodConstraintMapping(String method) {
        Constraint constraint = Constraint.from("Disable " + method, Constraint.Authorization.ANY_USER);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setMethod(method);
        mapping.setPathSpec("/");

        return mapping;
    }

}
