package br.com.fepweb.entity;

import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "CLIENTE_PJ")
@Data
public class ClientePJ extends BaseEntity{

    @NotBlank(message = "Nome obrigatório.")
    @Column(name = "NM_EMPRESA")
    private String nomeEmpresa;


    @CNPJ
    @NotBlank(message = "CNPJ obrigatório.")
    @Column(name = "CNPJ", unique = true)
    private String cnpj;

}
