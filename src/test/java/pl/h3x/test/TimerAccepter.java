package pl.h3x.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class TimerAccepter implements ExecutionListener {

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		System.out.println("TimerAccepter.notify()");
		arg0.setVariable("IS_ACCEPTED",  new Integer(1));
	}

	
	
}
