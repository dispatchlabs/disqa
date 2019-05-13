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
        allNodes = NodeSetup.quickSetup Delegate: 3,Seed: 1,Regular: 0
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"100000000000", PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"100000000000", PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate1.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"100000000000", PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate2.address ,From: "Genesis"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1,DataStatus: "Ok", Timeout: 10
    }

    @Test(description="Deploy contract",groups = ["smoke", "smart contract"])
    public void SmartContract_API1(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
    }

    @Test(description="Get contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API2(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Contract return types: integer",groups = ["smart contract"])
    public void SmartContract_API6(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnInt",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[20])
    }

    @Test(description="Contract return types: uint",groups = ["smart contract"])
    public void SmartContract_API9(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnUint",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[20])
    }

    @Test(description="Contract return types: address",groups = ["smart contract"])
    public void SmartContract_API10(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        println("denis")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnAddress",Params: "[]"
        def getID = response.Hash
        response = waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def returnedAddress = response.then().extract().path("data.receipt.contractResult")
        assert returnedAddress[0].size() == 20,"Error: address size should be 20."
    }

    @Test(description="Contract return types: boolean",groups = ["smart contract"])
    public void SmartContract_API11(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "returnBool",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[false])
    }


    @Test(description="Contract pass method parameter types: integer",groups = ["smart contract"])
    public void SmartContract_API13(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        println(contractAddress)
        //sleep(10000)
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: "[20]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }

    @Test(description="Contract pass method parameter types: uint",groups = ["smart contract"])
    public void SmartContract_API16(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "uintParam",Params: "[20]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }


    @Test(description="Contract pass method parameter types: boolean",groups = ["smart contract"])
    public void SmartContract_API17(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "boolParamType",Params: "[true]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test"])
    }

    @Test(description="Contract pass method parameter types: array",groups = ["smart contract"])
    public void SmartContract_API18(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "arrayParam",Params: "[[1,2]]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:[1])
    }


    @Test(description="Set contract value",groups = ["smoke", "smart contract"])
    public void SmartContract_API3(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"5555\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["5555"])
    }

    @Test(description="Set contract value through multiple delegates",groups = ["smart contract"])
    public void SmartContract_API3_b(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"000\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate1, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"111\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate2, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"222\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate3, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"333\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10


        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["333"])
    }

    @Test(description="Negative: Pass invalid value for parameter type: string",groups = ["smart contract"])
    public void SmartContract_API21(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[false]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Pass invalid value for parameter type: hash",groups = ["smart contract"])
    public void SmartContract_API22(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"value\":\"5555\"]",Status: "StatusJsonParseError: Params are not in a valid format (should be a json string of array of params)"
    }

    @Test(description="Pass invalid value for parameter type: integer",groups = ["smart contract"])
    public void SmartContract_API20(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "5555",Status: "StatusJsonParseError: Params are not in a valid format (should be a json string of array of params)"
    }

    @Test(description="Pass invalid value for parameter type: boolean",groups = ["smart contract"])
    public void SmartContract_API24(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "boolParamType",Params: "[\"testStr\"]",
                Status: "StatusJsonParseError: Invalid value provided for method boolParamType: boolean value required, provided value is testStr"
        //waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Contract returns no value",groups = ["smart contract"])
    public void SmartContract_API26(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"10\"]"
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response.then().assertThat().body("data.receipt.contractAddress",equalTo(contractAddress))
    }

    @Test(description="Negative: Pass only some parameter values for methods",groups = ["smart contract"])
    public void SmartContract_API28(){

        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "multiParams",Params: "[\"sdfsdf\"]",
                Status: "StatusJsonParseError: The method multiParams, requires 2 parameters and 1 are provided"
        //def getID = response.Hash
        //waitForTransactionStatus ID:getID,Node:allNodes.Delegates.Delegate0, Status: "NotFound", Timeout: 10
    }

    @Test(description="Deploy exact same contract",groups = ["smart contract"])
    public void SmartContract_API29(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress2 = response.then().extract().path("data.receipt.contractAddress")

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"1234\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10

        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress2, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params:"[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["aaaaaaaaaaaaa"])
    }

    @Test(description="Negative: Empty ABI on contract method call",groups = ["smart contract"])
    public void SmartContract_API31(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "",
                Method: "setVar5",Params: "[\"10\"]"//,Status: "StatusJsonParseError: value for field 'abi' is invalid"
    }

    @Test(description="Negative: Invalid values for ABI on execute",groups = ["smart contract"])
    public void SmartContract_API32(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "asldfkjdslkfhkjldshafdkjshf",
                Method: "setVar5",Params: "[\"5555\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: "lfksajdlkfsdjfksjdhflaksdfhjs",
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["5555"])
    }

    @Test(description="Negative: Invalid values for ABI on deploy",groups = ["smart contract"])
    public void SmartContract_API32_b(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: "aaaaadddddsdfsdfsfsdffffffffffffffffffffffffff",Status:"StatusJsonParseError: The ABI provided is not a valid ABI structure: aaaaadddddsdfsdfsfsdffffffffffffffffffffffffff"
        //response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        //def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        //response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
        //        ABI: "aaaaadddddsdfsdfsfsdffffffffffffffffffffffffff",
        //        Method: "setVar5",Params: ["10"],
        //        Status: "StatusJsonParseError: value for field 'abi' is invalid"
        //def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus: "InternalError",HumanReadable: '''invalid character 'ª' looking for beginning of value''')
    }

    @Test(description="Negative: Empty smart contract",groups = ["smart contract"])
    public void SmartContract_API35(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:"",ABI: DefaultSampleContract.defaultSampleABI,Status: "InvalidTransaction"
    }

    @Test(description="Negative: Partial bytecode of valid contract",groups = ["smart contract"])
    public void SmartContract_API36(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                ABI: DefaultSampleContract.defaultSampleABI,
                Code:"60806040523480156100105760"//DefaultSampleContract.defaultSample.substring(0,DefaultSampleContract.defaultSample.size()-10)
        def getID = response.Hash
        response = waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
    }

    @Test(description="Contract returns multiple values (2 strings)",groups = ["smart contract"])
    public void SmartContract_API106(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "getMultiReturn",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:["test1","test2"])
    }

    @Test(description="Contract method does logging (emit the event)",groups = ["smart contract"])
    public void SmartContract_API108(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "logEvent",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"")
    }

    @Test(description="Negative: Call out method which does not exist",groups = ["smart contract"])
    public void SmartContract_API107(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "notThere",Params: "[]",
                Status: "StatusJsonParseError: This method 'notThere' is not valid for this contract"
        def getID = response.Hash
        //waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "InternalError", Timeout: 10
        //verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus:"InternalError",HumanReadable:"method 'notThere' not found")
    }

    @Test(description="Negative: Pass more parameters for a method than it has",groups = ["smart contract"])
    public void SmartContract_API109(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress1 = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress1, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "setVar5",Params: "[\"asfsf\",\"asfsfsdfsdf\"]",
                Status: "StatusJsonParseError: The method setVar5, requires 1 parameters and 2 are provided"
    }

    @Test(description="Negative: Smart contract method throws an exception",groups = ["smart contract"])
    public void SmartContract_API116(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "throwException",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,DataStatus:"InternalError",HumanReadable:"invalid opcode 0xfe")
    }

    @Test(description="Negative buffer overflow test with integer",groups = ["smart contract"])
    public void SmartContract_API19(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:ComplexContract.contract,ABI: ComplexContract.abi
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: "[99999999999999999999999999999999999999999999999999999999999999999999999999999]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,humanReadableStatus:"abi: cannot use float64 as type ptr as argument")
    }

    @Test(description="Run a method against invalid 'to' contract",groups = ["smart contract"])
    public void SmartContract_API117(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f7565b50600060016000018190555060006001800160006101000a81548160ff02191690831515021790555060018060010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f6262626262626262626262000000000000000000000000000000000000000000815250600160020190805190602001906100f19291906100f7565b5061019c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013857805160ff1916838001178555610166565b82800160010185558215610166579182015b8281111561016557825182559160200191906001019061014a565b5b5090506101739190610177565b5090565b61019991905b8082111561019557600081600090555060010161017d565b5090565b90565b610664806101ab6000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806333e538e91461007d57806334e45f531461010d5780633a458b1f1461017657806378d8866e1461022557806379af6473146102b5578063cb69e300146102cc575b600080fd5b34801561008957600080fd5b50610092610335565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d25780820151818401526020810190506100b7565b50505050905090810190601f1680156100ff5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011957600080fd5b50610174600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d7565b005b34801561018257600080fd5b5061018b6103f4565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101e75780820151818401526020810190506101cc565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561023157600080fd5b5061023a6104c4565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561027a57808201518184015260208101905061025f565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102c157600080fd5b506102ca610562565b005b3480156102d857600080fd5b50610333600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610579565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905090565b80600160020190805190602001906103f0929190610593565b5050565b60018060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ba5780601f1061048f576101008083540402835291602001916104ba565b820191906000526020600020905b81548152906001019060200180831161049d57829003601f168201915b5050505050905084565b60008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081565b600160000160008154809291906001019190505550565b806000908051906020019061058f929190610593565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105d457805160ff1916838001178555610602565b82800160010185558215610602579182015b828111156106015782518255916020019190600101906105e6565b5b50905061060f9190610613565b5090565b61063591905b80821115610631576000816000905550600101610619565b5090565b905600a165627a7a7230582026a289af0b033267e3fc13869c446b0552e2c62b9b3fa46bc626be2c683528680029", PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: "[99999999999999999999999999999999999999999999999999999999999999999999999999999]",
                Status: "NotFound: Could not find contract with address 608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f7565b50600060016000018190555060006001800160006101000a81548160ff02191690831515021790555060018060010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f6262626262626262626262000000000000000000000000000000000000000000815250600160020190805190602001906100f19291906100f7565b5061019c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013857805160ff1916838001178555610166565b82800160010185558215610166579182015b8281111561016557825182559160200191906001019061014a565b5b5090506101739190610177565b5090565b61019991905b8082111561019557600081600090555060010161017d565b5090565b90565b610664806101ab6000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806333e538e91461007d57806334e45f531461010d5780633a458b1f1461017657806378d8866e1461022557806379af6473146102b5578063cb69e300146102cc575b600080fd5b34801561008957600080fd5b50610092610335565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d25780820151818401526020810190506100b7565b50505050905090810190601f1680156100ff5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011957600080fd5b50610174600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d7565b005b34801561018257600080fd5b5061018b6103f4565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101e75780820151818401526020810190506101cc565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561023157600080fd5b5061023a6104c4565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561027a57808201518184015260208101905061025f565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102c157600080fd5b506102ca610562565b005b3480156102d857600080fd5b50610333600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610579565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905090565b80600160020190805190602001906103f0929190610593565b5050565b60018060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ba5780601f1061048f576101008083540402835291602001916104ba565b820191906000526020600020905b81548152906001019060200180831161049d57829003601f168201915b5050505050905084565b60008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081565b600160000160008154809291906001019190505550565b806000908051906020019061058f929190610593565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105d457805160ff1916838001178555610602565b82800160010185558215610602579182015b828111156106015782518255916020019190600101906105e6565b5b50905061060f9190610613565b5090565b61063591905b80821115610631576000816000905550600101610619565b5090565b905600a165627a7a7230582026a289af0b033267e3fc13869c446b0552e2c62b9b3fa46bc626be2c683528680029"
    }

    @Test(description="Set contract value non standard characters",groups = ["smart contract"])
    public void SmartContract_API73(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "setVar5",Params: "[\"ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁᚱᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗᛋᚳᛖᚪᛚ᛫ᚦᛖᚪᚻ᛫ᛗᚪᚾᚾᚪ᛫ᚷᛖᚻᚹᛦᛚᚳ᛫ᛗᛁᚳᛚᚢᚾ᛫ᚻᛦᛏ᛫ᛞᚫᛚᚪᚾᚷᛁᚠ᛫ᚻᛖ᛫ᚹᛁᛚᛖ᛫ᚠᚩᚱ᛫ᛞᚱᛁᚻᛏᚾᛖ᛫ᛞᚩᛗᛖᛋ᛫ᚻᛚᛇᛏᚪᚾ᛬\"]"
        waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:contractAddress, PrivateKey:"Genesis",Type:2,
                ABI: DefaultSampleContract.defaultSampleABI,
                Method: "getVar5",Params: "[]"
        def getID = response.Hash
        waitForTransactionStatus ID:getID ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        verifyStatusForTransaction(Nodes:[allNodes.Delegates.Delegate0],ID:getID,ContractResult:"[ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁᚱᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗᛋᚳᛖᚪᛚ᛫ᚦᛖᚪᚻ᛫ᛗᚪᚾᚾᚪ᛫ᚷᛖᚻᚹᛦᛚᚳ᛫ᛗᛁᚳᛚᚢᚾ᛫ᚻᛦᛏ᛫ᛞᚫᛚᚪᚾᚷᛁᚠ᛫ᚻᛖ᛫ᚹᛁᛚᛖ᛫ᚠᚩᚱ᛫ᛞᚱᛁᚻᛏᚾᛖ᛫ᛞᚩᛗᛖᛋ᛫ᚻᛚᛇᛏᚪᚾ᛬]")
    }


    @Test(description="Run a method against invalid 'to' contract",groups = ["smart contract"])
    public void SmartContract_API1171111(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"", PrivateKey:"Genesis",Type:1,
                Code:DefaultSampleContract.defaultSample,ABI: DefaultSampleContract.defaultSampleABI
        response = waitForTransactionStatus ID:response.Hash ,Node:allNodes.Delegates.Delegate0, DataStatus: "Ok", Timeout: 10
        def contractAddress = response.then().extract().path("data.receipt.contractAddress")
        response = sendTransaction Node:allNodes.Delegates.Delegate0, Value:"0",From:"Genesis",To:"608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f7565b50600060016000018190555060006001800160006101000a81548160ff02191690831515021790555060018060010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f6262626262626262626262000000000000000000000000000000000000000000815250600160020190805190602001906100f19291906100f7565b5061019c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013857805160ff1916838001178555610166565b82800160010185558215610166579182015b8281111561016557825182559160200191906001019061014a565b5b5090506101739190610177565b5090565b61019991905b8082111561019557600081600090555060010161017d565b5090565b90565b610664806101ab6000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806333e538e91461007d57806334e45f531461010d5780633a458b1f1461017657806378d8866e1461022557806379af6473146102b5578063cb69e300146102cc575b600080fd5b34801561008957600080fd5b50610092610335565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d25780820151818401526020810190506100b7565b50505050905090810190601f1680156100ff5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011957600080fd5b50610174600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d7565b005b34801561018257600080fd5b5061018b6103f4565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101e75780820151818401526020810190506101cc565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561023157600080fd5b5061023a6104c4565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561027a57808201518184015260208101905061025f565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102c157600080fd5b506102ca610562565b005b3480156102d857600080fd5b50610333600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610579565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905090565b80600160020190805190602001906103f0929190610593565b5050565b60018060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ba5780601f1061048f576101008083540402835291602001916104ba565b820191906000526020600020905b81548152906001019060200180831161049d57829003601f168201915b5050505050905084565b60008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081565b600160000160008154809291906001019190505550565b806000908051906020019061058f929190610593565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105d457805160ff1916838001178555610602565b82800160010185558215610602579182015b828111156106015782518255916020019190600101906105e6565b5b50905061060f9190610613565b5090565b61063591905b80821115610631576000816000905550600101610619565b5090565b905600a165627a7a7230582026a289af0b033267e3fc13869c446b0552e2c62b9b3fa46bc626be2c683528680029", PrivateKey:"Genesis",Type:2,
                ABI: ComplexContract.abi,
                Method: "intParam",Params: "[99999999999999999999999999999999999999999999999999999999999999999999999999999]",
                Status: "NotFound: Could not find contract with address 608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f7565b50600060016000018190555060006001800160006101000a81548160ff02191690831515021790555060018060010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f6262626262626262626262000000000000000000000000000000000000000000815250600160020190805190602001906100f19291906100f7565b5061019c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013857805160ff1916838001178555610166565b82800160010185558215610166579182015b8281111561016557825182559160200191906001019061014a565b5b5090506101739190610177565b5090565b61019991905b8082111561019557600081600090555060010161017d565b5090565b90565b610664806101ab6000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806333e538e91461007d57806334e45f531461010d5780633a458b1f1461017657806378d8866e1461022557806379af6473146102b5578063cb69e300146102cc575b600080fd5b34801561008957600080fd5b50610092610335565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d25780820151818401526020810190506100b7565b50505050905090810190601f1680156100ff5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011957600080fd5b50610174600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d7565b005b34801561018257600080fd5b5061018b6103f4565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101e75780820151818401526020810190506101cc565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561023157600080fd5b5061023a6104c4565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561027a57808201518184015260208101905061025f565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102c157600080fd5b506102ca610562565b005b3480156102d857600080fd5b50610333600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610579565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905090565b80600160020190805190602001906103f0929190610593565b5050565b60018060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ba5780601f1061048f576101008083540402835291602001916104ba565b820191906000526020600020905b81548152906001019060200180831161049d57829003601f168201915b5050505050905084565b60008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081565b600160000160008154809291906001019190505550565b806000908051906020019061058f929190610593565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105d457805160ff1916838001178555610602565b82800160010185558215610602579182015b828111156106015782518255916020019190600101906105e6565b5b50905061060f9190610613565b5090565b61063591905b80821115610631576000816000905550600101610619565b5090565b905600a165627a7a7230582026a289af0b033267e3fc13869c446b0552e2c62b9b3fa46bc626be2c683528680029"
    }


}
