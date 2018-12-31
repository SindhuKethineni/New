package com.fashionapp.Entity;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public void init() throws ServletException{
		message="Hello";
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws
															ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println(message);
		
	}
	public void destroy() {
		
	}

}
