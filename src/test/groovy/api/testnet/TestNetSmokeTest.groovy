package api.testnet

import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class TestNetSmokeTest {
    def allNodes =
            [
                    /*Delegates:[
                        "Delegate0": [
                            "IP": "35.197.60.1",
                            "HttpPort": 1975
                        ],
                        "Delegate1": [
                                "IP": "35.203.166.220",
                                "HttpPort": 1975
                        ]
                    ]*/
                    Delegates:[
                            "Delegate0": [
                                    "IP": "127.0.0.1",
                                    "HttpPort": 3552
                            ],
                            "Delegate1": [
                                    "IP": "127.0.0.1",
                                    "HttpPort": 3554
                            ]
                    ]
            ]

    @BeforeClass
    public void findAllNodes(){
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://35.230.56.85:1975")
        Response response = request.get("/v1/delegates")
        response.then().log().all()
        def delegates = response.then().extract().path("data")
        allNodes = [:]
        allNodes.Delegates = [:]
        delegates.eachWithIndex{delegate,index->
            allNodes.Delegates."Delegate$index" = [
                    "IP": delegate.httpEndpoint.host,
                    "HttpPort": delegate.httpEndpoint.port
            ]
        }
    }

    @Test(description="Genesis to all delegates: 1 token transfer",groups = ["test net"])
    public void transactions_TESTNET01(){
        def wallet1 = createWallet()
        def balance = 0
        allNodes.Delegates.each{key,delegate->
            balance++
            def response = sendTransaction Node:delegate, Value:1, PrivateKey:"Genesis",
                    To:wallet1.Address ,From: "Genesis"
            waitForTransactionStatus ID:response.Hash ,Node:delegate, DataStatus: "Ok", Timeout: 10
        }
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: balance
    }

    @Test(description="Transfer tokens from one delegate to another and then back again",groups = ["test net"])
    public void transactions_TESTNET02(){
        def wallet1 = createWallet()
        def wallet2 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:wallet1.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:wallet2.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:500, PrivateKey:wallet1.PrivateKey,
                To:wallet2.Address ,From: wallet1.Address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: 501
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:501, PrivateKey:wallet2.PrivateKey,
                To:wallet1.Address ,From: wallet2.Address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: 0
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: 1000
    }

    @Test(description="Deploy contract",groups = ["test net"])
    public void SmartContract_TESTNET03(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.contractAddress")
    }

    @Test(description="Get contract value",groups = ["test net"])
    public void SmartContract_TESTNET04(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Set contract value",groups = ["test net"])
    public void SmartContract_TESTNET05(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["5555"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["5555"])
    }

}
