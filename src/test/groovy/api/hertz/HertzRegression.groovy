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
        allNodes = NodeSetup.quickSetup Delegate: 5,Seed: 1,Regular: 1

//        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
//                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
//        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
//        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
//                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
//        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
//        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
//                To:allNodes.Delegates.Delegate2.address ,From: "Genesis"
//        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
//        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
//                To:allNodes.Delegates.Delegate3.address ,From: "Genesis"
//        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
    }

    @Test(description="Deploy contract",groups = ["smart contract"])
    public void Hertz_API111111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
    }

    @Test(description="Verify that wallet has same amount of hertz as tokens",groups = ["hertz"])
    public void HertzTransaction_API1h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:900000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        sleep(21000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "900000000"
    }

    @Test(description="Verify that after transaction of 1 there are 100000008 hertz available",groups = ["hertz"])
    public void HertzTransaction_API2h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000009, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "900000000"

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000009, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        sleep(21000)

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        sleep(2000)
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "100000008"
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Hertz: "100000010"
    }

    @Test(description="Verify that after transaction",groups = ["hertz"])
    public void HertzTransaction_API3h(){
        def response
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:19000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:19000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate2.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        300.times{
            response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allNodes.Delegates.Delegate2.privateKey,
                    To:allNodes.Delegates.Delegate3.address ,From: allNodes.Delegates.Delegate2.address
        }
        //sleep(4000)
        //waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 20


        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        sleep(11000)
        verifyConsensusForAccount SkipWait:true,Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "9999"
        verifyConsensusForAccount SkipWait:true,Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Hertz: "10001"
    }

    @Test(description="Verify that minimum number of tokens can be traded.",groups = ["hertz"])
    public void HertzTransaction_API4h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:900000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "900000000"

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:900000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "0"
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Hertz: "1"
    }


    @Test(description="Verify that hertz are given back.",groups = ["hertz"])
    public void HertzTransaction_API5h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:900000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "900000000"

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:900000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        sleep(33000)
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "899999999"
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Hertz: "900000001"
    }


    @Test(description="Verify that InsufficientHertz status is reported if trade exceeds your hertz.",groups = ["hertz"])
    public void HertzTransaction_API6h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:990000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        sleep(10000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "990000000"

        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:990000000, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "InsufficientHertz", Timeout: 10
    }

    @Test(description="Deploy smart contract verify hertz.",groups = ["hertz"])
    public void HertzTransaction_API7h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1900000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:"", PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Hertz: "1866000"
    }

    @Test(description="Deploy smart contract without enough hertz.",groups = ["hertz"])
    public void HertzTransaction_API8h(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:9000000, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:"", PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "InsufficientHertz", Timeout: 10
    }

    @Test(description="Get contract value",groups = ["smart contract"])
    public void SmartContract_API22222(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"1900000", PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: "1900000"
        //sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:"", PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:1,
                Code:ComplexContract.contract2,ABI: ComplexContract.abi2
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"//, Hertz: "1866000"


        /*def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: []
        def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        sleep(500)
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,SkipWait:true)
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"*/
        /*response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: []
        getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,SkipWait:true)
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"*/
        println("denis account")
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"
        println("denis loop")
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "infiniteLoop",Params: "[]"
        def getID = response.Hash
        sleep(4000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok",SkipWait:true
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
        //, Hertz: 1000000
    }

    @Test(description="Get contract value",groups = ["smart contract"])
    public void SmartContract_API33333(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"900000", PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: "900000"
        //sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:"", PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:1,
                Code:ComplexContract.contract2,ABI: ComplexContract.abi2
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        println("before method")
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"//, Hertz: "1866000"


        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: "[]"
        def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        //sleep(1000)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: "[]"
        getID = response.Hash/*
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: "[]"
        getID = response.Hash*/
        //println("verify shit")
        //sleep(1000)
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,SkipWait:true)
        //sleep(2000)
        println("after method")
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"
        sleep(10000)
        println("wait some more")
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"
        /*response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "getMultiReturn",Params: []
        getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,SkipWait:true)
        verifyConsensusForAccount SkipWait:true, Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"*/
        /*println("denis account")
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok"
        println("denis loop")
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:allNodes.Delegates.Delegate0.address,To:contractAddress, PrivateKey:allNodes.Delegates.Delegate0.privateKey,Type:2,
                ABI: ComplexContract.abi2,
                Method: "infiniteLoop",Params: "[]"
        def getID = response.Hash
        sleep(4000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok",SkipWait:true
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10*/
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
        //, Hertz: 1000000
    }
    @Test(description="Negative: Try to deploy contract without enough hertz",groups = ["smart contract"])
    public void SmartContract_API4444() {
        def response = sendTransaction Node: allNodes.Delegates.Delegate1, Value: "90000", PrivateKey: "Genesis",
                To: allNodes.Delegates.Delegate0.address, From: "Genesis"
        response = waitForTransactionStatus ID: response.Hash, Node: allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes: allNodes.Delegates, ID: allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: "90000"
        //sleep(10000)
        response = sendTransaction Node: allNodes.Delegates.Delegate0, Value: "0", From: allNodes.Delegates.Delegate0.address, To: "", PrivateKey: allNodes.Delegates.Delegate0.privateKey, Type: 1,
                Code: ComplexContract.contract2, ABI: ComplexContract.abi2
        //response = waitForTransactionStatus ID: response.Hash, Node: allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        sleep(10000)
        println("before method")
        verifyConsensusForAccount SkipWait: true, Nodes: allNodes.Delegates, ID: allNodes.Delegates.Delegate0.address, Status: "Ok"
        //, Hertz: "1866000"
    }

}
