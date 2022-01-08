package br.com.fepweb.services;

import br.com.fepweb.dto.UploadDTO;
import br.com.fepweb.entity.Arquivo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class StorageService {


    private static final String ROOT_DIR = "arquivos";
    private static final String ARQUIVOS_DIR = "cliente/%s/%s";

    public UploadDTO salvar(long clienteID, MultipartFile arquivo) {
        Path diretorioPath = Paths.get(ROOT_DIR, String.format(ARQUIVOS_DIR, clienteID, LocalDate.now().getYear()));
        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());

        try {
            Files.createDirectories(diretorioPath);
            Files.write(arquivoPath, arquivo.getBytes());
            return UploadDTO.builder()
                    .location(arquivoPath.toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar arquivo, ".concat(e.getMessage()), e);
        }
    }

    public byte[] lerArquivo(Arquivo arquivo) {
        Path caminhoArquivo = Path.of(arquivo.getCaminho());
        if (!caminhoArquivo.toFile().exists()) {
            throw new RuntimeException("Arquivo físico " + caminhoArquivo + " não encontrado.");
        }
        try {
            return Files.readAllBytes(caminhoArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo " + caminhoArquivo + ".", e);
        }
    }

    public void remover(Arquivo arquivo) {
        File file = new File(arquivo.getCaminho());
        file.delete();
    }
}

