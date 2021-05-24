package cz.prague.vida.vocab.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The Class Word.
 */
@Entity
@Table(name = "CONFIG")
@Data
@EqualsAndHashCode(of = "id")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "C_ID")
    private Long id;
    @Column(name = "C_TYPE")
    private String type;
    @Column(name = "C_NAME")
    private String name;
    @Column(name = "C_VALUE")
    private String value;

}
