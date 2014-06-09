package eu.trentorise.game.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Luca Piras
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation = Propagation.REQUIRED, 
               isolation = Isolation.DEFAULT, 
               rollbackFor = {java.lang.Exception.class}, 
               readOnly = true)
public @interface TransactionalGameReadOnly {}