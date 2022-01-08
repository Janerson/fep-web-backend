package br.com.fepweb.services;

import br.com.fepweb.entity.BaseEntity;
import br.com.fepweb.repository.BaseRespository;
import br.com.fepweb.repository.Specifications;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BaseService<T extends BaseEntity> {

    private final BaseRespository<T, Long> repository;
    private final Class<T> clazz;

    public BaseService(BaseRespository<T, Long> repository, Class<T> clazz) {
        this.repository = repository;
        this.clazz = clazz;
    }

    public T salvar(T t) {
        return repository.save(t);
    }

    public T buscarPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(this::notFound);
    }

    public T atualizar(T t) {
        T saved = buscarPorID(t.getId());
        BeanUtils.copyProperties(t, saved);
        return salvar(saved);
    }

    public void apagarPorId(Long id) {
        repository.deleteById(id);
    }

    public List<T> listar() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());

    }

    public Page<T> listarPaginadao(Pageable pageable, String searchTerm) {
        return repository.findAll(Specifications.allColumnsLike(searchTerm), pageable);
    }

    public NoSuchElementException notFound() {
        return notFound(String.format("%s não localizado(a).",
                clazz.getSimpleName().split("(?=\\p{Upper})")[0]));
    }

    public NoSuchElementException notFound(String msg) {
        return new NoSuchElementException(msg);
    }
}
