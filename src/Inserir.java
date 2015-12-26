
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import entidade.Filme;
import persistencia.FilmeDAO;

/**
 * Servlet implementation class UploadArquivo
 */
@WebServlet("/inserirSRV")
public class Inserir extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Filme umFilme;
	private boolean salvar = false;

	/**
	 * Default constructor.
	 */
	public Inserir() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String resposta = null;
		umFilme = new Filme();

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("Não tem conteúdo Multipart");
			resposta = "Não tem conteúdo Multipart!!!";
		} else {
			// TUDO Uma outra maneira(nativa):
			// http://java-x.blogspot.com.br/2010/08/file-upload-with-servlet-30.html
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<?> items = null;

			try {
				items = upload.parseRequest(new ServletRequestContext(request));
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			Iterator<?> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String nomeCampo = item.getFieldName();
					String valor = item.getString();
					int num;
					switch (nomeCampo) {
					// TODO Fazer o tratamento dos valores.
					case "titulo":
						umFilme.setTitulo(valor);
						break;
					case "ano":
						num = 0;
						try {
							num = Integer.parseInt(valor);
						} catch (NumberFormatException e) {
						}
						umFilme.setAno(num);
						break;
					case "nota":
						num = 0;
						try {
							num = Integer.parseInt(valor);
						} catch (NumberFormatException e) {
						}
						umFilme.setNota(num);
						break;
					case "genero":
						umFilme.setGenero(valor);
						break;
					default:
						break;
					}
				} else {
					String nomeArquivo = item.getName();
					if (nomeArquivo.isEmpty()) {
						System.out.println("Arquivo não Carregado");
						resposta = "Arquivo não Carregado!!!";
						salvar = false;
					} else {
						salvar = true;
						Random generator = new Random();
						int r = Math.abs(generator.nextInt());
						int IndexOf = nomeArquivo.indexOf(".");
						String domainName;
						if (IndexOf >= 0)
							domainName = nomeArquivo.substring(IndexOf);
						else
							domainName = ".PNG";
						String nomeFinalArquivo = r + domainName;
						String caminho = getServletContext().getRealPath("imgrecebidas");

						File nomeEcaminhoArquivo = new File(caminho, nomeFinalArquivo);
						try {
							item.write(nomeEcaminhoArquivo);
						} catch (Exception e) {
							e.printStackTrace();
						}
						umFilme.setImagemUrl("imgrecebidas/" + nomeFinalArquivo);
						resposta = "Caminho do arquivo: " + caminho + " <br>Nome do arquivo: " + umFilme.getImagemUrl();
					}
				}
			}

			if (salvar) {
				FilmeDAO daoFilme = new FilmeDAO();
				daoFilme.inserirFilme(umFilme);
			}
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(resposta);

	}
}
