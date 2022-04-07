package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.Usuario;

public class DAO_Usuario {
    public static Connection conexao_BD;
    
    //ResultSet vai buscar os dados no banco por meio de uma ObservableList<>
    public static ResultSet r;
    
    public static PreparedStatement st;
    
    public DAO_Usuario () {
        //Vai acionar a Fabrica de Conexões para se conectar ao banco de dados
        conexao_BD = ConnectionFactory.getConnection();
    }
    
    //CREATE
    public static void Insere_Cadastro(Usuario usuario) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "INSERT INTO usuarios" + "(nome, cpf, email, senha, funcionario)"
                + "VALUES (?,?,?,MD5(?),?)";
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
            stat.setString(1, usuario.getNome());
            stat.setString(2, usuario.getCpf());
            stat.setString(3, usuario.getEmail());
            stat.setString(4, usuario.getSenha());
            stat.setBoolean(5, usuario.isFuncionario().equals("Sim"));
            //A ordem da numeração, tem que ser na SEQUÊNCIA dos
            //dados definidos na tabela do banco de dados.
            //E sempre de maneira SEQUENCIAL
            
            stat.execute();
            stat.close();
            //conexao_BD.close();
        }
        catch (SQLException ex) {
            System.out.println("Insere usuario - " + ex.getMessage());
        }
    }
    
    
    //RETRIEVE
    public ObservableList<Usuario> get_Usuarios() {
        try {
            ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
            //ObservableList vai carregar os dados da tabela do banco de dados
            //chamada avioes por meio do observableArrayList()

            PreparedStatement stmt = this.conexao_BD.prepareStatement("SELECT id, nome, cpf, email, senha, funcionario FROM usuarios");
            
            ResultSet rs = stmt.executeQuery();

            r = rs;
            st = stmt;
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                Usuario usuario = new Usuario();
                
                usuario.setId_usuario(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setFuncionario(rs.getBoolean("funcionario") ? "Sim" : "Nao");
                
                usuarios.add(usuario);
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
            return usuarios; //vai retornar a tabela do banco receitas
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    //RETRIEVE
    public ArrayList<Usuario> get_UsuariosLogin(String CPF, String senha) {
        ArrayList<Usuario> usuarios = new ArrayList();
        //ObservableList vai carregar os dados da tabela do banco de dados
        //chamada avioes por meio do observableArrayList()

        String sql = "SELECT id, nome, cpf, email, senha, funcionario"
                   + " FROM usuarios"
                   + " WHERE cpf = ? AND senha = MD5(?)";
        try {
            PreparedStatement stmt = this.conexao_BD.prepareStatement(sql);
            
            stmt.setString(1, CPF);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();

            r = rs;
            st = stmt;
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                Usuario usuario = new Usuario();
                
                usuario.setId_usuario(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setFuncionario(rs.getBoolean("funcionario") ? "Sim" : "Nao");
                
                usuarios.add(usuario);
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
            conexao_BD.close();
            return usuarios; //vai retornar a tabela do banco receitas
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    //UPDATE
    public void Atualiza_Usuario (Usuario usuario) {
        String sql = "UPDATE `usuarios` SET "
                + "nome = ?, "
                + "cpf = ?, "
                + "email = ?, "
                + "senha = MD5(?), "
                + "funcionario = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setBoolean(5, usuario.isFuncionario().equals("Sim"));
            stmt.setInt(6, usuario.getId_usuario());
            
            stmt.execute();
            stmt.close();
            //conexao_BD.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    //DELETE
    public void Deleta_Usuario (Usuario usuario) {
        String sql = "DELETE FROM usuarios WHERE id=?";

        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setInt(1, usuario.getId_usuario());
            //Vou deletar somente a chave primária, e assim todo o 
            //registro vai ser deletado também
            stmt.execute();
            stmt.close();
            //conexao_BD.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
