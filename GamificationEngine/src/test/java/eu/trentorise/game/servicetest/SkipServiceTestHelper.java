package eu.trentorise.game.servicetest;

import org.junit.Assume;
import org.junit.Before;

/**
 *
 * @author Luca Piras
 */
public class SkipServiceTestHelper {
    
    protected String testClassName;

    public SkipServiceTestHelper(String testClassName) {
        this.testClassName = testClassName;
    }
    
    @Before
    public void beforeMethod() {
        //An assumption failure causes the test to be ignored.
        Assume.assumeTrue(RestTemplateJsonServiceTestHelper.SERVICE_TEST_NOT_EXECUTED + this.testClassName, 
                          RestTemplateJsonServiceTestHelper.SERVICE_TEST_ACTIVATED);
        // rest of setup.
    }
}