package br.com.fepweb.repository;

import br.com.fepweb.entity.Arquivo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoRespository extends BaseRespository<Arquivo,Long> {

    @Query("from  Arquivo a inner join a.clientePJ pj where pj.id=:id ")
    List<Arquivo> listarArquivosPorClienteID(@Param("id") long clienteID);
}
