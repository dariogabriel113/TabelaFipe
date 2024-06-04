package br.com.alura.TabelaFipe.principal;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        System.out.println("Digite o número do tipo de veículo que deseja consultar, (1) para carro, (2) moto ou (3) caminhão):");

        try {
            Integer valor = scanner.nextInt();

            List<Integer> tipos = new ArrayList<>(Arrays.asList(1,2,3));

            String msg = "Tipo não identificado, você digitou: ";

            if (tipos.contains(valor)) {
                msg = "Tipos escolhido: ";
            }

            System.out.println(msg + valor);
        } catch(InputMismatchException inputMismatchException) {
            System.out.println("Erro na leitura do tipo digitado.");
        }
    }
}
