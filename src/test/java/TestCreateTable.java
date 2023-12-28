import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class TestCreateTable {

    /**
     * homework:
     *  1. 熟悉相对应类
     *      - RuntimeService
     *      - TaskService
     *      - HistoryService
     *      - ManagementService
     *      - ProcessEngine
     *  2. 熟悉相对应表
     */
    @Test
    public void testCreateDbTable() {
        //默认创建方式
        ProcessEngine processEngine =
                ProcessEngines.getDefaultProcessEngine();
        //通用的创建方式，指定配置文件名和Bean名称
        // ProcessEngineConfiguration processEngineConfiguration =
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
        // ProcessEngine processEngine1 = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }
}
