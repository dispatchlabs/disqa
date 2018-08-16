package api.testnet

import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
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
    def allWallets = [
            Wallet0:[
                    "address": "81418cd6b383cc474a6cd3a3e928be8ed6d83d6d",
                    "privateKey": "de2bd07d0e3366ab727f4175de143b4cfe5e21f359a80ad2336b8c98b6dcdffb"
            ],
            Wallet1:[
                    "Address": "f2c79f5fca181a8e34b249a5585e7fb35c4a1981",
                    "PrivateKey": "68260c297fe906e1794b21f35d33a96e5085314a7232f9c65a3cccf7f145a328"
            ],
            Wallet2:[
                    "Address": "fa128e7340acc30659e18fdb3958a40bfb366644",
                    "PrivateKey": "51ba1d72203fda615721c15ff2562a913c6c9743ee01251a4dcecbe1e02b0af6"
            ]
    ]

    @Test(description="Genesis to delegate: 1 token transfer",groups = ["test net"])
    public void transactions_TESTNET01(){
        def wallet1 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                To:wallet1.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
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
