package br.com.ufmt.apacheshiro.model.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Papel {

    @PersistenceContext
    transient EntityManager entityManager;
    private String nome;

    @ManyToMany
    private Set<Permissao> permissoes = new HashSet<Permissao>();
    @Version
    @Column(name = "version")
    private Integer version;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    public static final EntityManager entityManager() {
        EntityManager em = new Papel().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countPapels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Papel o", Long.class).getSingleResult();
    }

    public static List<Papel> findAllPapels() {
        return entityManager().createQuery("SELECT o FROM Papel o", Papel.class).getResultList();
    }

    public static Papel findPapel(Long id) {
        if (id == null) return null;
        return entityManager().find(Papel.class, id);
    }

    public static List<Papel> findPapelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Papel o", Papel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
	public String toString() {
		return nome;
	}

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public Set<Permissao> getPermissoes() {
        return this.permissoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Papel attached = Papel.findPapel(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public Papel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Papel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
}
