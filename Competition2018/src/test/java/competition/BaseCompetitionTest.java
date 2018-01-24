package competition;

import org.junit.Ignore;

import competition.operator_interface.OperatorInterface;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;

@Ignore
public class BaseCompetitionTest extends BaseWPITest {
    
    protected OperatorInterface oi;
    
    protected class TestModule extends UnitTestModule {
        @Override
        protected void configure() {
            super.configure();
        }
    }
    
    public BaseCompetitionTest() {
        guiceModule = new TestModule();
    }
    
    @Override
    public void setUp() {
        super.setUp();
        
        oi = injector.getInstance(OperatorInterface.class);
    }
}
