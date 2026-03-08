package org.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.example.exceptions.ProdutoNaoEncontradoException;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
   static Produto[] lerProdutos(String nomeArquivoDados) {

        Produto[] vetorProdutos = new Produto[0];

        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivoDados))) {
            String primeiraLinha = leitor.readLine();
            int quantidade = Integer.parseInt(primeiraLinha);
            vetorProdutos = new Produto[quantidade + MAX_NOVOS_PRODUTOS];
            for (int i = 0; i < quantidade; i++) {
                String linha = leitor.readLine();

                Produto p = Produto.criarDoTexto(linha);
                vetorProdutos[i] = p;
            }
            quantosProdutos = quantidade;

        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            vetorProdutos = new Produto[0];
        }

        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     *  A busca não é sensível ao caso.  Em caso de não encontrar o produto, imprime mensagem padrão */
    static void localizarProdutos() throws ProdutoNaoEncontradoException {
        try {
            System.out.println("Digite o nome do produto que deseja localizar: ");
            String produtoPesquisado = teclado.nextLine();
            boolean produtoEncontrado = false;
            // Produto temporário para a busca
            Produto produtoBusca = new ProdutoNaoPerecivel(produtoPesquisado, 10, 10);
            for(int i = 0; i < quantosProdutos; i++){
                if(produtosCadastrados[i].equals(produtoBusca)){
                    System.out.println("Produto encontrado: " + produtosCadastrados[i].toString());
                    produtoEncontrado = true;
                    break;
                }
            }
            if(!produtoEncontrado){
                throw new ProdutoNaoEncontradoException("Produto não encontrado: " + produtoPesquisado);
            }

        } catch (Exception e){
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }  
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto(){
        try {
            // Pede ao usuário o tipo do produto
            System.out.println("Digite o tipo do produto (1 - Não Perecível, 2 - Perecível): ");
            int tipoProduto = Integer.parseInt(teclado.nextLine());
            System.out.println("Digite a descrição do produto: ");
            String descricao = teclado.nextLine();
            System.out.println("Digite o preço de custo do produto: ");
            String precoTexto = teclado.nextLine().replace(",", ".");
            double precoCusto = Double.parseDouble(precoTexto);
            System.out.println("Digite a margem de lucro do produto (em percentual, ex: 20 para 20%): ");
            String margemTexto = teclado.nextLine().replace(",", ".");
            double margemLucro = Double.parseDouble(margemTexto)/ 100.0;
            System.out.println("Digite a quantidade em estoque do produto: ");
            int quantidadeEmEstoque = Integer.parseInt(teclado.nextLine());
            if (tipoProduto == 1) {
                ProdutoNaoPerecivel novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
                produtosCadastrados[quantosProdutos] = novoProduto;
                quantosProdutos++;
                salvarProdutos(nomeArquivoDados);
            } else if(tipoProduto == 2){
                System.out.println("Digite a data de validade do produto (dd/MM/yyyy): ");
                String dataTexto = teclado.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataValidade = LocalDate.parse(dataTexto, formatter);
                ProdutoPerecivel novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade, quantidadeEmEstoque);
                produtosCadastrados[quantosProdutos] = novoProduto;
                quantosProdutos++;
                salvarProdutos(nomeArquivoDados);
            } else {
                System.out.println("Tipo de produto inválido. Produto não cadastrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(String.valueOf(quantosProdutos));
            writer.newLine();
            for (int i = 0; i < quantosProdutos; i++) {
                writer.write(produtosCadastrados[i].gerarDadosTexto());
                writer.newLine();
            }

            System.out.println("Produtos salvos com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}