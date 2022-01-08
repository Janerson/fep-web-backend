package br.com.fepweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ARQUIVO")
public class Arquivo extends BaseEntity {

    @Column(name = "NOME", nullable = false, updatable = false)
    private String nome;

    @Column(name = "EXTENSAO", nullable = true, updatable = false, length = 10)
    private String extensao;

    @Column(name = "CONTENT_TYPE", nullable = false, updatable = false, length = 100)
    private String tipoConteudo;

    @Column(name = "TAMANHO", nullable = false, updatable = false)
    private Long tamanho;

    @Column(name = "PATH", nullable = false, updatable = false)
    private String caminho;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENTE_PJ_ID", referencedColumnName = "ID",  foreignKey = @ForeignKey(name = "FK_CLIENTE_PJ"))
    private ClientePJ clientePJ;


    public Arquivo(MultipartFile multipartFile) {
        String nomeTemp = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (nomeTemp.contains(".")) {
            int indexUltimoPonto = nomeTemp.lastIndexOf('.');
            this.nome = nomeTemp.substring(0, indexUltimoPonto);
            if (indexUltimoPonto + 1 < nomeTemp.length())
                this.extensao = nomeTemp.substring(indexUltimoPonto + 1);
        } else {
            this.nome = nomeTemp;
        }
        this.tipoConteudo = multipartFile.getContentType();
        this.tamanho = multipartFile.getSize();
    }
}
