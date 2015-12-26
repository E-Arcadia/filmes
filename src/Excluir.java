

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistencia.FilmeDAO;

/**
 * Servlet implementation class Excluir
 */
@WebServlet("/excluirSRV")
public class Excluir extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Excluir() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idFilme = 0;
		String id = request.getParameter("id");
		String resposta = null;

		try {
			idFilme = Integer.parseInt(id);
		} catch (Exception e) {
			resposta = "Valores inválidos. Operação não realizada!!!";
		}

		FilmeDAO daoFilme = new FilmeDAO();
		if(daoFilme.excluiFilme(idFilme)){
			resposta = "Operação realizada!!!";
		} else {
			resposta = "Falha no servidor. Operação não realizada!!!";
		}
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(resposta);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
