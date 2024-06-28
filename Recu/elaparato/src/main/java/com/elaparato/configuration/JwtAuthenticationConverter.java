package com.elaparato.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter(); // Convierte JWT en `GrantedAuthority`.

    private final JwtAuthConverterProperties properties; // Propiedades personalizadas para la conversión de JWT.

    public JwtAuthenticationConverter(JwtAuthConverterProperties properties) { // Constructor que inyecta las propiedades.
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) { // Método que convierte JWT en `AbstractAuthenticationToken`.
        Collection<GrantedAuthority> authorities = Stream.concat( // Combina las autoridades extraídas del JWT.
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt)); // Crea un nuevo `JwtAuthenticationToken`.
    }

    private String getPrincipalClaimName(Jwt jwt) { // Obtiene el nombre de la reclamación que representa al principal.
        String claimName = JwtClaimNames.SUB; // Por defecto usa 'sub' como reclamación del principal.
        if (properties.getPrincipalAttribute() != null) { // Si está configurado un atributo principal personalizado, lo usa.
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName); // Retorna el valor de la reclamación principal.
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) { // Extrae roles de acceso a recursos del JWT.
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access"); // Accede a la reclamación de acceso a recursos.
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of(); // Si no se encuentran roles, retorna un conjunto vacío.
        }
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Convierte cada rol en un `SimpleGrantedAuthority`.
                .collect(Collectors.toSet());
    }

}
