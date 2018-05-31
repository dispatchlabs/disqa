package com.dispatchlabs.io.testing.common

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
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

        String code = ""
        String method = ""
        long hertz
        String fromName = ""
        String toName = ""

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
        byteArrayOutputStream.write(0); //type
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(from));
        byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(to));
        byteArrayOutputStream.write(Utils.longToBytes(value));
        //byteArrayOutputStream.write(value.toByteArray());
        //byteArrayOutputStream.write(Utils.doubleToBytes(value));
        byteArrayOutputStream.write(Utils.longToBytes(time));
        //byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(code));
        //byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(method));
        //byteArrayOutputStream.write(0); //hertz
        //byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(fromName));
        //byteArrayOutputStream.write(DatatypeConverter.parseHexBinary(toName));

        Keccak.Digest256 digestSHA3 = new Keccak.Digest256();
        digestSHA3.update(byteArrayOutputStream.toByteArray());
        byte[] hashBytes = digestSHA3.digest();
        String hash = Hex.toHexString(hashBytes)
        String signatureStr = Utils.sign(privateKey,hash)
        request.contentType(ContentType.JSON)
                //.body("{\"hash\":\"$hash\",\"type\":0,\"from\":\"$from\",\"to\":\"$to\",\"value\":$value,\"time\":$time,\"code\":\"$code\",\"method\":\"$method\",\"hertz\":0,\"fromName\":\"$fromName\",\"toName\":\"$toName\",\"signature\":\"$signatureStr\"}")
                .body("{\"hash\":\"$hash\",\"type\":0,\"from\":\"$from\",\"to\":\"$to\",\"value\":$value,\"time\":$time,\"signature\":\"$signatureStr\"}")
                .log().all()
        Response response = request.post("/v1/transactions")
        response.then().log().all()

        //Verify response (optional)
        if(params.Status)
            response.then().assertThat().body("status",equalTo(params.Status))

        return response
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
        int timeout = 20
        if(params.Timeout)
            timeout = params.Timeout
        else
            params.Timeout = timeout

        while (timeout>0){
            RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
            request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
            Response response = request.get("/v1/statuses/"+params.ID)
            response.then().log().all()
            if(response.then().statusCode(200).extract().path("status") == params.Status) return
            sleep(1000)
            timeout--
        }

        assert false, "Error: status ${params.Status} for transaction ${params.ID} was not found in ${params.Timeout} seconds."
    }

    public static verifyConsensusForAccount(def params){
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
        params.Nodes.each{Node->
            RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
            request.baseUri("http://"+Node.IP+":"+Node.HttpPort)
            Response response = request.get("/v1/statuses/"+params.ID)
            response.then().log().all()
            if(params.Status)
                response.then().assertThat().body("status",equalTo(params.Status))
            if(params.Balance)
                response.then().assertThat().body("data.balance",equalTo(params.Balance))
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
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/transactions/from/"+params.Address)
        response.then().log().all()

    }

    public static verifyTransactionsByTo(def params){
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).log().all()
        request.baseUri("http://"+params.Node.IP+":"+params.Node.HttpPort)
        Response response = request.get("/v1/transactions/to/"+params.Address)
        response.then().log().all()

    }

}
