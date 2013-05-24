jQuery ($) ->

	$table = $('.container table')
	productListUrl = $table.data('list')

	$.get productListUrl, (products) ->
		$.each products, (index, eanCode) ->
			row = $('<tr/>').append $('<td/>').text(eanCode)
			$table.append row