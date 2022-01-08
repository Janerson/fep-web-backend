package br.com.fepweb.controllers;

import br.com.fepweb.entity.ClientePJ;
import br.com.fepweb.services.ClientePJService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "Operações de CRUD Clientes PJ")
@RequestMapping("cliente")
public class ClientePJController extends BaseController<ClientePJ>{

    private ClientePJService service;

    @Autowired
    public ClientePJController(ClientePJService service) {
        super(service);
        this.service=service;
    }
}
