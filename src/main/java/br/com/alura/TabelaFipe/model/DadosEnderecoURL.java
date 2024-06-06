package br.com.alura.TabelaFipe.model;

public class DadosEnderecoURL {
    private String tipo;
    private Integer codigoMarca;
    private Integer codigoModelo;
    private String ano;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(Integer codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public Integer getCodigoModelo() {
        return codigoModelo;
    }

    public void setCodigoModelo(Integer codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
