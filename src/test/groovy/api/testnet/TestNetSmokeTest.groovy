package api.testnet

import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.specification.RequestSpecification
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class TestNetSmokeTest {
    def pkey = "a14f707e630faa421ad830ad2d820f32ab947c488eb472216685e151179541d7"
    def akey = "5fca768372eacb2fa39daf98685634bdd852a3b3"

    def allNodes =
            [
                    Delegates:[
                        "Delegate0": [
                            "IP": "35.233.231.3",
                            "HttpPort": 1975
                        ],
                        "Delegate1": [
                                "IP": "35.233.241.115",
                                "HttpPort": 1975
                        ]
                    ]
                    /*Delegates:[
                            "Delegate0": [
                                    "IP": "127.0.0.1",
                                    "HttpPort": 3552
                            ],
                            "Delegate1": [
                                    "IP": "127.0.0.1",
                                    "HttpPort": 3554
                            ]
                    ]*/
            ]

    @BeforeClass
    public void findAllNodes(){
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        /*//request.baseUri("http://35.197.78.109:1975")
        //request.baseUri("http://35.197.127.151:1975")//TestNet seed
        request.baseUri("http://10.138.0.2:1975/v1/delegates")//MainNet seed
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
    //*/
    }

    @Test(description="Genesis to all delegates: 1 token transfer",groups = ["test net"])
    public void transactions_TESTNET01(){
        def wallet1 = createWallet()
        def balance = 0
        allNodes.Delegates.each{key,delegate->
            balance++
            def response = sendTransaction Node:delegate, Value:"1", PrivateKey:pkey,
                    To:wallet1.Address ,From: akey
            waitForTransactionStatus ID:response.Hash ,Node:delegate, DataStatus: "Ok", Timeout: 10
        }
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: balance.toString()
    }

    @Test(description="Transfer tokens from one delegate to another and then back again",groups = ["test net"])
    public void transactions_TESTNET02(){
        def wallet1 = createWallet()
        def wallet2 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:18000, PrivateKey:pkey,
                To:wallet1.Address ,From: akey
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: "18000"
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:18000, PrivateKey:pkey,
                To:wallet2.Address ,From: akey
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: "18000"

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:500, PrivateKey:wallet1.PrivateKey,
                To:wallet2.Address ,From: wallet1.Address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: "18500"
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:18500, PrivateKey:wallet2.PrivateKey,
                To:wallet1.Address ,From: wallet2.Address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: "0"
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: "36000"
    }

    @Test(description="Deploy contract",groups = ["test net"])
    public void SmartContract_TESTNET03(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From: akey,To:"", PrivateKey:pkey,Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.contractAddress")
    }

    @Test(description="Get contract value",groups = ["test net"])
    public void SmartContract_TESTNET04(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:akey,To:"", PrivateKey:pkey,Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From: akey,To:contractAddress, PrivateKey:pkey,Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Set contract value",groups = ["test net"])
    public void SmartContract_TESTNET05(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From: akey,To:"", PrivateKey:pkey,Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From: akey,To:contractAddress, PrivateKey:pkey,Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"5555\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From: akey,To:contractAddress, PrivateKey:pkey,Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["5555"])
    }

    @Test(description="Denis test",groups = ["test net"])
    public void SmartContract_TESTNET_DenisDebug(){
        def node = [:]
        node.IP = "35.233.212.74"
        node.HttpPort = "1975"
        def response = sendTransaction Node:node, Value:0,From: akey,To:"5e2694e7994fb3824dcbc16a86309b6567bb782b", PrivateKey:pkey,Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "balanceOf",Params: "[\"745e063db0b16cbf3859faa614e3488222c13f1e\"]"
        def getID = response.Hash
        //println()
        waitForTransactionStatus ID:getID ,Node:node, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[node],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
        /*def wallet1 = createWallet()
        println("Private key 1: "+wallet1.PrivateKey)
        println("Address key 1: "+wallet1.Address)*/
        /*def wallet2 = createWallet()
        println("Private key 2: "+wallet2.PrivateKey)
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:pkey,
                To:wallet1.Address ,From: akey
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet1.Address,Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:500, PrivateKey:pkey,
                To:wallet2.Address ,From: akey
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:wallet2.Address,Status: "Ok", Balance: 500*/

    }

}
