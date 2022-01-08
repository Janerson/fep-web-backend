package br.com.fepweb.controllers;

import br.com.fepweb.entity.BaseEntity;
import br.com.fepweb.services.BaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BaseController<T extends BaseEntity> {

    private final BaseService<T> service;

    public BaseController(BaseService<T> service) {
        this.service = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Inserir", notes = "Insere um novo registro.")
    public ResponseEntity<T> gravar(@Valid @RequestBody T entidade) {
        T t = service.salvar(entidade);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(t.getId()).toUri();
        return ResponseEntity.created(uri).body(t);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Alterar", notes = "Altera o registo")
    public ResponseEntity<T> atualizar(@RequestBody T entidade) {
        return ResponseEntity.ok(service.atualizar(entidade));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Apagar", notes = "Apaga o registro correspondente ao id informado.")
    public void apagarPorId(@PathVariable("id") Long id) {
        service.apagarPorId(id);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar", notes = "Retorna o registro correspondente ao id informado.")
    public ResponseEntity<T> buscarPeloID(@PathVariable("id") Long id) {

        Optional<T> optionalT = Optional.ofNullable(service.buscarPorID(id));

        return optionalT.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping()
    @ApiOperation(value = "Listar", notes = "Lista dados com paginação.")
    public Page<T> paginado(@RequestParam(value = "search", defaultValue = "", required = false) String search, Pageable pageable) {
        return service.listarPaginadao(pageable, search);
    }

    @GetMapping(path = "/listar")
    @ApiOperation(value = "Listar", notes = "Lista dados.")
    public List<T> listar() {
        return service.listar();
    }

}
