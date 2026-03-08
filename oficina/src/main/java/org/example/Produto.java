package org.example;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	private String descricao;
	private double precoCusto;
	private double margemLucro;
	private int quantidadeEmEstoque;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro, int quantidadeEmEstoque) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
			this.quantidadeEmEstoque = quantidadeEmEstoque;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, double margemLucro, int quantidadeEmEstoque) {
		init(desc, precoCusto, margemLucro, quantidadeEmEstoque);
	}
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, int quantidadeEmEstoque) {
		init(desc, precoCusto, MARGEM_PADRAO, quantidadeEmEstoque);
	}
	
	//Getters e Setters
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getPrecoCusto() {
		return precoCusto;
	}
	public void setPrecoCusto(double precoCusto) {
		this.precoCusto = precoCusto;
	}
	public double getMargemLucro() {
		return margemLucro;
	}
	public void setMargemLucro(double margemLucro) {
		this.margemLucro = margemLucro;
	}
	public int getQuantidadeEmEstoque() {
		return quantidadeEmEstoque;
	}
	public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {		
		this.quantidadeEmEstoque = quantidadeEmEstoque;
	}
	 /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
	
	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}
	/**
	* Igualdade de produtos: caso possuam o mesmo nome/descrição.
	* @param obj Outro produto a ser comparado
	* @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
	*/
	@Override
	public boolean equals(Object obj){
		Produto outro = (Produto)obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	/**
	* Gera uma linha de texto a partir dos dados do produto
	* @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	*/
	public abstract String gerarDadosTexto();

	/**
	* Cria um produto a partir de uma linha de dados em formato texto. A linha de dados deve estar de acordo com a
	formatação
	* "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	* ou o funcionamento não será garantido. Os tipos são 1 para produto não perecível e 2 para perecível.
	* @param linha Linha com os dados do produto a ser criado.
	* @return Um produto com os dados recebidos
	*/
	static Produto criarDoTexto(String linha){
		Produto novoProduto = null;
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String [] atributos = linha.split(";");
		int tipoProduto = Integer.parseInt(atributos[0]);
		String descricao = atributos[1];
		double precoCusto = Double.parseDouble(atributos[2]);
		double margemLucro = Double.parseDouble(atributos[3]);
		if(tipoProduto == 1){
			int quantidadeEmEstoque = Integer.parseInt(atributos[4]);
			novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
		} else if (tipoProduto == 2){
			String dataValidade = atributos[4];
			int quantidadeEmEstoque = Integer.parseInt(atributos[5]);
			novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, LocalDate.parse(dataValidade, formatoData), quantidadeEmEstoque);
		}
		return novoProduto;
	}
}
