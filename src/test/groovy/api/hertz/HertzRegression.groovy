package api.hertz

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.contracts.ComplexContract
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
        allNodes = NodeSetup.quickSetup Delegate: 5,Seed: 1,Regular: 0
        wallet1 = createWallet()
        wallet2 = createWallet()
        wallet3 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:10000000, PrivateKey:"Genesis",
                To:wallet1.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:10000000, PrivateKey:"Genesis",
                To:wallet1.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
    }

    @Test(description="Deploy contract",groups = ["smoke", "smart contract"])
    public void Hertz_API111111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
    }

    @Test(description="Get contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API2(){
        def wallet1 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:wallet1.Address,To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        //def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
        //        Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Contract return types: integer",groups = ["smart contract"])
    public void SmartContract_API6(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnInt",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[20])
    }

    @Test(description="Contract return types: uint",groups = ["smart contract"])
    public void SmartContract_API9(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnUint",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[20])
    }
}
