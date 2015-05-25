package br.com.geocab.util;

/**
 * Created by Joaz Vieira Soares on 11/05/2015.
 */
public interface DelegateHandler<T extends Object> {

    public void responseHandler(T result);

}
