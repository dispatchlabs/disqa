package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class MonkeyTest {
    private List ledger = Collections.synchronizedList(new ArrayList())
    def allNodes

    private calculateNodeAccountValues(){
        ledger.each {transaction->
            allNodes.Delegates."${transaction.NodeID}".balance += transaction.Amount
        }
    }

    private verifyAccounts(){
        allNodes.Delegates.each{nodeID,node->
            verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates."${nodeID}".address, Status: "Ok", Balance: node.balance
        }
    }

    private verifyTransactions(){
        def transactions = []
        allNodes.Delegates.each{nodeID,node->
            def page = 1
            while(true){
                RequestSpecification request = RestAssured.given()
                request.baseUri("http://"+allNodes.Delegates."${nodeID}".IP+":"+allNodes.Delegates."${nodeID}".HttpPort)
                Response response = request.get("/v1/transactions?page=$page")
                println page
                def getTrans = response.then().statusCode(200).extract().path("data")
                if(getTrans.size() == 0) break
                transactions.addAll(getTrans)
                page++
            }
        }
        ledger.each {transaction->
            assert transactions.find{it.hash == transaction.TransID} != null,"Error transaction with hash: ${transaction.TransID} was not found."
        }
    }

    @Test(description="Do trade between wallets at random intervals with random values until you stop it.",groups = ["load"])
    public void test(){
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
        /*RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://35.203.143.69:1975")
        Response responseDel = request.get("/v1/delegates")
        responseDel.then().log().all()
        def delegates = responseDel.then().extract().path("data")
        allNodes = [:]
        allNodes.Delegates = [:]
        delegates.eachWithIndex{delegate,index->
            allNodes.Delegates."Delegate$index" = [
                    "IP": delegate.httpEndpoint.host,
                    "HttpPort": delegate.httpEndpoint.port,
                    address:delegate.address
            ]
        }
        */

        def wallets = []
        allNodes.Delegates.each {key,value->
            //give each wallet enough tokens
            def response = sendTransaction Node:value, Value:1000000, PrivateKey:"Genesis",
                    To:value.address ,From: "Genesis"
            waitForTransactionStatus ID:response.Hash ,Node:value, DataStatus: "Ok", Timeout: 10

            def wallet = new Wallet()
            wallet.allNodes = allNodes.Delegates
            value.balance = 1000000
            value.nodeID = key
            wallet.node = value
            wallet.ledger = ledger
            wallets << wallet
        }
        wallets.each {
            it.start()
        }
        int times = 1
        while (true){
            sleep(2*60*1000)
            //pause all trading
            wallets.each {
                it.pauseTrade()
            }
            sleep(40*1000)
            println times
            //make sure ledgers are in sync
            calculateNodeAccountValues()
            verifyTransactions()
            verifyAccounts()

            ledger = Collections.synchronizedList(new ArrayList())
            //resume all trading
            wallets.each {
                it.ledger = ledger
                it.resumeTrade()
            }
            times++
        }
    }

}

public class Wallet extends Thread  {
    def node
    def allNodes
    def ledger
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
        if(node.address == value.address) {
            return randomNode()
        }
        else{
            return value
        }
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
                    def response = sendTransaction Node:node, Value:1, PrivateKey:node.privateKey,
                            To:toNode.address ,From: node.address
                    def transID = response.Hash
                    waitForTransactionStatus ID: transID,Node:node, DataStatus: "Ok", Timeout: 40
                    ledger.add([NodeID:node.nodeID,Amount:-1,TransID:transID])
                    ledger.add([NodeID:toNode.nodeID,Amount:1,TransID:transID])
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    println e.getMessage()
                    System.exit(1)
                }
                catch (Error e){
                    println e.getMessage()
                    System.exit(1)
                }
            }
            sleep(random.nextInt(1000))
        }
    }
}
