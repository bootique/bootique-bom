package io.bootique.bom.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "bom", urlPatterns = "/*")
public class BomServlet extends HttpServlet {

	private static final long serialVersionUID = 3100731292623511396L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().println("bom_servlet_query_string: " + request.getQueryString());
	}
}
