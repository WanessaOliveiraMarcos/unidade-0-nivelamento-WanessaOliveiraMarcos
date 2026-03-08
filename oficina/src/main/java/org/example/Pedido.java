package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.example.exceptions.EstoqueInsuficienteException;

public class Pedido {

	/** Quantidade máxima de produto de um pedido */
	private static final int MAX_ITENS = 10;
	
	/** Porcentagem de desconto para pagamentos à vista */
	private static final double DESCONTO_PG_A_VISTA = 0.15;
	
	/** Vetor para armazenar os itens do pedido */
	private ItemDePedido[] itens;
	
	/** Data de criação do pedido */
	private LocalDate dataPedido;
	
	/** Indica a quantidade total de itens no pedido até o momento */
	private int quantItens = 0;
	
	/** Indica a forma de pagamento do pedido sendo: 1, pagamento à vista; 2, pagamento parcelado */
	private int formaDePagamento;
	
	
	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		itens = new ItemDePedido[MAX_ITENS];
		quantItens = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}
	
	
	public boolean incluirItem(ItemDePedido novo) {

		// Primeiro procura se o item já existe
		for (ItemDePedido item : itens) {
			if (item != null && item.equals(novo)) {
				try {
					item.aumentarQuantidade(novo.getQuantidade());
				} catch (EstoqueInsuficienteException e) {
					System.err.println("Estoque insuficiente para aumentar a quantidade do item: " + e.getMessage());
					return false;
				}
				return true;
			}
		}

		// Se não encontrou, tenta adicionar novo item
		if (quantItens < MAX_ITENS) {
			itens[quantItens++] = novo;
			return true;
		}

		return false;
	}
	
	
	/**
     * Calcula e retorna o valor final do pedido (soma do valor de venda de todos os produtos do pedido).
     * Caso a forma de pagamento do pedido seja à vista, aplica o desconto correspondente.
     * @return Valor final do pedido (double)
     */
	public double valorFinal() {
		double valorPedido = 0;
		
		for (int i = 0; i < quantItens; i++) {
			valorPedido += itens[i].calcularSubtotal();
		}
		
		if (formaDePagamento == 1) {
			valorPedido = valorPedido * (1.0 - DESCONTO_PG_A_VISTA);
		}
		return valorPedido;
		
	}
	
	/**
     * Representação, em String, do pedido.
     * Contém um cabeçalho com sua data e o número de produtos no pedido.
     * Depois, em cada linha, a descrição de cada produto do pedido.
     * Ao final, mostra a forma de pagamento, o percentual de desconto (se for o caso) e o valor a ser pago pelo pedido.
     * Exemplo:
     * Data do pedido: 25/08/2025
     * Pedido com 2 produtos.
     * Produtos no pedido:
     * NOME: Iogurte: R$ 8.00
     * Válido até: 29/08/2025
     * NOME: Guardanapos: R$ 2.75
     * Pedido pago à vista. Percentual de desconto: 15,00%
     * Valor total do pedido: R$ 10.75 
     * @return Uma string contendo dados do pedido conforme especificado (cabeçalho, detalhes, forma de pagamento,
     * percentual de desconto - se for o caso - e valor a pagar)
     */
	@Override
	public String toString() {
		
		StringBuilder stringPedido = new StringBuilder();
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		stringPedido.append("Data do pedido:" + formatoData.format(dataPedido) + "\n");
		
		stringPedido.append("Pedido com " + quantItens + " produtos.\n");
		stringPedido.append("Produtos no pedido:\n");
		for (int i = 0; i < quantItens; i++ ) {
			stringPedido.append(itens[i].toString() + "\n");
		}
		
		stringPedido.append("Pedido pago ");
		if (formaDePagamento == 1) {
			stringPedido.append("à vista. Percentual de desconto: " + String.format("%.2f", DESCONTO_PG_A_VISTA * 100) + "%\n");
		} else {
			stringPedido.append("parcelado.\n");
		}
		
		stringPedido.append("Valor total do pedido: R$ " + String.format("%.2f", valorFinal()));
		
		return stringPedido.toString();
	}
	
	/**
     * Igualdade de pedidos: caso possuam a mesma data. 
     * @param obj Outro pedido a ser comparado 
     * @return booleano true/false conforme o parâmetro possua a data igual ou não a este pedido.
     */
    @Override
    public boolean equals(Object obj) {
        Pedido outro = (Pedido)obj;
        return this.dataPedido.equals(outro.dataPedido);
    }
}
