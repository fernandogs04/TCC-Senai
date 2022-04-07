package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.Emprestimo;

public class DAO_Emprestimo {
    public static Connection conexao_BD;
    
    //ResultSet vai buscar os dados no banco por meio de uma ObservableList<>
    public static ResultSet r;
    
    public static PreparedStatement st;
    
    public DAO_Emprestimo () {
        //Vai acionar a Fabrica de Conexões para se conectar ao banco de dados
        conexao_BD = ConnectionFactory.getConnection();
    }
    
    //CREATE
    public static void Insere_Emprestimo(Emprestimo emprestimo) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "INSERT INTO emprestimos" + "(id_material, id_usuario, data_retiro, dias, devolvido)"
                + "VALUES (?,?,?,?,?)";
        //? significa que não sabemos os valores que  o usuario
        //digitou na tela
        Date data_retiro = Date.valueOf(emprestimo.getData_retiro());
        try {
            //O PreparedStatement ele prepara o objeto aviao
            //para executar a String sql e assim enviar para o banco
            //de dados
            PreparedStatement stat = conexao_BD.prepareStatement(sql);
            //stat é um objeto da classe PreparedStatement que vai
            //enviar os dados recolhidos do método Insere_Aviao
            //para o banco de dados
            stat.setInt(1, emprestimo.getId_material());
            stat.setInt(2, emprestimo.getId_usuario());
            stat.setDate(3, data_retiro);//TODO codificar em b64
            stat.setInt(4, emprestimo.getDias());
            stat.setBoolean(5, emprestimo.isDevolvido().equals("Sim"));
            //A ordem da numeração, tem que ser na SEQUÊNCIA dos
            //dados definidos na tabela do banco de dados.
            //E sempre de maneira SEQUENCIAL
            
            stat.execute();
            stat.close();
            //conexao_BD.close();
        }
        catch (SQLException ex) {
            System.out.println("Insere emprestimo - " + ex.getMessage());
        }
    }
    
    //CREATE
    public static void Marcar_Devolvido(Emprestimo emprestimo) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "UPDATE emprestimos SET devolvido = true WHERE id = ?";
        //? significa que não sabemos os valores que  o usuario
        //digitou na tela
        try {
            //O PreparedStatement ele prepara o objeto aviao
            //para executar a String sql e assim enviar para o banco
            //de dados
            PreparedStatement stat = conexao_BD.prepareStatement(sql);
            //stat é um objeto da classe PreparedStatement que vai
            //enviar os dados recolhidos do método Insere_Aviao
            //para o banco de dados
            stat.setInt(1, emprestimo.getId_emprestimo());
            //A ordem da numeração, tem que ser na SEQUÊNCIA dos
            //dados definidos na tabela do banco de dados.
            //E sempre de maneira SEQUENCIAL
            
            stat.execute();
            stat.close();
            //conexao_BD.close();
        }
        catch (SQLException ex) {
            System.out.println("Insere emprestimo - " + ex.getMessage());
        }
    }
    
    //RETRIEVE
    public ObservableList<Emprestimo> get_Emprestimos() {
        try {
            ObservableList<Emprestimo> emprestimos = FXCollections.observableArrayList();
            //ObservableList vai carregar os dados da tabela do banco de dados
            //chamada avioes por meio do observableArrayList()
            
            String sql = "SELECT emprestimos.id, emprestimos.id_usuario, emprestimos.id_material, emprestimos.data_retiro, emprestimos.dias, emprestimos.devolvido, "
                       + "materiais.nome AS 'nomeMaterial', usuarios.nome AS 'nomeUsuario' "
                       + "FROM emprestimos INNER JOIN materiais ON emprestimos.id_material=materiais.id INNER JOIN usuarios ON emprestimos.id_usuario=usuarios.id";

            PreparedStatement stmt = this.conexao_BD.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();

            r = rs;
            st = stmt;
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                
                emprestimo.setId_emprestimo(rs.getInt("id"));
                emprestimo.setId_usuario(rs.getInt("id_usuario"));
                emprestimo.setNomeUsuario(rs.getString("nomeUsuario"));
                emprestimo.setId_material(rs.getInt("id_material"));
                emprestimo.setNomeMaterial(rs.getString("nomeMaterial"));
                emprestimo.setData_retiro(rs.getDate("data_retiro").toLocalDate());
                emprestimo.setDias(rs.getInt("dias"));
                emprestimo.setDevolvido(rs.getBoolean("devolvido") ? "Sim" : "Nao");
                
                emprestimos.add(emprestimo);
                //A tabela avioes vai adicionar com add
                //o objeto usuario para colocar os dados
                //do banco de dados nele, e assim
                //transferi-los para a classe Aviao
                //para que seja exibido na tela
            }
            //fechar a conexão
            stmt.executeQuery();
            rs.close();
            stmt.close();
            //conexao_BD.close();
            return emprestimos; //vai retornar a tabela do banco receitas
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    //UPDATE
    public void Atualiza_Emprestimo (Emprestimo emprestimo) {
        String sql = "UPDATE `emprestimos` SET "
                + "id_material = ?, "
                + "id_usuario = ?, "
                + "data_retiro = ?, "
                + "dias = ?, "
                + "devolvido = ? "
                + "WHERE id = ?";
        Date data_retiro = Date.valueOf(emprestimo.getData_retiro());
        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setInt(1, emprestimo.getId_material());
            stmt.setInt(2, emprestimo.getId_usuario());
            stmt.setDate(3, data_retiro);
            stmt.setInt(4, emprestimo.getDias());
            stmt.setBoolean(5, emprestimo.isDevolvido().equals("Sim"));
            stmt.setInt(6, emprestimo.getId_emprestimo());
            
            stmt.execute();
            stmt.close();
            //conexao_BD.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
