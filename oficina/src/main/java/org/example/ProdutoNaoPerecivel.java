package org.example;
import java.time.format.DateTimeFormatter;

public class ProdutoNaoPerecivel extends Produto{
    public ProdutoNaoPerecivel (String descricao, double precoCusto, double margemLucro, int quantidadeEmEstoque){
        super(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto, int quantidadeEmEstoque){
        super(descricao, precoCusto, quantidadeEmEstoque);
    }

    @Override
    public double valorDeVenda() {
        return super.valorDeVenda();
    }

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * @return Uma string no formato "1; descrição;preçoDeCusto;margemDeLucro"
    */
    @Override
    public String gerarDadosTexto(){
        String precoFormatado = String.format("%.2f", this.getPrecoCusto()).replace(",",".");
        String margemFormatada = String.format("%.2f", this.getMargemLucro()).replace(",",".");
        String quantidadeFormatada = String.valueOf(this.getQuantidadeEmEstoque());
        return ("1;%s;%s;%s;%s".formatted(getDescricao(), precoFormatado, margemFormatada, quantidadeFormatada));
    }
}
