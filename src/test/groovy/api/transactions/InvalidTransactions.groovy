package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.Utils
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import groovy.json.JsonOutput
import org.bouncycastle.jcajce.provider.digest.Keccak
import org.bouncycastle.util.encoders.Hex
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.xml.bind.DatatypeConverter

import static com.dispatchlabs.io.testing.common.APIs.*
import static org.hamcrest.Matchers.equalTo

class InvalidTransactions {
    public static sendcustomTransaction(def params){
        RequestSpecification request = RestAssured.given()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)

        String privateKey
        if(params.PrivateKey == "Genesis")
            privateKey = NodeSetup.genPrivateKey
        else
            privateKey = params.PrivateKey

        String from
        if(params.From == "Genesis")
            from = NodeSetup.genAddress
        else
            from = params.From

        String to = params.To
        long value = params.Value
        //BigInteger value = params.Value
        //double value = params.Value
        long time
        if(params.Time!=null)
            time = params.Time
        else
            time = System.currentTimeMillis()

        def code = ""
        def method = ""
        def abi = ""
        def paramsToContract = null
        def hertz = 0
        def fromName = ""
        def toName = ""
        byte type = 0
        if(params.Type) type = params.Type

        if(params.Code != "" && params.Code != null) code = params.Code
        if(params.Method != "" && params.Method != null) method = params.Method
        if(params.ABI != "" && params.ABI != null) abi = params.ABI
        if(params.Params != null) paramsToContract = params.Params
        if(params.Hertz != 0 && params.Hertz != null) hertz = params.Hertz

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
        byteArrayOutputStream.write(type);
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(from));
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(to));
        byteArrayOutputStream.write(Utils.longToBytes(value));
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(code));
        byteArrayOutputStream.write(abi.getBytes("UTF-8"));
        byteArrayOutputStream.write(method.getBytes("UTF-8"));
        //byteArrayOutputStream.write(code.getBytes());
        byteArrayOutputStream.write(Utils.longToBytes(time));


        Keccak.Digest256 digestSHA3 = new Keccak.Digest256();
        digestSHA3.update(byteArrayOutputStream.toByteArray());
        byte[] hashBytes = digestSHA3.digest();
        String hash = Hex.toHexString(hashBytes)
        String signatureStr = Utils.sign(privateKey,hash)

        def transaction = [
                code:code,
                hash:hash,
                type:type,
                from:from,
                to:to,
                value:value,
                time:time,
                signature:signatureStr,
                params:paramsToContract,
                abi:abi,
                method:method,
                hertz:hertz,
                fromName:"",
                toName:""
        ]
        request.contentType(ContentType.JSON)
                .body(JsonOutput.toJson(transaction))
                .log().all()
        Response response = request.post("/v1/transactions")
        response.then().log().all()

        //Verify response (optional)
        if(params.Status)
            response.then().assertThat().body("status",equalTo(params.Status))

        return response
    }

    def allNodes
    def delegates

    @BeforeMethod
    public void baseState(){
        //create and start all needed nodes for each test
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
    }

    @Test(description="Delegate to delegate: 999 token transfer",groups = ["transactions"])
    public void transactions_API(){
        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendcustomTransaction Node:allNodes.Delegates.Delegate2, Value:15, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address, Status: "InvalidTransaction"
        //waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate1, Status: "Ok", Timeout: 10
        sleep(2000)
        //verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate1.address, Status: "Ok", Balance: 15
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
    }
}
