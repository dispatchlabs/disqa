package api.testnet

import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*

class TestNetSmokeTest {
    def allNodes =
            [
                    Delegates:[
                        "Delegate0": [
                            "IP": "35.230.0.126",
                            "HttpPort": 1975
                        ],
                        "Delegate1": [
                                "IP": "35.233.231.3",
                                "HttpPort": 1975
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

    @Test(description="Delegate to delegate: 1 token transfer",groups = ["test net"])
    public void transactions_TESTNET01(){
        def wallet1 = createWallet()
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                To:wallet1.Address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
    }
}
