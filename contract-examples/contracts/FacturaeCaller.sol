pragma solidity >=0.4.21 < 0.7.0;

import "./Facturae.sol";


contract FacturaeCaller {

//   Facturae[] private invoices;
//   mapping(string => uint) private invoiceByID;
//   mapping(address => uint) private consults;

  /*address private caixabank;
  address private santander;
  address private sabadell;
  address private bbva;
  address private bankia;

  address private aef;*/

  //uint consultA = 0;
  //uint consultB = 0;

  Facturae invoice;

  /**
* @dev Contract constructor
*/
  constructor(/*address _caixabank, address _santander, address _sabadell,
              address _bbva, address _bankia, */Facturae addr) public
  {
    /*caixabank = _caixabank;
    santander = _santander;
    sabadell = _sabadell;
    bbva = _bbva;
    bankia = _bankia;*/
    invoice = Facturae(addr);
  }

  /*********************************************** MODIFIERS **********************************************/

     function setFacturas(uint _inblockID, uint _uploadingTimestamp, bool _validInvoice, bool _doubledInvoice, 
    bool _entityDiscountStatus, bool _discountCheckExecuted, bool _existenceCheckExecuted, bool _discountStatus, 
    bool _existenceStatus, string memory _datos) public {
       // invoice.setFacturas(_inblockID, _uploadingTimestamp, _validInvoice, _doubledInvoice, _entityDiscountStatus, _existenceStatus, _datos);
    }

     function getFacturas(uint _inblockID) public view returns(uint _inBlockID, uint _uploadingTimestamp, bool _validInvoice, bool _doubledInvoice, bool _entityDiscountStatus) {
        return invoice.getFacturas(_inblockID);
    }
    function getFacturas2(uint _inblockID) public view returns(bool _discountCheckExecuted, bool _existenceCheckExecuted, bool _discountStatus, bool _existenceStatus, string memory _datos) {
        return invoice.getFacturas2(_inblockID);
    }
   
}
