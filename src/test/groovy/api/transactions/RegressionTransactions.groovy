package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.Utils
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*
import static org.hamcrest.Matchers.equalTo

class RegressionTransactions {
    def allNodes
    def delegates

    @BeforeMethod(alwaysRun = true)
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 5,Seed: 1,Regular: 1
    }

    //@AfterMethod
    //public void afterState(){
    //    println("sleep now")
    //    sleep(20000)
    //}

    @Test(description="Genesis to Delegate token transfer",groups = ["smoke", "transactions"])
    public void transactions_API101(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Genesis to New Account token transfer",groups = ["smoke", "transactions"])
    public void transactions_API102(){
        def newAccount = Utils.createAccount()

        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:newAccount.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:newAccount.address,Status: "Ok", Balance: 999
    }

    @Test(description="Delegate to delegate: 999 token transfer",groups = ["smoke", "transactions"])
    public void transactions_API51(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 15
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 984
    }

    @Test(description="All delegate tokens transferred",groups = ["transactions"])
    public void transactions_API103(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:999, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 999
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 0
    }

    @Test(description="Transfer zero tokens",groups = ["transactions"])
    public void transactions_API96(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1

        sendTransaction Node:allNodes.Delegates.Delegate2, Value:0, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Status:"InvalidTransaction"
        //waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 998
    }

    @Test(description="Negative: Transfer more than available tokens in the wallet",groups = ["transactions"])
    public void transactions_API84(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:1000, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "InsufficientTokens", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Transfer tokens from one delegate to another and then back again",groups = ["smoke", "transactions"])
    public void transactions_API104(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:500, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 501
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:501, PrivateKey:allNodes.Delegates.Delegate1.privateKey,
                To:allNodes.Delegates.Delegate0.address ,From: allNodes.Delegates.Delegate1.address
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 0
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 1000
    }

    @Test(description="Negative: Transfer negative amount of tokens to Delegate",groups = ["transactions"])
    public void transactions_API78(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        sendTransaction Node:allNodes.Delegates.Delegate2, Value:-5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,DataStatus: "InvalidTransaction"
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Run exact same transactions body",groups = ["transactions"])
    public void transactions_API76(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        def time = System.currentTimeMillis()
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address, Time:time

        println("Sending same transaction again.")
        sleep(300)
        sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address, Time:time,Status: "DuplicateTransaction"
        sleep(2000)
        //waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 15
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 984

    }

    @Test(description="Negative: To value is empty string",groups = ["transactions"])
    public void transactions_API110(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:"" ,From: allNodes.Delegates.Delegate0.address
        response.Response.then().statusCode(400).assertThat().body("status",equalTo("InvalidTransaction"))
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: From value is empty string",groups = ["transactions"])
    public void transactions_API111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10

        sendTransaction Node:allNodes.Delegates.Delegate2, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: "",Status: "InvalidTransaction"
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: Time value 1 year in the past",groups = ["transactions"])
    public void transactions_API68(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time: -1500108301994,Status: "StatusJsonParseError: transaction time cannot be negative"
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: Time value 1 year in the future",groups = ["transactions"])
    public void transactions_API67(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                //To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time: 1576108301994,DataStatus: "JSON_PARSE_ERROR: transaction time cannot be in the future"
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time: System.currentTimeMillis()+30000000000,Status: "StatusJsonParseError: transaction time cannot be in the future"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,StatusCode:404,Status: "NotFound", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: Time value 200 ms in the future",groups = ["transactions"])
    public void transactions_API115(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time: System.currentTimeMillis()+200,Status: "StatusJsonParseError: transaction time cannot be in the future"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,StatusCode: 404,Status: "NotFound", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: From and To is the same wallet token transaction",groups = ["transactions"])
    public void transactions_API114(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate0.address ,From: allNodes.Delegates.Delegate0.address,Status: "InvalidTransaction"
        sleep(2000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999
    }

    @Test(description="Negative: Try to do same transaction from 2 different nodes at ones",groups = ["transactions"])
    public void transactions_API116(){
        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"

        def request1 = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,ReturnRequest:true

        def request2 = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,ReturnRequest:true

        request1.post("/v1/transactions")
        request2.post("/v1/transactions")

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 0
    }

    @Test(description="Negative: Try to do same transaction from 2 different nodes at ones with same time",groups = ["transactions"])
    public void transactions_API117(){
        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        def time = System.currentTimeMillis()

        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time:time

        sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,Time:time

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 0
    }

    @Test(description="Negative: Try to do same transaction from 2 different nodes at ones then try to spend them",groups = ["transactions"])
    public void transactions_API118(){
        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"

        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address

        sendTransaction Node:allNodes.Delegates.Delegate0, Value:2, PrivateKey:allNodes.Delegates.Delegate1.privateKey,
                To:allNodes.Delegates.Delegate2.address ,From: allNodes.Delegates.Delegate1.address

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 0
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate2.address,Status: "NotFound"
    }

    @Test(description="Negative buffer overflow number of tokens",groups = ["transactions"])
    public void transactions_API119(){
        def newAccount = Utils.createAccount()

        sendTransaction Node:allNodes.Delegates.Delegate0, Value:99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999, PrivateKey:"Genesis",
                To:newAccount.address ,From: "Genesis",Status: "InvalidTransaction"
    }

    @Test(description="Loop through 10 transactions, verify consensus on each one",groups = ["smoke", "transactions"])
    public void transactions_API120(){
        def newAccount = Utils.createAccount()
        def balance = 0
        10.times{
            balance++
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:newAccount.address ,From: "Genesis"
            waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
            verifyConsensusForAccount Nodes:allNodes.Delegates, ID:newAccount.address,Status: "Ok", Balance: balance
        }
    }

    @Test(description="Loop through 10 transactions, restart delegates verify consensus on each one",groups = ["transactions"])
    public void transactions_API121(){
        def newAccount = Utils.createAccount()
        def balance = 0
        10.times{
            balance++
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:newAccount.address ,From: "Genesis"
            waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
            verifyConsensusForAccount Nodes:allNodes.Delegates, ID:newAccount.address,Status: "Ok", Balance: balance
            allNodes.Delegates.each{key,value->
                value.disgoProc.destroy()
                sleep 1000
                value.startProcess()
            }
        }
    }

    @Test(description="Test startup/shutdown of a Delegate.",groups = ["transactions"])
    public void transactions_API122(){
        def newAccount = Utils.createAccount()
        def balance = 0
        allNodes.Delegates.Delegate3.disgoProc.destroy()
        5.times{
            balance++
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:newAccount.address ,From: "Genesis"
            waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        }
        allNodes.Delegates.Delegate3.startProcess()
        sleep(10000)
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:newAccount.address,Status: "Ok", Balance: balance
    }


    @Test(description="Genesis to Delegate token transfer",groups = ["transactions"])
    public void transactions_APItest(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        sleep(1000)
        def response2 = sendTransaction Node:allNodes.Delegates.Delegate2, Value:40, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        sleep(1000)
        verifyQueue Node:allNodes.Delegates.Delegate2
        println(response.Hash)
        println(response2)
        //waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2,DataStatus: "Ok", Timeout: 10
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 15
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 984
    }

    @Test(description="Negative: Try to do send transactions in incorrect order",groups = ["transactions"])
    public void transactions_API123(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10

        def request1 = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:allNodes.Delegates.Delegate1.privateKey,
                To:allNodes.Delegates.Delegate0.address ,From: allNodes.Delegates.Delegate1.address,ReturnRequest:true
        sleep(1)
        def request2 = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address,ReturnRequest:true

        request2.post("/v1/transactions")
        sleep(200)
        request1.post("/v1/transactions")

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 0
    }

    @Test(description="Send 2 transactions with same timestamp",groups = ["transactions"])
    public void transactions_API124(){
        def time = System.currentTimeMillis()
        sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis",Time: time
        def trans2 = sendTransaction Node:allNodes.Delegates.Delegate1, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis",Time: time
        waitForTransactionStatus ID:trans2.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,Status: "Ok", Balance: 1
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 1
    }

    @Test(description="Shutdown seed, try transaction",groups = ["transactions"])
    public void transactions_API125(){
        allNodes.Seeds.Seed0.disgoProc.destroy()
        sleep(1000)
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"

        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10

        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,Status: "Ok", Balance: 1
    }

    @Test(description="Negative: Change delegate address see if transaction goes through",groups = ["transactions"])
    public void transactions_API126(){
        def account = new JsonSlurper().parseText(new File(allNodes.Delegates.Delegate0.configDir+"/account.json").text)
        println account
        account.address = "34cbcb5de0fab890bd2c1a3349783e47470ae333"
        new File(allNodes.Delegates.Delegate0.configDir+"/account.json").write JsonOutput.toJson(account)
        allNodes.Delegates.Delegate0.disgoProc.destroy()
        sleep 1000
        allNodes.Delegates.Delegate0.startProcess()
        sleep(2000)
        try{
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        }
        catch (java.net.ConnectException ex){
            return
        }
        assert false,"Transaction should not have gone through at all"
    }

    @Test(description="Negative: Change delegate private Key see if transaction goes through",groups = ["transactions"])
    public void transactions_API127(){
        def account = new JsonSlurper().parseText(new File(allNodes.Delegates.Delegate0.configDir+"/account.json").text)
        account.privateKey = "f94edd5484eae95b7e5295eb005578d8e3ba7b9d20dbbf97d7c5d32fe4986444"
        new File(allNodes.Delegates.Delegate0.configDir+"/account.json").write JsonOutput.toJson(account)
        allNodes.Delegates.Delegate0.disgoProc.destroy()
        sleep 1000
        allNodes.Delegates.Delegate0.startProcess()
        sleep(2000)
        try{
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        }
        catch (java.net.ConnectException ex){
            return
        }
        assert false,"Transaction should not have gone through at all"
    }

    @Test(description="Negative: Change seed private Key see if delegates can start up",groups = ["transactions"])
    public void transactions_API128(){
        def account = new JsonSlurper().parseText(new File(allNodes.Seeds.Seed0.configDir+"/account.json").text)
        account.privateKey = "f94edd5484eae95b7e5295eb005578d8e3ba7b9d20dbbf97d7c5d32fe4986444"
        new File(allNodes.Seeds.Seed0.configDir+"/account.json").write JsonOutput.toJson(account)
        allNodes.Seeds.Seed0.disgoProc.destroy()
        sleep 1000
        allNodes.Seeds.Seed0.startProcess()
        sleep(2000)

        allNodes.Delegates.Delegate0.disgoProc.destroy()
        sleep 1000
        allNodes.Delegates.Delegate0.startProcess()
        sleep(2000)

        try{
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        }
        catch (java.net.ConnectException ex){
            return
        }
        assert false,"Transaction should not have gone through at all"
    }

    @Test(description="Negative:send transaction older than 1 second",groups = ["transactions"])
    public void transactions_API129(){
        def time = System.currentTimeMillis()
        sleep(1400)
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis",Time:time,Status: "StatusTransactionTimeOut"
    }

    @Test(description="Negative:send transaction to regular node",groups = ["transactions"])
    public void transactions_API130(){
        sendTransaction Node:allNodes.Regulars.Regular0, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis",Status: "StatusNotDelegate",StatusCode:418
    }

    @Test(description="Test transaction pagination",groups = ["transactions"])
    public void transactions_API131(){
        def transactions = []
        11.times{
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
            transactions.add(0,response.Hash)
            //transactions << response.Hash
            sleep(1)
        }
        sleep(10000)
        def transactionsGet = getTransactions(Node:allNodes.Delegates.Delegate0,Page: 1)
        assert transactionsGet.size() == 10
        transactionsGet.eachWithIndex {trans,index->
            assert trans.hash == transactions[index]
        }
        transactionsGet = getTransactions(Node:allNodes.Delegates.Delegate0,Page: 2)
        assert transactionsGet.size() == 10
        assert transactionsGet[0].hash == transactions[10]
    }

    @Test(description="Test accounts pagination",groups = ["transactions"])
    public void transactions_API132(){
        def accounts = []
        11.times{
            def wallet = createWallet()
            accounts.add(0,wallet)
            def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:1, PrivateKey:"Genesis",
                    To:wallet.Address ,From: "Genesis"
            sleep(1)
        }
        sleep(10000)
        def accountsGet = getAccounts(Node:allNodes.Delegates.Delegate0,Page: 1)
        assert accountsGet.size() == 10
        accountsGet.eachWithIndex {acc,index->
            assert acc.address == accounts[index].Address
        }
        accountsGet = getAccounts(Node:allNodes.Delegates.Delegate0,Page: 2)
        assert accountsGet.size() == 10
        assert accountsGet[0].address == accounts[0].Address
    }
    /*
    @Test(description="Decimal point tokens",groups = ["smoke", "transactions"])
    public void transactionRegression6_DelegateDecimalTokens(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,DataStatus: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0.5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0,DataStatus: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address,DataStatus: "Ok", Balance: 998.5
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address,DataStatus: "Ok", Balance: 0.5
    }*/

}
