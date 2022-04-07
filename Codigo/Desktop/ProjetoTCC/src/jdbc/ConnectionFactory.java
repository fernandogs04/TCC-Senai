package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection () {
        
        Connection conexao_BD_POSTGREE = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //O local para conectar o sistema, pode ser local ou na nuvem
            conexao_BD_POSTGREE = DriverManager.getConnection ("jdbc:mysql://localhost/sistemaemprestimos?useTimezone=true&serverTimezone=UTC", "root", "");
            
            return conexao_BD_POSTGREE;
        }
        catch (ClassNotFoundException e) {            
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        }
        catch (SQLException ex){
            //Se der erro de conexão, o erro vai mostrar aqui
            System.out.println("Connection Factory - " + ex.getMessage());
        }
        return null;
    }
}