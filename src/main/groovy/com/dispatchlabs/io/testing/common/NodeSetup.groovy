package com.dispatchlabs.io.testing.common

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.file.Files
import java.nio.file.Path

class NodeSetup {
    private static portRange = [3500,4000]
    private static int lastPort = 0
    private static thisIP = "127.0.0.1"
    public static genPrivateKey = "646449644112bd02641a9e5ca3fa6070ddc1af072210b77dda5e38b203a95033"
    public static genWalletID = "937db6dc29984f3887dff2d2524b29e8ac2579b1"
    private static genTransaction = '''{\"hash\":\"da9ac662faf0a353d210b39fc1fbd44aadb470bfb7abceceec15bede74df21b2\",\"type\":0,\"from\":\"f74abc9a20c64bd04398fd5037f48811be57b645\",\"fromName\":null,\"to\":\"937db6dc29984f3887dff2d2524b29e8ac2579b1\",\"toName\":null,\"value\":1000000000,\"time\":0,\"signature\":\"536d4cd992501ef410d5d981651cbb8823140b3a8271a88d8e7d55e5de1304b76cd62a9882674870586fa0616554c64a6f214ece4f6a8cb7ab862e7c26ae8a0e00\"}'''


    public static def quickSetup(def params){
        File directory = new File(System.getenv("NODES_DIR"))
        File exeLocation = new File(System.getenv("TEST_EXE_LOCATION"))

        def allNodes = [:]
        if(params.Seed){
            params.Seed.times{
                allNodes["Seed$it"] = [IsDelegate:false,IsSeed:true]
            }
        }
        if(params.Delegate){
            params.Delegate.times{
                allNodes["Delegate$it"] = [IsDelegate:true,IsSeed:false]
            }
        }
        if(params.Regular) {
            params.Regular.times {
                allNodes["Regular$it"] = [IsDelegate: false, IsSeed: false]
            }
        }
        setupNodes(allNodes,directory,exeLocation)
        println JsonOutput.prettyPrint(JsonOutput.toJson(allNodes))
        return allNodes
    }

    public static void setupNodes(def nodeSetup,File directory,File exeLocation){
        if(lastPort == 0) lastPort = portRange[0]
        assert lastPort < portRange[1],"Error: out of ports!"
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            "Taskkill /IM disgo.exe /F".execute().waitFor()
        }
        else{
            "pkill disgo".execute().waitFor()
        }
        if(directory.exists()){
            directory.deleteDir()
            directory.mkdirs()
        }

        def allDelegates = []
        def allSeeds = []
        //find all delegates and create a list of them
        nodeSetup.each { nodeID, setup ->
            setup.IP = thisIP
            setup.HttpPort = lastPort
            setup.GrpcPort = lastPort+1
            lastPort = lastPort+2
            if(setup.IsDelegate == true) allDelegates << "127.0.0.1:$setup.GrpcPort"
            if(setup.IsSeed == true) allSeeds << "127.0.0.1:$setup.GrpcPort"
        }

        nodeSetup.each{nodeID,setup->
            def config = [
                    "HttpPort": setup.HttpPort,
                    "HttpHostIp": setup.IP,
                    "GrpcPort": setup.GrpcPort,
                    "GrpcTimeout": 5,
                    "UseQuantumEntropy": false,
                    "IsSeed": setup.IsSeed,
                    "IsDelegate": setup.IsDelegate,
                    "SeedList": allSeeds,
                    "DaposDelegates": allDelegates,
                    "NodeId": nodeID,
                    "ThisIp": setup.IP+":"+setup.GrpcPort,
                    "genesisTransaction":genTransaction
            ]
            config = JsonOutput.toJson(config)
            def basePath = directory.getAbsolutePath()+"/"+nodeID
            new File(basePath).mkdir()
            new File(basePath+"/config").mkdir()
            new File(basePath+"/config/config.json").write config
            def exeName = "disgo"
            if(System.getProperty("os.name").toLowerCase().contains("win")){
                exeName = "disgo.exe"
            }
            Files.copy(exeLocation.toPath(),new File(basePath+"/$exeName").toPath())
            setup.procBuilder = new ProcessBuilder(basePath+"/$exeName")
                    .directory(new File(basePath))
                    .redirectOutput(new File(basePath+'/output.log'))
                    .redirectErrorStream(true)
            setup.disgoProc = setup.procBuilder.start()
        }

        nodeSetup.each { nodeID, setup ->
            def basePath = directory.getAbsolutePath()+"/"+nodeID
            //wait for wallet ID
            int timeout = 20
            while (timeout> 0){
                sleep(500)
                if(new File(basePath+"/config/account.json").exists()){
                    def inputFile = new File(basePath+"/config/account.json")
                    def InputJSON = new JsonSlurper().parseText(inputFile.text)
                    setup.walletID = InputJSON.address
                    setup.privateKey = InputJSON.privateKey
                    return
                }
                timeout--
            }
            assert false,"Error unable to get wallet ID from: ${nodeID} in 10 seconds"
        }
    }
}
