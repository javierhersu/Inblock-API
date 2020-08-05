pragma solidity >=0.4.21 <0.7.0;

contract Facturae {
  
    mapping(uint=>Factura) facturas;
    uint[] allFacturas;

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

}
