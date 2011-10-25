package pl.h3x.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class AcceptanceChecnkingListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		
		Integer flag = (Integer)arg0.getVariable("COUNTER");
		System.out.println("COUNTER---in listener------------>  " + flag);
		//arg0.setVariable("COUNTER", new Integer(1));
	}

	
}
