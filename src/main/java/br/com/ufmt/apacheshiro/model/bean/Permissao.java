package br.com.ufmt.apacheshiro.model.bean;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Permissao {

    @PersistenceContext
    transient EntityManager entityManager;
    @NotNull
    private String nome;
    @NotNull
    private String dominios;
    @NotNull
    private String operacoes;
    @Version
    @Column(name = "version")
    private Integer version;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    public static final EntityManager entityManager() {
        EntityManager em = new Permissao().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countPermissaos() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Permissao o", Long.class).getSingleResult();
    }

    public static List<Permissao> findAllPermissaos() {
        return entityManager().createQuery("SELECT o FROM Permissao o", Permissao.class).getResultList();
    }

    public static Permissao findPermissao(Long id) {
        if (id == null) return null;
        return entityManager().find(Permissao.class, id);
    }

    public static List<Permissao> findPermissaoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Permissao o", Permissao.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public Set<String> getDominiosSet(){
    	if(dominios == null){
    		return Collections.emptySet();
    	}
    	return new HashSet<String>(Arrays.asList(dominios.split(",")));
    }
    
    public void setDominiosSet(Set<String> dominiosSet){
    	StringBuilder stringBuilder = new StringBuilder();
		for(String dominio : dominiosSet){
			stringBuilder.append(dominio.toString());
			stringBuilder.append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		dominios = stringBuilder.toString();
    }
    
    public Set<String> getOperacoesSet(){
    	if(operacoes == null){
    		return Collections.emptySet();
    	}
    	return new HashSet<String>(Arrays.asList(operacoes.split(",")));
    }
    
    public void setOperacoesSet(Set<String> operacoesSet){
    	StringBuilder stringBuilder = new StringBuilder();
		for(String operacao : operacoesSet){
			stringBuilder.append(operacao.toString());
			stringBuilder.append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		operacoes = stringBuilder.toString();
    }
    
	@Override
	public String toString() {
		return nome;
	}

    public void setOperacoes(String operacoes) {
        this.operacoes = operacoes;
    }

    public String getOperacoes() {
        return this.operacoes;
    }

    public void setDominios(String dominios) {
        this.dominios = dominios;
    }

    public String getDominios() {
        return this.dominios;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
            Permissao attached = Permissao.findPermissao(this.id);
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
    public Permissao merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Permissao merged = this.entityManager.merge(this);
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
