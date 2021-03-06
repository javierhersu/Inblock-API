package quorum;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.14.
 */
@SuppressWarnings("rawtypes")
public class Hello extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5060408051808201909152600b8082526a12195b1b1bc815dbdc9b1960aa1b602090920191825261004391600091610049565b506100e4565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061008a57805160ff19168380011785556100b7565b828001600101855582156100b7579182015b828111156100b757825182559160200191906001019061009c565b506100c39291506100c7565b5090565b6100e191905b808211156100c357600081556001016100cd565b90565b6105ec806100f36000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80632184c0c0116100665780632184c0c0146101af5780634ed3885e146101d55780636d4ce63c1461027b578063b2d6eb2314610283578063eb077342146102a957610093565b806306f2791a1461009857806306fdde03146100d05780630b2d447c1461014d57806319ab453c14610187575b600080fd5b6100be600480360360208110156100ae57600080fd5b50356001600160a01b03166102e8565b60408051918252519081900360200190f35b6100d8610303565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101125781810151838201526020016100fa565b50505050905090810190601f16801561013f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101736004803603602081101561016357600080fd5b50356001600160a01b0316610391565b604080519115158252519081900360200190f35b6101ad6004803603602081101561019d57600080fd5b50356001600160a01b03166103af565b005b6101ad600480360360208110156101c557600080fd5b50356001600160a01b03166103fd565b6101ad600480360360208110156101eb57600080fd5b81019060208101813564010000000081111561020657600080fd5b82018360208201111561021857600080fd5b8035906020019184600183028401116401000000008311171561023a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610420945050505050565b6100d8610437565b6101ad6004803603602081101561029957600080fd5b50356001600160a01b03166104ce565b6102cf600480360360208110156102bf57600080fd5b50356001600160a01b03166104f2565b6040805192835290151560208301528051918290030190f35b6001600160a01b031660009081526001602052604090205490565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156103895780601f1061035e57610100808354040283529160200191610389565b820191906000526020600020905b81548152906001019060200180831161036c57829003601f168201915b505050505081565b6001600160a01b031660009081526002602052604090205460ff1690565b6001600160a01b0381166000908152600160205260409020546103fa576001600160a01b038116600090815260016020818152604080842092909255600290529020805460ff191690555b50565b6001600160a01b0316600090815260016020819052604090912080549091019055565b805161043390600090602084019061051f565b5050565b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156104c35780601f10610498576101008083540402835291602001916104c3565b820191906000526020600020905b8154815290600101906020018083116104a657829003601f168201915b505050505090505b90565b6001600160a01b03166000908152600260205260409020805460ff19166001179055565b6001600160a01b03811660009081526001602090815260408083205460029092529091205460ff16915091565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061056057805160ff191683800117855561058d565b8280016001018555821561058d579182015b8281111561058d578251825591602001919060010190610572565b5061059992915061059d565b5090565b6104cb91905b8082111561059957600081556001016105a356fea265627a7a723158205f157f3a641511010def2eb618ae767e104c0249d667c26d1b10b59bf5488a4164736f6c63430005100032";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETALL = "getAll";

    public static final String FUNC_GETDISCOUNT = "getDiscount";

    public static final String FUNC_GETEXISTENCE = "getExistence";

    public static final String FUNC_INIT = "init";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SET = "set";

    public static final String FUNC_SETDISCOUNT = "setDiscount";

    public static final String FUNC_SETEXISTENCE = "setExistence";

    @Deprecated
    protected Hello(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Hello(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Hello(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Hello(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, Boolean>> getAll(String _hash) {
        final Function function = new Function(FUNC_GETALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, Boolean>>(function,
                new Callable<Tuple2<BigInteger, Boolean>>() {
                    @Override
                    public Tuple2<BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getDiscount(String _hash) {
        final Function function = new Function(FUNC_GETDISCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> getExistence(String _hash) {
        final Function function = new Function(FUNC_GETEXISTENCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> init(String _hash) {
        final Function function = new Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> set(String _name) {
        final Function function = new Function(
                FUNC_SET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setDiscount(String _hash) {
        final Function function = new Function(
                FUNC_SETDISCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setExistence(String _hash) {
        final Function function = new Function(
                FUNC_SETEXISTENCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Hello load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Hello(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Hello load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Hello(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Hello load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Hello(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Hello load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Hello(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Hello> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Hello.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Hello> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Hello.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Hello> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Hello.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Hello> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Hello.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
