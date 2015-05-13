package br.com.ufmt.apacheshiro.model.bean;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
@Configurable
public class Comentario {

    @PersistenceContext
    transient EntityManager entityManager;
    @ManyToOne
    private Usuario autor;

    @Size(min = 15, max = 255)
    private String texto;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Version
    @Column(name = "version")
    private Integer version;

    public static final EntityManager entityManager() {
        EntityManager em = new Comentario().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countComentarios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Comentario o", Long.class).getSingleResult();
    }

    public static List<Comentario> findAllComentarios() {
        return entityManager().createQuery("SELECT o FROM Comentario o", Comentario.class).getResultList();
    }

    public static Comentario findComentario(Long id) {
        if (id == null) return null;
        return entityManager().find(Comentario.class, id);
    }

    public static List<Comentario> findComentarioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Comentario o", Comentario.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return this.texto;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Usuario getAutor() {
        return this.autor;
    }

    @Transactional
    public Comentario merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Comentario merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Comentario attached = Comentario.findComentario(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
