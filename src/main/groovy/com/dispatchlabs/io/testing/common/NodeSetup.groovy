package com.dispatchlabs.io.testing.common

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.file.Files

class NodeSetup {
    private static portRange = [3500,4000]
    private static int lastPort = 0
    private static thisIP = "127.0.0.1"
    public static genPrivateKey = "0f86ea981203b26b5b8244c8f661e30e5104555068a4bd168d3e3015db9bb25a"
    //public static genPrivateKey = "d3386ee62b2cf413abf5d5a109e0cf97ba158a33ede5efb9cb0d75a68e74bc51"
    public static genAddress = "3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c"
    //public static genAddress = "937db6dc29984f3887dff2d2524b29e8ac2579b1"
    private static genTransaction = "{\"hash\":\"a48ff2bd1fb99d9170e2bae2f4ed94ed79dbc8c1002986f8054a369655e29276\",\"type\":0,\"from\":\"e6098cc0d5c20c6c31c4d69f0201a02975264e94\",\"to\":\"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c\",\"value\":10000000,\"data\":\"\",\"time\":0,\"signature\":\"03c1fdb91cd10aa441e0025dd21def5ebe045762c1eeea0f6a3f7e63b27deb9c40e08b656a744f6c69c55f7cb41751eebd49c1eedfbd10b861834f0352c510b200\",\"hertz\":0,\"fromName\":\"\",\"toName\":\"\"}"
    //private static genTransaction = "{\"hash\":\"9e93b5165c148aa9aee9498967be6e37fcc67d40b842b63a8cd28aa9ac9125dd\",\"type\":0,\"from\":\"e6098cc0d5c20c6c31c4d69f0201a02975264e94\",\"to\":\"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c\",\"value\":18000000000000000000000000000,\"data\":\"\",\"time\":0,\"signature\":\"2E2FC81FE735F5712A3390A565D48057DFAB2C66B16A4C72F17B0A1757FE66E545BD4863D807994EA3BF5BC6E1125935DB3A3696B0D9B35255F4574142892AE000\",\"hertz\":0,\"fromName\":\"\",\"toName\":\"\"}"
    //private static genTransaction = '''{\"hash\":\"da9ac662faf0a353d210b39fc1fbd44aadb470bfb7abceceec15bede74df21b2\",\"type\":0,\"from\":\"f74abc9a20c64bd04398fd5037f48811be57b645\",\"fromName\":null,\"to\":\"937db6dc29984f3887dff2d2524b29e8ac2579b1\",\"toName\":null,\"value\":1000000000,\"time\":0,\"signature\":\"536d4cd992501ef410d5d981651cbb8823140b3a8271a88d8e7d55e5de1304b76cd62a9882674870586fa0616554c64a6f214ece4f6a8cb7ab862e7c26ae8a0e00\"}'''


    public static def quickSetup(def params){
        File directory = new File(System.getenv("NODES_DIR"))
        File exeLocation = new File(System.getenv("TEST_EXE_LOCATION"))

        def allNodes = [:]
        def returnNodes = [:]
        if(params.Seed){
            returnNodes.Seeds = [:]
            params.Seed.times{
                returnNodes.Seeds["Seed$it"] = [IsDelegate:false,IsSeed:true]
                allNodes["Seed$it"] = returnNodes.Seeds["Seed$it"]
            }
        }
        if(params.Delegate){
            returnNodes.Delegates = [:]
            params.Delegate.times{
                returnNodes.Delegates["Delegate$it"] = [IsDelegate:true,IsSeed:false]
                allNodes["Delegate$it"] = returnNodes.Delegates["Delegate$it"]
            }
        }
        if(params.Regular) {
            returnNodes.Regulars = [:]
            params.Regular.times {
                returnNodes.Regulars["Regular$it"] = [IsDelegate: false, IsSeed: false]
                allNodes["Regular$it"] = returnNodes.Regulars["Regular$it"]
            }
        }
        setupNodes(allNodes,directory,exeLocation)
        println JsonOutput.prettyPrint(JsonOutput.toJson(allNodes))
        return returnNodes
    }

    public static void setupNodes(def nodeSetup,File directory,File exeLocation){
        sleep(1000)
        if(lastPort == 0) lastPort = portRange[0]
        assert lastPort < portRange[1],"Error: out of ports!"
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            "Taskkill /IM disgo.exe /F".execute().waitFor()
        }
        else{
            "pkill disgo".execute().waitFor()
        }
        sleep(2000)
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
            if(setup.IsSeed == true) allSeeds << [host:"127.0.0.1",port:setup.GrpcPort]
        }

        nodeSetup.each{nodeID,setup->
            def config = [
                    httpEndpoint:[
                            host:setup.IP,
                            port:setup.HttpPort
                    ],
                    grpcEndpoint:[
                            host:setup.IP,
                            port:setup.GrpcPort
                    ],
                    "grpcTimeout": 5,
                    "useQuantumEntropy": false,
                    "seedEndpoints": allSeeds,
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
            setup.startProcess = {return new ProcessBuilder(basePath+"/$exeName")
                    .directory(new File(basePath))
                    .redirectOutput(new File(basePath+'/output.log'))
                    .redirectErrorStream(true).start()
            }
            setup.disgoProc = setup.startProcess()
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
                    setup.address = InputJSON.address
                    setup.privateKey = InputJSON.privateKey
                    return
                }
                timeout--
            }
            assert false,"Error unable to get wallet ID from: ${nodeID} in 10 seconds"
        }
    }
}
