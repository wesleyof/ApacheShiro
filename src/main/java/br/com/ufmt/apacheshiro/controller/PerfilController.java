package br.com.ufmt.apacheshiro.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import br.com.ufmt.apacheshiro.model.bean.Usuario;
import br.com.ufmt.apacheshiro.service.UsuarioService;

@RequestMapping("/perfil")
@Controller
public class PerfilController {

    // Requisitamos que o Spring injete o UsuarioService
    @Inject
    private UsuarioService usuarioService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(Usuario usuario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return createForm(uiModel);
        }
        uiModel.asMap().clear();
        
        //Apenas passamos o objeto Usuario para nosso método
        usuarioService.cadastre(usuario);
        // Redirecionamos para a página de login
        return "redirect:/perfil?formlogin";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Usuario());
        return "perfil/create";
    }
	
	@RequestMapping(value="/login", method = RequestMethod.POST, produces = "text/html")
    public String login(Usuario usuario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (bindingResult.hasErrors()) {
            return loginForm(uiModel);
        }
        uiModel.asMap().clear();
        
        try{
            // Criamos um token com o login e senha do usuário
            UsernamePasswordToken token = new UsernamePasswordToken
                (usuario.getLogin(), usuario.getSenha());
            // Chamamos o método de login com esse token
            SecurityUtils.getSubject().login(token);
            // Caso passe da linha acima, a autenticação foi feita com sucesso
            try {
                /* Aqui está como é feito o redirecionamento para o destino 
                    original. Repare que usamos o nome completo da classe 
                    porque já existe uma classe com o nome WebUtils do Spring
                    sendo usada nesse Controller. */
                org.apache.shiro.web.util.WebUtils.redirectToSavedRequest
                    (httpServletRequest, httpServletResponse, "/perfil");
            } catch (IOException e) {
                /*Caso aconteça algum erro para recuperar a requisição salva,
                    redireciona para a página de perfil*/
                return "redirect:/perfil";
            }
            /*É importante retornar null para informar o Spring MVC que já 
            cuidamos da resposta da requisição.*/
            return null; //tells Spring MVC you've handled the response, and not to render a view
        }
        catch(AuthenticationException e){
            e.printStackTrace();
            /* Caso aconteça algum problema na autenticação, como usuário
                e senha incorretos, apenas redirecionamos para a página de
                login */
            return loginForm(uiModel);
        }
    }
	
	@RequestMapping(params = "formlogin", produces = "text/html")
	public String loginForm(Model uiModel){
		populateLoginForm(uiModel, new Usuario());
		return "perfil/login";
	}
	
	@RequestMapping("/logout")
    public String logout() {
        /*Simplesmente chama o método de logout e
          redireciona para a página inicial.*/
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
	
	@RequestMapping("/nao_autorizado")
	public String naoAutorizado(){
		return "perfil/nao_autorizado";
	}

	@RequestMapping(produces = "text/html")
    @RequiresAuthentication
    public String show(Model uiModel) {
        // Indica qual JSPX o Spring MVC deverá mostrar
        return "perfil/show";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Usuario usuario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateLoginForm(uiModel, usuario);
            return "perfil/update";
        }
        uiModel.asMap().clear();
        usuario.merge();
        return "redirect:/perfil/" + encodeUrlPathSegment(usuario.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Usuario.findUsuario(id));
        return "perfil/update";
    }

	void populateEditForm(Model uiModel, Usuario usuario) {
        uiModel.addAttribute("usuario", usuario);
    }
	
	void populateLoginForm(Model uiModel, Usuario usuario) {
        uiModel.addAttribute("usuario", usuario);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
