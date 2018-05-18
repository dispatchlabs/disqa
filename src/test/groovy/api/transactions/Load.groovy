package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.sendTransaction
import static com.dispatchlabs.io.testing.common.APIs.verifyConsensusForAccount
import static com.dispatchlabs.io.testing.common.APIs.waitForTransactionStatus

class Load {
    def allNodes

    @BeforeClass
    public void baseState(){
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @DataProvider(name = "SlowLoad")
    public static Object[][] credentials() {
        ArrayList<Object[]> data = [][]
        40.times {
            data.add([1,it+1])
        }
        return data as Object[][]
    }

    @Test(dataProvider = "SlowLoad")
    public void load1_slow(int amount,balance){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:amount, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok"
    }
}
