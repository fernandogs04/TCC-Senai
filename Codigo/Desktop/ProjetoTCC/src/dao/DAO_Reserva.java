package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.Reserva;

public class DAO_Reserva {
    public static Connection conexao_BD;
    
    //ResultSet vai buscar os dados no banco por meio de uma ObservableList<>
    public static ResultSet r;
    
    public static PreparedStatement st;
    
    public DAO_Reserva () {
        //Vai acionar a Fabrica de Conexões para se conectar ao banco de dados
        conexao_BD = ConnectionFactory.getConnection();
    }
    
    //CREATE
    public static void Insere_Reserva(Reserva reserva) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "INSERT INTO reservas" + "(id_usuario, id_material, data_retiro, dias, status)"
                + "VALUES (?,?,?,?,?)";
        //? significa que não sabemos os valores que  o usuario
        //digitou na tela
        Date data_retiro = Date.valueOf(reserva.getData_retiro());
        try {
            //O PreparedStatement ele prepara o objeto aviao
            //para executar a String sql e assim enviar para o banco
            //de dados
            PreparedStatement stat = conexao_BD.prepareStatement(sql);
            //stat é um objeto da classe PreparedStatement que vai
            //enviar os dados recolhidos do método Insere_Aviao
            //para o banco de dados
            stat.setInt(1, reserva.getId_usuario());
            stat.setInt(2, reserva.getId_material());
            stat.setDate(3, data_retiro);
            stat.setInt(4, reserva.getDias());
            stat.setString(5, reserva.getStatus());
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
    public static void Marcar_Devolvido(Reserva reserva) {
        //String sql vai receber o código SQL em um método JAVA
        String sql = "UPDATE reservas SET status = 'Concluido' WHERE id = ?";
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
            stat.setInt(1, reserva.getId_reserva());
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
    public ObservableList<Reserva> get_Reservas() {
        try {
            ObservableList<Reserva> reservas = FXCollections.observableArrayList();
            //ObservableList vai carregar os dados da tabela do banco de dados
            //chamada avioes por meio do observableArrayList()
            
            String sql = "SELECT reservas.id, reservas.id_usuario, reservas.id_material, reservas.data_retiro, "
                       + "reservas.dias, reservas.status, materiais.nome AS 'nomeMaterial', usuarios.nome AS 'nomeUsuario' "
                       + "FROM reservas "
                       + "INNER JOIN materiais ON reservas.id_material=materiais.id "
                       + "INNER JOIN usuarios ON reservas.id_usuario=usuarios.id";

            PreparedStatement stmt = this.conexao_BD.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();

            r = rs;
            st = stmt;
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                Reserva reserva = new Reserva();
                
                reserva.setId_reserva(rs.getInt("id"));
                reserva.setId_usuario(rs.getInt("id_usuario"));
                reserva.setNomeUsuario(rs.getString("nomeUsuario"));
                reserva.setId_material(rs.getInt("id_material"));
                reserva.setNomeMaterial(rs.getString("nomeMaterial"));
                reserva.setData_retiro(rs.getDate("data_retiro").toLocalDate());
                reserva.setDias(rs.getInt("dias"));
                reserva.setStatus(rs.getString("status"));
                
                reservas.add(reserva);
                //A tabela avioes vai adicionar com add
                //o objeto usuario para colocar os dados
                //do banco de dados nele, e assim
                //transferi-los para a classe Aviao
                //para que seja exibido na tela
            }
            //fechar a conexão
            //stmt.executeQuery();
            rs.close();
            stmt.close();
            //conexao_BD.close();
            return reservas; //vai retornar a tabela do banco receitas
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ArrayList<LocalDate> get_DiasOcupados(LocalDate diaRetiro, int idMaterial, int diasRetirados) {
        try {
            ArrayList<LocalDate> diasOcupados = new ArrayList();
            //ObservableList vai carregar os dados da tabela do banco de dados
            //chamada avioes por meio do observableArrayList()
            
            String sql = "SELECT data_retiro, dias"
                       + " FROM reservas"
                       + " WHERE data_retiro"
                       + " BETWEEN DATE('" + diaRetiro +"' - INTERVAL 2 MONTH)"
                       + " AND DATE('" + diaRetiro +"' + INTERVAL 2 MONTH)"
                       + " AND id_material = " + idMaterial
                       + " AND status = 'Aberto'"
                       + " ORDER BY data_retiro ASC";
            
            Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            
            //rs vai executar a query ate a proxima vez dentro do WHILE
            //para buscar da tabela usuarios os dados do banco e associá-lo
            //a classe Usuario
            while (rs.next()) {
                //enquanto a for menor que o numero de dias da nova reserva; a++
                for (int a = 0; a < diasRetirados; a++) {
                    //Adicionar + a a data, na primeira vez, a é 0 e deixara a data intacta
                    LocalDate dataRet = diaRetiro.plusDays(a);
                    //enquanto b for menor que o numero de dias da reserva antiga; b++
                    for (int b = 0; b < rs.getInt("dias"); b++) {
                        //Adicionar + b a data, na primeira vez, b é 0 e deixara a data intacta
                        LocalDate dataLinha = rs.getDate("data_retiro").toLocalDate().plusDays(b);
                        
                        if (dataRet.equals(dataLinha)) {
                            diasOcupados.add(dataLinha);
                        }
                    }
                }
            }
            //fechar a conexão
            rs.close();
            stmt.close();
            //conexao_BD.close();
            return diasOcupados; //vai retornar os dias ocupados
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    //UPDATE
    public void Atualiza_Reserva (Reserva reserva) {
        Date data_retiro = Date.valueOf(reserva.getData_retiro());
        
        String sql = "UPDATE `reservas` SET "
                + "id_usuario = ?, "
                + "id_material = ?, "
                + "data_retiro = ?, "
                + "dias = ?, "
                + "status = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement stmt = conexao_BD.prepareStatement(sql);
            stmt.setInt(1, reserva.getId_usuario());
            stmt.setInt(2, reserva.getId_material());
            stmt.setDate(3, data_retiro);
            stmt.setInt(4, reserva.getDias());
            stmt.setString(5, reserva.getStatus());
            stmt.setInt(6, reserva.getId_reserva());
            
            stmt.execute();
            stmt.close();
            //conexao_BD.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
