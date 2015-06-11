package br.com.ufmt.apacheshiro.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.ufmt.apacheshiro.model.bean.Usuario;
import br.com.ufmt.apacheshiro.service.UsuarioService;

/*Declaramos essa classe como um componente do Spring, para que possamos
  injetá-la depois pelo nome*/
@Component("usuarioCorrenteInterceptor")
public class UsuarioCorrenteInterceptor extends HandlerInterceptorAdapter {

    // Pedimos uma referência ao UsuarioService
	@Inject
	private UsuarioService usuarioService;
	
	@Override
    public void postHandle(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o, 
            ModelAndView modelAndView) throws Exception {
        // Obtemos o usuário corrente
        Usuario usuarioCorrente = usuarioService.getUsuarioCorrente();
        if( usuarioCorrente != null ) {
            /* Caso seja diferente de null, o colocamos como um atributo da 
               requisição. Se fosse um Filter, faríamos o mesmo. */
            httpServletRequest.setAttribute(UsuarioService.USUARIO_CORRENTE, 
                usuarioCorrente );
        }
    }
	
}
