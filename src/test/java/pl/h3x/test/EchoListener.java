package pl.h3x.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class EchoListener implements ExecutionListener{

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		System.out.println("EchoListener.notify()");
		
	}

}
