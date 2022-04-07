package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.Material;

public class DAO_Material {
    public static Connection conexao_BD;
    
    //ResultSet vai buscar os dados no banco por meio de uma ObservableList<>
    public static ResultSet r;
    
    public static PreparedStatement st;
    
    public DAO_Material () {
        //Vai acionar a Fabrica de Conexões para se conectar ao banco de dados
        DAO_Material.conexao_BD = ConnectionFactory.getConnection();
        
    }
    
    //CREATE
    public static void Insere_Material(Material material) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "INSERT INTO materiais" + "(nome, descricao, disponivel)"
                + "VALUES (?,?,?)";
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
            stat.setString(1, material.getNome());
            stat.setString(2, material.getDescricao());
            stat.setBoolean(3, material.isDisponivel().equals("Sim"));
            //stat.setInt(3, material.getId_registrante());
            //A ordem da numeração, tem que ser na SEQUÊNCIA dos
            //dados definidos na tabela do banco de dados.
            //E sempre de maneira SEQUENCIAL
            
            stat.execute();
            stat.close();
            //conexao_BD.close();
        }
        catch (SQLException ex) {
            System.out.println("Insere material - " + ex.getMessage());
        }
    }
    
    
    //RETRIEVE
    public ObservableList<Material> get_Materiais() {
        try {
            ObservableList<Material> materiais = FXCollections.observableArrayList();
            //ObservableList vai carregar os dados da tabela do banco de dados
            //chamada avioes por meio do observableArrayList()

            PreparedStatement stmt = this.conexao_BD.prepareStatement("SELECT id, nome, descricao, disponivel FROM materiais");
            
            ResultSet rs = stmt.executeQuery();

            r = rs;
            st = stmt;
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                Material material = new Material();
                
                material.setId_material(rs.getInt("id"));
                material.setNome(rs.getString("nome"));
                material.setDescricao(rs.getString("descricao"));
                material.setDisponivel(rs.getBoolean("disponivel") ? "Sim" : "Nao");
                
                materiais.add(material);
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
            return materiais; //vai retornar a tabela do banco receitas
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    //UPDATE
    public void Atualiza_Material (Material material) {
        String sql = "UPDATE materiais SET "+ 
                "nome = ?" + 
                ", descricao = ?" + 
                ", disponivel = ?" + 
                " WHERE id = ?";
        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getDescricao());
            stmt.setBoolean(3, material.isDisponivel().equals("Sim"));
            stmt.setInt(4, material.getId_material());
            
            stmt.execute();
            stmt.close();
            //conexao_BD.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    //UPDATE
    public static void Marcar_Disponivel (int id_material) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "UPDATE materiais SET disponivel = true WHERE id = ?";
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
            stat.setInt(1, id_material);
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
    
    
    //DELETE
    public void Deleta_Material (Material material) {
        String sql = "DELETE FROM materiais WHERE id = ?";

        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setInt(1, material.getId_material());
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
