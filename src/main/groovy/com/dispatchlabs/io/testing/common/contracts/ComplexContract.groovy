package com.dispatchlabs.io.testing.common.contracts

class ComplexContract {
/*
pragma solidity ^0.4.0;
contract TestContract {

    struct ComplexStruct {
        uint var1;
        bool var2;
        uint8 var3;
        string var4;
    }

    string public var5;
    int public intVar;

    ComplexStruct public var6;

    constructor() public {
        var5 = "aaaaaaaaaaaaa";
        var6.var1 = 0;
        var6.var2 = false;
        var6.var3 = 1;
        var6.var4 = "bbbbbbbbbbb";
    }

	function multiParams(string value,string value2) public {

    }

    function setVar5(string value) public {
        var5 = value;
    }

    function setVar6Var4(string value) public {
        var6.var4 = value;
    }

    function incVar6Var1() public {
        var6.var1++;
    }

    function getVar5() public view returns (string) {
        return var5;
    }

    function getMultiReturn() public view returns (string,string) {
        return ("test1","test2");
    }

    function throwException() public{
        uint x = uint(5)/uint(0);
    }

    event testLog(string test1, string test2);

    function logEvent() public{
        testLog("test1","test2");
    }

    function boolParamType(bool value)public view returns (string){
        bool test = value;
        return "test";
    }

    function returnInt() public view returns (int){
        return 20;
    }

    function returnUint() public view returns (uint){
        return 20;
    }

    function returnAddress() public view returns (address){
        return this;
    }


    function returnBool() public returns (bool){
        return false;
    }

    function intParam(int param) public view returns (string) {
        param * 5;
        return "test";
    }

    function uintParam(uint param) public view returns (string) {
        param * 5;
        return "test";
    }

    function arrayParam(uint[] param) public view returns (uint) {
        return param[0];
    }

}
 */
    public static def contract2 =
'''608060405234801561001057600080fd5b5060408051808201909152600d8082527f61616161616161616161616161000000000000000000000000000000000000006020909201918252610055916000916100b4565b5060006002556003805461ffff191661010017905560408051808201909152600b8082527f626262626262626262626200000000000000000000000000000000000000000060209092019182526100ae916004916100b4565b5061014f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100f557805160ff1916838001178555610122565b82800160010185558215610122579182015b82811115610122578251825591602001919060010190610107565b5061012e929150610132565b5090565b61014c91905b8082111561012e5760008155600101610138565b90565b610aa08061015e6000396000f30060806040526004361061011c5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166304c6f56a81146101215780631af35da3146101ae5780631dbf353d146101d75780631e358c3e146101ee578063216a52e514610255578063222e0412146102ec57806330bc6db21461030157806333e538e91461031657806334e45f531461032b5780633a458b1f146103845780634a846e02146104305780634aea8b141461012157806363655af31461052357806378d8866e1461053857806379af64731461054d578063943640c314610562578063a5fe0872146105a0578063af445500146105b5578063cb69e300146105ca578063d13f25ad14610301578063fd213d0c14610623575b600080fd5b34801561012d57600080fd5b50610139600435610639565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561017357818101518382015260200161015b565b50505050905090810190601f1680156101a05780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101ba57600080fd5b506101c3610671565b604080519115158252519081900360200190f35b3480156101e357600080fd5b506101ec610677565b005b3480156101fa57600080fd5b5060408051602060048035808201358381028086018501909652808552610243953695939460249493850192918291850190849080828437509497506106919650505050505050565b60408051918252519081900360200190f35b34801561026157600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101ec94369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506106b39650505050505050565b3480156102f857600080fd5b506101ec6106b7565b34801561030d57600080fd5b506102436106be565b34801561032257600080fd5b506101396106c3565b34801561033757600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101ec9436949293602493928401919081908401838280828437509497506107599650505050505050565b34801561039057600080fd5b5061039961076c565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156103f25781810151838201526020016103da565b50505050905090810190601f16801561041f5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561043c57600080fd5b50610445610815565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561048657818101518382015260200161046e565b50505050905090810190601f1680156104b35780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156104e65781810151838201526020016104ce565b50505050905090810190601f1680156105135780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561052f57600080fd5b506101ec610880565b34801561054457600080fd5b50610139610895565b34801561055957600080fd5b506101ec610923565b34801561056e57600080fd5b5061057761092e565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156105ac57600080fd5b506101ec610932565b3480156105c157600080fd5b506102436109c7565b3480156105d657600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101ec9436949293602493928401919081908401838280828437509497506109cd9650505050505050565b34801561062f57600080fd5b5061013960043515155b5060408051808201909152600481527f7465737400000000000000000000000000000000000000000000000000000000602082015290565b60005b90565b60005b80151561068e57610689610932565b61067a565b50565b60008160008151811015156106a257fe5b906020019060200201519050919050565b5050565b6000806005fe5b601490565b60008054604080516020601f600260001961010060018816150201909516949094049384018190048102820181019092528281526060939092909183018282801561074f5780601f106107245761010080835404028352916020019161074f565b820191906000526020600020905b81548152906001019060200180831161073257829003601f168201915b5050505050905090565b80516106b39060049060208401906109dc565b60028054600354600480546040805160206101006001851615810260001901909416889004601f8101829004820283018201909352828252959660ff80871697949096049095169493909290919083018282801561080b5780601f106107e05761010080835404028352916020019161080b565b820191906000526020600020905b8154815290600101906020018083116107ee57829003601f168201915b5050505050905084565b60408051808201825260058082527f746573743100000000000000000000000000000000000000000000000000000060208084019190915283518085019094529083527f74657374320000000000000000000000000000000000000000000000000000009083015291565b60005b6180e881121561068e57600101610883565b6000805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561091b5780601f106108f05761010080835404028352916020019161091b565b820191906000526020600020905b8154815290600101906020018083116108fe57829003601f168201915b505050505081565b600280546001019055565b3090565b6040805181815260058183018190527f746573743100000000000000000000000000000000000000000000000000000060608301526080602083018190528201527f746573743200000000000000000000000000000000000000000000000000000060a082015290517fb20aa8922321b2e5be1e9784294eda54d640a58038ceede50492f7d7ffc8ad629181900360c00190a1565b60015481565b80516106b39060009060208401905b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a1d57805160ff1916838001178555610a4a565b82800160010185558215610a4a579182015b82811115610a4a578251825591602001919060010190610a2f565b50610a56929150610a5a565b5090565b61067491905b80821115610a565760008155600101610a605600a165627a7a723058209d00b85520cb57637b3dc2716286471369c9d6645bc6e2c748ada25de8d851470029'''
    public static def contract ='''608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f8565b5060006002600001819055506000600260010160006101000a81548160ff0219169083151502179055506001600260010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f62626262626262626262620000000000000000000000000000000000000000008152506002800190805190602001906100f29291906100f8565b5061019d565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013957805160ff1916838001178555610167565b82800160010185558215610167579182015b8281111561016657825182559160200191906001019061014b565b5b5090506101749190610178565b5090565b61019a91905b8082111561019657600081600090555060010161017e565b5090565b90565b610e7f80620001ad6000396000f300608060405260043610610107576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806304c6f56a1461010c5780631af35da3146101b25780631e358c3e146101e1578063216a52e51461025b578063222e04121461030a57806330bc6db21461032157806333e538e91461034c57806334e45f53146103dc5780633a458b1f146104455780634a846e02146104f45780634aea8b14146105f057806378d8866e1461069657806379af647314610726578063943640c31461073d578063a5fe087214610794578063af445500146107ab578063cb69e300146107d6578063d13f25ad1461083f578063fd213d0c1461086a575b600080fd5b34801561011857600080fd5b5061013760048036038101908080359060200190929190505050610912565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561017757808201518184015260208101905061015c565b50505050905090810190601f1680156101a45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101be57600080fd5b506101c7610951565b604051808215151515815260200191505060405180910390f35b3480156101ed57600080fd5b5061024560048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610959565b6040518082815260200191505060405180910390f35b34801561026757600080fd5b50610308600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061097b565b005b34801561031657600080fd5b5061031f61097f565b005b34801561032d57600080fd5b50610336610993565b6040518082815260200191505060405180910390f35b34801561035857600080fd5b5061036161099c565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103a1578082015181840152602081019050610386565b50505050905090810190601f1680156103ce5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156103e857600080fd5b50610443600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610a3e565b005b34801561045157600080fd5b5061045a610a5a565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156104b657808201518184015260208101905061049b565b50505050905090810190601f1680156104e35780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561050057600080fd5b50610509610b2a565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561054d578082015181840152602081019050610532565b50505050905090810190601f16801561057a5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156105b3578082015181840152602081019050610598565b50505050905090810190601f1680156105e05780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156105fc57600080fd5b5061061b60048036038101908080359060200190929190505050610ba1565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561065b578082015181840152602081019050610640565b50505050905090810190601f1680156106885780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156106a257600080fd5b506106ab610be0565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156106eb5780820151818401526020810190506106d0565b50505050905090810190601f1680156107185780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561073257600080fd5b5061073b610c7e565b005b34801561074957600080fd5b50610752610c95565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156107a057600080fd5b506107a9610c9d565b005b3480156107b757600080fd5b506107c0610d40565b6040518082815260200191505060405180910390f35b3480156107e257600080fd5b5061083d600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610d46565b005b34801561084b57600080fd5b50610854610d60565b6040518082815260200191505060405180910390f35b34801561087657600080fd5b50610897600480360381019080803515159060200190929190505050610d69565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156108d75780820151818401526020810190506108bc565b50505050905090810190601f1680156109045780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60606040805190810160405280600481526020017f74657374000000000000000000000000000000000000000000000000000000008152509050919050565b600080905090565b600081600081518110151561096a57fe5b906020019060200201519050919050565b5050565b600080600581151561098d57fe5b04905050565b60006014905090565b606060008054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a345780601f10610a0957610100808354040283529160200191610a34565b820191906000526020600020905b815481529060010190602001808311610a1757829003601f168201915b5050505050905090565b80600280019080519060200190610a56929190610dae565b5050565b60028060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b205780601f10610af557610100808354040283529160200191610b20565b820191906000526020600020905b815481529060010190602001808311610b0357829003601f168201915b5050505050905084565b6060806040805190810160405280600581526020017f74657374310000000000000000000000000000000000000000000000000000008152506040805190810160405280600581526020017f7465737432000000000000000000000000000000000000000000000000000000815250915091509091565b60606040805190810160405280600481526020017f74657374000000000000000000000000000000000000000000000000000000008152509050919050565b60008054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c765780601f10610c4b57610100808354040283529160200191610c76565b820191906000526020600020905b815481529060010190602001808311610c5957829003601f168201915b505050505081565b600260000160008154809291906001019190505550565b600030905090565b7fb20aa8922321b2e5be1e9784294eda54d640a58038ceede50492f7d7ffc8ad62604051808060200180602001838103835260058152602001807f7465737431000000000000000000000000000000000000000000000000000000815250602001838103825260058152602001807f74657374320000000000000000000000000000000000000000000000000000008152506020019250505060405180910390a1565b60015481565b8060009080519060200190610d5c929190610dae565b5050565b60006014905090565b606060008290506040805190810160405280600481526020017f7465737400000000000000000000000000000000000000000000000000000000815250915050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610def57805160ff1916838001178555610e1d565b82800160010185558215610e1d579182015b82811115610e1c578251825591602001919060010190610e01565b5b509050610e2a9190610e2e565b5090565b610e5091905b80821115610e4c576000816000905550600101610e34565b5090565b905600a165627a7a72305820c9b844c8d34eaad7a158dac9563fe47841fbeb0c6a7ed87255094dd3d1921d140029'''

    public static def abi2 = "[\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"intParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnBool\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"infiniteLoop\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"uint256[]\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"arrayParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value2\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"multiParams\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"throwException\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnUint\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getVar5\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"setVar6Var4\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"var6\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var1\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var2\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var3\",\n" +
            "\t\t\t\t\"type\": \"uint8\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var4\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getMultiReturn\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"uintParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"limitedLoop\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"var5\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"incVar6Var1\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnAddress\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"logEvent\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"intVar\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"setVar5\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnInt\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"boolParamType\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"constructor\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"test1\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"test2\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"testLog\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t}\n" +
            "]"

    public static def abi = //'''[ { "constant": true, "inputs": [ { "name": "param", "type": "int256" } ], "name": "intParam", "outputs": [ { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [], "name": "returnBool", "outputs": [ { "name": "", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "param", "type": "uint256[]" } ], "name": "arrayParam", "outputs": [ { "name": "", "type": "uint256" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "value", "type": "string" }, { "name": "value2", "type": "string" } ], "name": "multiParams", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [], "name": "throwException", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "returnUint", "outputs": [ { "name": "", "type": "uint256" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "getVar5", "outputs": [ { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "value", "type": "string" } ], "name": "setVar6Var4", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "var6", "outputs": [ { "name": "var1", "type": "uint256" }, { "name": "var2", "type": "bool" }, { "name": "var3", "type": "uint8" }, { "name": "var4", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "getMultiReturn", "outputs": [ { "name": "", "type": "string" }, { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "param", "type": "uint256" } ], "name": "uintParam", "outputs": [ { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "var5", "outputs": [ { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [], "name": "incVar6Var1", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "returnAddress", "outputs": [ { "name": "", "type": "address" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [], "name": "logEvent", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "intVar", "outputs": [ { "name": "", "type": "int256" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "value", "type": "string" } ], "name": "setVar5", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "returnInt", "outputs": [ { "name": "", "type": "int256" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "value", "type": "bool" } ], "name": "boolParamType", "outputs": [ { "name": "", "type": "string" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "inputs": [], "payable": false, "stateMutability": "nonpayable", "type": "constructor" }, { "anonymous": false, "inputs": [ { "indexed": false, "name": "test1", "type": "string" }, { "indexed": false, "name": "test2", "type": "string" } ], "name": "testLog", "type": "event" }]'''
    //'''[{"constant":true,"inputs":[{"name":"param","type":"int256"}],"name":"intParam","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"returnBool","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"param","type":"uint256[]"}],"name":"arrayParam","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"value","type":"string"},{"name":"value2","type":"string"}],"name":"multiParams","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[],"name":"throwException","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"returnUint","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getVar5","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"value","type":"string"}],"name":"setVar6Var4","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"var6","outputs":[{"name":"var1","type":"uint256"},{"name":"var2","type":"bool"},{"name":"var3","type":"uint8"},{"name":"var4","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getMultiReturn","outputs":[{"name":"","type":"string"},{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"param","type":"uint256"}],"name":"uintParam","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"var5","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"incVar6Var1","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"returnAddress","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"logEvent","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"intVar","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"value","type":"string"}],"name":"setVar5","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"returnInt","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"value","type":"bool"}],"name":"boolParamType","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":false,"name":"test1","type":"string"},{"indexed":false,"name":"test2","type":"string"}],"name":"testLog","type":"event"}]'''
    // '''[{'constant':true,'inputs':[{'name':'param','type':'int256'}],'name':'intParam','outputs':[{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[],'name':'returnBool','outputs':[{'name':'','type':'bool'}],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[{'name':'param','type':'uint256[]'}],'name':'arrayParam','outputs':[{'name':'','type':'uint256'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[{'name':'value','type':'string'},{'name':'value2','type':'string'}],'name':'multiParams','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':false,'inputs':[],'name':'throwException','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[],'name':'returnUint','outputs':[{'name':'','type':'uint256'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':true,'inputs':[],'name':'getVar5','outputs':[{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[{'name':'value','type':'string'}],'name':'setVar6Var4','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[],'name':'var6','outputs':[{'name':'var1','type':'uint256'},{'name':'var2','type':'bool'},{'name':'var3','type':'uint8'},{'name':'var4','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':true,'inputs':[],'name':'getMultiReturn','outputs':[{'name':'','type':'string'},{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':true,'inputs':[{'name':'param','type':'uint256'}],'name':'uintParam','outputs':[{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':true,'inputs':[],'name':'var5','outputs':[{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[],'name':'incVar6Var1','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[],'name':'returnAddress','outputs':[{'name':'','type':'address'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[],'name':'logEvent','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[],'name':'intVar','outputs':[{'name':'','type':'int256'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':false,'inputs':[{'name':'value','type':'string'}],'name':'setVar5','outputs':[],'payable':false,'stateMutability':'nonpayable','type':'function'},{'constant':true,'inputs':[],'name':'returnInt','outputs':[{'name':'','type':'int256'}],'payable':false,'stateMutability':'view','type':'function'},{'constant':true,'inputs':[{'name':'value','type':'bool'}],'name':'boolParamType','outputs':[{'name':'','type':'string'}],'payable':false,'stateMutability':'view','type':'function'},{'inputs':[],'payable':false,'stateMutability':'nonpayable','type':'constructor'},{'anonymous':false,'inputs':[{'indexed':false,'name':'test1','type':'string'},{'indexed':false,'name':'test2','type':'string'}],'name':'testLog','type':'event'}]'''
//'''5b0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a2022706172616d222c0a090909092274797065223a2022696e74323536220a0909097d0a09095d2c0a0909226e616d65223a2022696e74506172616d222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202272657475726e426f6f6c222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022626f6f6c220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a2022706172616d222c0a090909092274797065223a202275696e743235365b5d220a0909097d0a09095d2c0a0909226e616d65223a20226172726179506172616d222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a202275696e74323536220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a202276616c7565222c0a090909092274797065223a2022737472696e67220a0909097d2c0a0909097b0a09090909226e616d65223a202276616c756532222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a0909226e616d65223a20226d756c7469506172616d73222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a20227468726f77457863657074696f6e222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202272657475726e55696e74222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a202275696e74323536220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202267657456617235222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a202276616c7565222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a0909226e616d65223a20227365745661723656617234222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202276617236222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a202276617231222c0a090909092274797065223a202275696e74323536220a0909097d2c0a0909097b0a09090909226e616d65223a202276617232222c0a090909092274797065223a2022626f6f6c220a0909097d2c0a0909097b0a09090909226e616d65223a202276617233222c0a090909092274797065223a202275696e7438220a0909097d2c0a0909097b0a09090909226e616d65223a202276617234222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a20226765744d756c746952657475726e222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d2c0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a2022706172616d222c0a090909092274797065223a202275696e74323536220a0909097d0a09095d2c0a0909226e616d65223a202275696e74506172616d222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202276617235222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a2022696e635661723656617231222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202272657475726e41646472657373222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a202261646472657373220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a20226c6f674576656e74222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a2022696e74566172222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022696e74323536220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a2066616c73652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a202276616c7565222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a0909226e616d65223a202273657456617235222c0a0909226f757470757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b5d2c0a0909226e616d65223a202272657475726e496e74222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022696e74323536220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922636f6e7374616e74223a20747275652c0a090922696e70757473223a205b0a0909097b0a09090909226e616d65223a202276616c7565222c0a090909092274797065223a2022626f6f6c220a0909097d0a09095d2c0a0909226e616d65223a2022626f6f6c506172616d54797065222c0a0909226f757470757473223a205b0a0909097b0a09090909226e616d65223a2022222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a202276696577222c0a09092274797065223a202266756e6374696f6e220a097d2c0a097b0a090922696e70757473223a205b5d2c0a09092270617961626c65223a2066616c73652c0a09092273746174654d75746162696c697479223a20226e6f6e70617961626c65222c0a09092274797065223a2022636f6e7374727563746f72220a097d2c0a097b0a090922616e6f6e796d6f7573223a2066616c73652c0a090922696e70757473223a205b0a0909097b0a0909090922696e6465786564223a2066616c73652c0a09090909226e616d65223a20227465737431222c0a090909092274797065223a2022737472696e67220a0909097d2c0a0909097b0a0909090922696e6465786564223a2066616c73652c0a09090909226e616d65223a20227465737432222c0a090909092274797065223a2022737472696e67220a0909097d0a09095d2c0a0909226e616d65223a2022746573744c6f67222c0a09092274797065223a20226576656e74220a097d0a5d'''
    //'[ { \"constant\": true, \"inputs\": [], \"name\": \"getMultiReturn\", \"outputs\": [ { \"name\": \"\", \"type\": \"string\" }, { \"name\": \"\", \"type\": \"string\" } ], \"payable\": false, \"stateMutability\": \"view\", \"type\": \"function\" } ]'

    /*"[\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getMultiReturn\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t}\n" +
            "]"*/



    "[\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"intParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnBool\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"uint256[]\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"arrayParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value2\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"multiParams\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"throwException\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnUint\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getVar5\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"setVar6Var4\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"var6\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var1\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var2\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var3\",\n" +
            "\t\t\t\t\"type\": \"uint8\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"var4\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getMultiReturn\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"param\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"uintParam\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"var5\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"incVar6Var1\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnAddress\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"logEvent\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"intVar\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"setVar5\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"returnInt\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"int256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"value\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"boolParamType\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"constructor\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"test1\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"test2\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"testLog\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t}\n" +
            "]"
}
