/**
 *
 */
package br.gov.itaipu.geocab.infrastructure.file;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author thiago
 */
@Component
public class FileRepository {
    public File[] listFilePath(String path) {
        final File iconsFolder = new File(path);
        File[] listFiles = iconsFolder.listFiles();
        return listFiles;
    }

}
