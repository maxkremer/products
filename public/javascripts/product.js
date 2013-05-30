(function() {
  jQuery(function($) {
    var $table, loadProductDetails, loadProductTable, productDetailsUrl, productListUrl, saveRow;
    $table = $('.container table');
    $newDiv = $('');
    productListUrl = $table.data('list');
    
    loadProductTable = function() {
      return $.get(productListUrl, function(products) {
        return $.each(products, function(index, id) {
          var row, cell;
          row = $('<tr/>')
          cell = $('<td/>').text(id);
          $(cell).css('visibility','hidden');
          row.append(cell);
          row.attr('contenteditable', true);
          $table.append(row);
          return loadProductDetails(row);
        });
      });
    };
    
    productDetailsUrl = function(id) {
      return $table.data('details').replace('0', id);
    };
    
    loadProductDetails = function(tableRow) {
      var eanCode;
      eanCode = tableRow.text();
      
      return $.getJSON(productDetailsUrl(eanCode), function(product) {
        tableRow.append($('<td/>').text(product.name));
         tableRow.append($('<td/>').text(product.description));
      });
      
    };
    
    loadProductTable();
    
    saveRow = function($row) {
      var description, ean, jqxhr, name, product, _ref;
      _ref = $row.children().map(function() {
        return $(this).text();
      });
      id = _ref[0], name = _ref[1], description = _ref[2];
      product = {
        id: parseInt(id),
        name: name,
        description: description
      };
      $('#result').text(product);
      jqxhr = $.ajax({
        type: "PUT",
        url: productDetailsUrl(id),
        contentType: "application/json",
        data: JSON.stringify(product)
      });
      jqxhr.done(function(response) {
        var $label;
        $label = $('<span/>').addClass('label label-success');
        $row.children().last().append($label.text(response));
        return $label.delay(3000).fadeOut();
      });
      return jqxhr.fail(function(data) {
        var $label, message;
        $label = $('<span/>').addClass('label label-important');
        message = data.responseText || data.statusText;
        return $row.children().last().append($label.text(message));
      });
    };
    
    $("#btnnew").click(function() {
		$("#newitem").toggle();
	});  
   
    $.fn.serializeObject = function()
    {
    	var description, ean, jqxhr, name, product, _ref;
    	_ref = $(":input").map(function() {
            return $(this).val();
        });
    	
    	name = _ref[1], description = _ref[2];
    	product = {
    		        name: name,
    		        description: description
    		      };
        return product;
    };
    
   

    $('#newRecord').submit(function() {
    	var productData =  JSON.stringify($('#newRecord').serializeObject())
    	$('#result').text(productData);
    	
    	$.ajax({
    		type: "POST",
    		url: "/products/new",
    		contentType: "application/json",
    		data: productData,
    		
    		success: function(msg){
    			$("#formResponse").html(msg.message);
    			loadProductTable();
    		},
    		error: function(){
    			$("#formResponse").html("there was an error");
    		}
    	});
    	
    	return false;    
  });
  

    
    return $('[contenteditable]').live('blur', function() {
        return saveRow($(this));
      });
      
});
}).call(this);

