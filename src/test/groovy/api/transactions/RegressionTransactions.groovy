package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class RegressionTransactions {
    def allNodes
    def delegates

    @BeforeMethod
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="All delegate tokens transferred",groups = ["smoke", "transactions"])
    public void transactionRegression1_AllDelegateTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:999, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 999
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 0
    }

    @Test(description="Transfer 0 tokens",groups = ["smoke", "transactions"])
    public void transactionRegression2_0DelegateTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 998
    }

    @Test(description="Transfer more than available tokens",groups = ["smoke", "transactions"])
    public void transactionRegression3_DelegateMoreThanAllTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:1000, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "InsufficientTokens", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
    }

    @Test(description="Transfer tokens from one delegate to another and then back again",groups = ["smoke", "transactions"])
    public void transactionRegression4_DelegateTokensBackandForth(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:500, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 501
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:501, PrivateKey:allNodes.Delegates.Delegate1.privateKey,
                To:allNodes.Delegates.Delegate0.address ,From: allNodes.Delegates.Delegate1.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 0
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 1000
    }

    @Test(description="Negative token transfer",groups = ["smoke", "transactions"])
    public void transactionRegression5_DelegateNegativeTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:-5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address, Status: "InvalidTransaction"
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
    }
    /*
    @Test(description="Decimal point tokens",groups = ["smoke", "transactions"])
    public void transactionRegression6_DelegateDecimalTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0.5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 998.5
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 0.5
    }*/

    @Test(description="Decimal point tokens",groups = ["smoke", "transactions"])
    public void transactionRegression7_DelegateNegativeTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0.5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 998.5
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 0.5
    }




}
