package Portafolio_TiffanyPorras;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/ejemplo2").setViewName("ejemplo2");
        registry.addViewController("/multimedia").setViewName("multimedia");
        registry.addViewController("/iframes").setViewName("iframes");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/acceso_denegado").setViewName("acceso_denegado"); //Semana 9
        registry.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /* ===================== Semana 9: Spring Security ===================== */

    //Rutas a las que cualquiera puede entrar, aunque no haya hecho login
    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/fav/**", "/carrito/**", "/consultas/**", "/registro/**",
        "/js/**", "/webjars/**", "/login", "/acceso_denegado"
    };

    //Rutas solo para el rol ADMIN (crear, modificar, eliminar)
    public static final String[] ADMIN_URLS = {
        "/producto/nuevo", "/producto/guardar", "/producto/modificar/**", "/producto/eliminar/**",
        "/categoria/nuevo", "/categoria/guardar", "/categoria/modificar/**", "/categoria/eliminar/**",
        "/usuario/nuevo", "/usuario/guardar", "/usuario/modificar/**", "/usuario/eliminar/**"
    };

    //Rutas para ADMIN o VENDEDOR (solo ver los listados)
    public static final String[] ADMIN_OR_VENDEDOR_URLS = {
        "/producto/listado", "/categoria/listado", "/usuario/listado"
    };

    //Rutas para el rol USUARIO (cliente)
    public static final String[] USUARIO_URLS = {
        "/facturar/carrito"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(ADMIN_OR_VENDEDOR_URLS).hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(USUARIO_URLS).hasRole("USUARIO")
                .anyRequest().authenticated()
        ).formLogin(form -> form // Configuración de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout // Configuración de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Página cuando no tiene permiso
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Una sola sesión por usuario
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    //Encripta las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Usuarios de prueba en memoria (la profe lo reemplaza la siguiente semana por la base de datos)
    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("juan")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails sales = User.builder()
                .username("rebeca")
                .password(passwordEncoder.encode("456"))
                .roles("VENDEDOR")
                .build();

        UserDetails user = User.builder()
                .username("pedro")
                .password(passwordEncoder.encode("789"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, sales, user);
    }
}
