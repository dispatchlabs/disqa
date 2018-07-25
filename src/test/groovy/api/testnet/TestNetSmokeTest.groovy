package api.testnet

import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class TestNetSmokeTest {
    def allNodes =
            [
                    Delegates:[
                        "Delegate0": [
                            "IP": "35.199.177.99",
                            "HttpPort": 1975,
                            "GrpcPort": 3501
                        ],
                        "Delegate1": [
                                "IP": "35.197.127.151",
                                "HttpPort": 1975,
                                "GrpcPort": 3501
                        ]
                    ]
            ]
    def allWallets = [
            Wallet0:[
                    "address": "81418cd6b383cc474a6cd3a3e928be8ed6d83d6d",
                    "privateKey": "de2bd07d0e3366ab727f4175de143b4cfe5e21f359a80ad2336b8c98b6dcdffb"
            ],
            Wallet1:[
                    "address": "f2c79f5fca181a8e34b249a5585e7fb35c4a1981",
                    "privateKey": "68260c297fe906e1794b21f35d33a96e5085314a7232f9c65a3cccf7f145a328"
            ],
            Wallet2:[
                    "address": "fa128e7340acc30659e18fdb3958a40bfb366644",
                    "privateKey": "51ba1d72203fda615721c15ff2562a913c6c9743ee01251a4dcecbe1e02b0af6"
            ]
    ]

    @Test(description="Delegate to delegate: 1 token transfer",groups = ["test net"])
    public void transactions_TESTNET01(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allWallets.Wallet0.privateKey,
                To:allWallets.Wallet1.address ,From: allWallets.Wallet0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
    }
}
