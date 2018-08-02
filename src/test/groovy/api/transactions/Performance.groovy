package api.transactions

import com.dispatchlabs.io.testing.common.NodeSetup
import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import org.testng.annotations.Test

import static com.dispatchlabs.io.testing.common.APIs.sendTransaction
import static com.jayway.restassured.config.RestAssuredConfig.*
import com.jayway.restassured.config.HttpClientConfig

class Performance {
    def allNodes

    @Test(description="Send as many transactions as possible",groups = ["performance"])
    public void performance(){
        System.setProperty("http.maxConnections","10000");
        //RestAssured.config = newConfig().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance());
        def users = []
        allNodes = NodeSetup.quickSetup Delegate: 4,Seed: 1,Regular: 0
        4.times {
            def user = new PerfUser()
            user.node = allNodes.Delegates.Delegate0
            users << user
            user.start()
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

    @Override
    public void run() {
        while (true){
            def requests = []
            500.times{
                def request = sendTransaction Node:node, Value:1, PrivateKey:"Genesis",
                        To:node.address ,From: "Genesis",Log: false,ReturnRequest:true
                requests << request
                sleep(1)
            }
            println "posting"
            //println requests.size()
            requests.each {
                Response response = it.post("/v1/transactions")
                //response.then().log().all()
                count++
            }
//            sendTransaction Node:node, Value:1, PrivateKey:"Genesis",
//                        To:node.address ,From: "Genesis",Log: false
//            count++
        }
    }
}