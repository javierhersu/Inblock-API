package quorum;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.privacy.PrivGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.BesuPrivateTransactionManager;
import org.web3j.tx.PrivateTransactionManager;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.utils.Base64String;

public class Blockchain {
	private Web3jService service;
    private Besu besu;
    private BigInteger gasPrice = BigInteger.valueOf(0);
    private BigInteger gasLimit = BigInteger.valueOf(4000000);
    private int retries = 5;
    private Credentials credentials;
    private Base64String privacyGroupId;
    private Base64String privateFrom;
    private String networkId;
    private BesuPrivateTransactionManager transactionManager;

    public Blockchain(String host, String privateKey, String privFrom) {
        try {
        	service = new HttpService(host);
        	besu = Besu.build(service);
            credentials = Credentials.create(privateKey);
            privacyGroupId = Base64String.wrap(Constants.GROUP_ID);
            privateFrom = Base64String.wrap(privFrom);
            networkId = besu.netVersion().send().getResult();
            transactionManager = new BesuPrivateTransactionManager(besu, getDefaultBesuPrivacyGasProvider(), credentials, Long.parseLong(networkId), privateFrom, privacyGroupId);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public String deployContract() throws Exception {
        Hello contract = Hello.deploy(besu, transactionManager, getDefaultBesuPrivacyGasProvider()).send();
        return contract.getContractAddress();
    }
    
    public void init(String _hash) throws Exception {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	contract.init(_hash).sendAsync().get();
    }
    
    public void setDiscount(String _hash) throws Exception {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	contract.setDiscount(_hash).sendAsync().get();
    }
    
    public void setExistence(String _hash) throws Exception {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	contract.setExistence(_hash).sendAsync().get();
    }
    
    public Tuple2<BigInteger, Boolean> getAll(String _hash) throws Exception {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	return contract.getAll(_hash).sendAsync().get();
    }
    
    public BigInteger getDiscount(String _hash) throws InterruptedException, ExecutionException {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	return contract.getDiscount(_hash).sendAsync().get();
    }
    
    public boolean getExistence(String _hash) throws InterruptedException, ExecutionException {
    	Hello contract = Hello.load(Constants.CONTRACT_ADDRESS, besu, transactionManager, getDefaultBesuPrivacyGasProvider());
    	return contract.getExistence(_hash).sendAsync().get();
    }
    
    /******************************* AUX METHODS ****************************************/
    
    public static byte[] stringToBytes(String string, int lenght) {
    	byte[] byteValue = string.getBytes();
    	byte[] byteValueLen = new byte[lenght];
    	System.arraycopy(byteValue, 0, byteValueLen, 0, lenght);
    	return byteValueLen;
    }
    
    private String sendPrivTransaction(String contractAddress, String encodeFunction) throws Exception {
    	String hash = transactionManager.sendTransaction(gasPrice, gasLimit, contractAddress, encodeFunction, BigInteger.valueOf(0), false).getTransactionHash();
    	PrivGetTransactionReceipt receipt = getPrivTransactionReceipt(hash, retries);
    	if (receipt == null) throw new Exception("Transaction not mined");
    	return hash;
    }
    
    PrivGetTransactionReceipt getPrivTransactionReceipt(String txHash, int retries) throws Exception {
    	PrivGetTransactionReceipt transactionReceipt = null;
    	int cont = 0;
    	while (cont <= retries) {
    	transactionReceipt = besu.privGetTransactionReceipt(txHash).send();
    	if (transactionReceipt.getResult() != null) {
    	break;
    	}
    	cont++;
    	Thread.sleep(1 * 1000);
    	}
    	return transactionReceipt;
    }
    
    BesuPrivacyGasProvider getDefaultBesuPrivacyGasProvider() {
        BesuPrivacyGasProvider besuPrivacyGasProvider = new BesuPrivacyGasProvider(gasPrice, gasLimit);
        return besuPrivacyGasProvider;
    }
    
}
