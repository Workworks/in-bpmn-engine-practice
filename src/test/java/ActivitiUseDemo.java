import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiUseDemo {

    /**
     * 启动流程实例
     * 变更表：
     *  1. act_hi_actinst 流程实例执行历史
     *  2. act_hi_identitylink 流程的参与用户历史信息
     *  3. act_hi_procinst 流程实例历史信息
     *  4. act_hi_taskinst 流程任务历史信息
     *  5. act_ru_execution 流程执行信息
     *  6. act_ru_identitylink 流程的参与用户信息
     *  7. act_ru_task 任务信息
     */
    @Test
    public void testStartProcess(){
        // 1、创建ProcessEngine
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myLeave");
        // 输出内容
        System.out.println("流程定义id：" +
                processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        // 任务负责人
        String assignee = "worker";
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myLeave") //流程Key
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 完成任务
     * 这个任务完成后，这一个请假流程就推动到了下一个步骤，部门经理审批了。后续可以用不同的用户来推动流程结束。
     * 其实在完成审批任务的过程中，可以针对这个taskId，进行其他一些补充操作。例如添加Comment，添加附件，添加子任务，添加候选负责人等等。具体可以看下taskService的API。
     */
    @Test
    public void completTask(){
        // 获取引擎
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myLeave") //流程Key
                .taskAssignee("worker") //要查询的负责人
                .singleResult();
        // 完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        // 获取引擎
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // repositoryService
        RepositoryService repositoryService =
                processEngine.getRepositoryService();
        // 得到ProcessDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery =
                repositoryService.createProcessDefinitionQuery();
        // 查询出当前所有的流程定义
        // 条件：processDefinitionKey =evection
        // orderByProcessDefinitionVersion 按照版本排序
        // desc倒叙
        // list 返回集合
        List<ProcessDefinition> definitionList =
                processDefinitionQuery.processDefinitionKey("myLeave")
                        .orderByProcessDefinitionVersion()
                        .desc()
                        .list();
        // 输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    @Test
    public void deleteDeployment() {
        // 流程部署id
        String deploymentId = "1";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错(启动时删除会报错， 正常现象)
//        repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级连删除方式（项目开发中，级联删除操作一般只开放给管理员使用）
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
