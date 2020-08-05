pragma solidity >=0.5.16;

import "./FacturaeSmartContract.sol";


contract FacturaeSmartContractCaller {
    FacturaeSmartContract facturae;
    
    constructor(FacturaeSmartContract add) public {
       facturae = FacturaeSmartContract(add);
    }
    function getEj() public view returns(string memory){
        return facturae.getEj();
    }
    function setEj(string memory ej) public {
        facturae.setEj(ej);
    }
   /* function setStep(uint _inblockID, uint _id, uint _timestamp, string memory _step) public {
        facturae.setStep(_inblockID, _id, _timestamp, _step);
    }
    function getStep(uint _inblockID, uint _index) public view returns (uint, uint, string memory) {
       facturae.getSteps(_inblockID, _index);
    }*/
    function setFacturas(uint _inblockID, bool _validInvoice, bool _doubledInvoice, 
    bool _entityDiscountStatus, bool _discountCheckExecuted, bool _existenceCheckExecuted, bool _discountStatus,
     bool _existenceStatus, string memory _datos) public {
        facturae.setFacturas(_inblockID, now, _validInvoice, _doubledInvoice, 
        _entityDiscountStatus,  _discountCheckExecuted, _existenceCheckExecuted, _discountStatus, 
        _existenceStatus, _datos);
        }
    function getFacturas(uint _inblockID) public view returns (uint, uint, bool, bool, bool) {
        return facturae.getFacturas(_inblockID);
    }
     function getFacturas2(uint _inblockID) public view returns (bool, bool, bool, bool, string memory) {
        return facturae.getFacturas2(_inblockID);
    }
    function setDiscount(uint _inblockID, bool _discountStatus) public {
        facturae.setDiscount(_inblockID, _discountStatus);
    }
    function setCheckExistence(uint _inblockID, bool _existenceStatus) public {
        facturae.setCheckExistence(_inblockID, _existenceStatus);
    }
    function setCheckDiscount(uint _inblockID, bool _entityDiscountStatus) public {
        facturae.setCheckDiscount(_inblockID, _entityDiscountStatus);
    }
}