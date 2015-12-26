package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import conexao.JDBCMySQL;
import entidade.Filme;
import entidade.Genero;

public class GeneroDAO {

	private final String SQL_INSERE_GENERO = "INSERT INTO appfilme.genero(descricao,fk_filme) VALUES (?,?);";
	private final String SQL_EXCLUI_GENERO = "DELETE FROM appfilme.genero WHERE fk_filme=?;";
	private final String SQL_SELECIONA_GENERO = "SELECT genero.* FROM appfilme.genero WHERE genero.fk_filme = ?;";
	private final String SQL_SELECIONA_GENERO_TODOS = "SELECT distinct genero.descricao FROM appfilme.genero order by descricao;";

	private PreparedStatement pst = null;
	private JDBCMySQL cnx;
	private Connection conn;
	
	public GeneroDAO() {
		cnx = new JDBCMySQL();
		conn = cnx.conectar();
	}
	
	public boolean inserirGenero(Filme umFilme) {
		boolean ret = false;
		try {
			for (Genero umGenero : umFilme.getGenero()) {
				pst = conn.prepareStatement(SQL_INSERE_GENERO);
				pst.setString(1, umGenero.getDescricao());
				pst.setString(2, umFilme.getId());
				ret = pst.execute();
			}
			cnx.fecharCNX();
			ret = true;
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statment " + e.toString());
		}
		return ret;
	}

	public ArrayList<Genero> listarTodosGeneros() {
		ArrayList<Genero> listaDeGeneros = new ArrayList<Genero>();
		Genero umGenero;
		try {
			pst = conn.prepareStatement(SQL_SELECIONA_GENERO_TODOS);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				umGenero = new Genero();
				umGenero.setId(rs.getInt("idgenero"));
				umGenero.setDescricao(rs.getString("descricao"));
				listaDeGeneros.add(umGenero);
			}
			cnx.fecharCNX();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statement" + e.toString());
		}
		return listaDeGeneros;
	}

	public ArrayList<Genero> listarGeneros(int idFilme) {
		ArrayList<Genero> listaDeGeneros = new ArrayList<Genero>();
		Genero umGenero;
		try {
			pst = conn.prepareStatement(SQL_SELECIONA_GENERO);
			pst.setInt(1, idFilme);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				umGenero = new Genero();
				umGenero.setId(rs.getInt("idgenero"));
				umGenero.setDescricao(rs.getString("descricao"));
				listaDeGeneros.add(umGenero);
			}
			cnx.fecharCNX();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statement" + e.toString());
		}
		return listaDeGeneros;
	}

	public boolean excluiGenero(int idFilme) {
		boolean ret = false;
		try {
			pst = conn.prepareStatement(SQL_EXCLUI_GENERO);
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
