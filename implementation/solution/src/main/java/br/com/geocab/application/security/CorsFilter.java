package br.com.geocab.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * @author rodrigo
 */
public class CorsFilter extends OncePerRequestFilter 
{
    private static final String ORIGIN = "Origin";

    /**
     * 
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException 
    {
        if ( request.getHeader(ORIGIN) != null && !request.getHeader(ORIGIN).equals("null") ) 
        {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Max-Age", "30");
            
            final String requestHeaders = request.getHeader("Access-Control-Request-Headers");
            if ( requestHeaders != null && !requestHeaders.isEmpty() ) 
            {
                response.addHeader("Access-Control-Allow-Headers", requestHeaders);
            }
        }
        
        if ( request.getMethod().equals("OPTIONS") ) 
        {
            try 
            {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } 
            catch (IOException e) 
            {
            	e.printStackTrace();
            }
        } 
        else
        {
            filterChain.doFilter(request, response);
        }
    }
 }