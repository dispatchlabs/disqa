package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
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

    @Test(description="Do trade between wallets at random intervals with random values until you stop it.",groups = ["load"])
    public void test(){
        allNodes = NodeSetup.quickSetup Delegate: 21,Seed: 1,Regular: 0
        def wallets = []
        allNodes.Delegates.each {key,value->
            //give each wallet enough tokens
            def response = sendTransaction Node:value, Value:1000000, PrivateKey:"Genesis",
                    To:value.address ,From: "Genesis"
            waitForTransactionStatus ID:response.then().extract().path("id") ,Node:value, Status: "Ok", Timeout: 10

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
            sleep(120000)
            println times
            //make sure ledgers are in sync
            calculateNodeAccountValues()
            ledger = Collections.synchronizedList(new ArrayList())
            verifyAccounts()
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
                    waitForTransactionStatus ID:response.then().extract().path("id") ,Node:node, Status: "Ok", Timeout: 10
                    ledger.add([NodeID:node.nodeID,Amount:-1])
                    ledger.add([NodeID:toNode.nodeID,Amount:1])
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
