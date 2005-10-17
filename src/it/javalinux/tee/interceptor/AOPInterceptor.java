/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.annotations.TeeEvent;
import it.javalinux.tee.event.Event;
import it.javalinux.tee.misc.ServiceLocator;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@Aspect
public class AOPInterceptor
{
	//AND within(@it.javalinux.tee.annotations.TeeEvent)
   @Bind (pointcut="execution( * @it.javalinux.tee.annotations.TeeEvent->@it.javalinux.tee.annotations.TeeEventInterceptor(..)) OR " +
   		"execution( @it.javalinux.tee.annotations.TeeEvent->@it.javalinux.tee.annotations.TeeEventInterceptor(..)) ")
   public Object interceptorAdvice(Invocation invocation) throws Throwable
   {
      try
      {
		  Object obj = null;
		  if (invocation instanceof ConstructorInvocation) {
			  obj = invocation.invokeNext();
		  } else {
			  invocation.invokeNext();
			  obj = invocation.getTargetObject();
			  
		  }
		  //System.out.println(obj.toString());
		  String teeName = ((TeeEvent) invocation.getAdvisor().resolveAnnotation(TeeEvent.class)).TeeName();
		  this.intercept((Event) obj,teeName);
		  return obj;
	  } catch (Exception e) {
		  e.printStackTrace();
		  throw e;
	  } finally {
		  System.out.println(">>> Leaving AOPInterceptor.interceptorAdvice");
	  }
   }
//
//   @Bind (pointcut="execution(void POJO->method())")
//   public Object methodAdvice(MethodInvocation invocation) throws Throwable
//   {
//      try
//      {
//         System.out.println("<<< MyAdvice.methodAdvice accessing: " + invocation.getMethod().toString());
//         return invocation.invokeNext();
//      }
//      finally
//      {
//         System.out.println(">>> Leaving MyAdvice.methodAdvice");
//      }
//   }
//
//   @Bind (pointcut="set(int POJO->field)")
//   public Object fieldAdvice(FieldReadInvocation invocation) throws Throwable
//   {
//      try
//      {
//         System.out.println("<<< MyAspect.fieldAdvice writing to field: " + invocation.getField().getName());
//         return invocation.invokeNext();
//      }
//      finally
//      {
//         System.out.println(">>> Leaving MyAspect.fieldAdvice");
//      }
//   }
//
//   @Bind (pointcut="get(int POJO->field)")
//   public Object fieldAdvice(FieldWriteInvocation invocation) throws Throwable
//   {
//      try
//      {
//         System.out.println("<<< MyAspect.fieldAdvice reading field: " + invocation.getField().getName());
//         return invocation.invokeNext();
//      }
//      finally
//      {
//         System.out.println(">>> Leaving MyAspect.fieldAdvice");
//      }
//   }
   
   public void intercept(Event event, String teeName) {
	    RMIAdaptor rmiserver = null;
       try {
		   String jndiName = "it.javalinux:service="+teeName;
           Object[] parArray = {event};
           String[] signArray = {"it.javalinux.tee.event.Event"};
		   event.setInterceptionTimeMillis(new Long(System.currentTimeMillis()));
		   ServiceLocator.getInstance().callMBean(jndiName,"process",parArray, signArray);
       } catch (Exception e) {
           Logger.getLogger(this.getClass()).error("Error calling Tee service!");
           StringWriter sw = new StringWriter();
           e.printStackTrace(new PrintWriter(sw));
           Logger.getLogger(this.getClass()).error(sw.toString());
       }
	}
}
