package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.example.exceptions.ProdutoVencidoException;

public class ProdutoPerecivel extends Produto {
    private static final int PRAZO_DESCONTO = 7;
    private static final double DESCONTO = 0.25;
    LocalDate dataDeValidade;
    // Construtores
    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataDeValidade, int quantidadeEmEstoque) {
        super(desc, precoCusto, margemLucro, quantidadeEmEstoque);
        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de validade não pode ser anterior à data atual");
        }
        this.dataDeValidade = dataDeValidade;
    }

    public ProdutoPerecivel(String desc, double precoCusto, LocalDate dataDeValidade, int quantidadeEmEstoque) {
        super(desc, precoCusto, quantidadeEmEstoque);
        this.dataDeValidade = dataDeValidade;
    }

    // Getters e Setters
    public LocalDate getDataDeValidade() {
        return dataDeValidade;
    }

    public void setDataDeValidade(LocalDate dataDeValidade) {
        this.dataDeValidade = dataDeValidade;
    }

    // Métodos
    public boolean venceEm7DiasOuMenos() {
        return !dataDeValidade.isBefore(LocalDate.now()) &&
                !dataDeValidade.isAfter(LocalDate.now().plusDays(7));
    }
    @Override
    public double valorDeVenda() throws ProdutoVencidoException {
        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new ProdutoVencidoException("O produto esta fora da data de validade");

        }else{
            if(venceEm7DiasOuMenos()){
                double valorSemDesconto = super.valorDeVenda();
                return valorSemDesconto - (valorSemDesconto * 0.25);
            } else {
                return super.valorDeVenda();
            }
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() +
                " | Data de Validade: " + dataDeValidade.format(formato);
    }

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * Data de validade vai no formato dd/mm/aaaa
    * @return Uma string no formato "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
    */
    @Override
    public String gerarDadosTexto(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String precoFormatado = String.format("%.2f", this.getPrecoCusto()).replace(",",".");
        String margemFormatada = String.format("%.2f", this.getMargemLucro()).replace(",",".");
        String dataFormatada = formato.format(dataDeValidade);
        String quantidadeFormatada = String.valueOf(this.getQuantidadeEmEstoque());
        return ("2;%s;%s;%s;%s;%s".formatted(getDescricao(), precoFormatado, margemFormatada, dataFormatada, quantidadeFormatada));
    }
}
