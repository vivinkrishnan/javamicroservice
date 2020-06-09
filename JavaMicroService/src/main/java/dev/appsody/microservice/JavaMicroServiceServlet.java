package dev.appsody.microservice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sayHello")
public class JavaMicroServiceServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JavaMicroServiceServlet(){
        super();
    }


    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Return hardcoded value
		response.getWriter().append("19336");
	}
    
}