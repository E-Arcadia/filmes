package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import conexao.JDBCMySQL;
import entidade.Filme;

public class FilmeDAO {

	private final String SQL_INSERE_FILME = "INSERT INTO appfilme.filme (titulo,imagemURL,ano,nota) VALUES (?,?,?,?);";
	private final String SQL_EXCLUI_FILME = "DELETE FROM appfilme.filme WHERE idfilme=?;";
	private final String SQL_SELECIONA_FILME_ANTIGOS = "SELECT filme.* FROM filme WHERE filme.idfilme < ?  order by filme.idfilme desc LIMIT ?;";
	private final String SQL_SELECIONA_FILME_NOVOS = "SELECT filme.* FROM filme WHERE filme.idfilme > ?  order by filme.idfilme desc LIMIT ?;";
	private final String SQL_SELECIONA_FILME_ULTIMOS = "SELECT filme.* FROM filme WHERE idfilme <= (SELECT max(idfilme) FROM appfilme.filme)  order by filme.idfilme desc LIMIT ?";
	private final String SQL_SELECIONA_FILME_TODOS = "SELECT filme.* FROM filme;";

	private PreparedStatement pst = null;
	private JDBCMySQL cnx;
	private Connection conn;

	public FilmeDAO() {
		cnx = new JDBCMySQL();
		conn = cnx.conectar();
	}

	public boolean inserirFilme(Filme umFilme) {
		boolean ret = false;
		try {
			pst = conn.prepareStatement(SQL_INSERE_FILME, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, umFilme.getTitulo());
			pst.setString(2, umFilme.getImagemUrl());
			pst.setInt(3, umFilme.getAno());
			pst.setDouble(4, umFilme.getNota());
			ret = pst.execute();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int idFilme = rs.getInt(1);
				umFilme.setId(idFilme);
			}
			GeneroDAO generoDAO = new GeneroDAO();
			generoDAO.inserirGenero(umFilme);

			pst.close();
			cnx.fecharCNX();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statment " + e.toString());
		}
		return ret;
	}

	public ArrayList<Filme> listarTodosFilmes() {
		ArrayList<Filme> listaDeFilmes = new ArrayList<Filme>();
		Filme umFilme;
		try {
			pst = conn.prepareStatement(SQL_SELECIONA_FILME_TODOS);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				umFilme = new Filme();
				umFilme.setId(rs.getInt("idfilme"));
				umFilme.setTitulo(rs.getString("titulo"));
				umFilme.setImagemUrl(rs.getString("imagemURL"));
				umFilme.setAno(rs.getInt("ano"));
				umFilme.setNota(rs.getDouble("nota"));
				listaDeFilmes.add(umFilme);
			}
			rs.close();
			pst.close();
			cnx.fecharCNX();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statement" + e.toString());
		}
		return listaDeFilmes;
	}

	public ArrayList<Filme> listarFilmes(int idFilme, int Qtde, String direcao) {
		ArrayList<Filme> listaDeFilmes = new ArrayList<Filme>();
		Filme umFilme;
		try {
			if (direcao.equals("DIRECAO_ULTIMOS")) {
				pst = conn.prepareStatement(SQL_SELECIONA_FILME_ULTIMOS);
				pst.setInt(1, Qtde);
			} else {
				if (direcao.equals("DIRECAO_NOVOS"))
					pst = conn.prepareStatement(SQL_SELECIONA_FILME_NOVOS);
				else if (direcao.equals("DIRECAO_ANTIGOS"))
					pst = conn.prepareStatement(SQL_SELECIONA_FILME_ANTIGOS);

				pst.setInt(1, idFilme);
				pst.setInt(2, Qtde);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				umFilme = new Filme();
				umFilme.setId(rs.getInt("idfilme"));
				umFilme.setTitulo(rs.getString("titulo"));
				umFilme.setImagemUrl(rs.getString("imagemURL"));
				umFilme.setAno(rs.getInt("ano"));
				umFilme.setNota(rs.getDouble("nota"));
				listaDeFilmes.add(umFilme);

				GeneroDAO generoDAO = new GeneroDAO();
				umFilme.setGenero(generoDAO.listarGeneros(umFilme.getId_INT()));
			}
			rs.close();
			pst.close();
			cnx.fecharCNX();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statement" + e.toString());
		}
		return listaDeFilmes;
	}

	public boolean excluiFilme(Filme umFilme) {
		return this.excluiFilme(umFilme.getId_INT());
	}
	
	public boolean excluiFilme(int idFilme) {
		boolean ret = false;
		new GeneroDAO().excluiGenero(idFilme);
		try {
			pst = conn.prepareStatement(SQL_EXCLUI_FILME);
			pst.setInt(1, idFilme);
			ret = pst.execute();
			pst.close();
			cnx.fecharCNX();
			ret = true;
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statment " + e.toString());
		}
		return ret;
	}
}
