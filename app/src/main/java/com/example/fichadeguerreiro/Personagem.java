package com.example.fichadeguerreiro;

import java.io.Serializable;
import java.util.List;

public class Personagem implements Serializable {
    private String id;
    private String nome;
    private String raca;
    private String classe;
    private int forca;
    private int destreza;
    private int constituicao;
    private int inteligencia;
    private int sabedoria;
    private int carisma;
    private List<String> pericias;

    public Personagem() {
    }

    public Personagem(String nome, String raca, String classe,
                      int forca, int destreza, int constituicao,
                      int inteligencia, int sabedoria, int carisma,
                      List<String> pericias) {
        this.nome = nome;
        this.raca = raca;
        this.classe = classe;
        this.forca = forca;
        this.destreza = destreza;
        this.constituicao = constituicao;
        this.inteligencia = inteligencia;
        this.sabedoria = sabedoria;
        this.carisma = carisma;
        this.pericias = pericias;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public int getForca() { return forca; }
    public void setForca(int forca) { this.forca = forca; }

    public int getDestreza() { return destreza; }
    public void setDestreza(int destreza) { this.destreza = destreza; }

    public int getConstituicao() { return constituicao; }
    public void setConstituicao(int constituicao) { this.constituicao = constituicao; }

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }

    public int getSabedoria() { return sabedoria; }
    public void setSabedoria(int sabedoria) { this.sabedoria = sabedoria; }

    public int getCarisma() { return carisma; }
    public void setCarisma(int carisma) { this.carisma = carisma; }

    public List<String> getPericias() { return pericias; }
    public void setPericias(List<String> pericias) { this.pericias = pericias; }
}
