package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class BasicTransactions {
    def allNodes

    @BeforeMethod
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test
    //Send basic genesis transaction
    //Verify consensus on all nodes
    public void basicTest(){
        def response = sendTransaction Node:allNodes.Delegate0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegate0.walletID ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegate0, Status: "OK", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes, ID:allNodes.Delegate0.walletID, Status: "OK", Balance: 999
    }
}
