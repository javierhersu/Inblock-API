pragma solidity >=0.4.21 <0.7.0;

contract Hello {
  string public name;
  mapping(address => uint) discountMapping;
  mapping(address => bool) existenceMapping;

  constructor() public {
    name = "Hello World";
  }

  function set(string memory _name) public {
    name = _name;
  }

  function get() public view returns(string memory _name){
      return name;
  }

  function init(address _hash) public {
    if(discountMapping[_hash] == 0){
        discountMapping[_hash] = 1;
        existenceMapping[_hash] = false;
    }
  }

  function setExistence(address _hash) public {
    existenceMapping[_hash] = true;
  }

  function setDiscount(address _hash) public {
    discountMapping[_hash] += 1;
  }

  function getAll(address _hash) public view returns(uint, bool) {
    return(discountMapping[_hash], existenceMapping[_hash]);
  }

  function getDiscount(address _hash) public view returns(uint) {
    return(discountMapping[_hash]);
  }

  function getExistence(address _hash) public view returns(bool) {
    return(existenceMapping[_hash]);
  }
}
