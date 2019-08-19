# Payment Transfer Microservice 

## Overview
 Payment Transfer Microservice is a demo application for money transfer between two sources/money-wallets/accounts. Keeping simplicity , I have used two sources say, Google Pay and Amazon Pay.This microservice is responsible for adding/deleting/transferring money from Amazon Pay to Google Pay using simple REST APIs.

The detail functionalities are,

At the time of deploying the application Rs.6000.00 will be added to the Amazon Pay. Now, Payment operations API we can add/ retrieve/delete amounts from Amazon Pay. Using Transfer management API we can transfer any amount from Amazon Pay to Google Pay. The respective APIs are,

Payment operations API
	1. POST (/payment) - Adding amount to Amazon Pay.
	2. GET (/payment/{source-id}) - Checking the Balance of the accounts, i.e. source-id=1 for Amazon Pay and source-id=2 for Google Pay.
	3. DELETE (/payment/{transactionId}) - Every transaction (ADD) will have a unique Id. Using the unique Id we can remove the whole transaction details from Amazon Pay.
	
Transfer management API
	1. PUT (/transfer) - Transfer the whole amount from Amazon Pay to Google Pay.
	2. PUT (/transfer/{amount}) - Transfer an amount from Amazon Pay to Google Pay.
	
The model for addition of payment details into Amazon Pay is -
{
	sourceId * --Integer
	sourceName --String
	amount --Integer
	currency --String
}
Please check swagger page for more details and APIs.

Points considered -
	1. At the time of deploying the application Rs.6000.00 will be added to amazon pay and Rs.0 to Google Pay.
	2. Every add transaction in Amazon Pay will include a unique Id as transaction Id. This can be used to remove/withdraw the transaction.
	3. Only transfer and balance checking functionality is available for Google Pay account.
	4. All operations are maintained with default currency INR.
	 
## Getting Started
Some used technology stacks are,
	1. Java 8
	2. Dropwizard 
	3. Swagger
	4. Maven
	5. PowerMockito
	6. Clover

### Prerequisites
	* Git
	* JDK 8 or later
	* Maven

### Clone and Configuration
To get started you can simply clone this repository using git:
```
From the project directory
	To build the application - "mvn clean install"
	To deploy the application - "java -jar target\PaymentTransferMicroservice-1.0.0-SNAPSHOT.jar server src\main\resources\payment-microservice.yaml", 
								 the application API documentation and API testing can be done with swagger and URL for accessing the same is - http://localhost:8090/api/payment-transfer/swagger
	To generate Clover reports for test coverage (Only unit tests) - "mvn clean clover:setup test clover:aggregate clover:clover", 
													The report will be generated at - PaymentMicroservice\target\site\clover\dashboard.html

```