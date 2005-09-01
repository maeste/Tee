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
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    public static class RetryPolicy implements RejectedExecutionHandler {
        /**
         * Creates a <tt>DiscardPolicy</tt>.
         */
        public RetryPolicy() { }

        /**
         * Does nothing, which has the effect of discarding task r.
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//			while (e.getActiveCount() >= e.getMaximumPoolSize()) {
//				System.out.println("waiting");
//			}
//			e.remove(r);
//			e.execute(r);
        }
    }
  private static ExecutorService executor =  Executors.newFixedThreadPool(5); 
//  new ThreadPoolExecutor(0, 25,
//          															15L, TimeUnit.SECONDS,
//          															new SynchronousQueue<Runnable>(), new MyExecutor.RetryPolicy());

  public void setAdvisor(Advisor advisor)
  {

  }

  public RemotableFuture execute(MethodInvocation invocation) throws Exception
  {
     //System.out.println("MyExecutor");
	 final MethodInvocation copy = (MethodInvocation) invocation.copy();
     final ClassLoader cl = Thread.currentThread().getContextClassLoader();
     
     java.util.concurrent.Future future = executor.submit(new Callable()
     {
        public Object call() throws Exception
        {
           try
           {
              Thread.currentThread().setContextClassLoader(cl);
              return copy.invokeNext();
           }
           catch (Throwable throwable)
           {
              if (throwable instanceof Exception)
              {
                 throw ((Exception) throwable);
              }
              else
                 throw new Exception(throwable);
           }
        }
     });

     return new FutureImplJavaUtilConcurrent(future);
  }

}
