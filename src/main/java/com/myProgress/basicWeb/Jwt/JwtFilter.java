package com.myProgress.basicWeb.Jwt;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myProgress.basicWeb.Services.CustomUserDetailsService;
import com.myProgress.basicWeb.Services.Token.ITokenUtilService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    private Log logging = LogFactory.getLog(JwtFilter.class);

    @Autowired
    private ITokenUtilService _tokenUtilService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        //get the authorization header
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        //checks if the header contains "Bearer "
        if(requestHeader != null && requestHeader.startsWith("Bearer ")){

            try{

                token = requestHeader.substring(7, requestHeader.length());
                username = _tokenUtilService.getUsernameFromToken(token);
            }
            catch(MalformedJwtException e){

                logging.warn("Invalid JWT token: " + e.getMessage());
                throw new MalformedJwtException("Invalid JWT token: " + e.getMessage());
            }
            catch(ExpiredJwtException e){

                String isRefreshToken = request.getHeader("isRefreshToken");
                String requestUrl = request.getRequestURL().toString();

                if(isRefreshToken != null && isRefreshToken.equals("true") && requestUrl.contains("refreshToken")){

                    allowForRefreshToken(e, request);
                }
                else{
                    throw new ExpiredJwtException(null, null, "token expired", e);
                }
                
            }
            catch(UnsupportedJwtException e){

                logging.warn("JWT token is unsupported: " + e.getMessage());

                throw new UnsupportedJwtException("JWT token is unsupported: " + e.getMessage());
            }
        }
        else{
            logging.warn("the token doesn't start with bearer");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(_tokenUtilService.validateToken(token, userDetails)){

                // creating a UsernamePasswordAuthenticationToken with userDetails values.
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        
        filterChain.doFilter(request, response);
    }

    private void allowForRefreshToken(ExpiredJwtException e, HttpServletRequest request) {

        // creating a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            null, null, null);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);

         // Set the claims so services will be using it to create new JWT
        request.setAttribute("claims", e.getClaims());
    }
    
}
