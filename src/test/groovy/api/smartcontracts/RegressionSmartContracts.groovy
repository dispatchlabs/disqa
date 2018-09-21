package api.smartcontracts

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.contracts.ComplexContract
import com.dispatchlabs.io.testing.common.contracts.DefaultSampleContract
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.*
import static org.hamcrest.Matchers.equalTo

class RegressionSmartContracts {
    def allNodes
    def delegates

    @BeforeMethod(alwaysRun = true)
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 5,Seed: 1,Regular: 0
    }

    @Test(description="Deploy contract",groups = ["smoke", "smart contract"])
    public void SmartContract_API1(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
    }

    @Test(description="Get contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API2(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
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

    @Test(description="Contract return types: address",groups = ["smart contract"])
    public void SmartContract_API10(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnAddress",Params: []
        def getID = response.Hash
        response = waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def returnedAddress = response.then().extract().path("data.receipt.contractResult")
        assert returnedAddress[0].size() == 20,"Error: address size should be 20."
    }

    @Test(description="Contract return types: boolean",groups = ["smart contract"])
    public void SmartContract_API11(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnBool",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[false])
    }


    @Test(description="Contract pass method parameter types: integer",groups = ["smart contract"])
    public void SmartContract_API13(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        println(contractAddress)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: [20]
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }

    @Test(description="Contract pass method parameter types: uint",groups = ["smart contract"])
    public void SmartContract_API16(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "uintParam",Params: [20]
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }


    @Test(description="Contract pass method parameter types: boolean",groups = ["smart contract"])
    public void SmartContract_API17(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "boolParamType",Params: [true]
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }

    @Test(description="Contract pass method parameter types: array",groups = ["smart contract"])
    public void SmartContract_API18(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "arrayParam",Params: [[1,2]]
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[1])
    }


    @Test(description="Set contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API3(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
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

    @Test(description="Set contract value through multiple delegates",groups = ["smart contract"])
    public void SmartContract_API3_b(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["000"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["111"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["222"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate3, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["333"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10


        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["333"])
    }

    @Test(description="Negative: Pass invalid value for parameter type: string",groups = ["smart contract"])
    public void SmartContract_API21(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: [false]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Pass invalid value for parameter type: hash",groups = ["smart contract"])
    public void SmartContract_API22(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["value":"5555"],Status: "StatusJsonParseError: value for field 'params' must be an array"
    }

    @Test(description="Pass invalid value for parameter type: integer",groups = ["smart contract"])
    public void SmartContract_API20(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: 5555,Status: "StatusJsonParseError: value for field 'params' must be an array"
    }

    @Test(description="Pass invalid value for parameter type: boolean",groups = ["smart contract"])
    public void SmartContract_API24(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "boolParamType",Params: ["testStr"],
                Status: "StatusJsonParseError: Invalid value provided for method boolParamType: boolean value required, provided value is testStr"
        //waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Contract returns no value",groups = ["smart contract"])
    public void SmartContract_API26(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["10"]
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response.then().assertThat().body("data.receipt.contractAddress",equalTo(contractAddress))
    }

    @Test(description="Negative: Pass only some parameter values for methods",groups = ["smart contract"])
    public void SmartContract_API28(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "multiParams",Params: ["sdfsdf"],
                Status: "StatusJsonParseError: This method multiParams, requires 2 parameters and 1 are provided"
        //def getID = response.Hash
        //waitForTransactionStatus ID:getID,Node:allNodes.Delegates.Delegate0, Status: "NotFound", Timeout: 10
    }

    @Test(description="Deploy exact same contract",groups = ["smart contract"])
    public void SmartContract_API29(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress2 = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: ["1234"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress2, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Negative: Empty ABI on contract method call",groups = ["smart contract"])
    public void SmartContract_API31(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "",
                Method: "setVar5",Params: ["10"]//,Status: "StatusJsonParseError: value for field 'abi' is invalid"
    }

    @Test(description="Negative: Invalid values for ABI on execute",groups = ["smart contract"])
    public void SmartContract_API32(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "asldfkjdslkfhkjldshafdkjshf",
                Method: "setVar5",Params: ["5555"]
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "lfksajdlkfsdjfksjdhflaksdfhjs",
                Method: "getVar5",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["5555"])
    }

    @Test(description="Negative: Invalid values for ABI on deploy",groups = ["smart contract"])
    public void SmartContract_API32_b(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: "aaaaadddddsdfsdfsfsdffffffffffffffffffffffffff"
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        //def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        //response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
        //        ABI: "aaaaadddddsdfsdfsfsdffffffffffffffffffffffffff",
        //        Method: "setVar5",Params: ["10"],
        //        Status: "StatusJsonParseError: value for field 'abi' is invalid"
        //def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus: "InternalError",HumanReadable: '''invalid character 'ª' looking for beginning of value''')
    }

    @Test(description="Negative: Empty smart contract",groups = ["smart contract"])
    public void SmartContract_API35(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:"",ABI: DefaultSampleContract.defaultSampleABI,Status: "InvalidTransaction"
    }

    @Test(description="Negative: Partial bytecode of valid contract",groups = ["smart contract"])
    public void SmartContract_API36(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                ABI: DefaultSampleContract.defaultSampleABI,
                Code:"60806040523480156100105760"//DefaultSampleContract.defaultSample.substring(0,DefaultSampleContract.defaultSample.size()-10)
        def getID = response.Hash
        response = waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Contract returns multiple values (2 strings)",groups = ["smart contract"])
    public void SmartContract_API106(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "getMultiReturn",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test1","test2"])
    }

    @Test(description="Contract method does logging (emit the event)",groups = ["smart contract"])
    public void SmartContract_API108(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "logEvent",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"")
    }

    @Test(description="Negative: Call out method which does not exist",groups = ["smart contract"])
    public void SmartContract_API107(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "notThere",Params: [],
                Status: "StatusJsonParseError: This method notThere is not valid for this contract"
        def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus:"InternalError",HumanReadable:"method 'notThere' not found")
    }

    @Test(description="Negative: Pass more parameters for a method than it has",groups = ["smart contract"])
    public void SmartContract_API109(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "setVar5",Params: ["asfsf","asfsfsdfsdf"],
                Status: "StatusJsonParseError: This method setVar5, requires 1 parameters and 2 are provided"
    }

    @Test(description="Negative: Smart contract method throws an exception",groups = ["smart contract"])
    public void SmartContract_API116(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "throwException",Params: []
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus:"InternalError",HumanReadable:"invalid opcode 0xfe")
    }

    @Test(description="Negative buffer overflow test with integer",groups = ["smart contract"])
    public void SmartContract_API19(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: [99999999999999999999999999999999999999999999999999999999999999999999999999999]
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,humanReadableStatus:"abi: cannot use float64 as type ptr as argument")
    }

    @Test(description="Deploy contract",groups = ["smart contract"])
    public void SmartContract_API1111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:0,From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        println(contractAddress)
        verifyConsensusForAccount ID:contractAddress
    }
}
