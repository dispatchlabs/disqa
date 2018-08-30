package api.smartcontracts

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class MonkeyTest {
    def allNodes

    @Test(description="Do set values in contract until you stop it.",groups = ["monkey_test_contract"])
    public void test(){
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
        def wallets = []
        allNodes.Delegates.each {key,value->
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                    Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
            response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
            def contractAddress = response.then().extract().path("data.contractAddress")

            def wallet = new Wallet()
            wallet.allNodes = allNodes.Delegates
            wallet.contractAddress = contractAddress
            wallets << wallet
        }
        wallets.each {
            it.start()
        }
        int times = 1
        while (true){
            sleep(1*60*1000)
        }
    }

}

public class Wallet extends Thread  {
    def node
    def allNodes
    def contractAddress
    def paused = false

    public def pauseTrade(){
        paused = true
    }

    public def resumeTrade(){
        paused = false
    }

    private def randomNode(){
        Random       random    = new Random();
        List<String> keys      = new ArrayList<String>(allNodes.keySet());
        def       randomKey = keys.get( random.nextInt(keys.size()) );
        def       value     = allNodes.get(randomKey);
        return value
    }

    @Override
    public void run() {
        println "Starting test"
        Random random = new Random();
        while (true){
            if(paused == false)
            {
                try {
                    def toNode = randomNode()
                    def response = sendTransaction Node:toNode, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                            ABI: DefaultSampleContract.defaultSampleABI,
                            Method: "getVar5",Params: []
                    def getID = response.Hash
                    waitForTransactionStatus ID:getID ,Node:toNode, DataStatus: "Ok", Timeout: 10
                    verifyStatusForTransaction(Nodes:[toNode],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    println e.getMessage()
                    System.exit(0)
                }
                catch (Error e){
                    println e.getMessage()
                    System.exit(0)
                }
            }
            sleep(random.nextInt(1000))
        }
    }
}
