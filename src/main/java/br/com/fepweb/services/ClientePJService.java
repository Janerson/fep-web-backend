package br.com.fepweb.services;

import br.com.fepweb.entity.ClientePJ;
import br.com.fepweb.repository.ClientePJRespository;
import org.springframework.stereotype.Service;

@Service
public class ClientePJService extends BaseService<ClientePJ>{

    private final ClientePJRespository repository;


    public ClientePJService(ClientePJRespository repository) {
        super(repository, ClientePJ.class);
        this.repository = repository;
    }

    public void excluirCliente(Long id) {

        apagarPorId(id);
    }
}
