package model;

import java.time.LocalDate;

public class Reserva {
    private int id_reserva;
    private int id_material;
    private int id_usuario;
    private LocalDate data_retiro;
    private int dias;
    private String status;
    private String nomeUsuario;
    private String nomeMaterial;

    /**
     * @return the id_reserva
     */
    public int getId_reserva() {
        return id_reserva;
    }

    /**
     * @param id_reserva the id_reserva to set
     */
    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
