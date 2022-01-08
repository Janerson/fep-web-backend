package br.com.fepweb.services;

import br.com.fepweb.dto.UploadDTO;
import br.com.fepweb.entity.Arquivo;
import br.com.fepweb.entity.ClientePJ;
import br.com.fepweb.exception.InfraException;
import br.com.fepweb.repository.ArquivoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

@Service
public class ArquivoService extends BaseService<Arquivo> {

    private final String[] MIME_TYPES = {
            "application/pdf",
            "image/jpeg",
            "image/png"
    };


    private final ArquivoRespository repository;

    @Autowired
    private ClientePJService clientePJService;
    @Autowired
    private StorageService storageService;

    public ArquivoService(ArquivoRespository repository) {
        super(repository, Arquivo.class);
        this.repository = repository;
    }

    public void upload(MultipartFile file, Long clienteId) {

        validarUpload(file);

        ClientePJ clientePJ = clientePJService.buscarPorID(clienteId);
        UploadDTO uploadDTO = storageService.salvar(clienteId,file);

        Arquivo arquivo = new Arquivo(file);
        arquivo.setClientePJ(clientePJ);
        arquivo.setCaminho(uploadDTO.getLocation());

        salvar(arquivo);
    }


    public ResponseEntity<byte[]> download(long id, String tipoDisposicao) {
        Arquivo arquivo = buscarPorID(id);

        ContentDisposition.Builder disposicao = ContentDisposition.builder(tipoDisposicao);
        disposicao.name(arquivo.getNome());
        disposicao.filename(arquivo.getNome());

        HttpHeaders cabecalhos = new HttpHeaders();
        cabecalhos.setContentType(MediaType.parseMediaType(arquivo.getTipoConteudo()));
        cabecalhos.setContentDisposition(disposicao.build());
        cabecalhos.setContentLength(arquivo.getTamanho());

        return new ResponseEntity<byte[]>(storageService.lerArquivo(arquivo), cabecalhos, HttpStatus.OK);
    }

    public void validarUpload(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new InfraException("Arquivo não recebido.");
        }

        if (!Arrays.stream(MIME_TYPES).anyMatch(m -> m.equalsIgnoreCase(file.getContentType()))) {
            throw new InfraException("Arquivo " + file.getContentType() + " não permitido.");
        }

    }

    public void excluir(Long id) {
        Arquivo arquivo = buscarPorID(id);
        storageService.remover(arquivo);
        apagarPorId(id);
    }
}
