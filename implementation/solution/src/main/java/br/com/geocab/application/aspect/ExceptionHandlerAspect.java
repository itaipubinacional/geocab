package br.com.geocab.application.aspect;

import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.AccessDeniedException;

/**
 * 
 * @author Rodrigo P. Fraga
 * @since 20/09/2013
 * @version 1.0
 * @category
 */
@Aspect
public class ExceptionHandlerAspect
{
	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger( ExceptionHandlerAspect.class.getName() );
	
	/**
	 * 
	 */
	@Autowired
    private MessageSource messageSource;
	
	//---------
	// Database
	//---------
	/**
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "exception")
	public void handleException( JoinPoint joinPoint, org.springframework.dao.DuplicateKeyException exception )
	{
		throw new DuplicateKeyException( messageSource.getMessage("dao.duplicatedKey", null, LocaleContextHolder.getLocale()) );
	}
	
	/**
	 * Trata exceções geradas pelo Hibernate antes de enviar para o banco
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "exception")
	public void handleException( JoinPoint joinPoint, javax.validation.ConstraintViolationException exception )
	{
		final StringBuilder message = new StringBuilder();
		for ( ConstraintViolation<?> constraint : exception.getConstraintViolations() )
		{
			String annotationType = constraint.getConstraintDescriptor().getAnnotation().annotationType().getName();
			
			//Verifica o tipo da exceção
			if ( annotationType.equals( "javax.validation.constraints.NotNull" ) || annotationType.equals( "org.hibernate.validator.constraints.NotEmpty" ))
			{
				message.append("\nThe field " + constraint.getPropertyPath() + " must be set.");//FIXME Localize
			} else
			{
				message.append("\n" + constraint.getMessage());
			}
		}
		
		throw new ValidationException( message.toString() );
	}
	
	
	/**
	 * 
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "exception")
	public void handleException( JoinPoint joinPoint, org.springframework.dao.EmptyResultDataAccessException exception )
	{
		throw new EmptyResultDataAccessException( messageSource.getMessage("dao.emptyResult", null, LocaleContextHolder.getLocale()) , 1 );
	}
	
	/**
	 * Trata exceções de Constraint geradas pelo PostgreSQL 
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "exception")
	public void handleException( JoinPoint joinPoint, org.springframework.dao.DataIntegrityViolationException exception )
	{
		//Caso a exceção já tenha sido interceptada por outro Aspecto deve ser ignorada
		if ( exception.getStackTrace()[0].toString().contains( "ExceptionHandlerAspect" ) ) 
		{
			return;
		}

		if ( exception.getCause() instanceof ConstraintViolationException )
		{
			final ConstraintViolationException cause = (ConstraintViolationException) exception.getCause();
			final PSQLException sqlException = (PSQLException) cause.getSQLException();

			final String message = sqlException.getServerErrorMessage().getDetail();
			
			String key;
			//Verifica o código do erro gerado pelo PostgreSQL
			switch ( cause.getSQLState() )
			{
				case "23503": // foreign_key_violation 
				{
					key = message.substring(message.indexOf('"') + 1, message.indexOf('.') - 1);
					throw new DataIntegrityViolationException("Não é possível realizar a operação pois este registro é referenciado em " + key + ".");//FIXME Localize
				}
				case "23505": // unique_violation
				{
					key = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
					throw new DataIntegrityViolationException("O campo "+key+" informado já existe. Altere e tente novamente."); //FIXME Localize
				}
				case "23502": // not_null_violation
				{
					LOG.info(message);
					LOG.info("Not null violation.");
				}
				default:
				{
					throw new DataIntegrityViolationException("Não foi possível realizar a operação pois ocorreu um problema de integridade (Código " + cause.getSQLState() + "). Verifique e tente novamente."); //FIXME Localize
				}
			}
		}

		throw new DataIntegrityViolationException( messageSource.getMessage("dao.dataIntegrityViolation", null, LocaleContextHolder.getLocale()) );
	}
	
	//---------
	// Segurança
	//---------
	/**
	 * Trata exceção de acesso negado
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "exception")
	public void handleException( JoinPoint joinPoint, org.springframework.security.access.AccessDeniedException exception )
	{
		throw new AccessDeniedException( messageSource.getMessage("security.accessDenied", null, LocaleContextHolder.getLocale()) );
	}
}
