package com.nhl.bootique.bom.jetty;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "bom", urlPatterns = "/*")
public class BomFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		response.getWriter().println("bom_filter_before");
		chain.doFilter(request, response);
		response.getWriter().println("bom_filter_after");
	}
}
