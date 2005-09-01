package org.jboss.aspects.asynch;
import java.lang.annotation.*;

/**
 * Allow user to plug in executor that will run asynch tasks
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 1.1.8.3 $
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface AsynchExecutor
{
   Class value();
}