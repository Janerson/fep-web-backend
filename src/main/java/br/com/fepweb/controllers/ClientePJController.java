package br.com.fepweb.controllers;

import br.com.fepweb.entity.ClientePJ;
import br.com.fepweb.services.ArquivoService;
import br.com.fepweb.services.ClientePJService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "Operações de CRUD Clientes PJ")
@RequestMapping("cliente")
public class ClientePJController extends BaseController<ClientePJ>{

    private ClientePJService service;

    @Autowired
    private ArquivoService arquivoService;

    @Autowired
    public ClientePJController(ClientePJService service) {
        super(service);
        this.service=service;
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Apagar", notes = "Apaga o registro correspondente ao id informado.")
    public void apagarPorId(@PathVariable("id") Long id) {
        arquivoService.excluirArquivosCliente(id);
        service.excluirCliente(id);
    }
}
