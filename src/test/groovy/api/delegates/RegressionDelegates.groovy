package api.delegates

import com.dispatchlabs.io.testing.common.NodeSetup
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.verifyDelegates

class RegressionDelegates {
    def allNodes
    def delegates

    @BeforeMethod(alwaysRun = true)
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="Get the initial delegates that were spawned by base state",groups = ["smoke", "delegates"])
    public void delegates_API75(){
        sleep(5000)
        verifyDelegates Node:allNodes.Delegates.Delegate0, Delegates:allNodes.Delegates
    }

    @Test(description="Get the initial delegates that were spawned by base state",groups = ["smoke", "delegates"])
    public void delegates_API112(){
        sleep(5000)
        verifyDelegates Node:allNodes.Seeds.Seed0, Delegates:allNodes.Delegates
    }
}
