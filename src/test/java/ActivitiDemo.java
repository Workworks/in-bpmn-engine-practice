import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {
    /**
     * 部署流程定义 文件上传方式
     */
    @Test
    public void testDeployment() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // 2、得到RepositoryService实例
        RepositoryService repositoryService =
                processEngine.getRepositoryService();
        // 3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Leave.bpmn") // 添加bpmn资源
        //png资源命名是有规范的。Leave.[key].[png|jpg|gif|svg] 或者Leave.[png|jpg|gif|svg]
                .addClasspathResource("bpmn/Leave.myLeave.png") // 添加png资源
                .name("请假申请流程")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * zip压缩文件上传方式
     */
    @Test
    public void deployProcessByZip() {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "bpmn/Leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 获取repositoryService
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

}
