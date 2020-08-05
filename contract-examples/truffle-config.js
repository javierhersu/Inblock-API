const PrivateKeyProvider = require("truffle-hdwallet-provider");
const privateKey = "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
const privateKey2 = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";
const privateKey3 = "ae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f";
const jsonRpcEndpoint = "http://localhost:8545";
const PrivateKeyProvider2 = require("@truffle/hdwallet-provider");
const privateKeyProvider = new PrivateKeyProvider2(privateKey, jsonRpcEndpoint);
const privateKeyProvider2 = new PrivateKeyProvider2(privateKey2, jsonRpcEndpoint);
const privateKeyProvider3 = new PrivateKeyProvider2(privateKey3, jsonRpcEndpoint);
//const privateKeyProvider4 = new PrivateKeyProvider2(privateKey, jsonRpcEndpoint);

module.exports = {
  // See <http://truffleframework.com/docs/advanced/configuration>
  // for more about customizing your Truffle configuration!
  networks: {
    development: {
      host: "127.0.0.1",
      port: 8545,
      network_id: "*" // Match any network id
    },
    quickstartWallet: {
      provider: () => new PrivateKeyProvider(privateKey, jsonRpcEndpoint),
      network_id: "*"
    },
    besuWallet: {
      provider: privateKeyProvider,
      network_id: "*"
    },
    besuWallet2: {
      provider: privateKeyProvider2,
      network_id: "*"
    },
    besuWallet3: {
      provider: privateKeyProvider3,
      network_id: "*"
    },
    orion1:  {
      host: "localhost",
      port: 20000,
      network_id: "*",
      gasPrice: 0,
      from: '0x866b0df7138daf807300ed9204de733c1eb6d600'
    },
    orion2:  {
      host: "localhost",
      port: 20002,
      network_id: "*"
    },
    orion3:  {
      host: "localhost",
      port: 20004,
      network_id: "*"
    },
  }
};

//0x321336d3d1ddc41249371e70dbcdd94b255509eb4c8e06baf42d061f7357ea5b
//0x6226d453143fc753E287b1674AB7558D86c71Fc2
