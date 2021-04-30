def checkoutApi = { String subFolder ->
    checkout([
            $class      : "GitSCM",
            branches    : [[name: "*/main"]],
            extensions  : [[ $class: "RelativeTargetDirectory",
                             relativeTargetDir: subFolder
                           ]],
            userRemoteConfigs: [[
                                        url : 'git@github.com:ccx54392/corecodeproject_api_.git',
                                        credentialsId: "myprivatesshkey"
                                ]]
    ])
}

def checkoutTesting = { String subFolder ->
    checkout([
            $class      : "GitSCM",
            branches    : [[name: "*/main"]],
            extensions  : [[ $class: "RelativeTargetDirectory",
                             relativeTargetDir: subFolder
                           ]],
            userRemoteConfigs: [[
                                        url : 'git@github.com:JosueDa/corecodeproject_testing.git',
                                        credentialsId: "myprivatesshkey"
                                ]]
    ])
}

def runner = { commandToExecute -> isUnix() ? sh(commandToExecute) : bat(commandToExecute) }


unitTest:{
    stage("Unit Testing"){
        node("windowsNode"){
            checkoutApi("tests")
            runner 'cd tests && mvn test -Dgroups=unitTest'
        }
    }
}
IntegrationTest:{
    stage("Integration Testing"){
        node("windowsNode"){
            checkoutTesting("tests")
            runner 'cd tests && mvn test -Dgroups=integrationTest -DapiUrl=${apiUrl}'
        }
    }
}
SystemTest:{
    stage("System or E2E Testing"){
        node("windowsNode"){
            checkoutTesting("tests")
            runner 'cd tests && mvn test -Dgroups=systemTest -DwebUrl=${webUrl} -Dbrowser=${browser}'
        }
    }
}
