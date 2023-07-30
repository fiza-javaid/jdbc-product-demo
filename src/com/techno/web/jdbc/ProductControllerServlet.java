package com.techno.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/ProductControllerServlet")
public class ProductControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductDbUtil productDbUtil;
	
	@Resource(name="jdbc/web_product_tracker")
	private DataSource dataSource;

	public ProductControllerServlet(){
	    try {
            init();
        }catch (ServletException r){
	        r.getMessage();
        }
    }
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create our student db util ... and pass in the conn pool / datasource
		try {
			productDbUtil = new ProductDbUtil(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			// route to the appropriate method
			switch (theCommand) {
			
			case "LIST":
				listProducts(request, response);
				break;
				
			case "ADD":
				addProduct(request, response);
				break;
				
			case "LOAD":
				loadProduct(request, response);
				break;
				
			case "UPDATE":
				updateProduct(request, response);
				break;
			
			case "DELETE":
				deleteProduct(request, response);
				break;
				
			default:
				listProducts(request, response);
			}
				
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
		
	}

	private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read student id from form data
		String theProductId = request.getParameter("productId");
		
		// delete student from database
		productDbUtil.deleteProduct(theProductId);
		
		// send them back to "list students" page
		listProducts(request, response);
	}

	private void updateProduct(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read student info from form data
		int id = Integer.parseInt(request.getParameter("productId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Product theProduct = new Product(id, firstName, lastName, email);
		
		// perform update on database
		productDbUtil.updateProduct(theProduct);
		
		// send them back to the "list students" page
		listProducts(request, response);
		
	}

	private void loadProduct(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read student id from form data
		String theProductId = request.getParameter("productId");
		
		// get student from database (db util)
		Product theProduct = productDbUtil.getProduct(theProductId);
		
		// place student in the request attribute
		request.setAttribute("THE_PRODUCT", theProduct);
		
		// send to jsp page: update-product-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-product-form.jsp");
		dispatcher.forward(request, response);		
	}

	private void addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");		
		
		// create a new student object
		Product theProduct = new Product(firstName, lastName, email);
		
		// add the student to the database
		productDbUtil.addProduct(theProduct);
				
		// send back to main page (the student list)
		listProducts(request, response);
	}

	private void listProducts(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// get students from db util
		List<Product> products = productDbUtil.getProducts();
		
		// add students to the request
		request.setAttribute("PRODUCT_LIST", products);
				
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-products.jsp");
		dispatcher.forward(request, response);
	}

}













