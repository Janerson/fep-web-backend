package br.com.fepweb.controllers;

import br.com.fepweb.entity.Arquivo;
import br.com.fepweb.services.ArquivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;


@RestController
@Api(value = "Operações de CRUD Arquivo")
@RequestMapping("arquivo")
public class ArquivoController extends BaseController<Arquivo> {

    private ArquivoService service;

    private static final String UPLOAD_MESSAGE = "{\"mensagem\":\"upload realizado com sucesso.\"}";


    @Autowired
    public ArquivoController(ArquivoService service) {
        super(service);
        this.service = service;
    }

    @PostMapping(path = "/upload/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(@RequestParam MultipartFile file,
                                 @PathVariable("clienteId") Long clienteId) {

        service.upload(file, clienteId);

        return new ResponseEntity(UPLOAD_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@NotBlank @PathVariable("id") long id) {
        return service.download(id, "form-data");
    }


    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Apagar", notes = "Apaga o registro correspondente ao id informado.")
    public void apagarPorId(@PathVariable("id") Long id) {
        service.excluir(id);
    }

}
