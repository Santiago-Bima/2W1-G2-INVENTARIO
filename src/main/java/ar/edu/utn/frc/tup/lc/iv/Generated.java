package ar.edu.utn.frc.tup.lc.iv;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * anotacion para excluir metodos generados del coverage de la aplicacion.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, CONSTRUCTOR})
public @interface Generated {
  /*
   * Esta anotación se utilizará para evitar que
   * una clase o método sea tomada en cuenta en los reportes de Jacoco
   */
}
