package org.example.exceptions;

public class ProdutoJaInseridoException extends RuntimeException{
  public ProdutoJaInseridoException(String message) {
        super(message);
    }
}
