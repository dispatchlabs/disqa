package com.dispatchlabs.io.testing.common

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import groovy.json.JsonOutput
import org.bouncycastle.jcajce.provider.digest.Keccak
import org.bouncycastle.util.encoders.Hex

import javax.xml.bind.DatatypeConverter

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasItem

class APIs {

    public static sendTransaction(def params){
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
        if(params.Log != false){
            request.log().all()
        }
        if(params.ReturnRequest == true){
            return request
        }
        Response response = request.post("/v1/transactions")
        if(params.Log != false){
            response.then().log().all()
        }

        //Verify response (optional)
        if(params.Status)
            response.then().assertThat().body("status",equalTo(params.Status))

        return [Hash:hash,Response:response]
    }

    public static customAPICall(def params){
        RequestSpecification request = RestAssured.given()
        request.baseUri("http://"+params.IP+":"+params.HttpPort)

        request.contentType(ContentType.JSON)
                .body(params.Body)
                .log().all()
        Response response = request.post(params.API)
        response.then().log().all()

        //Verify response (optional)
        if(params.Status) {
            response.then().assertThat().body("status", equalTo(params.Status))
        }

        //Verify return code (optional)
        if(params.StatusCode){
            response.then().statusCode(params.StatusCode)
        }

        return response
    }

    public static waitForTransactionStatus(def params){
        def status
        int timeout = 20
        if(params.Timeout)
            timeout = params.Timeout
        else
            params.Timeout = timeout
        while (timeout>0){
            RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
            request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
            Response response = request.get("/v1/receipts/"+params.ID)
            response.then().log().all()
            if(params.DataStatus){
                status = params.DataStatus
                if (response.then().statusCode(200).extract().path("data") != null){
                    if(response.then().statusCode(200).extract().path("data.status") == params.DataStatus) return response
                }
            }
            else{
                status = params.Status
                if(response.then().statusCode(200).extract().path("status") == params.Status) return response
            }
            sleep(1000)
            timeout--
        }
        assert false, "Error: status ${status} for transaction ${params.ID} was not found in ${params.Timeout} seconds."
    }

    public static verifyConsensusForAccount(def params){
        sleep(10000)
        params.Nodes.each{Name,Node->
            RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
            request.baseUri("http://"+Node.IP+":"+Node.HttpPort)
            Response response = request.get("/v1/accounts/"+params.ID)
            response.then().log().all()
            if(params.Status)
                response.then().assertThat().body("status",equalTo(params.Status))
            if(params.Balance)
                response.then().assertThat().body("data.balance",equalTo(params.Balance))
        }
    }

    public static verifyStatusForTransaction(def params){
        sleep(10000)
        params.Nodes.each{Node->
            RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
            request.baseUri("http://"+Node.IP+":"+Node.HttpPort)
            Response response = request.get("/v1/receipts/"+params.ID)
            response.then().log().all()
            if(params.Status == "InternalError")
                response.then().assertThat().body("data.status",equalTo(params.Status))
            else if(params.Status)
                response.then().assertThat().body("status",equalTo(params.Status))
            if(params.Balance)
                response.then().assertThat().body("data.balance",equalTo(params.Balance))
            if(params.ContractResult)
                response.then().assertThat().body("data.contractResult",equalTo(params.ContractResult))
            if(params.HumanReadable)
                response.then().assertThat().body("data.humanReadableStatus",equalTo(params.HumanReadable))
        }
    }

    public static verifyDelegates(def params){
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/delegates")
        response.then().log().all()
        params.Delegates.each{Name,Delegate->
            println(Delegate.address)
            response.then().assertThat().body("data.address",hasItem(Delegate.address))
        }
    }

    public static verifyTransactionsByFrom(def params){
        sleep(10000)
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/transactions/from/"+params.Address)
        response.then().log().all()

    }

    public static verifyTransactionsByTo(def params){
        sleep(10000)
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/transactions/to/"+params.Address)
        response.then().log().all()

    }

    public static verifyQueue(def params){
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/queue")
        response.then().log().all()
        return response
    }

    public static createWallet(){
        Key key = new Key();
        byte[] publicKey = key.getPublicKeyBytes();

        System.out.println(Utils.toHexString(publicKey));


        byte[] hashablePublicKey = new byte[publicKey.length-1];
        for (int i=1; i<publicKey.length; i++) {
            hashablePublicKey[i-1] = publicKey[i];
        }
        Keccak.Digest256 keccak = new Keccak.Digest256();
        keccak.update(hashablePublicKey);
        byte[] hash = keccak.digest();

        byte[] address = new byte[20];
        for (int i=0; i<address.length; i++) {
            address[i] = hash[i+12];
        }

        return [Address:Utils.toHexString(address),PrivateKey:key.getPrivateKey()];
    }

}
