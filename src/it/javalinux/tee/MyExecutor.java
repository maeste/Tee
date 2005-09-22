/*

* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package it.javalinux.tee;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jboss.aop.Advisor;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aspects.asynch.ExecutorAbstraction;
import org.jboss.aspects.asynch.FutureImplJavaUtilConcurrent;
import org.jboss.aspects.asynch.RemotableFuture;

/**
* Comment
*
* @author <a href="mailto:bill@jboss.org">Bill Burke</a>
* @version $Revision: 1.5.2.4 $
*/
public class MyExecutor implements ExecutorAbstraction
{
	private static int NUMBER_OF_THREAD = 20;
  private static ExecutorService executor =  Executors.newFixedThreadPool(NUMBER_OF_THREAD);
  private Object s = new Integer(1);

  public void setAdvisor(Advisor advisor)
  {

  }

  public RemotableFuture execute(MethodInvocation invocation) throws Exception
  {
     final MethodInvocation copy = (MethodInvocation) invocation.copy();
     final ClassLoader cl = Thread.currentThread().getContextClassLoader();
     synchronized (s) {
		 while (((ThreadPoolExecutor) executor).getActiveCount()>= NUMBER_OF_THREAD){
			 s.wait();
			 
	     }
	 }
     java.util.concurrent.Future future = executor.submit(new Callable()
     {
        public Object call() throws Exception
        {
           try
           {
			  System.out.println("***" + Thread.currentThread().getName());
              Thread.currentThread().setContextClassLoader(cl);
              Object ritorno = copy.invokeNext();
			  return ritorno;
           }
           catch (Throwable throwable)
           {
              if (throwable instanceof Exception)
              {
                 throw ((Exception) throwable);
              }
              else
                 throw new Exception(throwable);
           }finally {
			   synchronized (s) {
				   s.notifyAll();
			   }
           }
        }
     });
	 return new FutureImplJavaUtilConcurrent(future);
  }

}
