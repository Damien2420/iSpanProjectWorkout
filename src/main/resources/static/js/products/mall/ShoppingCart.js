$(function () {
	const add = $('.add');
	const minus = $('.minus');
	let action;

	function increase(quantityBox) {
		let value = parseInt(quantityBox.val());
		quantityBox.val(value + 1);
		updatePrice(quantityBox);
	}

	function decrease(quantityBox) {
		let value = parseInt(quantityBox.val());
		if (value > 0) {
			quantityBox.val(value - 1);
			updatePrice(quantityBox);
		}
	}

	function startIncrease(event, quantityBox) {
		event.preventDefault();
		action = setInterval(function (event) {
			increase(quantityBox);
		}, 100);
	}

	function startDecrease(event, quantityBox) {
		event.preventDefault();
		action = setInterval(function (event) {
			decrease(quantityBox);
		}, 100);
	}

	function stop(event) {
		event.preventDefault();
		clearInterval(action);
	}

	add.on('click', function (event) {
		event.preventDefault();
		let quantityBox = $(this).siblings('.number');
		increase(quantityBox);
	});
	minus.on('click', function (event) {
		event.preventDefault();
		let quantityBox = $(this).siblings('.number');
		decrease(quantityBox);
	});

	add.on('mousedown', function (event) {
		let quantityBox = $(this).siblings('.number');
		startIncrease(event, quantityBox);
	}).on('mouseup', function (event) {
		stop(event);
	});

	minus.on('mousedown', function (event) {
		let quantityBox = $(this).siblings('.number');
		startDecrease(event, quantityBox);
	}).on('mouseup', function (event) {
		stop(event);
	});
	//如果購物車內沒有商品就取消送出訂單的按鈕
	function updateOrderButton() {
		let total = parseInt($('.total').text().substring(1));
		let cartIsEmpty = $('.item').length === 0;
		let totalIsZero = total === 0;
		$('.orderSubmit').prop('disabled', cartIsEmpty || totalIsZero);
	}
	//單個商品總金額
	function updatePrice(input) {
		const quantity = parseInt(input.val());
		const item = input.closest('.product');
		const price = parseFloat(item.find('.hiddenPrice').text());
		if (isNaN(quantity)) {
			item.find('.price').text('$0');
		} else {
			const totalPrice = quantity * price;
			item.find('.price').text('$' + totalPrice);
		}
		updateTotal();
	}
	//訂單總金額更新
	function updateTotal() {
		let totalPrice = 0;
		$('.product').each(function () {
			let quantity = parseInt($(this).find('.number').val());
			let price = parseInt($(this).find('.hiddenPrice').text());
			totalPrice += quantity * price;
		});
		$('.total').text('$' + totalPrice);
		updateOrderButton();
	}

	updateTotal();

	$('.number').on('input', function () {
		updatePrice($(this));
	});

	function wrapOrderData() {
		let orderItems = [];
		$('.product').each(function () {
			let productID = $(this).find('.productID').val();
			let quantity = $(this).find('.number').val();
			let productPrice = $(this).find('.price').text().substring(1);
			orderItems.push({
				productID: productID,
				quantity: quantity,
				productPrice: productPrice
			})
		})
		let orderTotalPrice = $('.total').text().substring(1);
		let orderData = {
			orderItems: orderItems,
			orderTotalPrice: orderTotalPrice
		}
		$('#orderData').val(JSON.stringify(orderData));
	}

	$('.orderSubmit').on('click', function () {
		wrapOrderData();
	})

	$('.emptybtn').on('click', function () {
		sessionStorage.removeItem('cart');
		location.reload();
	})
	
	$('.cart').css('display', 'none');
})