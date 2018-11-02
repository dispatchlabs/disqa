package api.hertz

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class HertzRegression {
    def allNodes
    def delegates

    def wallet1
    def wallet2
    def wallet3

    @BeforeMethod(alwaysRun = true)
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 5,Seed: 1,Regular: 1

        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate2.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate3.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
    }

    @Test(description="Deploy contract",groups = ["smart contract"])
    public void Hertz_API111111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
    }

    @Test(description="Verify that wallet has same amount of hertz as tokens",groups = ["hertz"])
    public void SmartContract_API1h(){
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: 1000000
        /*println("First contract deploy")
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        println("Second contract deploy")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])*/
    }


}
