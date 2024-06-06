package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

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

            String msg = "Tipo escolhido: ";
            var json = "";

            String tipoParaBusca = null;
            if (valor.equalsIgnoreCase("Carro")) {
                tipoParaBusca = "carros";
            } else if (valor.equalsIgnoreCase("Moto")) {
                tipoParaBusca = "motos";
            } else if (valor.equalsIgnoreCase("Caminhão") || valor.equalsIgnoreCase("Caminhao")) {
                tipoParaBusca = "caminhoes";
            } else {
                msg = "Tipo não identificado, você digitou: ";
            }

            System.out.println(msg + valor);
            if (tipoParaBusca != null) {
                json = consumoApi.obterDados(ENDERECO + tipoParaBusca + "/marcas");
                System.out.println(json);

                DadosVeiculo[] dados = conversor.obterDados(json, DadosVeiculo[].class);
                Arrays.stream(dados).toList()
                        .stream()
                        .sorted(Comparator.comparing(DadosVeiculo::codigo))
                        .forEach(e -> System.out.println("Código: " + e.codigo() + " - " + "Marca: " + e.nome()));
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do tipo digitado.");
        }
    }
}
