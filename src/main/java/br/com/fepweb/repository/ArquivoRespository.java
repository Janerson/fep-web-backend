package br.com.fepweb.repository;

import br.com.fepweb.entity.Arquivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoRespository extends BaseRespository<Arquivo,Long> {

    @Query("select a from  Arquivo a inner join a.clientePJ pj where pj.id=:id ")
    List<Arquivo> listarArquivosPorClienteID(@Param("id") long clienteID);

    @Query("Select a from  Arquivo a inner join a.clientePJ pj where pj.id=:id and a.nome like %:str%")
    Page<Arquivo> listarArquivosClienteID(Pageable pageable,@Param("id") long clienteID, @Param("str") String str);
}
