package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.*;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";

    public void exibirMenu() {
        System.out.println("**** OPÇÕES ****\n" +
                "\n" +
                "Carro\n" +
                "\n" +
                "Moto\n" +
                "\n" +
                "Caminhão\n" +
                "\n" +
                "Digite uma das opções para consultar valores:");

        try {
            String valor = scanner.nextLine();
            buscaDadosDeMarcas(valor);
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do tipo digitado.");
        }
    }

    public void buscaDadosDeMarcas(String valor) {
        var json = "";

        String tipoParaBusca = buscaTipoVeiculo(valor);
        if (tipoParaBusca != null) {
            System.out.println("Tipo escolhido: " + valor);

            json = consumoApi.obterDados(ENDERECO + tipoParaBusca + "/marcas");

            DadosVeiculo[] dados = conversor.obterDados(json, DadosVeiculo[].class);
            exibeListagemDeMarcas(dados);
        } else {
            System.out.println("Tipo não identificado, você digitou: " + valor);
        }
    }

    public String buscaTipoVeiculo(String valor) {
        if (valor.equalsIgnoreCase("Carro")) {
            return "carros";
        } else if (valor.equalsIgnoreCase("Moto")) {
            return "motos";
        } else if (valor.equalsIgnoreCase("Caminhão") || valor.equalsIgnoreCase("Caminhao")) {
            return "caminhoes";
        }

        return null;
    }

    public void exibeListagemDeMarcas(DadosVeiculo[] dados) {
        Arrays.stream(dados).toList()
                .stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(e -> System.out.println("Cód: " + e.codigo() + " - " + "Marca: " + e.nome()));
    }
}
