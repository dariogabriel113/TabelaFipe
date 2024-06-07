package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosEnderecoURL;
import br.com.alura.TabelaFipe.model.DadosModelos;
import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private DadosEnderecoURL dadosEndereco = new DadosEnderecoURL();

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
            exibeMenuMarcas(valor);
            if (dadosEndereco.getTipo() != null) {
                exibeMenuModelos();
                exibeMenuAnos();

                if (dadosEndereco.getAno() != null) {
                    buscaDadosVeiculo();
                }
            } else {
                System.out.println("Tipo não identificado, você digitou: " + valor);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do tipo digitado.");
        }
    }

    public void exibeMenuMarcas(String valor) {
        String tipoParaBusca = buscaTipoVeiculo(valor);
        if (tipoParaBusca != null) {
            System.out.println("Tipo escolhido: " + valor);

            var json = consumoApi.obterDados(ENDERECO + tipoParaBusca + "/marcas");

            DadosVeiculo[] dados = conversor.obterDados(json, DadosVeiculo[].class);
            exibeListagemDeDados(Arrays.stream(dados).toList());

            dadosEndereco.setTipo(tipoParaBusca);
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

    public void exibeListagemDeDados(List<DadosVeiculo> dados) {
        System.out.println(" ");
        System.out.println("=========================================");

        dados.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(e -> System.out.println("Cód: " + e.codigo() + " - " + "Nome: " + e.nome()));

        System.out.println("=========================================");
        System.out.println(" ");
    }

    public void exibeMenuModelos() {
        System.out.println("Informe o código da marca para consulta:");

        try {
            Integer valor = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Codigo inserido: " + valor);

            var json = consumoApi.obterDados(ENDERECO + dadosEndereco.getTipo() + "/marcas/" + valor + "/modelos");
            if (json != null) {
                dadosEndereco.setCodigoMarca(valor);

                DadosModelos dados = conversor.obterDados(json, DadosModelos.class);
                List<DadosVeiculo> dadosVeiculos = new ArrayList<>(dados.modelos());
                exibeListagemDeDados(dadosVeiculos);

                filtrarDadosVeiculos(dadosVeiculos);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do valor digitado.");
        }
    }

    public void filtrarDadosVeiculos(List<DadosVeiculo> dadosVeiculos) {
        List<DadosVeiculo> dadosVeiculosParaFiltro = new ArrayList<>(dadosVeiculos);

        boolean isFiltrarAtivo = true;
        while (isFiltrarAtivo) {
            System.out.println("Informe um nome para filtrar:");
            String valorFiltro = scanner.nextLine();

            if (!Objects.equals(valorFiltro, "")) {
                dadosVeiculosParaFiltro = dadosVeiculosParaFiltro.stream()
                        .filter(modelo -> modelo.nome().toUpperCase().contains(valorFiltro.toUpperCase()))
                        .collect(Collectors.toList());
                exibeListagemDeDados(dadosVeiculosParaFiltro);
            }

            System.out.println("Filtrar novamente? (Sim)/(Não)");
            String respostaContinuarFiltro = scanner.nextLine();
            if (respostaContinuarFiltro.equalsIgnoreCase("Não")) {
                isFiltrarAtivo = false;
            } else {
                dadosVeiculosParaFiltro = new ArrayList<>(dadosVeiculos);
            }
        }

        System.out.println("Informe o código de modelo para buscar os anos:");
        Integer codigo = scanner.nextInt();
        dadosEndereco.setCodigoModelo(codigo);
        scanner.nextLine();

        System.out.println("Código informado: " + codigo);
    }

    public void exibeMenuAnos() {
        var json = consumoApi.obterDados(ENDERECO + dadosEndereco.getTipo() + "/marcas/" + dadosEndereco.getCodigoMarca() + "/modelos/" + dadosEndereco.getCodigoModelo() + "/anos");
        if (json != null && !json.contains("error")) {
            DadosVeiculo[] dados = conversor.obterDados(json, DadosVeiculo[].class);
            exibeListagemDeDados(Arrays.stream(dados).toList());

            System.out.println("Informe o ano que deseja pesquisar:");
            String anoBusca = scanner.nextLine();
            if (anoBusca != null) {
                dadosEndereco.setAno(anoBusca);
            }
        } else {
            System.out.println("Código de modelo não identificado, você digitou: " + dadosEndereco.getCodigoModelo());
        }
    }

    public void buscaDadosVeiculo() {
        var json = consumoApi.obterDados(ENDERECO + dadosEndereco.getTipo() + "/marcas/" + dadosEndereco.getCodigoMarca() + "/modelos/" + dadosEndereco.getCodigoModelo() + "/anos/" + dadosEndereco.getAno());
        if (json != null) {
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            System.out.println(veiculo);
        }
    }
}
