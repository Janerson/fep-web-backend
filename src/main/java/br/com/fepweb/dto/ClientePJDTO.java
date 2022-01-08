package br.com.fepweb.dto;

import br.com.fepweb.entity.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "CLIENTE_PJ")
@Data
public class ClientePJDTO extends BaseEntity {

    @NotBlank(message = "Nome obrigatório.")
    @Column(name = "NM_EMPRESA")
    private String nomeEmpresa;

    @CNPJ
    @Length(min = 14, max = 18)
    @NotBlank(message = "CNPJ obrigatório.")
    @Column(name = "CNPJ", length = 14)
    private String cnpj;


    public void setCnpj(String cnpj) {
        this.cnpj = cnpj.replace("\\D","");
    }
}
