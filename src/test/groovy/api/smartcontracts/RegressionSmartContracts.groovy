package api.smartcontracts

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.contracts.ComplexContract
import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.xml.bind.DatatypeConverter

import static com.dispatchlabs.io.testing.common.APIs.*
import static org.hamcrest.Matchers.equalTo

class RegressionSmartContracts {
    def allNodes
    def delegates

    @BeforeMethod
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="Deploy contract",groups = ["smoke", "smart contract"])
    public void SmartContract_API1(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
    }

    @Test(description="Get contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API2(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"aaaaaaaaaaaaa")
    }

    @Test(description="Set contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API3(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["5555"]
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"5555")
    }

    @Test(description="Negative: Pass invalid value for parameter type: string",groups = ["smart contract"])
    public void SmartContract_API21(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: [false]
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
        //def getID = response.then().extract().path("id")
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"5555")
    }

    @Test(description="Pass invalid value for parameter type: hash",groups = ["smart contract"])
    public void SmartContract_API22(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["value":"5555"]
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Pass invalid value for parameter type: integer",groups = ["smart contract"])
    public void SmartContract_API20(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: 5555
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Pass invalid value for parameter type: boolean",groups = ["smart contract"])
    public void SmartContract_API24(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: [false]
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Contract returns no value",groups = ["smart contract"])
    public void SmartContract_API26(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["10"]
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        response.then().assertThat().body("contractAddress",equalTo(contractAddress))
    }

    @Test(description="Negative: Pass only some parameter values for methods",groups = ["smart contract"])
    public void SmartContract_API28(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "multiParams",Params: ["sdfsdf"]
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Deploy exact same contract",groups = ["smart contract"])
    public void SmartContract_API29(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress2 = response.then().extract().path("contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["1234"]
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress2, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5"
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"aaaaaaaaaaaaa")
    }

    @Test(description="Negative: Empty ABI on contract method call",groups = ["smart contract"])
    public void SmartContract_API31(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "",
                Method: "setVar5",Params: ["10"]
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Negative: Invalid values for ABI",groups = ["smart contract"])
    public void SmartContract_API32(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "adfsdfsdfsfsdffffffffffffffffffffffffff",
                Method: "setVar5",Params: ["10"]
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Negative: Empty smart contract",groups = ["smart contract"])
    public void SmartContract_API35(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:""
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
    }

    @Test(description="Negative: Partial bytecode of valid contract",groups = ["smart contract"])
    public void SmartContract_API36(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample.substring(0,DefaultSampleContract.defaultSample.size()-10)
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        response.then().assertThat().body("type",equalTo("InvalidTransaction"))
    }

    @Test(description="Contract returns multiple values (2 strings)",groups = ["smart contract"])
    public void SmartContract_API106(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "getMultiReturn",Params: []
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test1","test2"])
    }

    @Test(description="Contract method does logging (emit the event)",groups = ["smart contract"])
    public void SmartContract_API108(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "logEvent",Params: []
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"")
    }

    @Test(description="Negative: Call out method which does not exist",groups = ["smart contract"])
    public void SmartContract_API107(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "notThere",Params: []
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"")
    }

    @Test(description="Negative: Pass more parameters for a method than it has",groups = ["smart contract"])
    public void SmartContract_API109(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract
        response = waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "setVar5",Params: ["asfsf","asfsfsdfsdf"]
        def getID = response.then().extract().path("id")
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"")
    }
}
