package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.testng.annotations.Test

import java.nio.charset.StandardCharsets
import java.util.concurrent.CyclicBarrier

import static com.dispatchlabs.io.testing.common.APIs.sendTransaction
import static com.jayway.restassured.config.RestAssuredConfig.*
import com.jayway.restassured.config.HttpClientConfig


class Performance {
    def allNodes
    CyclicBarrier gate;

    @Test(description="Send as many transactions as possible",groups = ["performance"])
    public void performance(){
        System.setProperty("http.maxConnections","10000");
        //RestAssured.config = newConfig().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance());
        def users = []
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
        gate = new CyclicBarrier(2);
        2.times {
            def user = new PerfUser()
            user.node = allNodes.Delegates.Delegate0
            users << user
            user.start()
            user.gate = gate
        }
        println users.size()
        while(true){
            sleep(1*60*1000)
            def total = 0
            users.each {
                total = total + it.count
            }
            println total
            println "********************"
        }

    }
}

public class PerfUser extends Thread  {
    def node
    def count = 0
    def gate

    @Override
    public void run() {
        while (true){
            def requests = []
            3000.times{
                println it
                def request = sendTransaction Node:node, Value:1, PrivateKey:"Genesis",
                        To:node.address ,From: "Genesis",Log: false,ReturnRequest:true
                requests << request
                sleep(1)
            }
            gate.await()
            println "posting"
            //println requests.size()
            println new Date()
            println System.currentTimeMillis()
            requests.each {RequestSpecification post->
                URL url = new URL(requests[0].baseUri+"/v1/transactions")
                URLConnection con = url.openConnection()
                HttpURLConnection http = (HttpURLConnection)con
                http.setRequestMethod("POST")
                http.setDoOutput(true)
                http.setFixedLengthStreamingMode(439)
                http.setRequestProperty("Content-Length", "439");
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                http.connect();
                OutputStream os = http.getOutputStream()
                byte[] out = post.requestBody.getBytes(StandardCharsets.UTF_8)
                os.write(out)
                os.flush()
                os.close()
//                def buffer = new byte[1024];
//                while ((http.getInputStream().read(buffer)) > 0) {
//
//                }
                //http.getResponseCode()
                //assert http.getResponseCode() == 200

                //Response response = post.post("/v1/transactions")
                //response.then().statusCode(200)
                sleep(10)
                //response.then().log().all()
                count++
            }
            println new Date()
            println System.currentTimeMillis()
            gate.await()
            System.exit(0)
//            sendTransaction Node:node, Value:1, PrivateKey:"Genesis",
//                        To:node.address ,From: "Genesis",Log: false
//            count++
        }
    }
}