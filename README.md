# disqa
QA Automation Scripts

To build and run smoke tests:
gradlew.bat test

To run a specific group of tests:
gradlew.bat test -P testGroups=load

To run a specific test:
gradlew.bat test --tests api.transactions.RegressionTransactions.transactions_API101

