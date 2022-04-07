package model;

public class Material {
    private int id_material;
    private String nome;
    private String descricao;
    private String disponivel;

    
    public int getId_material() {
        return id_material;
    }

    public void setId_material(int id_receita) {
        this.id_material = id_receita;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String isDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(String disponivel) {
        this.disponivel = disponivel;
    }
}
