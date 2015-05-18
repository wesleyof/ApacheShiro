package br.com.ufmt.apacheshiro.model.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Usuario {

    @PersistenceContext
    transient EntityManager entityManager;
    @NotNull
    private String nome;
	@NotNull
	@Column(unique=true)
    private String login;

	@NotNull
    private String senha;

    private String salt;

    @ManyToMany
    private Set<Papel> papeis = new HashSet<Papel>();

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
        EntityManager em = new Usuario().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countUsuarios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Usuario o", Long.class).getSingleResult();
    }

    public static List<Usuario> findAllUsuarios() {
        return entityManager().createQuery("SELECT o FROM Usuario o", Usuario.class).getResultList();
    }

    public static Usuario findUsuario(Long id) {
        if (id == null) return null;
        return entityManager().find(Usuario.class, id);
    }

    public static List<Usuario> findUsuarioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Usuario o", Usuario.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
	public String toString() {
		return nome + " (" + login + ")";
	}

    public void setSalt(String salt) {
        this.salt = salt;
    }    

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Papel> getPapeis() {
        return this.papeis;
    }

    public void setPapeis(Set<Papel> papeis) {
        this.papeis = papeis;
    }

    public Set<Permissao> getPermissoes() {
        return this.permissoes;
    }

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
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

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    public String getSalt() {
        return this.salt;
    }    

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Usuario attached = Usuario.findUsuario(this.id);
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
    public Usuario merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Usuario merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
