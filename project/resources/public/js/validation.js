function validate() {
	var name = document.getElementById("name").value;
    if (name == null || name == "" || name == " ") {
        alert("Please enter medicine name.");
        return false;
    }
    var type = document.getElementById("type").value;
    if (type == null || type == "" || type == " ") {
        alert("Please enter medicine type.");
        return false;
    }
    var price = document.getElementById("price").value;
    if (price == null || price == "" || (isNaN(price))) {
        alert("Please enter a valid price.");
        return false;
    }
    var quantity = document.getElementById("quantity").value;
    if (quantity == null || quantity == "" || (isNaN(quantity))) {
        alert("Please enter a valid quantity.");
        return false;
    }
  return true;
  }