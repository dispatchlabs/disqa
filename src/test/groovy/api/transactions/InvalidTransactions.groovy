package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.dispatchlabs.io.testing.common.Utils
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.bouncycastle.jcajce.provider.digest.Keccak
import org.bouncycastle.util.encoders.Hex

import javax.xml.bind.DatatypeConverter

import static org.hamcrest.Matchers.equalTo

class InvalidTransactions {
    public static sendCustomTransaction(def params){
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
        long time
        if(params.Time)
            time = params.Time
        else
            time = System.currentTimeMillis()
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(from));
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(to));
        byteArrayOutputStream.write(Utils.longToBytes(value));
        byteArrayOutputStream.write(Utils.longToBytes(time));

        Keccak.Digest256 digestSHA3 = new Keccak.Digest256();
        digestSHA3.update(byteArrayOutputStream.toByteArray());
        byte[] hashBytes = digestSHA3.digest();
        String hash = Hex.toHexString(hashBytes)
        String signatureStr = Utils.sign(privateKey,hash)
        request.contentType(ContentType.JSON)
                .body("{\"hash\":\"$hash\",\"type\":0,\"from\":\"$from\",\"to\":\"$to\",\"value\":$value,\"time\":$time,\"signature\":\"$signatureStr\"}")
                .log().all()
        Response response = request.post("/v1/transactions")
        response.then().log().all()

        //Verify response (optional)
        if(params.Status)
            response.then().assertThat().body("status",equalTo(params.Status))

        return response
    }
    /*
    @Test(description="Invalid transaction",groups = ["smoke", "transactions"])
    public void transactionRegression7_DelegateNegativeTokens(){

        def Body = [:]
        Body = [
                "hash":"blah",
                "blah":"asdf"
        ]
        JsonOutput.toJson(Body)

        def response = sendTransaction Node:allNodes.Delegates.Delegate1, Value:999, PrivateKey:"Genesis",
                To:allNodes.Delegates.Delegate0.address ,From: "Genesis"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999

        response = sendTransaction Node:allNodes.Delegates.Delegate2, Value:0.5, PrivateKey:allNodes.Delegates.Delegate0.privateKey,
                To:allNodes.Delegates.Delegate1.address ,From: allNodes.Delegates.Delegate0.address, Status: "Ok"
        waitForTransactionStatus ID:response.then().extract().path("id") ,Node:allNodes.Delegates.Delegate0, Status: "Ok", Timeout: 10
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
        verifyConsensusForAccount Nodes:allNodes.Delegates, ID:allNodes.Delegates.Delegate0.address, Status: "Ok", Balance: 999
    }
    */
}
