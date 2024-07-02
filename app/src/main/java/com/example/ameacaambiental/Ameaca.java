package com.example.ameacaambiental;

public class Ameaca {
    private String id;
    private String descricao;
    private String endereco;
    private String data;
    private String image;

    public Ameaca() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public String getImage(){return  image;}
    public void setImage(String image){this.image = image;}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}