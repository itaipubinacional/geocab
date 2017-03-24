package br.gov.itaipu.geocab.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe que realiza o tratamento de todas as exceções lançadas pelo sistema e que
 * não são tratadas. A função desta é encapsular a mensagem da exceção em um objeto
 * JSOB que será retornado para o frontend na resposta da requisição com o código
 * <code>HTTP 500 (INTERNAL_SERVER_ERROR)</code>.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Classe que representa o objeto JSON a ser retornado com a mensagem de erro
     * para o frontend.
     */
    private static class BackendError {
        /**
         * A mensagem de erro.
         */
        public String message;

        /**
         * Construtor da classe.
         *
         * @param message Mensagem a ser retornada para o frontend.
         */
        public BackendError(String message) {
            this.message = message;
        }
    }

    /**
     * Função que faz o tratamento das exceções que não foram tratadas pelo sistema.
     *
     * @param e O objeto da exceção a ser tratada.
     * @return O objeto a ser retornado para o frontend com a mensagem contida na
     * exceção.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public
    @ResponseBody
    BackendError handleException(Exception e) {
        return new BackendError(e.getMessage());
    }
}
