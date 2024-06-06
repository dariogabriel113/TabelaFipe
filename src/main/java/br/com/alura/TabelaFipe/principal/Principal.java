package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosModelos;
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
            String tipoBuscado = exibeMenuMarcas(valor);
            if (tipoBuscado != null) {
                exibeMenuModelos(tipoBuscado);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do tipo digitado.");
        }
    }

    public String exibeMenuMarcas(String valor) {
        String tipoParaBusca = buscaTipoVeiculo(valor);
        if (tipoParaBusca != null) {
            System.out.println("Tipo escolhido: " + valor);

            var json = consumoApi.obterDados(ENDERECO + tipoParaBusca + "/marcas");

            DadosVeiculo[] dados = conversor.obterDados(json, DadosVeiculo[].class);
            exibeListagemDeDados(Arrays.stream(dados).toList());
            return tipoParaBusca;
        }

        System.out.println("Tipo não identificado, você digitou: " + valor);
        return null;
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

    public void exibeListagemDeDados(List<DadosVeiculo> dados) {
        System.out.println(" ");
        System.out.println("=========================================");

        dados.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(e -> System.out.println("Cód: " + e.codigo() + " - " + "Nome: " + e.nome()));

        System.out.println("=========================================");
        System.out.println(" ");
    }

    public Integer exibeMenuModelos(String tipoParaBusca) {
        System.out.println("Informe o código da marca para consulta:");

        try {
            Integer valor = scanner.nextInt();
            System.out.println("Codigo inserido: " + valor);

            var json = consumoApi.obterDados(ENDERECO + tipoParaBusca + "/marcas/" + valor + "/modelos");

            DadosModelos dados = conversor.obterDados(json, DadosModelos.class);
            List<DadosVeiculo> dadosVeiculos = new ArrayList<>(dados.modelos());
            exibeListagemDeDados(dadosVeiculos);
            return valor;
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do código digitado.");
        }

        return null;
    }
}
