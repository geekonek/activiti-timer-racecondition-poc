package pl.h3x.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MultiActorsProcessSubrocessVer2WithTimerTest {

	private static final int COUNT = 10;
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources = "diagrams/MultiActorsProcessSubprocessVer2WithTimer.bpmn20.xml")
	public void startProcess() {
		System.out
				.println("MultiActorsProcessSubrocessVer2WithTimer.startProcess()");
		// prepare stuff and start process instance
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		ProcessEngine eng = ProcessEngines.getDefaultProcessEngine();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("helloworld", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());

		TaskService taskService = eng.getTaskService();

		List<Task> tasks = taskService.createTaskQuery()
				.taskCandidateUser("testUser").list();
		System.out.println("testUser -->> " + tasks);

		tasks = taskService.createTaskQuery().taskCandidateUser("testUser1")
				.list();
		System.out.println("testUser1 -->> " + tasks);

		tasks = taskService.createTaskQuery().taskCandidateUser("testUser2")
				.list();
		System.out.println("testUser2 -->> " + tasks);

		for (Task task : tasks) {
			System.out.println("Following task is available for testUser2: "
					+ task.getName());
			taskService.claim(task.getId(), "testUser2");
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("IS_ACCEPTED", new Integer(0));

		paramMap.put("COUNTER", new Integer(0));
		
		
		List<String> users = new ArrayList<String>();
		for( int i = 1; i <= COUNT; i++) {
			users.add("subTestUser" + i);
		}
		paramMap.put("users", users);
		
		
		tasks = taskService.createTaskQuery().taskAssignee("testUser2").list();
		for (Task task : tasks) {
			System.out.println("Task for testUser2: " + task.getName());
			taskService.complete(task.getId(), paramMap);
		}

		System.out.println("---------------------------------------");
		
		boolean flag = true;
		while(true) {
		
			for( int i = 1; i <= COUNT; i++) {
				tasks = taskService.createTaskQuery().taskCandidateUser("subTestUser" + i).list();
				System.out.println("1 :::: subTestUser" + i + " -->> " + tasks);
				if( tasks.size() == 0) {
					//for any user, just break loop
					flag = false;
				}
			}
			
			if (!flag) break;
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Assert.assertTrue("Look at output above, you should see 2 exceptions printed by activiti engine. (example in exceptions.txt).\n" +
				"This is some race condition, so it works or throws exceptions. Just run it few times.", false);
		
	}

}
