package com.example.demo;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.security.ConstraintAware;
import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new CustomDispatcherServlet();
	}

	@Bean
	JettyServerCustomizer jettyServerCustomizer() {
		return (server) -> {
			ServletContextHandler servletContextHandler = findServletContextHandler(server);
			disableTraceMethodForHandler(servletContextHandler);
//			addDisableTraceFilter(servletContextHandler);
		};
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

	private static AtomicInteger counter = new AtomicInteger(0);

	private void addDisableTraceFilter(final ServletContextHandler servletContextHandler) {
		// filter names need to be unique (one for api servlet context, one for management servlet context)
		final String filterName = "disable-trace-" + counter.incrementAndGet();
		final FilterRegistration.Dynamic filterRegistration = servletContextHandler.getServletContext()
			.addFilter(filterName, new DisableTraceFilter());
		filterRegistration.setAsyncSupported(true);
		filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
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

	private static class DisableTraceFilter implements Filter {

		@Override
		public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			if ("trace".equalsIgnoreCase(httpRequest.getMethod())) {
				final HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			} else {
				chain.doFilter(request, response);
			}
		}

		@Override
		public void init(FilterConfig filterConfig) {

		}

		@Override
		public void destroy() {

		}
	}

}
