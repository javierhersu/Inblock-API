var Hello = artifacts.require("./Hello");
//var FacturaeSmartContract = artifacts.require("./FacturaeSmartContract");

module.exports = function(deployer) {
    deployer.deploy(Hello);
};
