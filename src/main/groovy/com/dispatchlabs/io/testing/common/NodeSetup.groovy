package com.dispatchlabs.io.testing.common

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.file.Files

class NodeSetup {
    private static portRange = [3500,4000]
    private static int lastPort = 0
    private static thisIP = "127.0.0.1"
    public static genPrivateKey = "0f86ea981203b26b5b8244c8f661e30e5104555068a4bd168d3e3015db9bb25a"
    public static genAddress = "3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c"
    private static genTransaction = "{\"hash\":\"a48ff2bd1fb99d9170e2bae2f4ed94ed79dbc8c1002986f8054a369655e29276\",\"type\":0,\"from\":\"e6098cc0d5c20c6c31c4d69f0201a02975264e94\",\"to\":\"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c\",\"value\":10000000,\"data\":\"\",\"time\":0,\"signature\":\"03c1fdb91cd10aa441e0025dd21def5ebe045762c1eeea0f6a3f7e63b27deb9c40e08b656a744f6c69c55f7cb41751eebd49c1eedfbd10b861834f0352c510b200\",\"hertz\":0,\"fromName\":\"\",\"toName\":\"\"}"
    private static genTokens = '{\n' +
            '  "address": "3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",\n' +
            //'  "balance": 10000000\n' +
            '  "balance": 900000000000\n' +
            '}'

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
                returnNodes.Regulars["Regular$it"] = [IsRegular:true,IsDelegate: false, IsSeed: false]
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
            //assert directory.exists() == false,"Error, unable to delete directory: ${directory}"
            directory.mkdirs()
        }


        def allDelegates = []
        def allSeeds = []
        def seedAddresses = []
        //find all delegates and create a list of them
        nodeSetup.each { nodeID, setup ->
            setup.IP = thisIP
            setup.HttpPort = lastPort
            setup.GrpcPort = lastPort+1
            lastPort = lastPort+2
            //if(setup.IsDelegate == true) allDelegates << [host:"127.0.0.1",port:setup.GrpcPort]
            //if(setup.IsSeed == true) allSeeds << [host:"127.0.0.1",port:setup.GrpcPort]
        }

        def createNodeConfig = {nodeID,setup->
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
                "seeds": allSeeds,
                //delegates:[],
                isBookkeeper:true,
                //"genesisTransaction":genTransaction,
                rateLimits:[
                        //"minTTL": 10000000000,
                        "minTTL": 20000000000,
                        "txPerMinute": 10,
                        avgHzPerTxn:10
                ]
        ]
//            if(setup.IsDelegate){
//                config."seedAddresses" = seedAddresses
//            }
            config = JsonOutput.toJson(config)
            setup.config = config
            def basePath = directory.getAbsolutePath()+"/"+nodeID
            new File(basePath).mkdir()
            new File(basePath+"/config").mkdir()
            setup.configDir = new File(basePath+"/config").absolutePath
            new File(basePath+"/config/config.json").write config
            new File(basePath+"/config/genesis_account.json").write genTokens
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
            //sleep(2000)
        }
//        nodeSetup.each{nodeID,setup->
//            if(setup.IsDelegate) createNodeConfig(nodeID,setup)
//        }

        def getAddress = { nodeID, setup ->
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
            assert false,"Error unable to get address from: ${nodeID} in 10 seconds"
        }

        nodeSetup.each{nodeID,setup->
            if(setup.IsSeed) {
                createNodeConfig(nodeID,setup)
                getAddress(nodeID,setup)
                allSeeds << [type:"Seed",grpcEndpoint:[host:"127.0.0.1",port:setup.GrpcPort],httpEndpoint:[host:"127.0.0.1",port:setup.HttpPort],address:setup.address]
                //seedAddresses << setup.address
            }
        }
        nodeSetup.each { nodeID, setup ->
            if(setup.IsDelegate || setup.IsRegular) {
                createNodeConfig(nodeID,setup)
                getAddress(nodeID,setup)
                //if(setup.IsDelegate) allDelegates << [type:"Delegate",grpcEndpoint:[host:"127.0.0.1",port:setup.GrpcPort],httpEndpoint:[host:"127.0.0.1",port:setup.HttpPort],address:setup.address]
                if(setup.IsDelegate) allDelegates << setup.address
            }
        }
        lastPort = 0

        nodeSetup.each{nodeID,setup->
            if(setup.IsSeed) {
                def config = new JsonSlurper().parseText(new File(setup.configDir+"/config.json").text)
                config.delegateAddresses = allDelegates
                new File(setup.configDir+"/config.json").write JsonOutput.toJson(config)
                setup.disgoProc.destroy()
                //sleep(1000)
                setup.disgoProc = setup.startProcess()
                //sleep(2000)
            }
        }

        nodeSetup.each { nodeID, setup ->
            if(setup.IsDelegate || setup.IsRegular) {
                setup.disgoProc.destroy()
                setup.disgoProc = setup.startProcess()
                sleep(2000)
            }
        }

    }
}
