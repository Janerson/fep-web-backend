package br.com.fepweb.repository;


import br.com.fepweb.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @param <T>  Classe de Persistência
 * @param <PK> Tipo de Chave Primária UUID, int, String...
 */
@NoRepositoryBean
public interface BaseRespository<T extends BaseEntity, PK> extends PagingAndSortingRepository<T, PK> {

	Page<T> findAll(Specification<T> spec, Pageable pageable);

}
