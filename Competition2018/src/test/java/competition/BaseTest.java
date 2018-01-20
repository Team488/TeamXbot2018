package competition;

import org.junit.Ignore;

import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;

@Ignore
public class BaseTest extends BaseWPITest {
    
    protected class TestModule extends UnitTestModule {
        @Override
        protected void configure() {
            super.configure();
        }
    }
    
    public BaseTest() {
        guiceModule = new TestModule();
    }
    
    @Override
    public void setUp() {
        super.setUp();
    }
}
