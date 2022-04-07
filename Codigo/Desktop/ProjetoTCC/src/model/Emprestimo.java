package model;

import java.time.LocalDate;

public class Emprestimo {
    private int id_emprestimo;
    private int id_material;
    private int id_usuario;
    private LocalDate data_retiro;
    private int dias;
    private String devolvido;
    private String nomeUsuario;
    private String nomeMaterial;

    /**
     * @return the id_emprestimo
     */
    public int getId_emprestimo() {
        return id_emprestimo;
    }

    /**
     * @param id_emprestimo the id_emprestimo to set
     */
    public void setId_emprestimo(int id_emprestimo) {
        this.id_emprestimo = id_emprestimo;
    }

    /**
     * @return the id_material
     */
    public int getId_material() {
        return id_material;
    }

    /**
     * @param id_material the id_material to set
     */
    public void setId_material(int id_material) {
        this.id_material = id_material;
    }

    /**
     * @return the id_usuario
     */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
     * @param id_usuario the id_usuario to set
     */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
     * @return the data_retiro
     */
    public LocalDate getData_retiro() {
        return data_retiro;
    }

    /**
     * @param data_retiro the data_retiro to set
     */
    public void setData_retiro(LocalDate data_retiro) {
        this.data_retiro = data_retiro;
    }

    /**
     * @return the dias
     */
    public int getDias() {
        return dias;
    }

    /**
     * @param dias the dias to set
     */
    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
     * @return the devolvido
     */
    public String isDevolvido() {
        return devolvido;
    }

    /**
     * @param devolvido the devolvido to set
     */
    public void setDevolvido(String devolvido) {
        this.devolvido = devolvido;
    }

    /**
     * @return the nomeUsuario
     */
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    /**
     * @param nomeUsuario the nomeUsuario to set
     */
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    /**
     * @return the nomeMaterial
     */
    public String getNomeMaterial() {
        return nomeMaterial;
    }

    /**
     * @param nomeMaterial the nomeMaterial to set
     */
    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }
    
    
}
