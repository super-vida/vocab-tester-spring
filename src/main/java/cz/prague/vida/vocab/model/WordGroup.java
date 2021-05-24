package cz.prague.vida.vocab.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The Class WordGroup.
 */
@Entity
@Table(name = "WORD_GROUP")
@Data
@EqualsAndHashCode(of = "id")
public class WordGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "WG_ID")
    private Long id;
    @Column(name = "WG_L_ID")
    private Long lessonId;
    @Column(name = "WG_W1_ID")
    private Long word1Id;
    @Column(name = "WG_W2_ID")
    private Long word2Id;


}
