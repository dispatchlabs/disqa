package api.transactionsByFrom

import com.dispatchlabs.io.testing.common.NodeSetup
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class RegressionTransactionsByFrom {
    def allNodes
    def delegates

    @BeforeMethod(alwaysRun = true)
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="Check a token transaction by from",groups = ["smoke", "transactions by from"])
    public void transactionsByFrom_API83(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10

        verifyTransactionsByFrom Node:allNodes.Delegates.Delegate0, Address: allNodes.Delegates.Delegate0.address
    }

}
