pragma solidity >=0.5.16;

contract FacturaeSmartContract {

    struct Step {
        uint id;
        uint timestamp;
        string step;
    }

    struct Factura {
        uint inBlockID;
        uint uploadingTimestamp;
        bool validInvoice;
        bool doubledInvoice;
        bool entityDiscountStatus;
        bool discountCheckExecuted;
        bool existenceCheckExecuted;
        bool discountStatus;
        bool existenceStatus;
        string datos;
        //Step[] steps;
    }
    mapping (uint => Factura) facturas;
    uint[] allFacturas;

    string ejemplo = "hello";

    constructor() public{

    }
    function getEj() public view returns(string memory){
        return ejemplo;
    }
    function setEj(string memory ej) public {
        ejemplo = ej;
    }
   /* function setStep(uint _inblockID, uint _id, uint _timestamp, string memory _step) public {
        Step memory step;
        step.id = _id;
        step.step = _step;
        step.timestamp = _timestamp;
        facturas[_inblockID].steps.push(step);
    }
    function getSteps(uint _inblockID, uint _index) public view returns (uint _id, uint _timestamp, string memory _step) {
        return (facturas[_inblockID].steps[_index].id,
                facturas[_inblockID].steps[_index].timestamp,
                facturas[_inblockID].steps[_index].step);
    }*/

    function setFacturas(uint _inblockID, uint _uploadingTimestamp, bool _validInvoice, bool _doubledInvoice, 
    bool _entityDiscountStatus, bool _discountCheckExecuted, bool _existenceCheckExecuted, bool _discountStatus, 
    bool _existenceStatus, string memory _datos) public {
        facturas[_inblockID].inBlockID = _inblockID;
        facturas[_inblockID].uploadingTimestamp = _uploadingTimestamp;
        facturas[_inblockID].validInvoice = _validInvoice;
        facturas[_inblockID].doubledInvoice = _doubledInvoice;
        facturas[_inblockID].entityDiscountStatus = _entityDiscountStatus;
        facturas[_inblockID].discountCheckExecuted = _discountCheckExecuted;
        facturas[_inblockID].existenceCheckExecuted = _existenceCheckExecuted;
        facturas[_inblockID].discountStatus = _discountStatus;
        facturas[_inblockID].existenceStatus = _existenceStatus;
        facturas[_inblockID].datos = _datos;
        //facturas[_inblockID].steps = [];
     
        allFacturas.push(_inblockID) ;
    }
    function getFacturas(uint _inblockID) public view returns(uint _inBlockID, uint _uploadingTimestamp, bool _validInvoice, bool _doubledInvoice, bool _entityDiscountStatus) {
        return(
            facturas[_inblockID].inBlockID,
            facturas[_inblockID].uploadingTimestamp,
            facturas[_inblockID].validInvoice,
            facturas[_inblockID].doubledInvoice,
            facturas[_inblockID].entityDiscountStatus
        );
    }
    function getFacturas2(uint _inblockID) public view returns(bool _discountCheckExecuted, bool _existenceCheckExecuted, bool _discountStatus, bool _existenceStatus, string memory _datos) {
        return (facturas[_inblockID].discountCheckExecuted,
                facturas[_inblockID].existenceCheckExecuted,
                facturas[_inblockID].discountStatus,
                facturas[_inblockID].existenceStatus,
                facturas[_inblockID].datos
        );
    }
    
    function setDiscount(uint _inblockID, bool _discountStatus) public {
        facturas[_inblockID].discountStatus = _discountStatus;
    }

    function setCheckExistence(uint _inblockID, bool _existenceStatus) public {
        facturas[_inblockID].existenceStatus = _existenceStatus;
    }
   
    function setCheckDiscount(uint _inblockID, bool _entityDiscountStatus) public {
        facturas[_inblockID].entityDiscountStatus = _entityDiscountStatus;
    }
}