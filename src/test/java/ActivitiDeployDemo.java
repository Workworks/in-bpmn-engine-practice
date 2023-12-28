import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ActivitiDeployDemo {
    /**
     * 部署流程定义 文件上传方式
     * 表变更：
     *      1. act_re_deployment 流程定义部署表
     *      2. act_re_procdef 流程定义表
     *      3. act_ge_bytearray 流程资源表
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

    //TODO 后续集成到项目中，步骤：流程定义 -> 上传文件 -> 启动流程
    @Test
    public void queryBpmnFile() throws IOException {
        // 1、得到引擎
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        // 2、获取repositoryService
        RepositoryService repositoryService =
                processEngine.getRepositoryService();
        // 3、得到查询器：ProcessDefinitionQuery，设置查询条件,得到想要的流程定义
        ProcessDefinition processDefinition =
                repositoryService.createProcessDefinitionQuery()
                        .processDefinitionKey("myLeave")
                        .singleResult();
        // 4、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();
        // 5、通过repositoryService的方法，实现读取图片信息和bpmn信息
        // png图片的流
        InputStream pngInput =
                repositoryService.getResourceAsStream(deploymentId,
                        processDefinition.getDiagramResourceName());
        // bpmn文件的流
        InputStream bpmnInput =
                repositoryService.getResourceAsStream(deploymentId,
                        processDefinition.getResourceName());
        // 6、构造OutputStream流
        File file_png = new File("d:/myLeave.png");
        File file_bpmn = new File("d:/myLeave.bpmn");
        FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
        FileOutputStream pngOut = new FileOutputStream(file_png);
        // 7、输入流，输出流的转换
        IOUtils.copy(pngInput,pngOut);
        IOUtils.copy(bpmnInput,bpmnOut);
        // 8、关闭流
        pngOut.close();
        bpmnOut.close();
        pngInput.close();
        bpmnInput.close();
    }

}
