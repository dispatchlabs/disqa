package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.Utils
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class BasicTransactions {
    def allNodes
    def delegates

    @BeforeMethod
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="Genesis to Delegate transaction and verify consensus on all nodes",groups = ["smoke", "transactions"])
    public void transactions1_GenesisToDelegate(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
    }

    @Test(description="Genesis to new Account transaction and verify consensus on all nodes",groups = ["smoke", "transactions"])
    public void transactions2_GenesisToAccount(){
        def newAccount = Utils.createAccount()

        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:newAccount.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:newAccount.address, Status: "Ok", Balance: 999
    }

    @Test(description="Delegate to Delegate transaction",groups = ["smoke", "transactions"])
    public void transactions3_DelegateToDelegate(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 15
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 984
    }

    @Test(description="Get the initial delegates that were spawned by base state",groups = ["smoke", "delegates"])
    public void delegates1_VerifyDelegates(){
        verifyDelegates Node:allNodes.Delegates.Delegate0, Delegates:allNodes.Delegates
    }

    @Test(description="Get the initial delegates that were spawned by base state",groups = ["smoke", "delegates"])
    public void transactionsByFrom1_VerifyFromTransaction(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10

        verifyTransactionsByFrom Node:allNodes.Delegates.Delegate0, Address: allNodes.Delegates.Delegate0.address
    }

    @Test(description="Get the initial delegates that were spawned by base state",groups = ["smoke", "delegates"])
    public void transactionsByTo1_VerifyToTransaction(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10

        verifyTransactionsByTo Node:allNodes.Delegates.Delegate1, Address: allNodes.Delegates.Delegate1.address
    }
}
