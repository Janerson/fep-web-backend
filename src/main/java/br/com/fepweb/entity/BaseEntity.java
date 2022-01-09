package br.com.fepweb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "DATA_CADASTRO",nullable = false, updatable = false)
    private LocalDate dataCadastro = LocalDate.now();


}
