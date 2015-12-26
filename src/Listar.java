
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entidade.Filme;
import persistencia.FilmeDAO;

/**
 * Servlet implementation class Listar
 */
@WebServlet("/listarSRV")
public class Listar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Listar() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idFilme = 0;
		int Qtde = 0;
		String resposta = null;
		boolean continua = false;

		String id = request.getParameter("id");
		String qtde = request.getParameter("qtde");
		String origem = request.getParameter("origem");
		String direcao = request.getParameter("direcao");

		try {
			idFilme = Integer.parseInt(id);
			Qtde = Integer.parseInt(qtde);
			continua = true;
		} catch (Exception e) {
			resposta = "Valores inválidos!!!";
			continua = false;
		}

		FilmeDAO daoFilme = new FilmeDAO();
		ArrayList<Filme> lista = daoFilme.listarFilmes(idFilme, Qtde, direcao);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		switch (origem) {
		case "emHTML":
			if (continua) {
				request.setAttribute("lista", lista);
				RequestDispatcher r = request.getRequestDispatcher("listadefilmes.jsp");
				r.forward(request, response);

				// response.sendRedirect("funcionou.html");
			}
			out.println(resposta);
			break;
		case "emJSON":
			Gson gson = new GsonBuilder().create();
			out.print(gson.toJson(lista));
			break;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
