<%@page import="entidade.Filme"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<table>
		<tr>
			<td>ID</td>
			<td>Titulo</td>
			<td>Ano</td>
			<td>Nota</td>
			<td>Genero</td>
			<td>Imagem</td>
			<td>Tratar</td>
		</tr>
		<%
			ArrayList<Filme> lista = (ArrayList) request.getAttribute("lista");
			for (Filme umFilme : lista) {
		%><tr>
			<td><%
				out.print(umFilme.getId());
			%>
			</td>
			<td>
				<%
					out.print(umFilme.getTitulo());
				%>
			</td>
			<td>
				<%
					out.print(umFilme.getAnoSTR());
				%>
			</td>
			<td>
				<%
					out.print(umFilme.getNotaSTR());
				%>
			</td>
			<td>
				<%
					out.print(umFilme.getGeneroSTR());
				%>
			</td>
			<td><img src="<%=umFilme.getImagemUrl()%>" alt="Imagem Postada"
				style="width: 100px; height: 10,ç8px;">
			</td>
			<td><a href="/filmes/excluirSRV?id=<%=umFilme.getId()%>"><img src="imagens/excluir.png" alt="Excluir"
					style="width: 30px; height: 31px;"></a>
			</td>
		</tr>
		<%
			}
		%>

	</table>


</body>
</html>