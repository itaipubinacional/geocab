package br.gov.itaipu.geocab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lcvmelo on 16/02/2017.
 */
@SpringBootApplication
public class Application {

    @Value("${files.repository.path}")
    public void setFileRepositoryPath(String fileRepositoryPath) {
        /*
         * O Spring Boot não ajusta as variáveis de ambiente com base nas
         * propriedades contidas no arquivo application.properties. Logo,
         * para fazer a configuração do JCR funcionar corretamente, é
         * necessário ajustar a propriedade como variável do sistema.
         */
        System.setProperty("files.repository.path", fileRepositoryPath);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
