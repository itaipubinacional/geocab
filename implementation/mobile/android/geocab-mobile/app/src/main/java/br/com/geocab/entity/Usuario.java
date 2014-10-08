package br.com.geocab.entity;

/**
 * Created by Vinicius on 24/09/2014.
 */
public class Usuario {

    private String key;
    private String otherkey;

    public Usuario(String key, String otherkey) {
        this.key = key;
        this.otherkey = otherkey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOtherkey() {
        return otherkey;
    }

    public void setOtherkey(String otherkey) {
        this.otherkey = otherkey;
    }
}
